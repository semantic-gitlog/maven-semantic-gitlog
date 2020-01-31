package team.yi.maven.plugin;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import se.bjurr.gitchangelog.api.GitChangelogApi;
import team.yi.maven.plugin.model.GitReleaseLog;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Date;

import static com.google.common.base.Strings.isNullOrEmpty;
import static se.bjurr.gitchangelog.api.GitChangelogApi.gitChangelogApiBuilder;

@SuppressWarnings("PMD.TooManyFields")
@Mojo(name = "derive", defaultPhase = LifecyclePhase.VALIDATE)
public class DeriveMojo extends AbstractMojo {
    @Parameter(property = "toRef")
    private String toRef;

    @Parameter(property = "toCommit")
    private String toCommit;

    @Parameter(property = "fromRef")
    private String fromRef;

    @Parameter(property = "fromCommit")
    private String fromCommit;

    @Parameter(property = "settingsFile")
    private String settingsFile;

    @Parameter(property = "templateFile")
    private String templateFile;

    @Parameter(property = "templateContent")
    private String templateContent;

    @Parameter(property = "readableTagName")
    private String readableTagName;

    @Parameter(property = "ignoreTagsIfNameMatches")
    private String ignoreTagsIfNameMatches;

    @Parameter(property = "dateFormat")
    private String dateFormat;

    @Parameter(property = "timeZone")
    private String timeZone;

    @Parameter(property = "removeIssueFromMessage")
    private boolean removeIssueFromMessage;

    @Parameter(property = "ignoreCommitsIfMessageMatches")
    private String ignoreCommitsIfMessageMatches;

    @Parameter(property = "ignoreCommitsOlderThan")
    private Date ignoreCommitsOlderThan;

    @Parameter(property = "untaggedName")
    private String untaggedName;

    @Parameter(property = "noIssueName")
    private String noIssueName;

    @Parameter(property = "ignoreCommitsWithoutIssue")
    private Boolean ignoreCommitsWithoutIssue;

    @Parameter(property = "skip")
    private Boolean skip;

    @Parameter(property = "gitReleaseLogSettings")
    private GitReleaseLogSettings gitReleaseLogSettings;

    public GitReleaseLogSettings getGitReleaseLogSettings() {
        if (gitReleaseLogSettings == null) gitReleaseLogSettings = new GitReleaseLogSettings();

        return gitReleaseLogSettings;
    }

    @Override
    public void execute() throws MojoFailureException {
        Log log = this.getLog();

        if (skip != null && skip) {
            log.info("Skipping changelog generation");

            return;
        }

        GitReleaseLogSettings gitReleaseLogSettings = this.getGitReleaseLogSettings();

        if (gitReleaseLogSettings == null || gitReleaseLogSettings.getDisabled()) {
            log.warn("gitReleaseLogSettings is disabled.");

            return;
        }

        try {
            GitChangelogApi builder = this.createGitChangelogApi();
            GitReleaseLogService gitReleaseLogService = new GitReleaseLogService(gitReleaseLogSettings, builder, getLog());

            GitReleaseLog gitReleaseLog = gitReleaseLogService.generate();

            if (gitReleaseLog == null) return;
            if (gitReleaseLog.getNewVersion() == null) return;

            String derivedVersionMark = gitReleaseLogSettings.getDerivedVersionMark();

            if (StringUtils.isEmpty(derivedVersionMark)) {
                if (log.isInfoEnabled()) {
                    log.info(gitReleaseLog.getNewVersion().toString());
                }
            } else {
                if (log.isInfoEnabled()) {
                    log.info(derivedVersionMark + gitReleaseLog.getNewVersion().toString());
                }
            }
        } catch (Exception e) {
            log.debug(e);

            throw new MojoFailureException(e.getMessage(), e);
        }
    }

    @SuppressWarnings("PMD.NPathComplexity")
    private GitChangelogApi createGitChangelogApi() throws MalformedURLException {
        GitChangelogApi builder = gitChangelogApiBuilder();

        if (isSupplied(settingsFile)) builder.withSettings(new File(settingsFile).toURI().toURL());
        if (isSupplied(toRef)) builder.withToRef(toRef);
        if (isSupplied(templateFile)) builder.withTemplatePath(templateFile);
        if (isSupplied(templateContent)) builder.withTemplateContent(templateContent);
        if (isSupplied(fromCommit)) builder.withFromCommit(fromCommit);
        if (isSupplied(fromRef)) builder.withFromRef(fromRef);
        if (isSupplied(toCommit)) builder.withToCommit(toCommit);
        if (isSupplied(ignoreTagsIfNameMatches)) builder.withIgnoreTagsIfNameMatches(ignoreTagsIfNameMatches);
        if (isSupplied(readableTagName)) builder.withReadableTagName(readableTagName);
        if (isSupplied(dateFormat)) builder.withDateFormat(dateFormat);
        if (isSupplied(timeZone)) builder.withTimeZone(timeZone);

        builder.withRemoveIssueFromMessageArgument(removeIssueFromMessage);

        if (isSupplied(ignoreCommitsIfMessageMatches)) builder.withIgnoreCommitsWithMessage(ignoreCommitsIfMessageMatches);
        if (ignoreCommitsOlderThan != null) builder.withIgnoreCommitsOlderThan(ignoreCommitsOlderThan);
        if (isSupplied(untaggedName)) builder.withUntaggedName(untaggedName);
        if (isSupplied(noIssueName)) builder.withNoIssueName(noIssueName);
        if (ignoreCommitsWithoutIssue != null) builder.withIgnoreCommitsWithoutIssue(ignoreCommitsWithoutIssue);

        return builder;
    }

    private boolean isSupplied(String parameter) {
        return !isNullOrEmpty(parameter);
    }
}
