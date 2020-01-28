package team.yi.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import se.bjurr.gitchangelog.api.GitChangelogApi;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.apache.maven.plugins.annotations.LifecyclePhase.PROCESS_SOURCES;
import static se.bjurr.gitchangelog.api.GitChangelogApi.gitChangelogApiBuilder;

@SuppressWarnings("PMD.TooManyFields")
@Mojo(name = "git-changelog", defaultPhase = PROCESS_SOURCES)
public class GitChangelogMojo extends AbstractMojo {
    private static final String DEFAULT_FILE = "CHANGELOG.md";

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

    @Parameter(property = "file")
    private File file;

    @Parameter(property = "mediaWikiUrl")
    private String mediaWikiUrl;

    @Parameter(property = "mediaWikiTitle")
    private String mediaWikiTitle;

    @Parameter(property = "mediaWikiUsername")
    private String mediaWikiUsername;

    @Parameter(property = "mediaWikiPassword")
    private String mediaWikiPassword;

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

    @Parameter(property = "gitHubApi")
    private String gitHubApi;

    @Parameter(property = "gitHubToken")
    private String gitHubToken;

    @Parameter(property = "gitHubIssuePattern")
    private String gitHubIssuePattern;

    @Parameter(property = "gitLabServer")
    private String gitLabServer;

    @Parameter(property = "gitLabProjectName")
    private String gitLabProjectName;

    @Parameter(property = "gitLabToken")
    private String gitLabToken;

    @Parameter(property = "jiraIssuePattern")
    private String jiraIssuePattern;

    @Parameter(property = "jiraPassword")
    private String jiraPassword;

    @Parameter(property = "jiraServer")
    private String jiraServer;

    @Parameter(property = "jiraUsername")
    private String jiraUsername;

    @Parameter(property = "ignoreCommitsWithoutIssue")
    private Boolean ignoreCommitsWithoutIssue;

    @Parameter(property = "customIssues")
    private List<CustomIssue> customIssues;

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

        try {
            GitChangelogApi builder = this.createGitChangelogApi();

            if (file == null && !isSupplied(mediaWikiUrl)) {
                log.info("No output set, using file " + DEFAULT_FILE);

                file = new File(DEFAULT_FILE);
            }

            if (file != null) {
                GitReleaseLogSettings gitReleaseLogSettings = this.getGitReleaseLogSettings();

                if (gitReleaseLogSettings.getDisabled()) {
                    builder.toFile(file);
                } else {
                    GitReleaseLogService gitReleaseLogService = new GitReleaseLogService(this.gitReleaseLogSettings, builder, log);

                    gitReleaseLogService.saveToFile(file);
                }

                log.info("#");
                log.info("# Wrote: " + file);
                log.info("#");
            }

            if (isSupplied(mediaWikiUrl)) {
                builder.toMediaWiki(
                    mediaWikiUsername,
                    mediaWikiPassword,
                    mediaWikiUrl,
                    mediaWikiTitle);
                log.info("#");
                log.info("# Created: " + mediaWikiUrl + "/index.php/" + mediaWikiTitle);
                log.info("#");
            }
        } catch (final Exception e) {
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

    private boolean isSupplied(String parameter) {
        return !isNullOrEmpty(parameter);
    }
}
