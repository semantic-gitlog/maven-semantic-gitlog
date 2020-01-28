package team.yi.maven.plugin;

import de.skuzzle.semantic.Version;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GitReleaseLogSettings {
    @Parameter(property = "disabled")
    private Boolean disabled = false;

    @Parameter(property = "useCrazyGrowing")
    private Boolean useCrazyGrowing = false;

    @Parameter(property = "lastVersion")
    private Version lastVersion;

    @Parameter(property = "preRelease")
    private String preRelease;

    @Parameter(property = "buildMetaData")
    private String buildMetaData;

    @Parameter(property = "majorTypes")
    private String majorTypes;

    @Parameter(property = "minorTypes", defaultValue = "feat")
    private String minorTypes = "feat";

    @Parameter(property = "patchTypes", defaultValue = "fix,perf,revert,refactor")
    private String patchTypes = "fix,perf,revert,refactor";

    @Parameter(property = "preReleaseTypes")
    private String preReleaseTypes;

    @Parameter(property = "buildMetaDataTypes")
    private String buildMetaDataTypes;

    @Parameter(property = "repoBaseUrl")
    private String repoBaseUrl;

    @Parameter(property = "commitUrlTemplate")
    private String commitUrlTemplate;

    @Parameter(property = "issueUrlTemplate")
    private String issueUrlTemplate;

    @Parameter(property = "derivedVersionMark")
    private String derivedVersionMark;

    public String getDerivedVersionMark() {
        return derivedVersionMark;
    }

    public void setDerivedVersionMark(String derivedVersionMark) {
        this.derivedVersionMark = derivedVersionMark;
    }

    public String getRepoBaseUrl() {
        return repoBaseUrl;
    }

    public void setRepoBaseUrl(String repoBaseUrl) {
        this.repoBaseUrl = repoBaseUrl;
    }

    public String getCommitUrlTemplate() {
        return commitUrlTemplate;
    }

    public void setCommitUrlTemplate(String commitUrlTemplate) {
        this.commitUrlTemplate = commitUrlTemplate;
    }

    public String getIssueUrlTemplate() {
        return issueUrlTemplate;
    }

    public void setIssueUrlTemplate(String issueUrlTemplate) {
        this.issueUrlTemplate = issueUrlTemplate;
    }

    public String getPreRelease() {
        return preRelease;
    }

    public void setPreRelease(String preRelease) {
        this.preRelease = preRelease;
    }

    public List<String> getPreReleaseTypes() {
        String[] items = StringUtils.split(this.preReleaseTypes, ",");

        return items == null || items.length == 0 ? new ArrayList<>() : Arrays.asList(items);
    }

    public void setPreReleaseTypes(String preReleaseTypes) {
        this.preReleaseTypes = preReleaseTypes;
    }

    public String getBuildMetaData() {
        return buildMetaData;
    }

    public void setBuildMetaData(String buildMetaData) {
        this.buildMetaData = buildMetaData;
    }

    public List<String> getBuildMetaDataTypes() {
        String[] items = StringUtils.split(this.buildMetaDataTypes, ",");

        return items == null || items.length == 0 ? new ArrayList<>() : Arrays.asList(items);
    }

    public void setBuildMetaDataTypes(String buildMetaDataTypes) {
        this.buildMetaDataTypes = buildMetaDataTypes;
    }

    public List<String> getMajorTypes() {
        String[] items = StringUtils.split(this.majorTypes, ",");

        return items == null || items.length == 0 ? new ArrayList<>() : Arrays.asList(items);
    }

    public void setMajorTypes(String majorTypes) {
        this.majorTypes = majorTypes;
    }

    public List<String> getMinorTypes() {
        String[] items = StringUtils.split(this.minorTypes, ",");

        return items == null || items.length == 0 ? new ArrayList<>() : Arrays.asList(items);
    }

    public void setMinorTypes(String minorTypes) {
        this.minorTypes = minorTypes;
    }

    public List<String> getPatchTypes() {
        String[] items = StringUtils.split(this.patchTypes, ",");

        return items == null || items.length == 0 ? new ArrayList<>() : Arrays.asList(items);
    }

    public void setPatchTypes(String patchTypes) {
        this.patchTypes = patchTypes;
    }

    public Boolean getUseCrazyGrowing() {
        return useCrazyGrowing;
    }

    public void setUseCrazyGrowing(Boolean useCrazyGrowing) {
        this.useCrazyGrowing = useCrazyGrowing;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Version getLastVersion() {
        return lastVersion;
    }

    public void setLastVersion(String lastVersion) {
        this.lastVersion = Version.parseVersion(lastVersion);
    }
}
