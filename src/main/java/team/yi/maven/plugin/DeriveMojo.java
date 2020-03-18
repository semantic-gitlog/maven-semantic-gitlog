package team.yi.maven.plugin;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import team.yi.tools.semanticgitlog.config.GitlogSettings;
import team.yi.tools.semanticgitlog.git.GitRepo;
import team.yi.tools.semanticgitlog.model.ReleaseLog;
import team.yi.tools.semanticgitlog.service.CommitLocaleService;
import team.yi.tools.semanticgitlog.service.GitlogService;

import java.io.IOException;

@Mojo(name = "derive", defaultPhase = LifecyclePhase.VALIDATE)
public class DeriveMojo extends GitChangelogMojo {
    @Parameter(property = "gitlog.derivedVersionMark", defaultValue = "NEXT_VERSION:==")
    protected String derivedVersionMark;

    @Override
    public void execute(final GitRepo gitRepo) throws IOException {
        final GitlogSettings gitlogSettings = this.getGitlogSettings();

        final CommitLocaleService commitLocaleProvider = new CommitLocaleService(gitlogSettings.getDefaultLang());
        commitLocaleProvider.load(gitlogSettings.getCommitLocales());

        final GitlogService gitlogService = new GitlogService(gitlogSettings, gitRepo, commitLocaleProvider);
        final ReleaseLog releaseLog = gitlogService.generate();

        if (releaseLog == null) return;

        this.exportJson(releaseLog);

        if (releaseLog.getNextVersion() == null) return;

        final Log log = this.getLog();

        if (log == null) return;

        if (StringUtils.isEmpty(this.derivedVersionMark)) {
            if (log.isInfoEnabled()) {
                log.info(releaseLog.getNextVersion().toString());
            }
        } else {
            if (log.isInfoEnabled()) {
                log.info(this.derivedVersionMark + releaseLog.getNextVersion().toString());
            }
        }
    }
}
