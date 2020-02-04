package team.yi.maven.plugin.utils;

import de.skuzzle.semantic.Version;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.maven.plugin.logging.Log;
import team.yi.maven.plugin.config.ReleaseLogSettings;
import team.yi.maven.plugin.model.ReleaseCommit;

import java.util.List;
import java.util.Stack;

public class VersionManager {
    private final Log log;
    private final List<String> majorTypes;
    private final List<String> minorTypes;
    private final List<String> patchTypes;
    private final List<String> preReleaseTypes;
    private final List<String> buildMetaDataTypes;

    private boolean useCrazyGrowing;

    public VersionManager(final ReleaseLogSettings releaseLogSettings, final Log log) {
        this.log = log;

        this.majorTypes = releaseLogSettings.getMajorTypes();
        this.minorTypes = releaseLogSettings.getMinorTypes();
        this.patchTypes = releaseLogSettings.getPatchTypes();
        this.preReleaseTypes = releaseLogSettings.getPreReleaseTypes();
        this.buildMetaDataTypes = releaseLogSettings.getBuildMetaDataTypes();

        this.useCrazyGrowing = BooleanUtils.toBooleanDefaultIfNull(releaseLogSettings.getUseCrazyGrowing(), true);
    }

    public Version deriveNextVersion(final Version lastVersion, final Stack<ReleaseCommit> versionCommits) {
        Version nextVersion = lastVersion == null
            ? Version.create(0, 1, 0)
            : Version.parseVersion(lastVersion.toString(), true);
        boolean minorUp = false;
        boolean patchUp = false;
        boolean preReleaseUp = false;
        boolean buildMetaDataUp = false;

        while (!versionCommits.isEmpty()) {
            final ReleaseCommit commit = versionCommits.pop();
            final String commitType = commit.getCommitType();

            if (this.log != null && this.log.isDebugEnabled()) {
                this.log.debug("commitType: " + commitType);
                this.log.debug("nextVersion: " + nextVersion);
            }

            if (commit.isBreakingChange() || this.majorTypes.contains(commitType)) {
                nextVersion = nextVersion.nextMajor();

                if (this.useCrazyGrowing) continue;

                break;
            } else if (this.minorTypes.contains(commitType)) {
                if (minorUp) {
                    if (this.useCrazyGrowing) {
                        nextVersion = nextVersion.nextMinor();
                    }
                } else {
                    nextVersion = nextVersion.nextMinor();
                    minorUp = true;
                }
            } else if (this.patchTypes.contains(commitType)) {
                if (patchUp) {
                    if (this.useCrazyGrowing) {
                        nextVersion = nextVersion.nextPatch();
                    }
                } else {
                    nextVersion = nextVersion.nextPatch();
                    patchUp = true;
                }
            } else if (this.preReleaseTypes.contains(commitType)) {
                if (preReleaseUp) {
                    if (this.useCrazyGrowing) {
                        nextVersion = nextVersion.nextPreRelease();
                    }
                } else {
                    nextVersion = nextVersion.nextPreRelease();
                    preReleaseUp = true;
                }
            } else if (this.buildMetaDataTypes.contains(commitType)) {
                if (buildMetaDataUp) {
                    if (this.useCrazyGrowing) {
                        nextVersion = nextVersion.nextBuildMetaData();
                    }
                } else {
                    nextVersion = nextVersion.nextBuildMetaData();
                    buildMetaDataUp = true;
                }
            }
        }

        return nextVersion;
    }
}
