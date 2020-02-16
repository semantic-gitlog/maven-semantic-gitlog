package team.yi.maven.plugin.service;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import de.skuzzle.semantic.Version;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.maven.plugin.logging.Log;
import se.bjurr.gitchangelog.api.GitChangelogApi;
import se.bjurr.gitchangelog.api.exceptions.GitChangelogRepositoryException;
import se.bjurr.gitchangelog.api.model.Changelog;
import se.bjurr.gitchangelog.api.model.Commit;
import se.bjurr.gitchangelog.api.model.Tag;
import se.bjurr.gitchangelog.internal.settings.Settings;
import team.yi.maven.plugin.config.FileSet;
import team.yi.maven.plugin.config.ReleaseLogSettings;
import team.yi.maven.plugin.model.ReleaseCommit;
import team.yi.maven.plugin.model.ReleaseDate;
import team.yi.maven.plugin.model.ReleaseLog;
import team.yi.maven.plugin.model.ReleaseSection;
import team.yi.maven.plugin.model.ReleaseSections;
import team.yi.maven.plugin.model.ReleaseTag;
import team.yi.maven.plugin.parser.CommitParser;
import team.yi.maven.plugin.utils.VersionManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

public class ReleaseLogService {
    private final ReleaseLogSettings releaseLogSettings;
    private final GitChangelogApi builder;
    private final Settings builderSettings;
    private final Log log;
    private final Stack<ReleaseCommit> versionCommits = new Stack<>();
    private final VersionManager versionManager;

    public ReleaseLogService(final ReleaseLogSettings releaseLogSettings, final GitChangelogApi builder, final Log log) {
        this.releaseLogSettings = releaseLogSettings;
        this.builder = builder;
        this.log = log;

        this.builderSettings = this.builder.getSettings();
        this.versionManager = new VersionManager(this.releaseLogSettings, log);
    }

    public void saveToFiles(final Set<FileSet> fileSets) throws IOException, GitChangelogRepositoryException {
        if (fileSets == null || fileSets.isEmpty()) return;

        for (final FileSet fileSet : fileSets) {
            final File target = fileSet.getTarget();
            final File template = fileSet.getTemplate();

            this.saveToFile(target, template);
        }
    }

    public void saveToFile(final File target, final File template) throws IOException, GitChangelogRepositoryException {
        FileUtils.forceMkdir(target.getParentFile());

        try (BufferedWriter writer = Files.newBufferedWriter(target.toPath(), StandardCharsets.UTF_8)) {
            if (this.log.isInfoEnabled()) {
                this.log.info("#");
            }

            render(writer, template);

            if (this.log.isInfoEnabled()) {
                this.log.info("#    template: " + template.getPath());
                this.log.info("#      target: " + target.getPath());
                this.log.info("#");
            }
        }
    }

    public String render(final File template) throws GitChangelogRepositoryException, IOException {
        final Writer writer = new StringWriter();

        render(writer, template);

        return writer.toString();
    }

    public void render(final Writer writer, final File template) throws GitChangelogRepositoryException, IOException {
        final String templateContent = FileUtils.readFileToString(template, StandardCharsets.UTF_8);

        try (StringReader reader = new StringReader(templateContent)) {
            final MustacheFactory mf = new DefaultMustacheFactory();
            final Mustache mustache = mf.compile(reader, template.getAbsolutePath());
            final ReleaseLog releaseLog = this.generate();
            final Object[] scopes = {releaseLog, this.builderSettings.getExtendedVariables()};

            mustache.execute(writer, scopes).flush();

            this.log.info("# nextVersion: " + releaseLog.getNextVersion());
            this.log.info("# lastVersion: " + releaseLog.getLastVersion());
        } catch (final IOException e) {
            throw new GitChangelogRepositoryException(e.getMessage(), e);
        }
    }

    public ReleaseLog generate() throws GitChangelogRepositoryException {
        final Changelog changelog = builder.getChangelog(false);

        if (changelog == null) return null;

        this.versionCommits.clear();

        final List<ReleaseTag> releaseTags = new ArrayList<>();
        final List<Tag> tags = changelog.getTags();
        ReleaseTag releaseTag = null;
        Version lastVersion = null;

        for (final Tag tag : tags) {
            final Version tagVersion = Version.isValidVersion(tag.getName())
                ? Version.parseVersion(tag.getName(), true)
                : null;

            if (lastVersion == null) lastVersion = tagVersion;

            final ReleaseTag section = this.processTag(tag, tagVersion, lastVersion);

            if (releaseTag == null
                || releaseTag.getVersion() == null
                || section.getVersion() == null
                || Version.compare(releaseTag.getVersion(), section.getVersion()) != 0) {
                releaseTag = section;
            }

            releaseTags.add(releaseTag);
        }

        if (lastVersion == null) lastVersion = this.releaseLogSettings.getLastVersion();

        final Version nextVersion = this.versionManager.deriveNextVersion(lastVersion, this.versionCommits);

        return new ReleaseLog(nextVersion, lastVersion, releaseTags);
    }

    private ReleaseTag processTag(final Tag tag, final Version tagVersion, final Version lastVersion) {
        final ReleaseDate releaseDate = this.getReleaseDate(tag);
        final List<ReleaseSection> groups = this.getGroups(tag, lastVersion);

        return new ReleaseTag(tagVersion, releaseDate, null, groups);
    }

    private List<ReleaseSection> getGroups(final Tag tag, final Version lastVersion) {
        final Map<String, List<ReleaseCommit>> map = new ConcurrentHashMap<>();

        for (final Commit item : tag.getCommits()) {
            ReleaseCommit commit = null;

            try {
                final CommitParser commitParser = new CommitParser(this.releaseLogSettings, item);

                commit = commitParser.parse();
            } catch (Exception e) {
                this.log.debug(e.getMessage(), e);
            }

            if (commit == null || StringUtils.isEmpty(commit.getCommitSubject())) continue;

            if (lastVersion == null) this.versionCommits.add(commit);

            if (this.releaseLogSettings.getHiddenTypes().contains(commit.getCommitType())) continue;

            final String groupTitle = ReleaseSections.fromCommitType(commit.getCommitType(), commit.isBreakingChange());

            if (map.containsKey(groupTitle)) {
                map.get(groupTitle).add(commit);
            } else {
                final List<ReleaseCommit> releaseCommits = new ArrayList<>();
                releaseCommits.add(commit);

                map.put(groupTitle, releaseCommits);
            }
        }

        final List<ReleaseSection> commitGroups = new ArrayList<>();

        map.forEach((key, value) -> {
            final ReleaseSection releaseSection = new ReleaseSection(key, value);

            commitGroups.add(releaseSection);
        });

        return commitGroups;
    }

    private ReleaseDate getReleaseDate(final Tag tag) {
        if (tag.isHasTagTime()) {
            try {
                final Date tagTime = DateUtils.parseDate(tag.getTagTime(), this.builderSettings.getDateFormat());
                final String longDateFormat = this.releaseLogSettings.getLongDateFormat();
                final String shortDateFormat = this.releaseLogSettings.getShortDateFormat();

                return new ReleaseDate(tagTime, longDateFormat, shortDateFormat);
            } catch (ParseException e) {
                this.log.debug(e);
            }
        }

        return null;
    }
}
