package team.yi.maven.plugin;

import de.skuzzle.semantic.Version;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import team.yi.tools.semanticgitlog.GitlogConstants;
import team.yi.tools.semanticgitlog.config.GitlogSettings;
import team.yi.tools.semanticgitlog.config.ReleaseStrategy;
import team.yi.tools.semanticgitlog.git.GitRepo;
import team.yi.tools.semanticgitlog.model.ReleaseLog;
import team.yi.tools.semanticgitlog.render.JsonGitlogRender;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@SuppressWarnings("PMD.TooManyFields")
public abstract class GitChangelogMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", readonly = true)
    protected MavenProject project;

    @Parameter(property = "gitlog.skip")
    protected Boolean skip;

    @Parameter(property = "gitlog.defaultLang")
    protected String defaultLang;
    @Parameter
    protected Map<String, File> commitLocales;

    @Parameter(property = "gitlog.closeIssueActions")
    protected String closeIssueActions;
    @Parameter(property = "gitlog.issueUrlTemplate", defaultValue = "${project.scm.url}/issues/:issueId")
    protected String issueUrlTemplate;
    @Parameter(property = "gitlog.commitUrlTemplate", defaultValue = "${project.scm.url}/commit/:commitId")
    protected String commitUrlTemplate;
    @Parameter(property = "gitlog.mentionUrlTemplate", defaultValue = "https://github.com/:username")
    protected String mentionUrlTemplate;

    @Parameter(property = "gitlog.fromRef")
    protected String fromRef;
    @Parameter(property = "gitlog.fromCommit")
    protected String fromCommit;
    @Parameter(property = "gitlog.toRef")
    protected String toRef;
    @Parameter(property = "gitlog.toCommit")
    protected String toCommit;

    @Parameter(property = "gitlog.strategy", defaultValue = "strict")
    protected ReleaseStrategy strategy;
    @Parameter(property = "gitlog.untaggedName", defaultValue = GitlogConstants.DEFAULT_UNTAGGED_NAME)
    protected String untaggedName;
    @Parameter(property = "gitlog.isUnstable")
    protected Boolean isUnstable;
    @Parameter(property = "gitlog.forceNextVersion")
    protected Boolean forceNextVersion;

    @Parameter(property = "gitlog.lastVersion")
    protected String lastVersion;
    @Parameter(property = "gitlog.preRelease")
    protected String preRelease;
    @Parameter(property = "gitlog.buildMetaData")
    protected String buildMetaData;
    @Parameter(property = "gitlog.majorTypes")
    protected String majorTypes;
    @Parameter(property = "gitlog.minorTypes")
    protected String minorTypes;
    @Parameter(property = "gitlog.patchTypes")
    protected String patchTypes;
    @Parameter(property = "gitlog.preReleaseTypes")
    protected String preReleaseTypes;
    @Parameter(property = "gitlog.buildMetaDataTypes")
    protected String buildMetaDataTypes;
    @Parameter(property = "gitlog.hiddenTypes")
    protected String hiddenTypes;

    @Parameter(property = "gitlog.jsonFile")
    protected File jsonFile;

    protected GitlogSettings getGitlogSettings() {
        final Version lastVersion = Version.isValidVersion(this.lastVersion) ? Version.parseVersion(this.lastVersion) : null;

        return GitlogSettings.builder()
            .defaultLang(this.defaultLang)
            .commitLocales(this.commitLocales)

            .closeIssueActions(this.closeIssueActions)
            .issueUrlTemplate(this.issueUrlTemplate)
            .commitUrlTemplate(this.commitUrlTemplate)
            .mentionUrlTemplate(this.mentionUrlTemplate)

            .fromRef(this.fromRef)
            .fromCommit(this.fromCommit)
            .toRef(this.toRef)
            .toCommit(this.toCommit)

            .strategy(this.strategy)
            .untaggedName(this.untaggedName)
            .isUnstable(this.isUnstable)
            .forceNextVersion(this.forceNextVersion)

            .lastVersion(lastVersion)
            .preRelease(this.preRelease)
            .buildMetaData(this.buildMetaData)
            .majorTypes(this.majorTypes)
            .minorTypes(this.minorTypes)
            .patchTypes(this.patchTypes)
            .preReleaseTypes(this.preReleaseTypes)
            .buildMetaDataTypes(this.buildMetaDataTypes)
            .hiddenTypes(this.hiddenTypes)

            .build();
    }

    @Override
    public final void execute() throws MojoFailureException {
        final Log log = this.getLog();

        if (this.skip != null && this.skip) {
            log.info("Skipping changelog generation");

            return;
        }

        final File baseDir = this.project.getBasedir();

        try (final GitRepo gitRepo = GitRepo.open(baseDir)) {
            this.execute(gitRepo);
        } catch (final Exception e) {
            log.debug(e);

            throw new MojoFailureException(e.getMessage(), e);
        }
    }

    protected abstract void execute(final GitRepo gitRepo) throws IOException;

    protected void exportJson(final ReleaseLog releaseLog) throws IOException {
        if (this.jsonFile == null) return;

        final JsonGitlogRender render = new JsonGitlogRender(releaseLog, StandardCharsets.UTF_8);

        render.render(this.jsonFile);
    }
}
