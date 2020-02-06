package team.yi.maven.plugin.model;

import de.skuzzle.semantic.Version;
import lombok.Value;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Value
public class ReleaseLog implements Serializable {
    public static final int VERSION = 1;

    private static final long serialVersionUID = -8269453799524252579L;

    private Version nextVersion;
    private Version lastVersion;
    private List<ReleaseTag> tags;

    public List<ReleaseTag> getTags() {
        if (tags == null || tags.isEmpty()) return null;

        return tags.stream()
            .distinct()
            .sorted(Comparator.nullsFirst((Comparator<ReleaseTag>) (o1, o2) -> {
                if (o2 == null) return -1;

                final ReleaseDate t1 = o1.getReleaseDate();
                final ReleaseDate t2 = o2.getReleaseDate();

                if (t1 == null && t2 == null) {
                    return 0;
                } else if (t1 == null) {
                    return 1;
                } else if (t2 == null) {
                    return -1;
                }

                return t1.getDate().compareTo(t2.getDate());
            }).reversed())
            .collect(Collectors.toList());
    }
}
