package team.yi.maven.plugin;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import se.bjurr.gitchangelog.api.GitChangelogApi;
import se.bjurr.gitchangelog.api.exceptions.GitChangelogIntegrationException;
import se.bjurr.gitchangelog.api.exceptions.GitChangelogRepositoryException;
import team.yi.maven.plugin.config.GitlogPluginSettings;
import team.yi.tools.semanticgitlog.GitlogService;
import team.yi.tools.semanticgitlog.config.FileSet;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.maven.plugins.annotations.LifecyclePhase.PROCESS_SOURCES;

@Mojo(name = "changelog", defaultPhase = PROCESS_SOURCES)
public class ChangelogMojo extends GitChangelogMojo {
    public static final String DEFAULT_TEMPLATE_FILE = "config/gitlog/CHANGELOG.md.mustache";
    public static final String DEFAULT_TARGET_FILE = "CHANGELOG.md";

    @Parameter(property = "gitlog.fileSets")
    protected Set<FileSet> fileSets;

    @Parameter(property = "gitlog.mediaWikiUrl")
    protected String mediaWikiUrl;

    @Parameter(property = "gitlog.mediaWikiTitle")
    protected String mediaWikiTitle;

    @Parameter(property = "gitlog.mediaWikiUsername")
    protected String mediaWikiUsername;

    @Parameter(property = "gitlog.mediaWikiPassword")
    protected String mediaWikiPassword;

    public Set<FileSet> getFileSets() {
        if (this.fileSets == null || this.fileSets.isEmpty()) return fileSets;

        return this.fileSets.stream()
            .filter(x -> x.getTemplate() != null)
            .filter(x -> x.getTarget() != null)
            .collect(Collectors.toSet());
    }

    @Override
    public void execute(final GitChangelogApi builder)
        throws IOException,
        GitChangelogRepositoryException,
        GitChangelogIntegrationException {

        final Log log = this.getLog();

        this.saveToFile(log, builder);
        this.saveToMediaWiki(log, builder);
    }

    private void saveToFile(final Log log, final GitChangelogApi builder)
        throws IOException,
        GitChangelogRepositoryException {

        Set<FileSet> fileSets = this.getFileSets();

        if (fileSets == null) fileSets = new HashSet<>();

        if (fileSets.isEmpty() && !this.isSupplied(this.mediaWikiUrl)) {
            if (log.isInfoEnabled()) {
                log.info("No output set, using file " + DEFAULT_TARGET_FILE);
            }

            final File template = new File(DEFAULT_TEMPLATE_FILE);
            final File target = new File(DEFAULT_TARGET_FILE);

            fileSets.add(new FileSet(template, target));
        }

        if (fileSets.isEmpty()) return;

        final GitlogPluginSettings releaseLogSettings = this.getReleaseLogSettings();

        if (releaseLogSettings.getDisabled()) {
            for (final FileSet fileSet : fileSets) {
                final File target = fileSet.getTarget();
                final File template = fileSet.getTemplate();

                builder.withTemplatePath(template.getPath());
                builder.toFile(target);

                if (log.isInfoEnabled()) {
                    log.info("#");
                    log.info("#    template: " + template.getPath());
                    log.info("#      target: " + target.getPath());
                    log.info("#");
                }
            }
        } else {
            final GitlogService releaseLogService = new GitlogService(releaseLogSettings, builder);

            releaseLogService.saveToFiles(fileSets);
        }
    }

    private void saveToMediaWiki(final Log log, final GitChangelogApi builder)
        throws GitChangelogRepositoryException,
        GitChangelogIntegrationException {

        if (!isSupplied(mediaWikiUrl)) return;

        builder.toMediaWiki(mediaWikiUsername, mediaWikiPassword, mediaWikiUrl, mediaWikiTitle);

        if (log.isInfoEnabled()) {
            log.info("#");
            log.info("#     created: " + mediaWikiUrl + "/index.php/" + mediaWikiTitle);
            log.info("#");
        }
    }
}
