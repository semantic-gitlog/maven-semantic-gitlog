package team.yi.maven.plugin.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;
import team.yi.maven.plugin.config.ReleaseLogSettings;
import team.yi.maven.plugin.model.ReleaseCommitLocale;
import team.yi.maven.plugin.parser.CommitLocaleParser;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CommitLocaleService {
    private final ReleaseLogSettings releaseLogSettings;
    private final Log log;
    private final Set<ReleaseCommitLocale> commitLocales = new HashSet<>();

    public CommitLocaleService(final ReleaseLogSettings releaseLogSettings, final Log log) {
        this.releaseLogSettings = releaseLogSettings;
        this.log = log;
    }

    public void load() {
        for (final Map.Entry<String, File> entry : this.releaseLogSettings.getCommitLocales().entrySet()) {
            final String lang = entry.getKey();
            final File file = entry.getValue();

            CommitLocaleParser parser = null;

            try {
                parser = new CommitLocaleParser(lang, file);
            } catch (IOException e) {
                this.log.debug(e.getMessage(), e);
            }

            if (parser == null) continue;

            final List<ReleaseCommitLocale> items = parser.parse();

            this.commitLocales.addAll(items);
        }
    }

    public ReleaseCommitLocale get(final String commitHash, final String lang) {
        return this.commitLocales.stream()
            .filter(x -> StringUtils.compareIgnoreCase(x.getLang(), lang) == 0)
            .filter(x -> StringUtils.startsWithIgnoreCase(commitHash, x.getCommitHash()))
            .findFirst()
            .orElse(null);
    }

    public List<ReleaseCommitLocale> get(final String commitHash) {
        return this.commitLocales.stream()
            .filter(x -> StringUtils.startsWithIgnoreCase(commitHash, x.getCommitHash()))
            .collect(Collectors.toList());
    }
}
