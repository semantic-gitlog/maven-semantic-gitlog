package team.yi.maven.plugin.service;

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
import team.yi.maven.plugin.config.ReleaseLogSettings;
import team.yi.maven.plugin.model.ReleaseCommit;
import team.yi.maven.plugin.model.ReleaseCommitGroup;
import team.yi.maven.plugin.model.ReleaseCommitGroups;
import team.yi.maven.plugin.model.ReleaseDate;
import team.yi.maven.plugin.model.ReleaseLog;
import team.yi.maven.plugin.model.ReleaseSection;
import team.yi.maven.plugin.utils.ReleaseCommitParser;

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

public class ReleaseLogService {
    private final ReleaseLogSettings releaseLogSettings;
    private final ReleaseCommitParser commitParser;
    private final GitChangelogApi builder;
    private final Settings builderSettings;
    private final Log log;
    private final Stack<ReleaseCommit> versionCommits = new Stack<>();

    private Version firstVersion;
    private Version lastVersion;

    public ReleaseLogService(ReleaseLogSettings releaseLogSettings, GitChangelogApi builder, Log log) {
        this.releaseLogSettings = releaseLogSettings;
        this.commitParser = new ReleaseCommitParser(releaseLogSettings);
        this.builder = builder;
        this.log = log;

        this.builderSettings = this.builder.getSettings();
    }

    private static boolean shouldUseIntegrationIfConfigured(final String templateContent) {
        return templateContent.contains("{{type}}") //
            || templateContent.contains("{{link}}") //
            || templateContent.contains("{{title}}") //
            || templateContent.replaceAll("\\r?\\n", " ").matches(".*\\{\\{#?labels}}.*");
    }

    private String getTemplateContent() throws IOException {
        checkArgument(this.builderSettings.getTemplatePath() != null, "You must specify a template!");

        String templateContent;

        try {
            templateContent = Resources.toString(getResource(this.builderSettings.getTemplatePath()), StandardCharsets.UTF_8);
        } catch (final Exception e) {
            final File file = new File(this.builderSettings.getTemplatePath());

            templateContent = Files.toString(file, StandardCharsets.UTF_8);
        }

        return checkNotNull(templateContent, "No template!");
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
        final String templateContent = this.getTemplateContent();

        try (StringReader reader = new StringReader(templateContent)) {
            final MustacheFactory mf = new DefaultMustacheFactory();
            final Mustache mustache = mf.compile(reader, this.builderSettings.getTemplatePath());
            final Object[] scopes = {this.generate(), this.builderSettings.getExtendedVariables()};

            mustache.execute(writer, scopes).flush();
        } catch (final IOException e) {
            throw new GitChangelogRepositoryException(e.getMessage(), e);
        }
    }

    public ReleaseLog generate() throws IOException {
        final String templateContent = this.getTemplateContent();
        final boolean useIntegrationIfConfigured = shouldUseIntegrationIfConfigured(templateContent);

        return this.generate(useIntegrationIfConfigured);
    }

    public ReleaseLog generate(boolean useIntegrationIfConfigured) {
        Changelog changelog;

        try {
            changelog = builder.getChangelog(useIntegrationIfConfigured);
        } catch (GitChangelogRepositoryException e) {
            return null;
        }

        if (changelog == null) return null;

        this.versionCommits.clear();

        final List<ReleaseSection> releaseSections = new ArrayList<>();
        final List<Tag> tags = changelog.getTags();
        ReleaseSection releaseSection = null;

        for (Tag tag : tags) {
            final ReleaseSection section = this.processTag(tag);

            if (releaseSection == null) {
                releaseSection = section;
            } else {
                if (releaseSection.getVersion() == null) {
                    if (section.getVersion() != null) {
                        releaseSection = section;
                    }
                } else if (!releaseSection.getVersion().equals(section.getVersion())) {
                    releaseSection = section;
                }
            }

            releaseSections.add(releaseSection);
        }

        Version lastVersion = this.releaseLogSettings.getLastVersion();

        if (lastVersion == null) lastVersion = this.lastVersion;

        final Version nextVersion = this.deriveNextVersion(lastVersion, this.versionCommits);

        return new ReleaseLog(nextVersion, this.lastVersion, releaseSections);
    }

    @SuppressWarnings("PMD.NPathComplexity")
    private ReleaseSection processTag(Tag tag) {
        final Map<String, List<ReleaseCommit>> groups = new ConcurrentHashMap<>();
        final List<Commit> commits = tag.getCommits();
        Version tagVersion = null;

        try {
            tagVersion = Version.parseVersion(tag.getName());
        } catch (Exception e) {
            this.log.debug(e);
        }

        if (tagVersion == null && this.firstVersion == null) {
            this.firstVersion = Version.parseVersion("0.1.0");
        } else if (this.firstVersion == null) {
            this.firstVersion = tagVersion;
        } else if (this.lastVersion == null) {
            this.lastVersion = tagVersion;
        }

        for (Commit item : commits) {
            final ReleaseCommit commit = this.commitParser.parse(item);

            if (StringUtils.isEmpty(commit.getCommitDescription())) continue;

            if (this.lastVersion == null) this.versionCommits.add(commit);

            final String groupTitle = ReleaseCommitGroups.fromCommitType(commit.getCommitType(), commit.isBreakingChange());

            if (groups.containsKey(groupTitle)) {
                groups.get(groupTitle).add(commit);
            } else {
                final List<ReleaseCommit> releaseCommits = new ArrayList<>();
                releaseCommits.add(commit);

                groups.put(groupTitle, releaseCommits);
            }
        }

        final List<ReleaseCommitGroup> contents = new ArrayList<>();

        groups.forEach((key, value) -> {
            final ReleaseCommitGroup releaseCommitGroup = new ReleaseCommitGroup(key, value);

            contents.add(releaseCommitGroup);
        });

        ReleaseDate releaseDate = null;

        if (tag.isHasTagTime()) {
            try {
                final Date tagTime = DateUtils.parseDate(tag.getTagTime(), this.builderSettings.getDateFormat());
                final String longDateFormat = this.releaseLogSettings.getLongDateFormat();
                final String shortDateFormat = this.releaseLogSettings.getShortDateFormat();

                releaseDate = new ReleaseDate(tagTime, longDateFormat, shortDateFormat);
            } catch (ParseException e) {
                this.log.debug(e);
            }
        }

        return new ReleaseSection(tagVersion, releaseDate, null, contents);
    }

    @SuppressWarnings({"PMD.NcssCount", "PMD.NPathComplexity"})
    private Version deriveNextVersion(Version lastVersion, Stack<ReleaseCommit> versionCommits) {
        Version nextVersion = lastVersion == null
            ? Version.create(0, 1, 0)
            : Version.parseVersion(lastVersion.toString());
        String preRelease = this.releaseLogSettings.getPreRelease();
        String buildMetaData = this.releaseLogSettings.getBuildMetaData();

        if (!StringUtils.isEmpty(preRelease)) nextVersion = nextVersion.withPreRelease(preRelease);
        if (!StringUtils.isEmpty(buildMetaData)) nextVersion = nextVersion.withBuildMetaData(buildMetaData);

        final List<String> majorTypes = this.releaseLogSettings.getMajorTypes();
        final List<String> minorTypes = this.releaseLogSettings.getMinorTypes();
        final List<String> patchTypes = this.releaseLogSettings.getPatchTypes();
        final List<String> preReleaseTypes = this.releaseLogSettings.getPreReleaseTypes();
        final List<String> buildMetaDataTypes = this.releaseLogSettings.getBuildMetaDataTypes();

        this.log.debug("nextVersion: " + nextVersion);

        while (!versionCommits.isEmpty()) {
            preRelease = nextVersion.getPreRelease();
            buildMetaData = nextVersion.getBuildMetaData();

            if (StringUtils.isEmpty(preRelease)) preRelease = this.releaseLogSettings.getPreRelease();
            if (StringUtils.isEmpty(buildMetaData)) buildMetaData = this.releaseLogSettings.getBuildMetaData();

            final ReleaseCommit commit = versionCommits.pop();
            final String commitType = commit.getCommitType();

            this.log.debug("preRelease: " + preRelease);
            this.log.debug("buildMetaData: " + buildMetaData);
            this.log.debug("commitType: " + commitType);
            this.log.debug("nextVersion: " + nextVersion);

            if (commit.isBreakingChange() || majorTypes.contains(commitType)) {
                nextVersion = nextVersion.nextMajor();

                if (!StringUtils.isEmpty(preRelease)) nextVersion = nextVersion.withPreRelease(preRelease);
                if (!StringUtils.isEmpty(buildMetaData)) nextVersion = nextVersion.withBuildMetaData(buildMetaData);
            } else if (minorTypes.contains(commitType)) {
                nextVersion = nextVersion.nextMinor();

                if (!StringUtils.isEmpty(preRelease)) nextVersion = nextVersion.withPreRelease(preRelease);
                if (!StringUtils.isEmpty(buildMetaData)) nextVersion = nextVersion.withBuildMetaData(buildMetaData);

                if (this.releaseLogSettings.getUseCrazyGrowing()) continue;

                break;
            } else if (patchTypes.contains(commitType)) {
                nextVersion = nextVersion.nextPatch();

                if (!StringUtils.isEmpty(preRelease)) nextVersion = nextVersion.withPreRelease(preRelease);
                if (!StringUtils.isEmpty(buildMetaData)) nextVersion = nextVersion.withBuildMetaData(buildMetaData);

                if (this.releaseLogSettings.getUseCrazyGrowing()) continue;

                break;
            } else if (preReleaseTypes.contains(commitType)) {
                nextVersion = nextVersion.nextPreRelease();

                if (!StringUtils.isEmpty(buildMetaData)) nextVersion = nextVersion.withBuildMetaData(buildMetaData);

                if (this.releaseLogSettings.getUseCrazyGrowing()) continue;

                break;
            } else if (buildMetaDataTypes.contains(commitType)) {
                nextVersion = nextVersion.nextBuildMetaData();

                if (this.releaseLogSettings.getUseCrazyGrowing()) continue;

                break;
            }
        }

        return nextVersion;
    }
}
