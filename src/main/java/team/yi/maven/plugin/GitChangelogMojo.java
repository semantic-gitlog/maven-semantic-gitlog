package team.yi.maven.plugin;

import de.skuzzle.semantic.Version;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.w3c.dom.*;
import team.yi.tools.semanticgitlog.GitlogConstants;
import team.yi.tools.semanticgitlog.VersionStrategies;
import team.yi.tools.semanticgitlog.config.GitlogSettings;
import team.yi.tools.semanticgitlog.git.GitRepo;
import team.yi.tools.semanticgitlog.model.ReleaseLog;
import team.yi.tools.semanticgitlog.render.JsonGitlogRender;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Map;
import java.util.stream.IntStream;

@SuppressWarnings("PMD.TooManyFields")
public abstract class GitChangelogMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", readonly = true)
    protected MavenProject project;

    @Parameter(property = "gitlog.skip")
    protected Boolean skip;

    @Parameter(property = "gitlog.defaultLang")
    protected String defaultLang;

    @Parameter
    protected Map<String, File> commitLocales;
    @Parameter
    protected Map<String, File> scopeProfiles;

    @Parameter(property = "gitlog.closeIssueActions")
    protected String closeIssueActions;
    @Parameter(property = "gitlog.issueUrlTemplate", defaultValue = "${project.scm.url}/issues/:issueId")
    protected String issueUrlTemplate;
    @Parameter(property = "gitlog.commitUrlTemplate", defaultValue = "${project.scm.url}/commit/:commitId")
    protected String commitUrlTemplate;
    @Parameter(property = "gitlog.mentionUrlTemplate", defaultValue = "https://github.com/:username")
    protected String mentionUrlTemplate;

    @Parameter(property = "gitlog.fromRef")
    protected String fromRef;
    @Parameter(property = "gitlog.fromCommit")
    protected String fromCommit;
    @Parameter(property = "gitlog.toRef")
    protected String toRef;
    @Parameter(property = "gitlog.toCommit")
    protected String toCommit;

    @Parameter(property = "gitlog.untaggedName", defaultValue = GitlogConstants.DEFAULT_UNTAGGED_NAME)
    protected String untaggedName;
    @Parameter(property = "gitlog.isUnstable")
    protected Boolean isUnstable;
    @Parameter(property = "gitlog.strategy")
    protected VersionStrategies strategy;
    @Parameter(property = "gitlog.forceNextVersion")
    protected Boolean forceNextVersion;

    @Parameter(property = "gitlog.lastVersion")
    protected String lastVersion;
    @Parameter(property = "gitlog.preRelease")
    protected String preRelease;
    @Parameter(property = "gitlog.buildMetaData")
    protected String buildMetaData;
    @Parameter(property = "gitlog.majorTypes")
    protected String majorTypes;
    @Parameter(property = "gitlog.minorTypes")
    protected String minorTypes;
    @Parameter(property = "gitlog.patchTypes")
    protected String patchTypes;
    @Parameter(property = "gitlog.preReleaseTypes")
    protected String preReleaseTypes;
    @Parameter(property = "gitlog.buildMetaDataTypes")
    protected String buildMetaDataTypes;
    @Parameter(property = "gitlog.hiddenTypes")
    protected String hiddenTypes;

    @Parameter(property = "gitlog.jsonFile")
    protected File jsonFile;

    @Parameter(property = "gitlog.updateProjectVersion")
    protected Boolean updateProjectVersion;

    protected GitlogSettings getGitlogSettings() {
        final Version lastVersion = Version.isValidVersion(this.lastVersion) ? Version.parseVersion(this.lastVersion) : null;

        return GitlogSettings.builder()
            .defaultLang(this.defaultLang)

            .commitLocales(this.commitLocales)
            .scopeProfiles(this.scopeProfiles)

            .closeIssueActions(this.closeIssueActions)
            .issueUrlTemplate(this.issueUrlTemplate)
            .commitUrlTemplate(this.commitUrlTemplate)
            .mentionUrlTemplate(this.mentionUrlTemplate)

            .fromRef(this.fromRef)
            .fromCommit(this.fromCommit)
            .toRef(this.toRef)
            .toCommit(this.toCommit)

            .untaggedName(this.untaggedName)
            .isUnstable(this.isUnstable)
            .strategy(this.strategy)
            .forceNextVersion(this.forceNextVersion)

            .lastVersion(lastVersion)
            .preRelease(this.preRelease)
            .buildMetaData(this.buildMetaData)
            .majorTypes(this.majorTypes)
            .minorTypes(this.minorTypes)
            .patchTypes(this.patchTypes)
            .preReleaseTypes(this.preReleaseTypes)
            .buildMetaDataTypes(this.buildMetaDataTypes)
            .hiddenTypes(this.hiddenTypes)

            .build();
    }

    @Override
    public final void execute() throws MojoFailureException {
        final Log log = this.getLog();

        if (this.skip != null && this.skip) {
            log.info("Skipping changelog generation");

            return;
        }

        final File baseDir = this.project.getBasedir();

        try (final GitRepo gitRepo = GitRepo.open(baseDir)) {
            this.execute(gitRepo);
        } catch (final Exception e) {
            log.debug(e);

            throw new MojoFailureException(e.getMessage(), e);
        }
    }

    protected abstract void execute(final GitRepo gitRepo) throws IOException;

    protected void exportJson(final ReleaseLog releaseLog) throws IOException {
        if (this.jsonFile == null) return;

        final JsonGitlogRender render = new JsonGitlogRender(releaseLog, StandardCharsets.UTF_8);

        render.render(this.jsonFile);
    }

    protected void updatePom(final ReleaseLog releaseLog) {
        if (this.updateProjectVersion == null || !this.updateProjectVersion) return;
        if (releaseLog == null || releaseLog.getNextVersion() == null) return;

        final Log log = this.getLog();
        final File baseDir = this.project.getBasedir();

        // pom.xml project -> version
        try {
            // backup pom
            final Path pomPath = Paths.get(baseDir.getAbsolutePath(), "pom.xml");
            final Path pomBackupPath = Paths.get(baseDir.getAbsolutePath(), "pom.xml.gitlogBackup");
            Files.copy(pomPath, pomBackupPath, StandardCopyOption.REPLACE_EXISTING);

            // read pom
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            final DocumentBuilder db = dbf.newDocumentBuilder();
            final Document doc = db.parse(pomPath.toFile());
            final NodeList childNodes = doc.getDocumentElement().getChildNodes();

            if (childNodes == null || childNodes.getLength() == 0) {
                log.error("pom.xml has no nodes.");

                return;
            }

            final Node versionNode = IntStream.range(0, childNodes.getLength())
                .mapToObj(childNodes::item)
                .filter(childNode -> childNode instanceof Element)
                .filter(childNode -> StringUtils.equals(childNode.getNodeName(), "version"))
                .findFirst()
                .orElse(null);

            if (versionNode == null) {
                log.error("The version node was not found in pom.xml.");

                return;
            }

            final String nextVersion = releaseLog.getNextVersion().toString();

            versionNode.setTextContent(nextVersion);

            log.info("The version was updated to: " + nextVersion + "");

            // write pom
            final Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.transform(new DOMSource(doc), new StreamResult(pomPath.toFile()));
        } catch (final Exception e) {
            log.debug(e.getMessage(), e);
        }
    }
}
