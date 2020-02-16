package team.yi.maven.plugin.parser;

import lombok.extern.java.Log;
import org.junit.Test;
import team.yi.maven.plugin.model.ReleaseCommitLocale;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.LogManager;

@Log
public class CommitLocaleParserTests {
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
    public void parseTest() throws IOException {
        Path path = Paths.get(System.getProperty("user.dir"), "config", "gitlog", "commit-locales.zh-cn.md");
        File file = path.toFile();
        CommitLocaleParser parser = new CommitLocaleParser("zh-cn", file);

        List<ReleaseCommitLocale> commitLocales = parser.parse();

        log.info(String.valueOf(commitLocales.size()));
    }
}
