package team.yi.maven.plugin;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import team.yi.tools.semanticgitlog.GitlogService;
import team.yi.tools.semanticgitlog.config.GitlogSettings;
import team.yi.tools.semanticgitlog.git.GitRepo;
import team.yi.tools.semanticgitlog.model.ReleaseLog;

import java.io.IOException;

@Mojo(name = "derive", defaultPhase = LifecyclePhase.VALIDATE)
public class DeriveMojo extends GitChangelogMojo {
    @Override
    public void execute(final GitRepo gitRepo) throws IOException {
        final Log log = this.getLog();
        final GitlogSettings gitlogSettings = this.getGitlogSettings();
        final GitlogService gitlogService = new GitlogService(gitlogSettings, gitRepo);
        final ReleaseLog releaseLog = gitlogService.generate();

        if (releaseLog == null) return;

        this.exportJson(releaseLog);

        if (releaseLog.getNextVersion() == null) return;

        final String derivedVersionMark = gitlogSettings.getDerivedVersionMark();

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
