package team.yi.maven.plugin.model;

import de.skuzzle.semantic.Version;

import java.util.List;

public class GitReleaseLog {
    public static final int VERSION = 1;

    private Version newVersion;
    private Version lastVersion;
    private List<GitReleaseSection> sections;

    public Version getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(Version newVersion) {
        this.newVersion = newVersion;
    }

    public Version getLastVersion() {
        return lastVersion;
    }

    public void setLastVersion(Version lastVersion) {
        this.lastVersion = lastVersion;
    }

    public List<GitReleaseSection> getSections() {
        return sections;
    }

    public void setSections(List<GitReleaseSection> sections) {
        this.sections = sections;
    }
}
