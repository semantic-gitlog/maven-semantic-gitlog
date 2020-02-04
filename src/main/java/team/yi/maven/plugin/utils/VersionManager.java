package team.yi.maven.plugin.utils;

import de.skuzzle.semantic.Version;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;
import team.yi.maven.plugin.config.ReleaseLogSettings;
import team.yi.maven.plugin.model.ReleaseCommit;
import team.yi.maven.plugin.model.ReleaseStrategy;

import java.util.List;
import java.util.Stack;

public class VersionManager {
    private final Log log;
    private final List<String> majorTypes;
    private final List<String> minorTypes;
    private final List<String> patchTypes;
    private final List<String> preReleaseTypes;
    private final List<String> buildMetaDataTypes;

    private final ReleaseStrategy strategy;
    private final String preRelease;
    private final String buildMetaData;

    public VersionManager(final ReleaseLogSettings releaseLogSettings, final Log log) {
        this.log = log;

        this.majorTypes = releaseLogSettings.getMajorTypes();
        this.minorTypes = releaseLogSettings.getMinorTypes();
        this.patchTypes = releaseLogSettings.getPatchTypes();
        this.preReleaseTypes = releaseLogSettings.getPreReleaseTypes();
        this.buildMetaDataTypes = releaseLogSettings.getBuildMetaDataTypes();
        this.preRelease = releaseLogSettings.getPreRelease();
        this.buildMetaData = releaseLogSettings.getBuildMetaData();

        this.strategy = releaseLogSettings.getStrategy();
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

            nextVersion = this.ensureSuffix(nextVersion);

            if (this.log != null && this.log.isDebugEnabled()) {
                this.log.debug("#");
                this.log.debug("#  messageTitle: " + commit.getMessageTitle());
                this.log.debug("#    commitType: " + commitType);
                this.log.debug("#   nextVersion: " + nextVersion);
                this.log.debug("#    preRelease: " + preRelease);
                this.log.debug("# buildMetaData: " + buildMetaData);
                this.log.debug("#");
            }

            if (commit.isBreakingChange() || this.majorTypes.contains(commitType)) {
                nextVersion = nextVersion.nextMajor();

                if (this.strategy != ReleaseStrategy.strict) break;
            } else if (this.minorTypes.contains(commitType)) {
                if (minorUp) {
                    if (this.strategy == ReleaseStrategy.strict) {
                        nextVersion = nextVersion.nextMinor();
                    }
                } else {
                    nextVersion = nextVersion.nextMinor();
                    minorUp = true;
                }
            } else if (this.patchTypes.contains(commitType)) {
                if (patchUp) {
                    if (this.strategy == ReleaseStrategy.strict) {
                        nextVersion = nextVersion.nextPatch();
                    }
                } else {
                    nextVersion = nextVersion.nextPatch();
                    patchUp = true;
                }
            } else if (this.preReleaseTypes.contains(commitType)) {
                if (preReleaseUp) {
                    if (this.strategy == ReleaseStrategy.strict) {
                        nextVersion = nextVersion.nextPreRelease();
                    }
                } else {
                    nextVersion = nextVersion.nextPreRelease();
                    preReleaseUp = true;
                }
            } else if (this.buildMetaDataTypes.contains(commitType)) {
                if (buildMetaDataUp) {
                    if (this.strategy == ReleaseStrategy.strict) {
                        nextVersion = nextVersion.nextBuildMetaData();
                    }
                } else {
                    nextVersion = nextVersion.nextBuildMetaData();
                    buildMetaDataUp = true;
                }
            }
        }

        return this.ensureSuffix(nextVersion);
    }

    public Version ensureSuffix(final Version nextVersion) {
        Version version = nextVersion;

        final String preRelease = StringUtils.defaultIfEmpty(version.getPreRelease(), this.preRelease);
        final String buildMetaData = StringUtils.defaultIfEmpty(version.getBuildMetaData(), this.buildMetaData);

        if (StringUtils.isNotEmpty(preRelease)) version = version.withPreRelease(preRelease);
        if (StringUtils.isNotEmpty(buildMetaData)) version = version.withBuildMetaData(buildMetaData);

        return version;
    }
}
