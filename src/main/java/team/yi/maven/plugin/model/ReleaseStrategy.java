package team.yi.maven.plugin.model;

public enum ReleaseStrategy {
    strict,
    slow;

    public static ReleaseStrategy of(final String name) {
        switch (name) {
            case "SLOW":
            case "slow":
                return slow;
            case "STRICT":
            default:
                return strict;
        }
    }
}
