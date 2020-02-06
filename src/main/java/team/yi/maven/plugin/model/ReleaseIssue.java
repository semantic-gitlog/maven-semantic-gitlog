package team.yi.maven.plugin.model;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serializable;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReleaseIssue implements Serializable {
    private static final long serialVersionUID = 6833555213108571041L;

    @EqualsAndHashCode.Include
    private final Integer id;
    private final String url;
    private final String action;

    public ReleaseIssue(final Integer issueId, final String url) {
        this(issueId, url, null);
    }

    public ReleaseIssue(final Integer issueId, final String url, final String action) {
        this.id = issueId;
        this.url = url;
        this.action = action;
    }
}
