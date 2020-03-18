package team.yi.maven.plugin;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import team.yi.tools.semanticgitlog.config.GitlogSettings;
import team.yi.tools.semanticgitlog.git.GitRepo;
import team.yi.tools.semanticgitlog.model.ReleaseLog;
import team.yi.tools.semanticgitlog.render.MustacheGitlogRender;
import team.yi.tools.semanticgitlog.service.CommitLocaleService;
import team.yi.tools.semanticgitlog.service.GitlogService;
import team.yi.tools.semanticgitlog.service.ScopeProfileService;

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
import java.util.stream.IntStream;

import static org.apache.maven.plugins.annotations.LifecyclePhase.PROCESS_SOURCES;

@Slf4j
@Mojo(name = "changelog", defaultPhase = PROCESS_SOURCES)
public class ChangelogMojo extends GitChangelogMojo {
    public static final String DEFAULT_TEMPLATE_FILE = "config/gitlog/CHANGELOG.md.mustache";
    public static final String DEFAULT_TARGET_FILE = "CHANGELOG.md";

    @Parameter
    protected Set<FileSet> fileSets;

    @Parameter(property = "gitlog.updateProjectVersion")
    protected Boolean updateProjectVersion;

    public Set<FileSet> getFileSets() {
        if (this.fileSets == null || this.fileSets.isEmpty()) return fileSets;

        return this.fileSets.stream()
            .filter(x -> x.getTemplate() != null)
            .filter(x -> x.getTarget() != null)
            .collect(Collectors.toSet());
    }

    @Override
    public void execute(final GitRepo gitRepo) throws IOException {
        final Log log = this.getLog();

        this.saveToFile(log, gitRepo);
    }

    private void saveToFile(final Log log, final GitRepo gitRepo) throws IOException {
        Set<FileSet> fileSets = this.getFileSets();

        if (fileSets == null) fileSets = new HashSet<>();

        if (fileSets.isEmpty()) {
            if (log.isInfoEnabled()) {
                log.info("No output set, using file " + DEFAULT_TARGET_FILE);
            }

            final File template = new File(DEFAULT_TEMPLATE_FILE);
            final File target = new File(DEFAULT_TARGET_FILE);

            fileSets.add(new FileSet(template, target));
        }

        if (fileSets.isEmpty()) return;

        final GitlogSettings gitlogSettings = this.getGitlogSettings();

        final CommitLocaleService commitLocaleProvider = new CommitLocaleService(gitlogSettings.getDefaultLang());
        commitLocaleProvider.load(gitlogSettings.getCommitLocales());

        final GitlogService gitlogService = new GitlogService(gitlogSettings, gitRepo, commitLocaleProvider);
        final ReleaseLog releaseLog = gitlogService.generate();

        final ScopeProfileService scopeProfileService = new ScopeProfileService(gitlogSettings.getDefaultLang());
        scopeProfileService.load(gitlogSettings.getScopeProfiles());

        this.saveToFiles(releaseLog, fileSets, scopeProfileService);
        this.updatePom(releaseLog);
        this.exportJson(releaseLog);
    }

    private void saveToFiles(final ReleaseLog releaseLog, final Set<FileSet> fileSets, final ScopeProfileService scopeProfileService) throws IOException {
        if (fileSets == null || fileSets.isEmpty()) return;

        for (final FileSet fileSet : fileSets) {
            final File target = fileSet.getTarget();
            final File template = fileSet.getTemplate();
            final MustacheGitlogRender render = new MustacheGitlogRender(releaseLog, template, scopeProfileService);

            render.render(target);
        }
    }

    protected void updatePom(final ReleaseLog releaseLog) {
        if (this.updateProjectVersion == null || !this.updateProjectVersion) return;
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

            log.info("The version was updated to: {}", nextVersion);

            // write pom
            final Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.transform(new DOMSource(doc), new StreamResult(pomPath.toFile()));
        } catch (final Exception e) {
            log.debug(e.getMessage(), e);
        }
    }
}
