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
    protected static final String DEFAULT_FILE = "CHANGELOG.md";

    @Parameter(property = "toRef")
    protected String toRef;

    @Parameter(property = "toCommit")
    protected String toCommit;

    @Parameter(property = "fromRef")
    protected String fromRef;

    @Parameter(property = "fromCommit")
    protected String fromCommit;

    @Parameter(property = "settingsFile")
    protected String settingsFile;

    @Parameter(property = "templateFile")
    protected String templateFile;

    @Parameter(property = "templateContent")
    protected String templateContent;

    @Parameter(property = "file")
    protected File file;

    @Parameter(property = "mediaWikiUrl")
    protected String mediaWikiUrl;

    @Parameter(property = "mediaWikiTitle")
    protected String mediaWikiTitle;

    @Parameter(property = "mediaWikiUsername")
    protected String mediaWikiUsername;

    @Parameter(property = "mediaWikiPassword")
    protected String mediaWikiPassword;

    @Parameter(property = "readableTagName")
    protected String readableTagName;

    @Parameter(property = "ignoreTagsIfNameMatches")
    protected String ignoreTagsIfNameMatches;

    @Parameter(property = "dateFormat")
    protected String dateFormat;

    @Parameter(property = "timeZone")
    protected String timeZone;

    @Parameter(property = "removeIssueFromMessage")
    protected boolean removeIssueFromMessage;

    @Parameter(property = "ignoreCommitsIfMessageMatches")
    protected String ignoreCommitsIfMessageMatches;

    @Parameter(property = "ignoreCommitsOlderThan")
    protected Date ignoreCommitsOlderThan;

    @Parameter(property = "untaggedName")
    protected String untaggedName;

    @Parameter(property = "noIssueName")
    protected String noIssueName;

    @Parameter(property = "gitHubApi")
    protected String gitHubApi;

    @Parameter(property = "gitHubToken")
    protected String gitHubToken;

    @Parameter(property = "gitHubIssuePattern")
    protected String gitHubIssuePattern;

    @Parameter(property = "gitLabServer")
    protected String gitLabServer;

    @Parameter(property = "gitLabProjectName")
    protected String gitLabProjectName;

    @Parameter(property = "gitLabToken")
    protected String gitLabToken;

    @Parameter(property = "jiraIssuePattern")
    protected String jiraIssuePattern;

    @Parameter(property = "jiraPassword")
    protected String jiraPassword;

    @Parameter(property = "jiraServer")
    protected String jiraServer;

    @Parameter(property = "jiraUsername")
    protected String jiraUsername;

    @Parameter(property = "ignoreCommitsWithoutIssue")
    protected Boolean ignoreCommitsWithoutIssue;

    @Parameter(property = "customIssues")
    protected List<CustomIssue> customIssues;

    @Parameter(property = "skip")
    protected Boolean skip;

    @Parameter(property = "releaseLogSettings")
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
