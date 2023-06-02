package team.yi.maven.plugin;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import team.yi.tools.semanticgitlog.config.GitlogSettings;
import team.yi.tools.semanticgitlog.git.GitRepo;
import team.yi.tools.semanticgitlog.model.ReleaseLog;
import team.yi.tools.semanticgitlog.render.MustacheGitlogRender;
import team.yi.tools.semanticgitlog.service.CommitLocaleService;
import team.yi.tools.semanticgitlog.service.GitlogService;
import team.yi.tools.semanticgitlog.service.ScopeProfileService;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.maven.plugins.annotations.LifecyclePhase.PROCESS_SOURCES;

@Slf4j
@Mojo(name = "changelog", defaultPhase = PROCESS_SOURCES)
public class ChangelogMojo extends GitChangelogMojo {
    public static final String DEFAULT_TEMPLATE_FILE = "config/gitlog/CHANGELOG.md.mustache";
    public static final String DEFAULT_TARGET_FILE = "CHANGELOG.md";

    @Parameter
    protected Set<FileSet> fileSets;

    public Set<FileSet> getFileSets() {
        if (this.fileSets == null || this.fileSets.isEmpty()) return fileSets;

        return this.fileSets.stream()
            .filter(x -> x.getTemplate() != null)
            .filter(x -> x.getTarget() != null)
            .collect(Collectors.toSet());
    }

    @Override
    public void execute(final GitRepo gitRepo) throws IOException {
        final Log log = this.getLog();

        this.saveToFile(log, gitRepo);
    }

    private void saveToFile(final Log log, final GitRepo gitRepo) throws IOException {
        final Set<FileSet> fileSets = new HashSet<>(this.getFileSets());

        if (fileSets.isEmpty()) {
            if (log.isInfoEnabled()) {
                log.info("No output set, using file " + DEFAULT_TARGET_FILE);
            }

            final File template = new File(DEFAULT_TEMPLATE_FILE);
            final File target = new File(DEFAULT_TARGET_FILE);

            fileSets.add(new FileSet(template, target));
        }

        final GitlogSettings gitlogSettings = this.getGitlogSettings();

        final CommitLocaleService commitLocaleProvider = new CommitLocaleService(gitlogSettings.getDefaultLang());
        commitLocaleProvider.load(gitlogSettings.getCommitLocales());

        final GitlogService gitlogService = new GitlogService(gitlogSettings, gitRepo, commitLocaleProvider);
        final ReleaseLog releaseLog = gitlogService.generate();

        final ScopeProfileService scopeProfileService = new ScopeProfileService(gitlogSettings.getDefaultLang());
        scopeProfileService.load(gitlogSettings.getScopeProfiles());

        this.saveToFiles(releaseLog, fileSets, scopeProfileService);
        this.updatePom(releaseLog);
        this.exportJson(releaseLog);
    }

    private void saveToFiles(final ReleaseLog releaseLog, final Set<FileSet> fileSets, final ScopeProfileService scopeProfileService) throws IOException {
        if (fileSets == null || fileSets.isEmpty()) return;

        for (final FileSet fileSet : fileSets) {
            final File target = fileSet.getTarget();
            final File template = fileSet.getTemplate();
            final MustacheGitlogRender render = new MustacheGitlogRender(releaseLog, template, scopeProfileService);

            render.render(target);
        }
    }
}
