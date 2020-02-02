package team.yi.maven.plugin.model;

import de.skuzzle.semantic.Version;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Value
public class ReleaseLog implements Serializable {
    public static final int VERSION = 1;

    private static final long serialVersionUID = -8269453799524252579L;

    private Version nextVersion;
    private Version lastVersion;
    private List<ReleaseSection> sections;
}
