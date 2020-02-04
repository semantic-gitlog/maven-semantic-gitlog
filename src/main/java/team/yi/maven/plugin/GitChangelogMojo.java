package team.yi.maven.plugin;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Parameter;
import se.bjurr.gitchangelog.api.GitChangelogApi;
import se.bjurr.gitchangelog.api.exceptions.GitChangelogIntegrationException;
import se.bjurr.gitchangelog.api.exceptions.GitChangelogRepositoryException;
import team.yi.maven.plugin.config.CustomIssue;
import team.yi.maven.plugin.config.ReleaseLogSettings;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import static se.bjurr.gitchangelog.api.GitChangelogApi.gitChangelogApiBuilder;

@SuppressWarnings("PMD.TooManyFields")
public abstract class GitChangelogMojo extends AbstractMojo {
    @Parameter(property = "gitlog.toRef")
    protected String toRef;

    @Parameter(property = "gitlog.toCommit")
    protected String toCommit;

    @Parameter(property = "gitlog.fromRef")
    protected String fromRef;

    @Parameter(property = "gitlog.fromCommit")
    protected String fromCommit;

    @Parameter(property = "gitlog.settingsFile")
    protected String settingsFile;

    @Parameter(property = "gitlog.readableTagName")
    protected String readableTagName;

    @Parameter(property = "gitlog.ignoreTagsIfNameMatches")
    protected String ignoreTagsIfNameMatches;

    @Parameter(property = "gitlog.dateFormat")
    protected String dateFormat;

    @Parameter(property = "gitlog.timeZone")
    protected String timeZone;

    @Parameter(property = "gitlog.removeIssueFromMessage")
    protected boolean removeIssueFromMessage;

    @Parameter(property = "gitlog.ignoreCommitsIfMessageMatches")
    protected String ignoreCommitsIfMessageMatches;

    @Parameter(property = "gitlog.ignoreCommitsOlderThan")
    protected Date ignoreCommitsOlderThan;

    @Parameter(property = "gitlog.untaggedName")
    protected String untaggedName;

    @Parameter(property = "gitlog.noIssueName")
    protected String noIssueName;

    @Parameter(property = "gitlog.gitHubApi")
    protected String gitHubApi;

    @Parameter(property = "gitlog.gitHubToken")
    protected String gitHubToken;

    @Parameter(property = "gitlog.gitHubIssuePattern")
    protected String gitHubIssuePattern;

    @Parameter(property = "gitlog.gitLabServer")
    protected String gitLabServer;

    @Parameter(property = "gitlog.gitLabProjectName")
    protected String gitLabProjectName;

    @Parameter(property = "gitlog.gitLabToken")
    protected String gitLabToken;

    @Parameter(property = "gitlog.jiraIssuePattern")
    protected String jiraIssuePattern;

    @Parameter(property = "gitlog.jiraPassword")
    protected String jiraPassword;

    @Parameter(property = "gitlog.jiraServer")
    protected String jiraServer;

    @Parameter(property = "gitlog.jiraUsername")
    protected String jiraUsername;

    @Parameter(property = "gitlog.ignoreCommitsWithoutIssue")
    protected Boolean ignoreCommitsWithoutIssue;

    @Parameter(property = "gitlog.customIssues")
    protected List<CustomIssue> customIssues;

    @Parameter(property = "gitlog.skip")
    protected Boolean skip;

    @Parameter
    private ReleaseLogSettings releaseLogSettings;

    protected ReleaseLogSettings getReleaseLogSettings() {
        if (releaseLogSettings == null) releaseLogSettings = new ReleaseLogSettings();

        return releaseLogSettings;
    }

    @Override
    public final void execute() throws MojoFailureException {
        final Log log = this.getLog();

        if (this.skip != null && this.skip) {
            log.info("Skipping changelog generation");

            return;
        }

        try {
            final GitChangelogApi builder = this.createGitChangelogApi();

            this.execute(builder);
        } catch (final Exception e) {
            log.debug(e);

            throw new MojoFailureException(e.getMessage(), e);
        }
    }

    protected abstract void execute(GitChangelogApi builder) throws MojoExecutionException,
        MojoFailureException,
        IOException,
        GitChangelogRepositoryException,
        GitChangelogIntegrationException;

    @SuppressWarnings("PMD.NPathComplexity")
    private GitChangelogApi createGitChangelogApi() throws MalformedURLException {
        final GitChangelogApi builder = gitChangelogApiBuilder();

        if (isSupplied(settingsFile)) builder.withSettings(new File(settingsFile).toURI().toURL());
        if (isSupplied(toRef)) builder.withToRef(toRef);
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

        for (final CustomIssue customIssue : customIssues) {
            builder.withCustomIssue(
                customIssue.getName(),
                customIssue.getPattern(),
                customIssue.getLink(),
                customIssue.getTitle());
        }

        if (isSupplied(gitHubApi)) builder.withGitHubApi(gitHubApi);
        if (isSupplied(gitHubToken)) builder.withGitHubToken(gitHubToken);
        if (isSupplied(gitHubIssuePattern)) builder.withGitHubIssuePattern(gitHubIssuePattern);

        if (isSupplied(gitLabProjectName)) builder.withGitLabProjectName(gitLabProjectName);
        if (isSupplied(gitLabServer)) builder.withGitLabServer(gitLabServer);
        if (isSupplied(gitLabToken)) builder.withGitLabToken(gitLabToken);

        if (isSupplied(jiraUsername)) builder.withJiraUsername(jiraUsername);
        if (isSupplied(jiraPassword)) builder.withJiraPassword(jiraPassword);
        if (isSupplied(jiraIssuePattern)) builder.withJiraIssuePattern(jiraIssuePattern);
        if (isSupplied(jiraServer)) builder.withJiraServer(jiraServer);

        return builder;
    }

    protected boolean isSupplied(String parameter) {
        return StringUtils.isNotEmpty(parameter);
    }
}
