package team.yi.maven.plugin;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import se.bjurr.gitchangelog.api.GitChangelogApi;
import se.bjurr.gitchangelog.api.exceptions.GitChangelogIntegrationException;
import se.bjurr.gitchangelog.api.exceptions.GitChangelogRepositoryException;
import team.yi.maven.plugin.config.FileSet;
import team.yi.maven.plugin.config.ReleaseLogSettings;
import team.yi.maven.plugin.service.ReleaseLogService;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.apache.maven.plugins.annotations.LifecyclePhase.PROCESS_SOURCES;

@Mojo(name = "changelog", defaultPhase = PROCESS_SOURCES)
public class ChangelogMojo extends GitChangelogMojo {
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

    @Override
    public void execute(GitChangelogApi builder) throws IOException, GitChangelogRepositoryException, GitChangelogIntegrationException {
        final Log log = this.getLog();

        this.saveToFile(log, builder);
        this.saveToMediaWiki(log, builder);
    }

    private void saveToFile(Log log, GitChangelogApi builder) throws IOException, GitChangelogRepositoryException {
        if (this.fileSets == null) this.fileSets = new HashSet<>();

        if (this.fileSets.isEmpty() && !this.isSupplied(this.mediaWikiUrl)) {
            if (log.isInfoEnabled()) {
                log.info("No output set, using file " + ReleaseLogSettings.DEFAULT_TARGET_FILE);
            }

            File template = new File(ReleaseLogSettings.DEFAULT_TEMPLATE_FILE);
            File target = new File(ReleaseLogSettings.DEFAULT_TARGET_FILE);

            this.fileSets.add(new FileSet(template, target));
        }

        if (this.fileSets.isEmpty()) return;

        final ReleaseLogSettings releaseLogSettings = this.getReleaseLogSettings();

        if (releaseLogSettings.getDisabled()) {
            for (FileSet fileSet : this.fileSets) {
                File target = fileSet.getTarget();
                File template = fileSet.getTemplate();

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
            final ReleaseLogService releaseLogService = new ReleaseLogService(releaseLogSettings, builder, log);

            releaseLogService.saveToFiles(this.fileSets);
        }
    }

    private void saveToMediaWiki(Log log, GitChangelogApi builder) throws GitChangelogRepositoryException, GitChangelogIntegrationException {
        if (!isSupplied(mediaWikiUrl)) return;

        builder.toMediaWiki(mediaWikiUsername, mediaWikiPassword, mediaWikiUrl, mediaWikiTitle);

        if (log.isInfoEnabled()) {
            log.info("#");
            log.info("#     created: " + mediaWikiUrl + "/index.php/" + mediaWikiTitle);
            log.info("#");
        }
    }
}
