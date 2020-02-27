package team.yi.maven.plugin.config;

import de.skuzzle.semantic.Version;
import team.yi.tools.semanticgitlog.config.GitlogSettings;
import team.yi.tools.semanticgitlog.config.ReleaseStrategy;

import java.io.File;
import java.util.List;
import java.util.Map;

@SuppressWarnings("PMD.ExcessivePublicCount")
public class GitlogPluginSettings extends GitlogSettings {
    private static final long serialVersionUID = -857288928493905104L;

    @Override
    public Version getLastVersion() {
        return super.getLastVersion();
    }

    @Override
    public void setLastVersion(Version lastVersion) {
        super.setLastVersion(lastVersion);
    }

    @Override
    public void setLastVersion(final String lastVersion) {
        super.setLastVersion(lastVersion);
    }

    @Override
    public ReleaseStrategy getStrategy() {
        return super.getStrategy();
    }

    @Override
    public List<String> getMajorTypes() {
        return super.getMajorTypes();
    }

    @Override
    public List<String> getMinorTypes() {
        return super.getMinorTypes();
    }

    @Override
    public List<String> getPatchTypes() {
        return super.getPatchTypes();
    }

    @Override
    public List<String> getPreReleaseTypes() {
        return super.getPreReleaseTypes();
    }

    @Override
    public List<String> getBuildMetaDataTypes() {
        return super.getBuildMetaDataTypes();
    }

    @Override
    public List<String> getHiddenTypes() {
        return super.getHiddenTypes();
    }

    @Override
    public Boolean getDisabled() {
        return super.getDisabled();
    }

    @Override
    public void setDisabled(final Boolean disabled) {
        super.setDisabled(disabled);
    }

    @Override
    public Boolean getIsPreRelease() {
        return super.getIsPreRelease();
    }

    @Override
    public void setIsPreRelease(final Boolean isPreRelease) {
        super.setIsPreRelease(isPreRelease);
    }

    @Override
    public Boolean getForceNextVersion() {
        return super.getForceNextVersion();
    }

    @Override
    public void setForceNextVersion(final Boolean forceNextVersion) {
        super.setForceNextVersion(forceNextVersion);
    }

    @Override
    public String getLongDateFormat() {
        return super.getLongDateFormat();
    }

    @Override
    public void setLongDateFormat(final String longDateFormat) {
        super.setLongDateFormat(longDateFormat);
    }

    @Override
    public String getShortDateFormat() {
        return super.getShortDateFormat();
    }

    @Override
    public void setShortDateFormat(final String shortDateFormat) {
        super.setShortDateFormat(shortDateFormat);
    }

    @Override
    public String getDerivedVersionMark() {
        return super.getDerivedVersionMark();
    }

    @Override
    public void setDerivedVersionMark(final String derivedVersionMark) {
        super.setDerivedVersionMark(derivedVersionMark);
    }

    @Override
    public String getPreRelease() {
        return super.getPreRelease();
    }

    @Override
    public void setPreRelease(final String preRelease) {
        super.setPreRelease(preRelease);
    }

    @Override
    public String getBuildMetaData() {
        return super.getBuildMetaData();
    }

    @Override
    public void setBuildMetaData(final String buildMetaData) {
        super.setBuildMetaData(buildMetaData);
    }

    @Override
    public String getRepoBaseUrl() {
        return super.getRepoBaseUrl();
    }

    @Override
    public String getRepoName() {
        return super.getRepoName();
    }

    @Override
    public Map<String, File> getCommitLocales() {
        return super.getCommitLocales();
    }

    @Override
    public File getJsonFile() {
        return super.getJsonFile();
    }

    @Override
    public void setJsonFile(File jsonFile) {
        super.setJsonFile(jsonFile);
    }

    @Override
    public void setCommitLocales(Map<String, File> commitLocales) {
        super.setCommitLocales(commitLocales);
    }

    @Override
    public void setRepoName(final String repoName) {
        super.setRepoName(repoName);
    }

    @Override
    public void setRepoBaseUrl(final String repoBaseUrl) {
        super.setRepoBaseUrl(repoBaseUrl);
    }

    @Override
    public void setHiddenTypes(final String hiddenTypes) {
        super.setHiddenTypes(hiddenTypes);
    }

    @Override
    public void setBuildMetaDataTypes(final String buildMetaDataTypes) {
        super.setBuildMetaDataTypes(buildMetaDataTypes);
    }

    @Override
    public void setPreReleaseTypes(final String preReleaseTypes) {
        super.setPreReleaseTypes(preReleaseTypes);
    }

    @Override
    public void setPatchTypes(final String patchTypes) {
        super.setPatchTypes(patchTypes);
    }

    @Override
    public void setMinorTypes(final String minorTypes) {
        super.setMinorTypes(minorTypes);
    }

    @Override
    public void setMajorTypes(final String majorTypes) {
        super.setMajorTypes(majorTypes);
    }

    @Override
    public void setStrategy(final ReleaseStrategy strategy) {
        super.setStrategy(strategy);
    }

    @Override
    public List<String> getCloseIssueActions() {
        return super.getCloseIssueActions();
    }

    @Override
    public void setCloseIssueActions(final String closeIssueActions) {
        super.setCloseIssueActions(closeIssueActions);
    }

    @Override
    public String getIssueUrlTemplate() {
        return super.getIssueUrlTemplate();
    }

    @Override
    public void setIssueUrlTemplate(final String issueUrlTemplate) {
        super.setIssueUrlTemplate(issueUrlTemplate);
    }

    @Override
    public String getCommitUrlTemplate() {
        return super.getCommitUrlTemplate();
    }

    @Override
    public void setCommitUrlTemplate(final String commitUrlTemplate) {
        super.setCommitUrlTemplate(commitUrlTemplate);
    }

    @Override
    public String getMentionUrlTemplate() {
        return super.getMentionUrlTemplate();
    }

    @Override
    public String getDefaultLang() {
        return super.getDefaultLang();
    }

    @Override
    public void setDefaultLang(String defaultLang) {
        super.setDefaultLang(defaultLang);
    }

    @Override
    public void setMentionUrlTemplate(final String mentionUrlTemplate) {
        super.setMentionUrlTemplate(mentionUrlTemplate);
    }
}
