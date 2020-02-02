package team.yi.maven.plugin.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import se.bjurr.gitchangelog.api.model.Commit;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ReleaseCommit extends Commit implements Serializable {
    private static final long serialVersionUID = 8295825971812538595L;

    private final Map<String, List<ReleaseIssue>> issueActions = new ConcurrentHashMap<>();

    private String shortHash;
    private String commitUrl;

    private String commitType;
    private String commitScope;
    private String commitDescription;
    private String commitContents;
    private boolean attention;
    private boolean breakingChange;
    private boolean deprecation;

    private ReleaseIssue commitIssue;

    public ReleaseCommit(Commit commit) {
        super(
            commit.getAuthorName(),
            commit.getAuthorEmailAddress(),
            commit.getCommitTime(),
            commit.getCommitTimeLong(),
            commit.getMessage(),
            commit.getHashFull(),
            commit.isMerge()
        );
    }

    @EqualsAndHashCode.Include
    @Override
    public String getHashFull() {
        return super.getHashFull();
    }

    public List<ReleaseIssue> getCloseIssues() {
        return this.issueActions.getOrDefault("close", null);
    }

    public boolean hasCloseIssues() {
        final List<ReleaseIssue> closeIssues = this.getCloseIssues();

        return closeIssues != null && !closeIssues.isEmpty();
    }
}
