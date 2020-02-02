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
    private final String action;
    private final String url;

    public ReleaseIssue(Integer issueId, String url) {
        this(issueId, url, null);
    }

    public ReleaseIssue(Integer issueId, String url, String issueAction) {
        this.id = issueId;
        this.action = issueAction;
        this.url = url;
    }
}
