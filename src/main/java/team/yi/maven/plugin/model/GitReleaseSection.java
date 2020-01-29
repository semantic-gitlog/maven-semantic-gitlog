package team.yi.maven.plugin.model;

import de.skuzzle.semantic.Version;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class GitReleaseSection {
    private static final List<String> orderList = new ArrayList<>();

    static {
        orderList.add(GitReleaseCommitGroup.BUG_FIXES);
        orderList.add(GitReleaseCommitGroup.CODE_REFACTORING);
        orderList.add(GitReleaseCommitGroup.FEATURES);
        orderList.add(GitReleaseCommitGroup.PERFORMANCE_IMPROVEMENTS);
        orderList.add(GitReleaseCommitGroup.DOCUMENTATION);
        orderList.add(GitReleaseCommitGroup.STYLES);
        orderList.add(GitReleaseCommitGroup.REVERTS);
        orderList.add(GitReleaseCommitGroup.BREAKING_CHANGE);
        orderList.add(GitReleaseCommitGroup.DEPRECATIONS);
        orderList.add(GitReleaseCommitGroup.TESTS);
        orderList.add(GitReleaseCommitGroup.BUILD_SYSTEM);
        orderList.add(GitReleaseCommitGroup.CONTINUOUS_INTEGRATION);
        orderList.add(GitReleaseCommitGroup.OTHERS);
    }

    private Version version;
    private GitReleaseDate releaseDate;
    private String description;
    private List<GitReleaseCommitGroup> groups;

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public GitReleaseDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(GitReleaseDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = new GitReleaseDate(releaseDate);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<GitReleaseCommitGroup> getGroups() {
        if (groups == null || groups.isEmpty()) return null;

        return groups.stream()
            .filter(x -> x.getCommits() != null)
            .filter(x -> !x.getCommits().isEmpty())
            .sorted((o1, o2) -> {
                if (o2 == null) return -1;

                String t1 = o1.getTitle();
                String t2 = o2.getTitle();

                if (t1 == null && t2 == null) {
                    return 0;
                } else if (t1 == null) {
                    return 1;
                } else if (t2 == null) {
                    return -1;
                } else if (t1.compareTo(t2) == 0) {
                    return 0;
                }

                int i1 = orderList.indexOf(t1);
                int i2 = orderList.indexOf(t2);

                return Integer.compare(i1, i2);
            })
            .collect(Collectors.toList());
    }

    public void setGroups(List<GitReleaseCommitGroup> groups) {
        this.groups = groups;
    }

    public GitReleaseDate getNow() {
        return new GitReleaseDate();
    }
}
