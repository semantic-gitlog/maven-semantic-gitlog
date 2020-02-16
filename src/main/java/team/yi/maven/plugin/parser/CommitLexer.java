package team.yi.maven.plugin.parser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import team.yi.maven.plugin.config.ReleaseLogSettings;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

@SuppressWarnings({"PMD.GodClass", "PMD.TooManyMethods"})
public class CommitLexer extends Lexer {
    private final List<String> closeIssueActions;

    protected CommitLexer(final Path path, final ReleaseLogSettings settings) throws IOException {
        this(path.toFile(), settings);
    }

    protected CommitLexer(final File file, final ReleaseLogSettings settings) throws IOException {
        this(file, StandardCharsets.UTF_8, settings);
    }

    public CommitLexer(final File file, final Charset charset, final ReleaseLogSettings settings) throws IOException {
        this(FileUtils.readFileToString(file, charset).trim(), settings);
    }

    public CommitLexer(final String contents, final ReleaseLogSettings settings) throws IOException {
        super(contents);

        this.closeIssueActions = settings.getCloseIssueActions();
    }

    @Override
    public Token next() {
        switch (this.currentMode) {
            case text:
                return this.nextText();
            case scope:
                return this.nextScope();
            case subject:
                return this.nextSubject();
            case body:
                return this.nextBody();
            case section:
                return this.nextSectionBoundary();
            case sectionLocale:
                return this.nextSectionLocale();
            case issueRef:
                return this.nextIssueRef();
            case actionIssueRef:
                return this.nextActionIssueRef();
            case mentionRef:
                return this.nextMentionRef();
            default:
                throw new ParseException(this.line, this.column, "unsupported lexer mode.");
        }
    }

    private Token nextMentionRef() {
        this.startRead();

        outer:
        while (true) {
            final Character ch = this.la(0);
            final Character ch1 = this.la(1);

            switch (ch) {
                case LexerConstants.EOF:
                    if (this.savedPos == this.position) return this.createToken(TokenKind.eof);

                    break outer;

                case CharUtils.LF:
                case CharUtils.CR:
                    this.readWhitespace();
                    this.leaveMode();

                    break outer;

                case LexerConstants.AT:
                    this.consume();

                    return this.createToken(TokenKind.mentionStart);

                case LexerConstants.SPACE:
                    this.leaveMode();

                    return this.createToken(TokenKind.mentionEnd, null);

                default:
                    this.consume();

                    if (LexerConstants.SPACE == ch1) break outer;

                    break;
            }
        }

        return this.createToken(TokenKind.mention);
    }

    private Token nextIssueRef() {
        this.startRead();

        outer:
        while (true) {
            final Character ch = this.la(0);
            final Character ch1 = this.la(1);

            switch (ch) {
                case LexerConstants.EOF:
                    if (this.savedPos == this.position) return this.createToken(TokenKind.eof);

                    break outer;

                case CharUtils.LF:
                case CharUtils.CR:
                    this.readWhitespace();
                    this.leaveMode();

                    break;

                case LexerConstants.OPEN_BRACKET:
                    this.consume();

                    return this.createToken(TokenKind.issueStart);

                case LexerConstants.CLOSE_BRACKET:
                    this.consume();
                    this.leaveMode();

                    return this.createToken(TokenKind.issueEnd);

                case LexerConstants.SHARP:
                    this.consume();

                    break outer;

                case LexerConstants.SPACE:
                case LexerConstants.SLASH:
                    if (this.last.getKind() == TokenKind.numberInteger) {
                        this.leaveMode();

                        return this.createToken(TokenKind.issueEnd);
                    }

                    if (LexerConstants.SHARP == ch1) {
                        this.consume();

                        return this.createToken(TokenKind.issueStart);
                    }

                    this.consume();

                    break;

                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    return this.readNumber();

                default:
                    if (this.last.getKind() == TokenKind.numberInteger) {
                        this.leaveMode();

                        return this.createToken(TokenKind.issueEnd);
                    }

                    this.consume();

                    break;
            }
        }

        return this.createToken(TokenKind.text);
    }

    private Token nextActionIssueRef() {
        this.startRead();

        outer:
        while (true) {
            final Character ch = this.la(0);
            final Character ch1 = this.la(1);
            final Character ch2 = this.la(2);

            switch (ch) {
                case LexerConstants.EOF:
                    if (this.savedPos == this.position) return this.createToken(TokenKind.eof);

                    break outer;

                // case CharUtils.CR:
                // case CharUtils.LF:
                //     this.readWhitespace();
                //
                //     return this.createToken(TokenKind.localeItemEnd);
                case LexerConstants.SPACE:
                case LexerConstants.OPEN_BRACKET:
                case LexerConstants.SLASH:
                    if (LexerConstants.SHARP == ch1 && Character.isDigit(ch2)) {
                        this.enterMode(LexerMode.issueRef);

                        break outer;
                    }

                    this.consume();

                    break;

                default:
                    if (Character.isWhitespace(ch)) {
                        final String spaces = this.pickWhitespace(0);

                        this.consume(spaces.length());

                        break outer;
                    } else {
                        if (this.last.getKind() == TokenKind.issueEnd) {
                            this.leaveMode();

                            break outer;
                        }

                        final String action = this.pickWord(0);

                        if (this.closeIssueActions.contains(action.toLowerCase(Locale.getDefault()))) {
                            this.consume(action.length());

                            return this.createToken(TokenKind.issueAction);
                        }
                    }

                    this.consume();

                    break;
            }
        }

        return this.createToken(TokenKind.issueRepo);
    }

    private Token nextSectionLocale() {
        this.startRead();

        outer:
        while (true) {
            final Character ch = this.la(0);
            final Character ch1 = this.la(1);
            final Character ch2 = this.la(2);

            switch (ch) {
                case LexerConstants.EOF:
                    if (this.savedPos == this.position) return this.createToken(TokenKind.eof);

                    break outer;

                case CharUtils.CR:
                case CharUtils.LF:
                    this.readWhitespace();

                    return this.createToken(TokenKind.localeItemEnd);

                case LexerConstants.HYPHEN:
                    if (LexerConstants.SPACE == ch1) {
                        this.consume(2);

                        return this.createToken(TokenKind.localeItemStart, LexerConstants.HYPHEN);
                    }

                    this.consume();
                    break;

                case LexerConstants.ASTERISK:
                    if (LexerConstants.ASTERISK == ch1 && LexerConstants.OPENING_BRACKET == ch2) {
                        final String lang = this.pickTo(3, LexerConstants.CLOSING_BRACKET);

                        this.consume(lang.length() + 6);
                        this.readWhitespace();

                        return this.createToken(TokenKind.localeLang, lang);
                    }

                    this.consume();
                    break;

                default:
                    if (ch1 == CharUtils.CR || ch1 == CharUtils.LF) {
                        this.consume();

                        break outer;
                    }

                    this.consume();

                    break;
            }
        }

        return this.createToken(TokenKind.localeSubject);
    }

    private Token nextSectionBoundary() {
        this.startRead();

        outer:
        while (true) {
            final Character ch = this.la(0);
            final Character ch1 = this.la(1);
            final Character chn1 = this.la(-1);

            switch (ch) {
                case LexerConstants.EOF:
                    if (this.savedPos == this.position) {
                        return this.createToken(TokenKind.eof);
                    }

                    break outer;

                case CharUtils.CR:
                case CharUtils.LF:
                    this.readWhitespace();

                    break;

                case LexerConstants.SHARP:
                    if (LexerConstants.SPACE == ch1) {
                        final String xHeader = this.pick(0, 8);
                        final Character chx0 = this.la(xHeader.length());

                        if (LexerConstants.LOCALE_LIST_HEADER.equals(xHeader)
                            && (CharUtils.LF == chn1 || CharUtils.CR == chn1)
                            && (CharUtils.LF == chx0 || CharUtils.CR == chx0)) {
                            this.consume(xHeader.length());
                            this.readWhitespace();
                            this.enterMode(LexerMode.sectionLocale);

                            return this.createToken(TokenKind.localeListHeader, LexerConstants.LOCALE_LIST_HEADER);
                        }
                    }

                    this.consume();
                    break;

                default:
                    break outer;
            }
        }

        return this.createToken(TokenKind.sectionBoundary);
    }

    private Token nextBody() {
        if (this.last.getKind() == TokenKind.subjectEnd) {
            return this.createToken(TokenKind.bodyStart, StringUtils.EMPTY);
        }

        this.startRead();

        outer:
        while (true) {
            final Character ch = this.la(0);
            final Character ch1 = this.la(1);
            final Character ch2 = this.la(2);

            switch (ch) {
                case LexerConstants.EOF:
                    if (this.savedPos == this.position) return this.createToken(TokenKind.eof);

                    break outer;

                case LexerConstants.HYPHEN: {
                    if (this.last.getKind() == TokenKind.body || this.last.getKind() == TokenKind.bodyStart || this.last.getKind() == TokenKind.bodyEnd) {
                        final String lineText = this.pickToLineEnd(0);

                        if (lineText.startsWith("--------")) {
                            if (this.last.getKind() == TokenKind.bodyEnd) {
                                this.consume(lineText.length());
                                this.leaveMode();
                                this.enterMode(LexerMode.section);

                                return this.createToken(TokenKind.sectionBoundary);
                            }

                            return this.createToken(TokenKind.bodyEnd, StringUtils.EMPTY);
                        }
                    }

                    this.consume();

                    break;
                }
                default:
                    if ((CharUtils.CR == ch1 || CharUtils.LF == ch1) && LexerConstants.HYPHEN == ch2) {
                        final String lineText = this.pickToLineEnd(2);

                        if (lineText.startsWith("--------")) {
                            this.consume(2);

                            break outer;
                        }
                    }

                    if (this.checkIssueActions(ch, ch1, ch2)) break outer;

                    this.consume();
                    break;
            }
        }

        return this.createToken(TokenKind.body);
    }

    private Token nextSubject() {
        this.startRead();

        outer:
        while (true) {
            final Character ch = this.la(0);
            final Character ch1 = this.la(1);
            final Character ch2 = this.la(2);

            switch (ch) {
                case LexerConstants.EOF:
                    if (this.savedPos == this.position) return this.createToken(TokenKind.eof);

                    break outer;

                case CharUtils.LF:
                case CharUtils.CR:
                    this.readWhitespace();
                    this.leaveMode();
                    this.enterMode(LexerMode.body);

                    return this.createToken(TokenKind.subjectEnd, StringUtils.EMPTY);

                // ` (#123)` | ` @xxx `
                case LexerConstants.SPACE:
                case LexerConstants.OPEN_BRACKET:
                    if (LexerConstants.SHARP == ch1 && Character.isDigit(ch2)) {
                        this.enterMode(LexerMode.issueRef);

                        break outer;
                    } else if (LexerConstants.AT == ch1) {
                        this.consume();
                        this.enterMode(LexerMode.mentionRef);

                        break outer;
                    }

                    this.consume();

                    break;

                default:
                    this.consume();

                    if (CharUtils.CR == ch1 || CharUtils.LF == ch1) break outer;

                    break;
            }
        }

        return this.createToken(TokenKind.subject);
    }

    private Token nextScope() {
        this.startRead();

        outer:
        while (true) {
            final Character ch = this.la(0);
            final Character ch1 = this.la(1);

            switch (ch) {
                case LexerConstants.EOF:
                case LexerConstants.SPACE:
                case LexerConstants.TAB:
                case CharUtils.LF:
                case CharUtils.CR:
                    this.leaveMode();

                    break outer;

                case LexerConstants.OPEN_BRACKET:
                    this.consume();

                    return this.createToken(TokenKind.scopeStart);

                case LexerConstants.CLOSE_BRACKET:
                    this.consume();

                    this.leaveMode();

                    return this.createToken(TokenKind.scopeEnd);

                default:
                    this.consume();

                    if (LexerConstants.CLOSE_BRACKET == ch1) break outer;

                    break;
            }
        }

        return this.createToken(TokenKind.scope);
    }

    private Token nextType() {
        this.startRead();

        outer:
        while (true) {
            final Character ch = this.la(0);
            final Character ch1 = this.la(1);

            switch (ch) {
                case LexerConstants.EOF:
                case LexerConstants.SPACE:
                case LexerConstants.TAB:
                case CharUtils.LF:
                case CharUtils.CR:
                    final String message = "Missing commit type.";

                    throw new LexerException(this.line, this.column, message);

                case LexerConstants.EXCLAMATION_MARK:
                case ':':
                    break outer;

                default:
                    this.consume();

                    if (LexerConstants.OPEN_BRACKET == ch1) {
                        this.enterMode(LexerMode.scope);

                        break outer;
                    }

                    break;
            }
        }

        return this.createToken(TokenKind.type);
    }

    private Token nextText() {
        this.startRead();

        if (line == 1 && this.column == 1) {
            return this.nextType();
        }

        outer:
        while (true) {
            final Character ch = this.la(0);
            final Character ch1 = this.la(1);

            switch (ch) {
                case LexerConstants.EOF:
                    if (this.savedPos == this.position) return this.createToken(TokenKind.eof);

                    break outer;

                case CharUtils.LF:
                case CharUtils.CR:
                    this.readWhitespace();

                    return this.nextText();

                case LexerConstants.EXCLAMATION_MARK:
                    this.consume();
                    this.enterMode(LexerMode.scope);

                    return this.createToken(TokenKind.attention);

                case ':':
                    this.consume();
                    this.readWhitespace();
                    this.enterMode(LexerMode.subject);

                    return this.createToken(TokenKind.subjectStart, ":");

                default:
                    if (LexerConstants.EXCLAMATION_MARK == ch1 || CharUtils.CR == ch1 || CharUtils.LF == ch1) {
                        this.consume();

                        break outer;
                    }

                    this.consume();

                    break;
            }
        }

        return this.createToken(TokenKind.text);
    }

    private boolean checkIssueActions(final char ch, final char ch1, final char ch2) {
        if (Character.isWhitespace(ch) || LexerConstants.SLASH == ch || LexerConstants.OPEN_BRACKET == ch) {
            if (LexerConstants.SHARP == ch1 && Character.isDigit(ch2)) {
                this.enterMode(LexerMode.issueRef);

                return true;
            } else if (LexerConstants.AT == ch1) {
                this.consume();
                this.enterMode(LexerMode.mentionRef);

                return true;
            }

            final String action = this.pickWord(1);
            final String normalizedAction = StringUtils.stripStart(action.toLowerCase(Locale.getDefault()), "/");

            if (this.closeIssueActions.contains(normalizedAction)) {
                final String whiteSpace = this.pickWhitespace(action.length() + 1);
                final String target = this.pickWord(action.length() + 1 + whiteSpace.length());

                if (StringUtils.isNotEmpty(target)) {
                    final int pos = target.lastIndexOf(LexerConstants.SHARP);

                    if (pos > -1) {
                        final String issueId = this.pickNumberInteger(action.length() + 2 + whiteSpace.length() + pos);

                        if (NumberUtils.isCreatable(issueId)) {
                            this.consume();
                            this.enterMode(LexerMode.actionIssueRef);

                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
