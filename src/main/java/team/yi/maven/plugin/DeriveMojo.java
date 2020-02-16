package team.yi.maven.plugin;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import se.bjurr.gitchangelog.api.GitChangelogApi;
import se.bjurr.gitchangelog.api.exceptions.GitChangelogRepositoryException;
import team.yi.maven.plugin.config.ReleaseLogSettings;
import team.yi.maven.plugin.model.ReleaseLog;
import team.yi.maven.plugin.service.ReleaseLogService;

@Mojo(name = "derive", defaultPhase = LifecyclePhase.VALIDATE)
public class DeriveMojo extends GitChangelogMojo {
    @Override
    public void execute(final GitChangelogApi builder) throws GitChangelogRepositoryException {
        final Log log = this.getLog();
        final ReleaseLogSettings releaseLogSettings = this.getReleaseLogSettings();

        if (log.isDebugEnabled()) {
            final Gson gson = new Gson();
            final String json = gson.toJson(releaseLogSettings);

            log.debug(json);
        }

        if (releaseLogSettings == null || releaseLogSettings.getDisabled()) {
            log.warn("derive is disabled.");

            return;
        }

        final ReleaseLogService releaseLogService = new ReleaseLogService(releaseLogSettings, builder, log);
        final ReleaseLog releaseLog = releaseLogService.generate();

        if (releaseLog == null) return;
        if (releaseLog.getNextVersion() == null) return;

        final String derivedVersionMark = releaseLogSettings.getDerivedVersionMark();

        if (StringUtils.isEmpty(derivedVersionMark)) {
            if (log.isInfoEnabled()) {
                log.info(releaseLog.getNextVersion().toString());
            }
        } else {
            if (log.isInfoEnabled()) {
                log.info(derivedVersionMark + releaseLog.getNextVersion().toString());
            }
        }
    }
}
