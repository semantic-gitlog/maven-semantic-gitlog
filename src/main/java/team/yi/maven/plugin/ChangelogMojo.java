package team.yi.maven.plugin;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import se.bjurr.gitchangelog.api.GitChangelogApi;
import se.bjurr.gitchangelog.api.exceptions.GitChangelogIntegrationException;
import se.bjurr.gitchangelog.api.exceptions.GitChangelogRepositoryException;
import team.yi.maven.plugin.config.GitlogPluginSettings;
import team.yi.tools.semanticgitlog.GitlogService;
import team.yi.tools.semanticgitlog.config.FileSet;
import team.yi.tools.semanticgitlog.model.ReleaseLog;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.maven.plugins.annotations.LifecyclePhase.PROCESS_SOURCES;

@Slf4j
@Mojo(name = "changelog", defaultPhase = PROCESS_SOURCES)
public class ChangelogMojo extends GitChangelogMojo {
    public static final String DEFAULT_TEMPLATE_FILE = "config/gitlog/CHANGELOG.md.mustache";
    public static final String DEFAULT_TARGET_FILE = "CHANGELOG.md";

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;
    @Parameter(property = "gitlog.fileSets")
    private Set<FileSet> fileSets;
    @Parameter(property = "gitlog.mediaWikiUrl")
    private String mediaWikiUrl;
    @Parameter(property = "gitlog.mediaWikiTitle")
    private String mediaWikiTitle;
    @Parameter(property = "gitlog.mediaWikiUsername")
    private String mediaWikiUsername;
    @Parameter(property = "gitlog.mediaWikiPassword")
    private String mediaWikiPassword;
    @Parameter(property = "gitlog.updatePomVersion")
    private Boolean updatePomVersion;

    public Set<FileSet> getFileSets() {
        if (this.fileSets == null || this.fileSets.isEmpty()) return fileSets;

        return this.fileSets.stream()
            .filter(x -> x.getTemplate() != null)
            .filter(x -> x.getTarget() != null)
            .collect(Collectors.toSet());
    }

    @Override
    public void execute(final GitChangelogApi builder)
        throws IOException,
        GitChangelogRepositoryException,
        GitChangelogIntegrationException {

        final Log log = this.getLog();

        this.saveToFile(log, builder);
        this.saveToMediaWiki(log, builder);
    }

    private void saveToFile(final Log log, final GitChangelogApi builder)
        throws IOException,
        GitChangelogRepositoryException {

        Set<FileSet> fileSets = this.getFileSets();

        if (fileSets == null) fileSets = new HashSet<>();

        if (fileSets.isEmpty() && !this.isSupplied(this.mediaWikiUrl)) {
            if (log.isInfoEnabled()) {
                log.info("No output set, using file " + DEFAULT_TARGET_FILE);
            }

            final File template = new File(DEFAULT_TEMPLATE_FILE);
            final File target = new File(DEFAULT_TARGET_FILE);

            fileSets.add(new FileSet(template, target));
        }

        if (fileSets.isEmpty()) return;

        final GitlogPluginSettings releaseLogSettings = this.getReleaseLogSettings();

        if (releaseLogSettings.getDisabled()) {
            for (final FileSet fileSet : fileSets) {
                final File target = fileSet.getTarget();
                final File template = fileSet.getTemplate();

                builder.withTemplatePath(template.getPath());
                builder.toFile(target);

                if (log.isInfoEnabled()) {
                    log.info("#");
                    log.info("#    template: " + template.getPath());
                    log.info("#      target: " + target.getPath());
                    log.info("#");
                }
            }
        } else {
            // TODO refactor `semantic-gitlog`
            final GitlogService releaseLogService = new GitlogService(releaseLogSettings, builder);

            releaseLogService.saveToFiles(fileSets);

            this.updatePom(releaseLogService.generate());
        }
    }

    protected void updatePom(final ReleaseLog releaseLog) {
        if (this.updatePomVersion == null || !this.updatePomVersion) return;
        if (releaseLog == null || releaseLog.getNextVersion() == null) return;

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
            final NodeList nodes = doc.getElementsByTagName("version");

            if (nodes == null || nodes.getLength() == 0) return;

            final String nextVersion = releaseLog.getNextVersion().toString();
            final Node version = nodes.item(0);
            version.setTextContent(nextVersion);

            // write pom
            final Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.transform(new DOMSource(doc), new StreamResult(pomPath.toFile()));
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
        }
    }

    private void saveToMediaWiki(final Log log, final GitChangelogApi builder)
        throws GitChangelogRepositoryException,
        GitChangelogIntegrationException {

        if (!isSupplied(mediaWikiUrl)) return;

        builder.toMediaWiki(mediaWikiUsername, mediaWikiPassword, mediaWikiUrl, mediaWikiTitle);

        if (log.isInfoEnabled()) {
            log.info("#");
            log.info("#     created: " + mediaWikiUrl + "/index.php/" + mediaWikiTitle);
            log.info("#");
        }
    }
}
