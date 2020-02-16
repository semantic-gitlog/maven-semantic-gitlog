package team.yi.maven.plugin.parser;

import org.apache.commons.lang3.StringUtils;
import team.yi.maven.plugin.model.ReleaseCommitLocale;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommitLocaleParser {
    private final String lang;
    private final CommitLocaleLexer lexer;
    private Token current;
    private List<ReleaseCommitLocale> commitLocales;

    public CommitLocaleParser(final String lang, final File file) throws IOException {
        this.lang = lang;
        this.lexer = new CommitLocaleLexer(file);
    }

    public void reset() {
        this.commitLocales = new ArrayList<>();
    }

    public List<ReleaseCommitLocale> parse() {
        this.reset();
        this.consume();

        while (this.current.getKind() != TokenKind.eof) {
            if (this.current.getKind() == TokenKind.localeItemStart) {
                this.readLocales();
            }

            this.consume();
        }

        return this.commitLocales;
    }

    private void readLocales() {
        String commitHash = null;
        String subject = null;

        while (this.current.getKind() != TokenKind.eof && this.current.getKind() != TokenKind.localeItemEnd) {
            switch (this.current.getKind()) {
                case commitHash:
                    commitHash = StringUtils.trimToNull(this.current.getValue());
                    break;
                case localeSubject:
                    subject = StringUtils.trimToNull(this.current.getValue());
                    break;
                default:
                    break;
            }

            this.consume();
        }

        if (StringUtils.isEmpty(commitHash)) return;
        if (StringUtils.isEmpty(subject)) return;

        final ReleaseCommitLocale commitLocale = new ReleaseCommitLocale(commitHash, this.lang, subject);

        this.commitLocales.add(commitLocale);
    }

    private Token consume() {
        final Token old = this.current;

        this.current = this.lexer.next();

        return old;
    }
}
