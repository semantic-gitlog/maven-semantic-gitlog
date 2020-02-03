package team.yi.maven.plugin.utils;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import se.bjurr.gitchangelog.api.model.Commit;
import team.yi.maven.plugin.config.ReleaseLogSettings;
import team.yi.maven.plugin.model.ReleaseCommit;
import team.yi.maven.plugin.model.ReleaseIssue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReleaseCommitParser {
    private static final Pattern DEFAULT_MESSAGE_PATTERN = Pattern.compile(ReleaseLogSettings.DEFAULT_MESSAGE_PATTERN, Pattern.DOTALL);
    private static final Pattern DEFAULT_COMMIT_ISSUE_PATTERN = Pattern.compile(ReleaseLogSettings.DEFAULT_COMMIT_ISSUE_PATTERN);
    private static final String BREAKING_CHANGE_PATTERN = "BREAKING CHANGE: ";
    private static final String DEPRECATED_PATTERN = "DEPRECATED: ";
    private static final String ISSUE_ID_PLACEHOLDER = ":issueId";
    private static final String COMMIT_ID_PLACEHOLDER = ":commitId";

    private final String commitUrlTemplate;
    private final String issueUrlTemplate;
    private final Pattern commitIssuePattern;
    private final Pattern quickActionPattern;
    private final List<String> closeIssueActions;

    @SuppressWarnings("PMD.NullAssignment")
    public ReleaseCommitParser(ReleaseLogSettings releaseLogSettings) {
        this.commitUrlTemplate = releaseLogSettings.getCommitUrlTemplate();
        this.issueUrlTemplate = releaseLogSettings.getIssueUrlTemplate();

        this.commitIssuePattern = StringUtils.isNotEmpty(releaseLogSettings.getCommitIssuePattern())
            ? Pattern.compile(releaseLogSettings.getCommitIssuePattern()) : DEFAULT_COMMIT_ISSUE_PATTERN;

        this.quickActionPattern = StringUtils.isNotEmpty(releaseLogSettings.getQuickActionPattern())
            ? Pattern.compile(releaseLogSettings.getQuickActionPattern()) : null;

        String[] closeIssueActions = StringUtils.splitPreserveAllTokens(releaseLogSettings.getCloseIssueActions(), ",|;");

        this.closeIssueActions = closeIssueActions == null || closeIssueActions.length == 0 ? null : Arrays.asList(closeIssueActions);
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
        releaseCommit.setHash7(commit.getHashFull().substring(0, 7));
        releaseCommit.setHash8(commit.getHashFull().substring(0, 8));
        releaseCommit.setCommitUrl(this.createCommitUrl(commit.getHashFull()));

        final Matcher messageMatcher = DEFAULT_MESSAGE_PATTERN.matcher(commit.getMessage());

        if (!messageMatcher.find()) return releaseCommit;

        // https://github.com/angular/components/blob/master/CONTRIBUTING.md
        // ^((?<type>[\w]+)(?<attention>!)?)(\((?<package>(\w+\/)*)(?<scope>[\w-$_]+)\))?:
        // (?<subject>[^\n]+)([\r\n]{2})(?<body>(.+([\r\n]{0,2}))*)?$

        releaseCommit.setCommitType(messageMatcher.group("type"));
        releaseCommit.setAttention("!".equals(messageMatcher.group("attention")));
        releaseCommit.setCommitPackage(messageMatcher.group("package"));
        releaseCommit.setCommitScope(messageMatcher.group("scope"));
        releaseCommit.setCommitSubject(StringUtils.strip(messageMatcher.group("subject"), "\"' \t."));
        releaseCommit.setCommitBody(StringUtils.trimToNull(messageMatcher.group("body")));

        this.parseSubject(releaseCommit);
        this.parseBody(releaseCommit);
        this.parseFooter(releaseCommit);

        return releaseCommit;
    }

    private void parseFooter(ReleaseCommit releaseCommit) {
        if (StringUtils.isEmpty(releaseCommit.getCommitBody())) return;

        String[] lines = StringUtils.split(releaseCommit.getCommitBody(), "\r\n");

        for (String line : lines) {
            if (!releaseCommit.isBreakingChange() && StringUtils.startsWith(line, BREAKING_CHANGE_PATTERN)) {
                releaseCommit.setBreakingChange(true);
            }

            if (!releaseCommit.isDeprecated() && StringUtils.startsWith(line, DEPRECATED_PATTERN)) {
                releaseCommit.setDeprecated(true);
            }
        }
    }

    private void parseBody(ReleaseCommit releaseCommit) {
        if (this.quickActionPattern == null) return;
        if (StringUtils.isEmpty(releaseCommit.getCommitBody())) return;

        this.parseBodyIssues(releaseCommit);
        this.parseQuickActions(releaseCommit);
    }

    private void parseBodyIssues(ReleaseCommit releaseCommit) {
        final List<ReleaseIssue> bodyIssues = releaseCommit.getBodyIssues();
        final Matcher commitIssueMatcher = this.commitIssuePattern.matcher(releaseCommit.getCommitBody());

        while (commitIssueMatcher.find()) {
            final Integer issueId = Integer.valueOf(commitIssueMatcher.group("id"));
            final String issueUrl = this.createIssueUrl(issueId);
            final ReleaseIssue commitIssue = new ReleaseIssue(issueId, issueUrl);

            if (releaseCommit.getCommitIssue() == null) releaseCommit.setCommitIssue(commitIssue);

            bodyIssues.add(commitIssue);
        }
    }

    private void parseQuickActions(ReleaseCommit releaseCommit) {
        final Map<String, List<ReleaseIssue>> quickActions = releaseCommit.getQuickActions();
        final List<ReleaseIssue> closeIssues = releaseCommit.getCloseIssues();
        final Matcher quickActionMatcher = this.quickActionPattern.matcher(releaseCommit.getCommitBody());

        while (quickActionMatcher.find()) {
            final Integer issueId = Integer.valueOf(quickActionMatcher.group("id"));
            final String issueUrl = this.createIssueUrl(issueId);
            final String quickAction = quickActionMatcher.group("action");
            final ReleaseIssue issue = new ReleaseIssue(issueId, issueUrl, quickAction);

            if (quickActions.containsKey(quickAction)) {
                final List<ReleaseIssue> issues = quickActions.get(quickAction);

                if (!issues.contains(issue)) issues.add(issue);
            } else {
                final List<ReleaseIssue> issues = new ArrayList<>();
                issues.add(issue);

                quickActions.put(quickAction, issues);
            }

            if (this.closeIssueActions.contains(quickAction)) closeIssues.add(issue);
        }
    }

    private void parseSubject(ReleaseCommit releaseCommit) {
        if (this.commitIssuePattern == null) return;

        // \(#(?<id>\d+)\)
        // Adds size specs to fake icon (#18160) (#18306)
        final String subject = releaseCommit.getCommitSubject();
        final List<ReleaseIssue> subjectIssues = releaseCommit.getSubjectIssues();
        final Matcher commitIssueMatcher = this.commitIssuePattern.matcher(subject);

        while (commitIssueMatcher.find()) {
            final Integer issueId = Integer.valueOf(commitIssueMatcher.group("id"));
            final String issueUrl = this.createIssueUrl(issueId);
            final ReleaseIssue commitIssue = new ReleaseIssue(issueId, issueUrl);

            if (releaseCommit.getCommitIssue() == null) releaseCommit.setCommitIssue(commitIssue);

            subjectIssues.add(commitIssue);
        }

        releaseCommit.setCommitSubject(StringUtils.trim(RegExUtils.removeAll(subject, this.commitIssuePattern)));
    }
}
