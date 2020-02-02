package team.yi.maven.plugin.utils;

import org.apache.commons.lang3.StringUtils;
import se.bjurr.gitchangelog.api.model.Commit;
import team.yi.maven.plugin.config.ReleaseLogSettings;
import team.yi.maven.plugin.model.ReleaseCommit;
import team.yi.maven.plugin.model.ReleaseIssue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReleaseCommitParser {
    private static final Pattern DEFAULT_MESSAGE_PATTERN = Pattern.compile(ReleaseLogSettings.DEFAULT_MESSAGE_PATTERN);
    private static final Pattern DEFAULT_COMMIT_ISSUE_PATTERN = Pattern.compile(ReleaseLogSettings.DEFAULT_COMMIT_ISSUE_PATTERN);
    private static final String BREAKING_CHANGE_PATTERN = "BREAKING CHANGE";
    private static final String DEPRECATION_PATTERN = "DEPRECATION";
    private static final String ISSUE_ID_PLACEHOLDER = ":issueId";
    private static final String COMMIT_ID_PLACEHOLDER = ":commitId";

    private final String commitUrlTemplate;
    private final String issueUrlTemplate;
    private final Pattern commitIssuePattern;
    private final Pattern quickActionPattern;

    @SuppressWarnings("PMD.NullAssignment")
    public ReleaseCommitParser(ReleaseLogSettings releaseLogSettings) {
        this.commitUrlTemplate = releaseLogSettings.getCommitUrlTemplate();
        this.issueUrlTemplate = releaseLogSettings.getIssueUrlTemplate();

        this.commitIssuePattern = StringUtils.isNotEmpty(releaseLogSettings.getCommitIssuePattern())
            ? Pattern.compile(releaseLogSettings.getCommitIssuePattern()) : DEFAULT_COMMIT_ISSUE_PATTERN;

        this.quickActionPattern = StringUtils.isNotEmpty(releaseLogSettings.getQuickActionPattern())
            ? Pattern.compile(releaseLogSettings.getQuickActionPattern(), Pattern.MULTILINE) : null;
    }

    public String createIssueUrl(Integer issueId) {
        if (StringUtils.isEmpty(this.issueUrlTemplate)) return null;

        return this.issueUrlTemplate.replaceAll(ISSUE_ID_PLACEHOLDER, String.valueOf(issueId));
    }

    public String createCommitUrl(String commitId) {
        if (StringUtils.isEmpty(this.commitUrlTemplate)) return null;

        return this.commitUrlTemplate.replaceAll(COMMIT_ID_PLACEHOLDER, commitId);
    }

    public ReleaseCommit parse(Commit commit) {
        final ReleaseCommit releaseCommit = new ReleaseCommit(commit);
        releaseCommit.setShortHash(commit.getHashFull().substring(0, 8));
        releaseCommit.setCommitUrl(this.createCommitUrl(commit.getHashFull()));

        final Matcher messageMatcher = DEFAULT_MESSAGE_PATTERN.matcher(commit.getMessage());

        if (!messageMatcher.find()) return releaseCommit;

        final String type = messageMatcher.group(1);

        releaseCommit.setAttention(type.endsWith("!"));
        releaseCommit.setCommitType(StringUtils.stripEnd(type, "!"));
        releaseCommit.setCommitScope(messageMatcher.group(3));
        releaseCommit.setCommitDescription(StringUtils.strip(messageMatcher.group(4), "\"' \t."));
        releaseCommit.setCommitContents(StringUtils.trimToNull(messageMatcher.group(5)));

        if (StringUtils.startsWith(releaseCommit.getCommitContents(), BREAKING_CHANGE_PATTERN)) {
            releaseCommit.setBreakingChange(true);
        } else if (StringUtils.startsWith(releaseCommit.getCommitContents(), DEPRECATION_PATTERN)) {
            releaseCommit.setDeprecation(true);
        }

        if (StringUtils.isEmpty(releaseCommit.getCommitDescription())) return releaseCommit;

        this.parseCommitIssue(releaseCommit);
        this.parseQuickAction(releaseCommit);

        return releaseCommit;
    }

    private void parseCommitIssue(ReleaseCommit releaseCommit) {
        if (this.commitIssuePattern == null) return;

        final Matcher commitIssueMatcher = this.commitIssuePattern.matcher(releaseCommit.getCommitDescription());

        if (!commitIssueMatcher.find()) return;

        final Integer issueId = Integer.valueOf(commitIssueMatcher.group("id"));
        final String issueUrl = this.createIssueUrl(issueId);
        final ReleaseIssue commitIssue = new ReleaseIssue(issueId, issueUrl);

        releaseCommit.setCommitIssue(commitIssue);
    }

    private void parseQuickAction(ReleaseCommit releaseCommit) {
        if (this.quickActionPattern == null) return;
        if (StringUtils.isEmpty(releaseCommit.getCommitContents())) return;

        final Map<String, List<ReleaseIssue>> issueActions = releaseCommit.getIssueActions();
        final Matcher quickActionMatcher = this.quickActionPattern.matcher(releaseCommit.getCommitContents());

        while (quickActionMatcher.find()) {
            final Integer issueId = Integer.valueOf(quickActionMatcher.group("id"));
            final String issueUrl = this.createIssueUrl(issueId);
            final String issueAction = quickActionMatcher.group("action");
            final ReleaseIssue issue = new ReleaseIssue(issueId, issueUrl, issueAction);

            if (issueActions.containsKey(issueAction)) {
                final List<ReleaseIssue> issues = issueActions.get(issueAction);

                if (!issues.contains(issue)) issues.add(issue);
            } else {
                final List<ReleaseIssue> issues = new ArrayList<>();
                issues.add(issue);

                issueActions.put(issueAction, issues);
            }
        }
    }
}
