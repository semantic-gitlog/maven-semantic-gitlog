package team.yi.maven.plugin;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import de.skuzzle.semantic.Version;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.maven.plugin.logging.Log;
import se.bjurr.gitchangelog.api.GitChangelogApi;
import se.bjurr.gitchangelog.api.exceptions.GitChangelogRepositoryException;
import se.bjurr.gitchangelog.api.model.Changelog;
import se.bjurr.gitchangelog.api.model.Commit;
import se.bjurr.gitchangelog.api.model.Tag;
import se.bjurr.gitchangelog.internal.settings.Settings;
import team.yi.maven.plugin.model.GitReleaseCommit;
import team.yi.maven.plugin.model.GitReleaseCommitGroup;
import team.yi.maven.plugin.model.GitReleaseLog;
import team.yi.maven.plugin.model.GitReleaseSection;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.io.Resources.getResource;

public class GitReleaseLogService {
    private final GitChangelogApi builder;
    private final Settings settings;
    private final Log log;
    private Version firstVersion;
    private Version lastVersion;
    private Stack<GitReleaseCommit> versionCommits = new Stack<>();

    private GitReleaseLogSettings versioningSettings;

    public GitReleaseLogService(GitReleaseLogSettings versioningSettings, GitChangelogApi builder, Log log) {
        this.versioningSettings = versioningSettings;
        this.builder = builder;
        this.log = log;
        this.settings = builder.getSettings();
    }

    private static String getGroupTitle(String commitType, boolean breakingChange) {
        /*
            feat        Features
            fix         Bug Fixes
            perf        Performance Improvements
            revert      Reverts

            docs        Documentation
            style       Styles
            refactor    Code Refactoring
            test        Tests

            build       Build System
            ci          Continuous Integration
        */

        if (commitType == null || commitType.isEmpty()) return GitReleaseCommitGroup.OTHERS;
        if (breakingChange) return GitReleaseCommitGroup.BREAKING_CHANGE;

        switch (commitType) {
            case "feat":
                return GitReleaseCommitGroup.FEATURES;
            case "fix":
                return GitReleaseCommitGroup.BUG_FIXES;
            case "perf":
                return GitReleaseCommitGroup.PERFORMANCE_IMPROVEMENTS;
            case "revert":
                return GitReleaseCommitGroup.REVERTS;
            case "docs":
                return GitReleaseCommitGroup.DOCUMENTATION;
            case "style":
                return GitReleaseCommitGroup.STYLES;
            case "refactor":
                return GitReleaseCommitGroup.CODE_REFACTORING;
            case "test":
                return GitReleaseCommitGroup.TESTS;
            case "build":
                return GitReleaseCommitGroup.BUILD_SYSTEM;
            case "ci":
                return GitReleaseCommitGroup.CONTINUOUS_INTEGRATION;
            default:
                return GitReleaseCommitGroup.OTHERS;
        }
    }

    private static boolean shouldUseIntegrationIfConfigured(final String templateContent) {
        return templateContent.contains("{{type}}") //
            || templateContent.contains("{{link}}") //
            || templateContent.contains("{{title}}") //
            || templateContent.replaceAll("\\r?\\n", " ").matches(".*\\{\\{#?labels}}.*");
    }

    public void saveToFile(File file) throws IOException, GitChangelogRepositoryException {
        Files.createParentDirs(file);
        Files.write(render().getBytes(StandardCharsets.UTF_8), file);
    }

    public String render() throws GitChangelogRepositoryException, IOException {
        final Writer writer = new StringWriter();

        render(writer);

        return writer.toString();
    }

    public void render(final Writer writer) throws GitChangelogRepositoryException, IOException {
        final String templateContent = checkNotNull(getTemplateContent(), "No template!");

        try (StringReader reader = new StringReader(templateContent)) {
            final MustacheFactory mf = new DefaultMustacheFactory();
            final Mustache mustache = mf.compile(reader, this.settings.getTemplatePath());
            GitReleaseLog gitReleaseLog = this.generate();

            mustache
                .execute(
                    writer, //
                    new Object[]{gitReleaseLog, this.settings.getExtendedVariables()} //
                )
                .flush();
        } catch (final IOException e) {
            // Should be impossible!
            throw new GitChangelogRepositoryException("", e);
        }
    }

    private String getTemplateContent() throws IOException {
        checkArgument(this.settings.getTemplatePath() != null, "You must specify a template!");

        try {
            return Resources.toString(getResource(this.settings.getTemplatePath()), StandardCharsets.UTF_8);
        } catch (final Exception e) {
            File file = new File(this.settings.getTemplatePath());

            return Files.toString(file, StandardCharsets.UTF_8);
        }
    }

    public GitReleaseLog generate() throws IOException {
        final String templateContent = checkNotNull(getTemplateContent(), "No template!");
        final boolean useIntegrationIfConfigured = shouldUseIntegrationIfConfigured(templateContent);

        return this.generate(useIntegrationIfConfigured);
    }

    public GitReleaseLog generate(boolean useIntegrationIfConfigured) {
        Changelog changelog;

        try {
            changelog = builder.getChangelog(useIntegrationIfConfigured);
        } catch (GitChangelogRepositoryException e) {
            return null;
        }

        if (changelog == null) return null;

        this.versionCommits.clear();

        List<GitReleaseSection> gitReleaseSections = new ArrayList<>();
        List<Tag> tags = changelog.getTags();
        GitReleaseSection gitReleaseSection = null;

        for (Tag tag : tags) {
            GitReleaseSection section = this.processTag(tag);

            if (gitReleaseSection == null) {
                gitReleaseSection = section;
            } else {
                if (gitReleaseSection.getVersion() == null) {
                    if (section.getVersion() != null) {
                        gitReleaseSection = section;
                    }
                } else {
                    if (gitReleaseSection.getVersion() == null || !gitReleaseSection.getVersion().equals(section.getVersion())) {
                        gitReleaseSection = section;
                    }
                }
            }

            gitReleaseSections.add(gitReleaseSection);
        }

        Version lastVersion = this.versioningSettings.getLastVersion();

        if (lastVersion == null) lastVersion = this.lastVersion;

        Version newVersion = this.deriveNewVersion(lastVersion, this.versionCommits);
        GitReleaseLog gitReleaseLog = new GitReleaseLog();

        gitReleaseLog.setLastVersion(this.lastVersion);
        gitReleaseLog.setNewVersion(newVersion);
        gitReleaseLog.setSections(gitReleaseSections);

        return gitReleaseLog;
    }

    private GitReleaseSection processTag(Tag tag) {
        Map<String, List<GitReleaseCommit>> groups = new ConcurrentHashMap<>();
        String commitUrlTemplate = this.versioningSettings.getCommitUrlTemplate();
        String issueUrlTemplate = this.versioningSettings.getIssueUrlTemplate();
        List<Commit> commits = tag.getCommits();

        for (Commit item : commits) {
            GitReleaseCommit commit = new GitReleaseCommit(item, commitUrlTemplate, issueUrlTemplate);

            if (StringUtils.isEmpty(commit.getCommitDescription())) continue;

            if (this.lastVersion == null) {
                this.versionCommits.add(commit);
            }

            String groupTitle = getGroupTitle(commit.getCommitType(), commit.isBreakingChange());

            if (groups.containsKey(groupTitle)) {
                groups.get(groupTitle).add(commit);
            } else {
                List<GitReleaseCommit> releaseCommits = new ArrayList<>();
                releaseCommits.add(commit);

                groups.put(groupTitle, releaseCommits);
            }
        }

        List<GitReleaseCommitGroup> contents = new ArrayList<>();

        groups.forEach((key, value) -> {
            GitReleaseCommitGroup gitReleaseCommitGroup = new GitReleaseCommitGroup();
            gitReleaseCommitGroup.setTitle(key);
            gitReleaseCommitGroup.setCommits(value);

            contents.add(gitReleaseCommitGroup);
        });

        Version tagVersion = null;

        try {
            tagVersion = Version.parseVersion(tag.getName());
        } catch (Exception e) {
            this.log.debug(e);
        }

        if (tagVersion == null && firstVersion == null) {
            firstVersion = Version.parseVersion("0.1.0");
        } else if (firstVersion == null) {
            firstVersion = tagVersion;
        } else if (lastVersion == null) {
            lastVersion = tagVersion;
        }

        GitReleaseSection gitReleaseSection = new GitReleaseSection();
        gitReleaseSection.setDescription(null);
        gitReleaseSection.setGroups(contents);

        if (tag.isHasTagTime()) {
            try {
                Date releaseDate = DateUtils.parseDate(tag.getTagTime(), this.settings.getDateFormat());

                gitReleaseSection.setReleaseDate(releaseDate);
            } catch (ParseException e) {
                this.log.debug(e);
            }
        }

        if (tagVersion != null) gitReleaseSection.setVersion(tagVersion);

        return gitReleaseSection;
    }

    @SuppressWarnings({"PMD.NcssCount", "PMD.NPathComplexity"})
    private Version deriveNewVersion(Version lastVersion, Stack<GitReleaseCommit> versionCommits) {
        Version nextVersion = lastVersion == null
            ? Version.create(0, 1, 0)
            : Version.parseVersion(lastVersion.toString());
        String preRelease = this.versioningSettings.getPreRelease();
        String buildMetaData = this.versioningSettings.getBuildMetaData();

        if (!StringUtils.isEmpty(preRelease)) nextVersion = nextVersion.withPreRelease(preRelease);
        if (!StringUtils.isEmpty(buildMetaData)) nextVersion = nextVersion.withBuildMetaData(buildMetaData);

        List<String> majorTypes = this.versioningSettings.getMajorTypes();
        List<String> minorTypes = this.versioningSettings.getMinorTypes();
        List<String> patchTypes = this.versioningSettings.getPatchTypes();
        List<String> preReleaseTypes = this.versioningSettings.getPreReleaseTypes();
        List<String> buildMetaDataTypes = this.versioningSettings.getBuildMetaDataTypes();

        this.log.debug("nextVersion: " + nextVersion);

        while (!versionCommits.isEmpty()) {
            preRelease = nextVersion.getPreRelease();
            buildMetaData = nextVersion.getBuildMetaData();

            if (StringUtils.isEmpty(preRelease)) preRelease = this.versioningSettings.getPreRelease();
            if (StringUtils.isEmpty(buildMetaData)) buildMetaData = this.versioningSettings.getBuildMetaData();

            GitReleaseCommit commit = versionCommits.pop();
            String commitType = commit.getCommitType();

            this.log.debug("preRelease: " + preRelease);
            this.log.debug("buildMetaData: " + buildMetaData);
            this.log.debug("commitType: " + commitType);
            this.log.debug("nextVersion: " + nextVersion);

            if (commit.isBreakingChange() || majorTypes.contains(commitType)) {
                nextVersion = nextVersion.nextMajor();

                if (!StringUtils.isEmpty(preRelease)) nextVersion = nextVersion.withPreRelease(preRelease);
                if (!StringUtils.isEmpty(buildMetaData)) nextVersion = nextVersion.withBuildMetaData(buildMetaData);

                if (this.versioningSettings.getUseCrazyGrowing()) continue;

                break;
            } else if (minorTypes.contains(commitType)) {
                nextVersion = nextVersion.nextMinor();

                if (!StringUtils.isEmpty(preRelease)) nextVersion = nextVersion.withPreRelease(preRelease);
                if (!StringUtils.isEmpty(buildMetaData)) nextVersion = nextVersion.withBuildMetaData(buildMetaData);

                if (this.versioningSettings.getUseCrazyGrowing()) continue;

                break;
            } else if (patchTypes.contains(commitType)) {
                nextVersion = nextVersion.nextPatch();

                if (!StringUtils.isEmpty(preRelease)) nextVersion = nextVersion.withPreRelease(preRelease);
                if (!StringUtils.isEmpty(buildMetaData)) nextVersion = nextVersion.withBuildMetaData(buildMetaData);

                if (this.versioningSettings.getUseCrazyGrowing()) continue;

                break;
            } else if (preReleaseTypes.contains(commitType)) {
                nextVersion = nextVersion.nextPreRelease();

                if (this.versioningSettings.getUseCrazyGrowing()) continue;

                break;
            } else if (buildMetaDataTypes.contains(commitType)) {
                nextVersion = nextVersion.nextBuildMetaData();

                if (this.versioningSettings.getUseCrazyGrowing()) continue;

                break;
            }
        }

        return nextVersion;
    }
}
