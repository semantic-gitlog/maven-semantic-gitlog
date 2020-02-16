package team.yi.maven.plugin.parser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.CharUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import static team.yi.maven.plugin.parser.LexerMode.text;

@SuppressWarnings({"PMD.GodClass", "PMD.TooManyMethods"})
public abstract class Lexer {
    protected final String contents;
    protected LexerMode currentMode;
    protected Stack<LexerMode> lexerModes;

    protected int length;
    protected int line;
    protected int column;
    protected int position;

    protected int savedColumn;
    protected int savedLine;
    protected int savedPos;

    protected Token last;

    protected Lexer(final Path path) throws IOException {
        this(path.toFile());
    }

    protected Lexer(final File file) throws IOException {
        this(file, StandardCharsets.UTF_8);
    }

    protected Lexer(final File file, final Charset charset) throws IOException {
        this.contents = FileUtils.readFileToString(file, charset).trim();
        this.length = this.contents.length();

        this.reset();
    }

    protected Lexer(final String contents) throws IOException {
        this.contents = contents;
        this.length = this.contents.length();

        this.reset();
    }

    public abstract Token next();

    protected final void enterMode(final LexerMode mode) {
        this.lexerModes.push(this.currentMode);
        this.currentMode = mode;
    }

    protected final void leaveMode() {
        this.currentMode = this.lexerModes.pop();
    }

    protected final void reset() {
        this.lexerModes = new Stack<>();
        this.currentMode = text;
        this.lexerModes.push(this.currentMode);

        this.line = 1;
        this.column = 1;
        this.position = 0;
    }

    protected final void newLine() {
        this.line++;
        this.column = 1;
    }

    protected final void startRead() {
        this.savedLine = this.line;
        this.savedColumn = this.column;
        this.savedPos = this.position;
    }

    protected final Character la(final int count) {
        return this.position + count >= this.length || this.position + count < 0
            ? LexerConstants.EOF
            : this.contents.charAt(this.position + count);
    }

    protected final String cs(final int count) {
        final StringBuilder builder = new StringBuilder();

        for (int i = 1; i <= count; i++) {
            final Character ch = this.la(i);

            if (ch.equals(LexerConstants.EOF)) break;

            builder.append(ch);
        }

        return builder.toString();
    }

    protected final void consume() {
        this.position++;
        this.column++;
    }

    protected final void consume(final int count) {
        int i = count;

        if (i <= 0) throw new ParseException(this.line, this.column, "count must greater than 0.");

        while (i > 0) {
            this.consume();

            i--;
        }
    }

    protected final Token createToken(final TokenKind kind, final char value) {
        return this.createToken(kind, String.valueOf(value));
    }

    protected final Token createToken(final TokenKind kind, final String value) {
        this.last = new Token(kind, value, this.line, this.column);

        return this.last;
    }

    protected final Token createToken(final TokenKind kind) {
        final String tokenData = this.contents.substring(this.savedPos, this.savedPos + this.position - this.savedPos);

        this.last = new Token(kind, tokenData, this.savedLine, this.savedColumn);

        return this.last;
    }

    protected final void readWhitespace() {
        while (true) {
            final Character ch = this.la(0);

            switch (ch) {
                case LexerConstants.SPACE:
                case LexerConstants.TAB:
                    this.consume();
                    break;

                case CharUtils.LF:
                    this.consume();
                    this.newLine();

                    break;

                case CharUtils.CR:
                    this.consume();

                    if (CharUtils.LF == this.la(0)) this.consume();

                    this.newLine();

                    break;
                default:
                    return;
            }
        }
    }

    protected final Token readNumber() {
        this.startRead();
        this.consume();

        boolean hasDot = false;

        while (true) {
            final Character ch = this.la(0);

            if (Character.isDigit(ch)) this.consume();
            else if (LexerConstants.DOT == ch && !hasDot && Character.isDigit(this.la(1))) {
                this.consume();
                hasDot = true;
            } else break;
        }

        return this.createToken(hasDot ? TokenKind.numberDouble : TokenKind.numberInteger);
    }

    protected final String pick(final int start, final int maxLength) {
        int i = start;
        final StringBuilder b = new StringBuilder();

        while (true) {
            final Character ch = this.la(i);

            if (i > maxLength || LexerConstants.EOF == ch) {
                break;
            } else {
                b.append(ch);

                i++;
            }
        }

        return b.toString();
    }

    protected final String pickWhitespace(final int start) {
        return pickWhitespace(start, Integer.MAX_VALUE);
    }

    protected final String pickWhitespace(final int start, final int maxLength) {
        int i = start;
        final StringBuilder b = new StringBuilder();

        while (true) {
            final Character ch = this.la(i);

            if (i > maxLength || LexerConstants.EOF == ch || !Character.isWhitespace(ch)) {
                break;
            } else {
                b.append(ch);

                i++;
            }
        }

        return b.toString();
    }

    protected final String pickNumberInteger(final int start) {
        return pickNumberInteger(start, Integer.MAX_VALUE);
    }

    protected final String pickNumberInteger(final int start, final int maxLength) {
        int i = start;
        final StringBuilder b = new StringBuilder();

        while (true) {
            final Character ch = this.la(i);

            if (i > maxLength || LexerConstants.EOF == ch || !Character.isDigit(ch)) {
                break;
            } else {
                b.append(ch);

                i++;
            }
        }

        return b.toString();
    }

    protected final String pickTo(final int start, final Character... cs) {
        int i = start;
        final List<Character> items = Arrays.asList(cs);
        final StringBuilder b = new StringBuilder();

        while (true) {
            final Character ch = this.la(i);

            if (LexerConstants.EOF == ch || items.contains(ch)) {
                break;
            } else {
                b.append(ch);

                i++;
            }
        }

        return b.toString();
    }

    protected final String pickWord(final int start) {
        int i = start;
        final StringBuilder b = new StringBuilder();

        while (true) {
            final Character ch = this.la(i);

            if (LexerConstants.EOF == ch || Character.isWhitespace(ch)) {
                break;
            } else {
                b.append(ch);

                i++;
            }
        }

        return b.toString();
    }

    protected final String pickToLineEnd(final int start) {
        int i = start;
        final StringBuilder b = new StringBuilder();

        while (true) {
            final Character ch = this.la(i);

            if (LexerConstants.EOF == ch || CharUtils.CR == ch || CharUtils.LF == ch) {
                break;
            } else {
                b.append(ch);

                i++;
            }
        }

        return b.toString();
    }

    protected final String pickLoops(final int start, final char c) {
        int i = start;
        final StringBuilder b = new StringBuilder();

        while (true) {
            final Character ch = this.la(i);

            if (c == ch) {
                b.append(ch);

                i++;
            } else {
                break;
            }
        }

        return b.toString();
    }

    protected final Token readLoops(final char c) {
        this.startRead();
        this.consume();

        while (true) {
            final Character ch = this.la(0);

            if (c == ch) {
                this.consume();
            } else {
                break;
            }
        }

        return this.createToken(TokenKind.looped);
    }

    protected final Token readWord() {
        this.startRead();
        this.consume();

        while (true) {
            final Character ch = this.la(0);

            if (Character.isWhitespace(ch)) break;

            this.consume();
        }

        return this.createToken(TokenKind.word);
    }
}
