package team.yi.maven.plugin.model;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serializable;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class IssueRef implements Serializable {
    private static final long serialVersionUID = 6833555213108571041L;

    @EqualsAndHashCode.Include
    private final String repo;

    @EqualsAndHashCode.Include
    private final Integer id;

    private final String url;
    private final String action;

    public IssueRef(final Integer issueId, final String url) {
        this(null, issueId, url);
    }

    public IssueRef(final String repo, final Integer issueId, final String url) {
        this(repo, issueId, url, null);
    }

    public IssueRef(final Integer issueId, final String url, final String action) {
        this(null, issueId, url, action);
    }

    public IssueRef(final String repo, final Integer issueId, final String url, final String action) {
        this.repo = repo;
        this.id = issueId;
        this.url = url;
        this.action = action;
    }
}

