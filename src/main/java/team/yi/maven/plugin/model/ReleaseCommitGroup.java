package team.yi.maven.plugin.model;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReleaseCommitGroup implements Serializable {
    private static final long serialVersionUID = -8070362801447560940L;

    @EqualsAndHashCode.Include
    private String title;
    private List<ReleaseCommit> commits;

    public List<ReleaseCommit> getCommits() {
        if (commits == null || commits.isEmpty()) return null;

        return commits.stream()
            .distinct()
            .sorted(Comparator.comparing(ReleaseCommit::getCommitScope, Comparator.nullsLast(String::compareTo)))
            .collect(Collectors.toList());
    }
}
