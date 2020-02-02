package team.yi.maven.plugin;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import se.bjurr.gitchangelog.api.GitChangelogApi;
import se.bjurr.gitchangelog.api.exceptions.GitChangelogIntegrationException;
import se.bjurr.gitchangelog.api.exceptions.GitChangelogRepositoryException;
import team.yi.maven.plugin.config.ReleaseLogSettings;
import team.yi.maven.plugin.service.ReleaseLogService;

import java.io.File;
import java.io.IOException;

import static org.apache.maven.plugins.annotations.LifecyclePhase.PROCESS_SOURCES;

@Mojo(name = "changelog", defaultPhase = PROCESS_SOURCES)
public class ChangelogMojo extends GitChangelogMojo {
    @Override
    public void execute(GitChangelogApi builder) throws IOException, GitChangelogRepositoryException, GitChangelogIntegrationException {
        final Log log = this.getLog();

        this.saveToFile(log, builder);
        this.saveToMediaWiki(log, builder);
    }

    private void saveToFile(Log log, GitChangelogApi builder) throws IOException, GitChangelogRepositoryException {
        if (this.file == null && !this.isSupplied(this.mediaWikiUrl)) {
            if (log.isInfoEnabled()) {
                log.info("No output set, using file " + DEFAULT_FILE);
            }

            this.file = new File(DEFAULT_FILE);
        }

        if (this.file == null) return;

        final ReleaseLogSettings releaseLogSettings = this.getReleaseLogSettings();

        if (releaseLogSettings.getDisabled()) {
            builder.toFile(file);
        } else {
            final ReleaseLogService releaseLogService = new ReleaseLogService(releaseLogSettings, builder, log);

            releaseLogService.saveToFile(file);
        }

        if (log.isInfoEnabled()) {
            log.info("#");
            log.info("# Wrote: " + file);
            log.info("#");
        }
    }

    private void saveToMediaWiki(Log log, GitChangelogApi builder) throws GitChangelogRepositoryException, GitChangelogIntegrationException {
        if (!isSupplied(mediaWikiUrl)) return;

        builder.toMediaWiki(mediaWikiUsername, mediaWikiPassword, mediaWikiUrl, mediaWikiTitle);

        if (log.isInfoEnabled()) {
            log.info("#");
            log.info("# Created: " + mediaWikiUrl + "/index.php/" + mediaWikiTitle);
            log.info("#");
        }
    }
}
