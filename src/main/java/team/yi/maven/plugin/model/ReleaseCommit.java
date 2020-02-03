package team.yi.maven.plugin.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import se.bjurr.gitchangelog.api.model.Commit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("PMD.TooManyFields")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ReleaseCommit extends Commit implements Serializable {
    private static final long serialVersionUID = 8295825971812538595L;

    private final Map<String, List<ReleaseIssue>> quickActions = new ConcurrentHashMap<>();
    private final List<ReleaseIssue> subjectIssues = new ArrayList<>();
    private final List<ReleaseIssue> bodyIssues = new ArrayList<>();
    private final List<ReleaseIssue> closeIssues = new ArrayList<>();

    private String hash7;
    private String hash8;
    private String commitUrl;
    private String commitType;
    private String commitPackage;
    private String commitScope;
    private String commitSubject;
    private String commitBody;
    private boolean attention;
    private boolean breakingChange;
    private boolean deprecated;
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

    public boolean hasCloseIssues() {
        final List<ReleaseIssue> closeIssues = this.getCloseIssues();

        return closeIssues != null && !closeIssues.isEmpty();
    }
}
