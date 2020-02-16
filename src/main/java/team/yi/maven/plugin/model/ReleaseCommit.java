package team.yi.maven.plugin.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import se.bjurr.gitchangelog.api.model.Commit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@SuppressWarnings("PMD.TooManyFields")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ReleaseCommit extends Commit implements Serializable {
    private static final long serialVersionUID = 8295825971812538595L;

    private final Map<String, List<IssueRef>> quickActions = new ConcurrentHashMap<>();
    private final List<IssueRef> closeIssues = new ArrayList<>();
    private final List<IssueRef> subjectIssues = new ArrayList<>();
    private final List<IssueRef> bodyIssues = new ArrayList<>();
    private final List<MentionRef> mentions = new ArrayList<>();
    private final List<ReleaseCommitLocale> locales = new ArrayList<>();

    private String hash7;
    private String hash8;

    @Setter
    private String commitUrl;
    @Setter
    private String commitType;
    @Setter
    private String commitPackage;
    @Setter
    private String commitScope;
    @Setter
    private String commitSubject;
    @Setter
    private String commitBody;
    @Setter
    private boolean attention;
    @Setter
    private boolean breakingChange;
    @Setter
    private boolean deprecated;

    public ReleaseCommit(final Commit commit) {
        super(
            commit.getAuthorName(),
            commit.getAuthorEmailAddress(),
            commit.getCommitTime(),
            commit.getCommitTimeLong(),
            commit.getMessage(),
            commit.getHashFull(),
            commit.isMerge()
        );

        this.hash7 = commit.getHashFull().substring(0, 7);
        this.hash8 = commit.getHashFull().substring(0, 8);
    }

    public IssueRef getCommitIssue() {
        return this.subjectIssues.isEmpty() ? null : this.subjectIssues.get(0);
    }

    @EqualsAndHashCode.Include
    @Override
    public String getHashFull() {
        return super.getHashFull();
    }

    public void add(final String action, final IssueRef issueRef) {
        final String issueAction = StringUtils.lowerCase(action);

        if (this.quickActions.containsKey(issueAction)) {
            final List<IssueRef> issues = quickActions.get(issueAction);

            if (!issues.contains(issueRef)) issues.add(issueRef);
        } else {
            final List<IssueRef> issues = new ArrayList<>();
            issues.add(issueRef);

            quickActions.put(issueAction, issues);
        }
    }

    public boolean hasQuickActions() {
        return !this.quickActions.isEmpty();
    }

    public boolean hasSubjectIssues() {
        return !this.subjectIssues.isEmpty();
    }

    public boolean hasBodyIssues() {
        return !this.bodyIssues.isEmpty();
    }

    public boolean hasCloseIssues() {
        return !this.closeIssues.isEmpty();
    }

    public Map<String, ReleaseCommitLocale> getLocaleMap() {
        if (this.locales.isEmpty()) return null;

        return this.locales.stream()
            .collect(Collectors.toMap(ReleaseCommitLocale::getLang, locale -> locale, (a, b) -> b));
    }
}
