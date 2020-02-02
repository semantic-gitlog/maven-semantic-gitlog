package team.yi.maven.plugin.model;

import org.apache.commons.lang3.StringUtils;
import se.bjurr.gitchangelog.api.model.Commit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitReleaseCommit extends Commit implements Serializable {
    public static final Pattern MESSAGE_PATTERN = Pattern.compile("^([\\w!]+)(\\(([\\w-$_]+)\\))?: ([^\\n]+)((\\n{1,2}([^\\n]+))*)$");
    public static final String BREAKING_CHANGE = "BREAKING CHANGE";
    public static final String DEPRECATION = "DEPRECATION";

    private static final long serialVersionUID = 3472695555192309191L;

    private final Map<String, List<GitReleaseIssue>> issueActions = new ConcurrentHashMap<>();

    private String commitType;
    private String commitScope;
    private String commitDescription;
    private String commitContents;
    private boolean attention;
    private boolean breakingChange;
    private boolean deprecation;
    private String shortHash;
    private GitReleaseIssue commitIssue;
    private String commitUrlTemplate;
    private String issueUrlTemplate;
    private Pattern commitIssuePattern;
    private Pattern quickActionPattern;

    public GitReleaseCommit(Commit commit, String commitUrlTemplate, String issueUrlTemplate, Pattern commitIssuePattern, Pattern quickActionPattern) {
        super(
            commit.getAuthorName(),
            commit.getAuthorEmailAddress(),
            commit.getCommitTime(),
            commit.getCommitTimeLong(),
            commit.getMessage(),
            commit.getHashFull(),
            commit.isMerge()
        );

        this.commitUrlTemplate = commitUrlTemplate;
        this.issueUrlTemplate = issueUrlTemplate;
        this.commitIssuePattern = commitIssuePattern;
        this.quickActionPattern = quickActionPattern;

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

    public GitReleaseIssue getCommitIssue() {
        return commitIssue;
    }

    public List<GitReleaseIssue> getCloseIssues() {
        if (this.issueActions.isEmpty()) return null;

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

    @SuppressWarnings("PMD.NPathComplexity")
    private void internalParse(Commit commit) {
        this.shortHash = commit.getHash().substring(0, 8);

        Matcher matcher = MESSAGE_PATTERN.matcher(commit.getMessage());

        if (!matcher.find()) return;

        String type = matcher.group(1);

        this.attention = type.endsWith("!");
        this.commitType = StringUtils.stripEnd(type, "!");
        this.commitScope = matcher.group(3);
        this.commitDescription = StringUtils.strip(matcher.group(4), "\"' \t.");

        // should throw
        if (StringUtils.isEmpty(this.commitDescription)) return;

        // fetch issueId
        if (this.commitIssuePattern != null) {
            Matcher commitIssueMatcher = this.commitIssuePattern.matcher(this.commitDescription);

            if (commitIssueMatcher.find()) {
                Integer issueId = Integer.valueOf(commitIssueMatcher.group("id"));
                String issueUrl = this.issueUrlTemplate == null
                    ? null
                    : this.issueUrlTemplate.replace(":issueId", String.valueOf(issueId));

                this.commitIssue = new GitReleaseIssue(issueId, null, issueUrl);
            }
        }

        this.commitContents = StringUtils.trimToNull(matcher.group(5));

        if (StringUtils.startsWith(this.commitContents, BREAKING_CHANGE)) {
            this.breakingChange = true;
        } else if (StringUtils.startsWith(this.commitContents, DEPRECATION)) {
            this.deprecation = true;
        }

        if (StringUtils.isEmpty(this.commitContents)) return;

        // fetch issues with action
        if (this.quickActionPattern == null) return;

        Matcher quickActionMatcher = this.quickActionPattern.matcher(this.commitContents);

        while (quickActionMatcher.find()) {
            String issueAction = quickActionMatcher.group("action");
            Integer issueId = Integer.valueOf(quickActionMatcher.group("id"));
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
