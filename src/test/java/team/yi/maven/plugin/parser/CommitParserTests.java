package team.yi.maven.plugin.parser;

import lombok.extern.java.Log;
import lombok.var;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import se.bjurr.gitchangelog.api.model.Commit;
import team.yi.maven.plugin.config.ReleaseLogSettings;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.logging.LogManager;

@Log
public class CommitParserTests {
    static {
        InputStream stream = CommitParserTests.class.getClassLoader().
            getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void parseTokensTest() throws URISyntaxException, IOException, ParseException {
        Class<? extends CommitParserTests> classType = getClass();
        ClassLoader classLoader = classType.getClassLoader();
        URL resource = classLoader.getResource("commit-message.md");

        if (resource == null) return;

        Path path = Paths.get(resource.toURI());

        String authorName = "ymind";
        String authorEmailAddress = "ymind.chan@yi.team";
        String commitTime = "2020-02-10 12:23:45";
        Long commitTimeLong = DateUtils.parseDate(commitTime, "yyyy-MM-dd HH:mm:ss").getTime();
        String message = FileUtils.readFileToString(path.toFile(), StandardCharsets.UTF_8).trim();
        String hashFull = "02ce19bbbf7058f474f760fe4a4447301190dea9";
        Boolean isMerge = false;

        ReleaseLogSettings settings = new ReleaseLogSettings();
        Commit commit = new Commit(authorName, authorEmailAddress, commitTime, commitTimeLong, message, hashFull, isMerge);
        CommitParser parser = new CommitParser(settings, commit);

        parser.parseTokens();
    }

    @Test
    public void parseTest() throws URISyntaxException, IOException, ParseException {
        Class<? extends CommitParserTests> classType = getClass();
        ClassLoader classLoader = classType.getClassLoader();
        URL resource = classLoader.getResource("commit-message.md");

        if (resource == null) return;

        Path path = Paths.get(resource.toURI());

        String authorName = "ymind";
        String authorEmailAddress = "ymind.chan@yi.team";
        String commitTime = "2020-02-10 12:23:45";
        Long commitTimeLong = DateUtils.parseDate(commitTime, "yyyy-MM-dd HH:mm:ss").getTime();
        String message = FileUtils.readFileToString(path.toFile(), StandardCharsets.UTF_8).trim();
        String hashFull = "02ce19bbbf7058f474f760fe4a4447301190dea9";
        Boolean isMerge = false;

        ReleaseLogSettings settings = new ReleaseLogSettings();
        settings.setIssueUrlTemplate("https://github.com/ymind/maven-semantic-gitlog/issues/:issueId");
        settings.setCommitUrlTemplate("https://github.com/ymind/maven-semantic-gitlog/commit/:commitId");
        settings.setMentionUrlTemplate("https://github.com/:username");
        Commit commit = new Commit(authorName, authorEmailAddress, commitTime, commitTimeLong, message, hashFull, isMerge);
        CommitParser parser = new CommitParser(settings, commit);
        var releaseCommit = parser.parse();

        if (releaseCommit == null) return;

        log.info(releaseCommit.getMessageTitle());
    }
}
