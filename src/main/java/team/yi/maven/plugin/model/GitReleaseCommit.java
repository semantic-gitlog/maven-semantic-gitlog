package team.yi.maven.plugin.model;

import org.apache.commons.lang3.StringUtils;
import se.bjurr.gitchangelog.api.model.Commit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitReleaseCommit extends Commit implements Serializable {
    public static final Pattern MESSAGE_PATTERN = Pattern.compile("^([\\w!]+)(\\(([\\w-$_]+)\\))?: ([^\\n]+)((\\n{1,2}([^\\n]+))*)$");
    public static final Pattern MESSAGE_ISSUE_PATTERN = Pattern.compile("\\n?^issue /(\\w+) #(\\d+)$", Pattern.MULTILINE);
    public static final String BREAKING_CHANGE = "BREAKING CHANGE";
    public static final String DEPRECATION = "DEPRECATION";

    private static final long serialVersionUID = 3472695555192309191L;

    private String commitType;
    private String commitScope;
    private String commitDescription;
    private String commitContents;
    private boolean attention;
    private boolean breakingChange;
    private boolean deprecation;
    private String shortHash;
    private Map<String, List<GitReleaseIssue>> issueActions;

    private String commitUrlTemplate;
    private String issueUrlTemplate;

    public GitReleaseCommit(Commit commit, String commitUrlTemplate, String issueUrlTemplate) {
        super(
            commit.getAuthorName(),
            commit.getAuthorEmailAddress(),
            commit.getCommitTime(),
            commit.getCommitTimeLong(),
            commit.getMessage(),
            commit.getHash(),
            commit.isMerge()
        );

        this.commitUrlTemplate = commitUrlTemplate;
        this.issueUrlTemplate = issueUrlTemplate;

        this.internalParse(commit);
    }

    public boolean isDeprecation() {
        return deprecation;
    }

    public String getShortHash() {
        return shortHash;
    }

    public String getCommitUrl() {
        if (StringUtils.isEmpty(this.commitUrlTemplate)) return null;

        return this.commitUrlTemplate.replaceAll(":commitId", String.valueOf(this.getHashFull()));
    }

    public String getFirstIssueUrl() {
        if (StringUtils.isEmpty(this.issueUrlTemplate)) return null;

        return this.issueUrlTemplate.replaceAll(":issueId", String.valueOf(this.getFirstIssueId()));
    }

    public Integer getFirstIssueId() {
        GitReleaseIssue issue = this.getFirstIssue();

        return issue == null ? null : issue.getId();
    }

    public String getFirstIssueAction() {
        if (this.issueActions == null || this.issueActions.isEmpty()) return null;

        return this.issueActions.entrySet().stream().findFirst().get().getKey();
    }

    public GitReleaseIssue getFirstIssue() {
        if (this.issueActions == null || this.issueActions.isEmpty()) return null;

        List<GitReleaseIssue> issues = this.issueActions.get(this.getFirstIssueAction());

        return issues == null || issues.isEmpty() ? null : issues.get(0);
    }

    public List<GitReleaseIssue> getCloseIssues() {
        if (this.issueActions == null || this.issueActions.isEmpty()) return null;

        return this.issueActions.getOrDefault("close", null);
    }

    public boolean hasCloseIssues() {
        List<GitReleaseIssue> closeIssues = this.getCloseIssues();

        return closeIssues != null && !closeIssues.isEmpty();
    }

    public String getCommitContents() {
        return commitContents;
    }

    public boolean isAttention() {
        return attention;
    }

    public boolean isBreakingChange() {
        return breakingChange;
    }

    private void internalParse(Commit commit) {
        this.shortHash = commit.getHash().substring(0, 8);

        Matcher matcher = MESSAGE_PATTERN.matcher(commit.getMessage());

        if (!matcher.find()) return;

        String type = matcher.group(1);

        this.attention = type.endsWith("!");
        this.commitType = StringUtils.stripEnd(type, "!");
        this.commitScope = matcher.group(3);
        this.commitDescription = StringUtils.strip(matcher.group(4), "\"'");
        this.commitContents = StringUtils.trimToNull(matcher.group(5));

        if (StringUtils.startsWith(this.commitContents, BREAKING_CHANGE)) {
            this.breakingChange = true;
        } else if (StringUtils.startsWith(this.commitContents, DEPRECATION)) {
            this.deprecation = true;
        }

        if (StringUtils.isEmpty(this.commitContents)) return;

        // fetch issueId
        if (this.issueActions == null) this.issueActions = new HashMap<>();

        Matcher issueMatcher = MESSAGE_ISSUE_PATTERN.matcher(this.commitContents);

        while (issueMatcher.find()) {
            String issueAction = issueMatcher.group(1);
            Integer issueId = Integer.valueOf(issueMatcher.group(2));
            String issueUrl = this.issueUrlTemplate == null
                ? null
                : this.issueUrlTemplate.replace(":issueId", String.valueOf(issueId));
            GitReleaseIssue issue = new GitReleaseIssue(issueId, issueAction, issueUrl);

            if (this.issueActions.containsKey(issueAction)) {
                List<GitReleaseIssue> issues = this.issueActions.get(issueAction);

                if (!issues.contains(issue)) issues.add(issue);
            } else {
                List<GitReleaseIssue> issues = new ArrayList<>();
                issues.add(issue);

                this.issueActions.put(issueAction, issues);
            }
        }
    }

    public String getCommitType() {
        return commitType;
    }

    public String getCommitScope() {
        return commitScope;
    }

    public String getCommitDescription() {
        return commitDescription;
    }
}
