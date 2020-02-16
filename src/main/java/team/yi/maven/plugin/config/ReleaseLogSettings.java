package team.yi.maven.plugin.config;

import de.skuzzle.semantic.Version;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugins.annotations.Parameter;
import team.yi.maven.plugin.model.ReleaseStrategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Data
@SuppressWarnings("PMD.TooManyFields")
public class ReleaseLogSettings implements Serializable {
    public static final String BREAKING_CHANGE_PATTERN = "BREAKING CHANGE: ";
    public static final String DEPRECATED_PATTERN = "DEPRECATED: ";
    public static final String ISSUE_ID_PLACEHOLDER = ":issueId";
    public static final String COMMIT_ID_PLACEHOLDER = ":commitId";
    public static final String MENTION_USERNAME_PLACEHOLDER = ":username";

    public static final String DEFAULT_LONG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_SHORT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_CLOSE_ISSUE_ACTIONS = "close,closes,closed,fix,fixes,fixed,resolve,resolves,resolved";
    public static final String DEFAULT_TEMPLATE_FILE = "config/gitlog/CHANGELOG.tpl.md";
    public static final String DEFAULT_TARGET_FILE = "CHANGELOG.md";

    private static final long serialVersionUID = -3088989076911346697L;

    private static final List<String> DEFAULT_MAJOR_TYPES = new ArrayList<>();
    private static final List<String> DEFAULT_MINOR_TYPES = Collections.singletonList("feat");
    private static final List<String> DEFAULT_PATCH_TYPES = Arrays.asList("fix", "perf", "revert", "refactor", "chore");
    private static final List<String> DEFAULT_PRE_RELEASE_TYPES = new ArrayList<>();
    private static final List<String> DEFAULT_BUILD_META_DATA_TYPES = new ArrayList<>();
    private static final List<String> DEFAULT_HIDDEN_TYPES = Collections.singletonList("release");

    @Parameter(property = "disabled", defaultValue = "false")
    private Boolean disabled = false;

    @Parameter(property = "isPreRelease", defaultValue = "false")
    private Boolean isPreRelease = false;

    @Parameter(property = "strategy", defaultValue = "strict")
    private ReleaseStrategy strategy = ReleaseStrategy.strict;

    @Parameter(property = "forceNextVersion", defaultValue = "true")
    private Boolean forceNextVersion = true;

    @Parameter(property = "lastVersion")
    private Version lastVersion;

    @Parameter(property = "preRelease")
    private String preRelease;

    @Parameter(property = "buildMetaData")
    private String buildMetaData;

    @Parameter(property = "majorTypes")
    private String majorTypes;

    @Parameter(property = "minorTypes", defaultValue = "feat")
    private String minorTypes;

    @Parameter(property = "patchTypes", defaultValue = "fix,perf,revert,refactor,chore")
    private String patchTypes;

    @Parameter(property = "preReleaseTypes")
    private String preReleaseTypes;

    @Parameter(property = "buildMetaDataTypes")
    private String buildMetaDataTypes;

    @Parameter(property = "hiddenTypes", defaultValue = "release")
    private String hiddenTypes;

    @Parameter(property = "repoBaseUrl")
    private String repoBaseUrl;

    @Parameter(property = "repoName")
    private String repoName;

    @Parameter(property = "issueUrlTemplate")
    private String issueUrlTemplate;

    @Parameter(property = "commitUrlTemplate")
    private String commitUrlTemplate;

    @Parameter(property = "mentionUrlTemplate")
    private String mentionUrlTemplate;

    @Parameter(property = "derivedVersionMark")
    private String derivedVersionMark;

    @Parameter(property = "longDateFormat", defaultValue = DEFAULT_LONG_DATE_FORMAT)
    private String longDateFormat = DEFAULT_LONG_DATE_FORMAT;

    @Parameter(property = "shortDateFormat", defaultValue = DEFAULT_SHORT_DATE_FORMAT)
    private String shortDateFormat = DEFAULT_SHORT_DATE_FORMAT;

    @Parameter(property = "closeIssueActions", defaultValue = DEFAULT_CLOSE_ISSUE_ACTIONS)
    private String closeIssueActions = DEFAULT_CLOSE_ISSUE_ACTIONS;

    public ReleaseStrategy getStrategy() {
        return this.strategy == null ? ReleaseStrategy.strict : strategy;
    }

    public List<String> getCloseIssueActions() {
        final String data = StringUtils.defaultIfBlank(this.closeIssueActions, DEFAULT_CLOSE_ISSUE_ACTIONS);
        final String[] items = StringUtils.splitPreserveAllTokens(data.toLowerCase(Locale.getDefault()), ",|;");

        return items == null || items.length == 0 ? DEFAULT_PRE_RELEASE_TYPES : Arrays.asList(items);
    }

    public List<String> getPreReleaseTypes() {
        final String[] items = StringUtils.split(this.preReleaseTypes, ",");

        return items == null || items.length == 0 ? DEFAULT_PRE_RELEASE_TYPES : Arrays.asList(items);
    }

    public List<String> getBuildMetaDataTypes() {
        final String[] items = StringUtils.split(this.buildMetaDataTypes, ",");

        return items == null || items.length == 0 ? DEFAULT_BUILD_META_DATA_TYPES : Arrays.asList(items);
    }

    public List<String> getMajorTypes() {
        final String[] items = StringUtils.split(this.majorTypes, ",");

        return items == null || items.length == 0 ? DEFAULT_MAJOR_TYPES : Arrays.asList(items);
    }

    public List<String> getMinorTypes() {
        final String[] items = StringUtils.split(this.minorTypes, ",");

        return items == null || items.length == 0 ? DEFAULT_MINOR_TYPES : Arrays.asList(items);
    }

    public List<String> getPatchTypes() {
        final String[] items = StringUtils.split(this.patchTypes, ",");

        return items == null || items.length == 0 ? DEFAULT_PATCH_TYPES : Arrays.asList(items);
    }

    public List<String> getHiddenTypes() {
        final String[] items = StringUtils.split(this.hiddenTypes, ",");

        return items == null || items.length == 0 ? DEFAULT_HIDDEN_TYPES : Arrays.asList(items);
    }

    public String createIssueUrl(final Integer issueId) {
        if (StringUtils.isEmpty(this.issueUrlTemplate)) return null;

        return this.issueUrlTemplate.replaceAll(ISSUE_ID_PLACEHOLDER, String.valueOf(issueId));
    }

    public String createCommitUrl(final String commitId) {
        if (StringUtils.isEmpty(this.commitUrlTemplate)) return null;

        return this.commitUrlTemplate.replaceAll(COMMIT_ID_PLACEHOLDER, commitId);
    }

    public String createMentionUrl(final String username) {
        if (StringUtils.isEmpty(this.mentionUrlTemplate)) return null;

        return this.mentionUrlTemplate.replaceAll(MENTION_USERNAME_PLACEHOLDER, username);
    }
}
