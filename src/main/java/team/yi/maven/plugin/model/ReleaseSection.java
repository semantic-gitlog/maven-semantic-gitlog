package team.yi.maven.plugin.model;

import de.skuzzle.semantic.Version;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReleaseSection implements Serializable {
    private static final long serialVersionUID = -3583377058573092174L;

    @EqualsAndHashCode.Include
    private Version version;
    private ReleaseDate releaseDate;
    private String description;
    private List<ReleaseCommitGroup> groups;

    public List<ReleaseCommitGroup> getGroups() {
        if (groups == null || groups.isEmpty()) return null;

        return groups.stream()
                .filter(x -> x.getCommits() != null)
                .filter(x -> !x.getCommits().isEmpty())
                .sorted((o1, o2) -> {
                    if (o2 == null) return -1;

                    final String t1 = o1.getTitle();
                    final String t2 = o2.getTitle();

                    if (t1 == null && t2 == null) {
                        return 0;
                    } else if (t1 == null) {
                        return 1;
                    } else if (t2 == null) {
                        return -1;
                    } else if (t1.compareTo(t2) == 0) {
                        return 0;
                    }

                    // todo make configuration
                    final int i1 = ReleaseCommitGroups.DEFAULT_ORDER_LIST.indexOf(t1);
                    final int i2 = ReleaseCommitGroups.DEFAULT_ORDER_LIST.indexOf(t2);

                    return Integer.compare(i1, i2);
                })
                .collect(Collectors.toList());
    }

    public ReleaseDate getNow() {
        return new ReleaseDate();
    }
}
