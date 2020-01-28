package team.yi.maven.plugin.model;

public class GitReleaseIssue {
    private Integer id;
    private String action;
    private String url;

    public GitReleaseIssue(Integer issueId, String issueAction, String url) {
        this.id = issueId;
        this.action = issueAction;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public String getAction() {
        return action;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GitReleaseIssue) {
            GitReleaseIssue issue = (GitReleaseIssue) obj;

            return this.id.equals(issue.id);
        }

        return false;
    }
}
