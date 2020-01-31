package team.yi.maven.plugin.model;

import java.util.Objects;

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
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GitReleaseIssue that = (GitReleaseIssue) o;

        return Objects.equals(id, that.id);
    }
}
