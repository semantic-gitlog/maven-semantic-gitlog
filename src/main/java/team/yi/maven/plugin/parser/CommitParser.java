package team.yi.maven.plugin.parser;

import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import se.bjurr.gitchangelog.api.model.Commit;
import team.yi.maven.plugin.config.ReleaseLogSettings;
import team.yi.maven.plugin.model.IssueRef;
import team.yi.maven.plugin.model.MentionRef;
import team.yi.maven.plugin.model.ReleaseCommit;
import team.yi.maven.plugin.model.ReleaseCommitLocale;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("PMD.TooManyMethods")
@Log
public class CommitParser {
    private final ReleaseLogSettings settings;
    private final CommitLexer lexer;
    private final Commit commit;
    private final List<String> closeIssueActions;
    private ReleaseCommit releaseCommit;
    private Token current;

    public CommitParser(final ReleaseLogSettings settings, final Commit commit) throws IOException {
        this.settings = settings;
        this.lexer = new CommitLexer(commit.getMessage(), this.settings);
        this.commit = commit;
        this.closeIssueActions = settings.getCloseIssueActions();
    }

    public void parseTokens() {
        Token token = this.lexer.next();

        while (token.getKind() != TokenKind.eof) {
            String value = StringUtils.replace(token.getValue(), "\r", "\\r");
            value = StringUtils.replace(value, "\n", "\\n");

            log.info(MessageFormat.format("{0}: {1}", StringUtils.leftPad(token.getKind().name(), 20), value));

            token = this.lexer.next();
        }

        log.info(MessageFormat.format("{0}: {1}", StringUtils.leftPad(token.getKind().name(), 20), token.getValue()));
    }

    public void reset() {
        this.releaseCommit = new ReleaseCommit(this.commit);
        this.releaseCommit.setCommitUrl(this.settings.createCommitUrl(this.releaseCommit.getHashFull()));
    }

    public ReleaseCommit parse() {
        this.reset();
        this.consume();

        while (this.current.getKind() != TokenKind.eof) {
            switch (this.current.getKind()) {
                case type:
                    this.releaseCommit.setCommitType(this.current.getValue());
                    break;
                case attention:
                    this.releaseCommit.setAttention("!".equals(this.current.getValue()));
                    break;
                case scope:
                    this.readScope();
                    break;
                case subject:
                    this.readSubject();
                    break;
                case body:
                    this.readBody();
                    break;
                case sectionBoundary:
                    this.readSection();
                    break;
                default:
                    break;
            }

            this.consume();
        }

        return this.releaseCommit;
    }

    private IssueRef readIssue(final boolean forSubject) {
        this.consume(TokenKind.issueStart);
        this.consume(TokenKind.text);

        final Integer issueId = Integer.valueOf(this.current.getValue());
        final String url = this.settings.createIssueUrl(issueId);
        final IssueRef issueRef = new IssueRef(issueId, url);

        if (forSubject) {
            this.releaseCommit.getSubjectIssues().add(issueRef);
        } else {
            this.releaseCommit.getBodyIssues().add(issueRef);
        }

        return issueRef;
    }

    private IssueRef readIssueAction() {
        final String action = StringUtils.stripStart(this.current.getValue(), "/");

        this.consume(TokenKind.issueAction);

        final String repo = StringUtils.stripToNull(this.current.getValue());

        this.consume(TokenKind.issueRepo);
        this.consume(TokenKind.issueStart);
        this.consume(TokenKind.text);

        final Integer issueId = Integer.valueOf(this.current.getValue());
        final String url = this.settings.createIssueUrl(issueId);
        final IssueRef issueRef = new IssueRef(repo, issueId, url, action);

        this.releaseCommit.add(action, issueRef);

        if (this.closeIssueActions.contains(action.toLowerCase(Locale.getDefault()))) {
            releaseCommit.getCloseIssues().add(issueRef);
        }

        return issueRef;
    }

    private MentionRef readMentionRef() {
        this.consume(TokenKind.mentionStart);

        final String username = this.current.getValue();
        final String url = this.settings.createMentionUrl(username);
        final MentionRef mentionRef = new MentionRef(username, url);

        this.releaseCommit.getMentions().add(mentionRef);

        this.consume(TokenKind.mention);

        return mentionRef;
    }

    private void readLocales() {
        this.consume();

        if (this.current.getKind() != TokenKind.localeItemStart) return;

        String lang = null;
        String subject = null;

        while (this.current.getKind() != TokenKind.sectionBoundary && this.current.getKind() != TokenKind.eof) {
            this.consume();

            switch (this.current.getKind()) {
                case localeLang:
                    lang = this.current.getValue();
                    break;
                case localeSubject:
                    subject = this.current.getValue();
                    break;
                case localeItemEnd:
                case eof:
                    if (StringUtils.isEmpty(lang)) break;

                    final ReleaseCommitLocale locale = new ReleaseCommitLocale(lang, subject);

                    this.releaseCommit.getLocales().add(locale);

                    lang = StringUtils.EMPTY;
                    subject = StringUtils.EMPTY;
                    break;
                default:
                    break;
            }
        }
    }

    private void readSection() {
        this.consume();

        if (this.current.getKind() == TokenKind.localeListHeader) {
            this.readLocales();
        }
    }

    private void readBody() {
        final StringBuilder builder = new StringBuilder("\n\n");

        while (this.current.getKind() != TokenKind.bodyEnd && this.current.getKind() != TokenKind.eof) {
            switch (this.current.getKind()) {
                case issueEnd:
                case body:
                    builder.append(this.current.getValue());
                    break;
                case mentionStart:
                    final MentionRef mentionRef = this.readMentionRef();

                    builder.append(LexerConstants.AT).append(mentionRef.getUsername());
                    break;
                case issueAction: {
                    final IssueRef issueRef = this.readIssueAction();

                    builder.append(issueRef.getAction()).append(LexerConstants.SPACE);

                    if (StringUtils.isNotEmpty(issueRef.getRepo())) {
                        builder.append(issueRef.getRepo()).append(LexerConstants.SLASH);
                    }

                    builder.append(LexerConstants.SHARP).append(issueRef.getId());
                    break;
                }
                case issueStart: {
                    final String issueStart = StringUtils.prependIfMissing(this.current.getValue(), StringUtils.SPACE);
                    final IssueRef issueRef = this.readIssue(false);

                    builder.append(issueStart)
                        .append(LexerConstants.SHARP)
                        .append(issueRef.getId());
                    break;
                }
                // case mentionEnd:
                // case bodyEnd:
                default:
                    break;
            }

            this.consume();
        }

        this.releaseCommit.setCommitBody(builder.toString().trim());

        final String[] lines = StringUtils.split(this.releaseCommit.getCommitBody(), "\r\n");

        for (final String line : lines) {
            if (!this.releaseCommit.isBreakingChange() && StringUtils.startsWith(line, ReleaseLogSettings.BREAKING_CHANGE_PATTERN)) {
                this.releaseCommit.setBreakingChange(true);
            }

            if (!this.releaseCommit.isDeprecated() && StringUtils.startsWith(line, ReleaseLogSettings.DEPRECATED_PATTERN)) {
                this.releaseCommit.setDeprecated(true);
            }
        }
    }

    private void readSubject() {
        final StringBuilder builder = new StringBuilder();

        while (this.current.getKind() != TokenKind.subjectEnd && this.current.getKind() != TokenKind.eof) {
            switch (this.current.getKind()) {
                case issueEnd:
                case subject:
                    builder.append(this.current.getValue());
                    break;
                case mentionStart:
                    final MentionRef mentionRef = this.readMentionRef();

                    builder.append(LexerConstants.AT).append(mentionRef.getUsername());
                    break;
                case issueStart:
                    final String issueStart = StringUtils.prependIfMissing(this.current.getValue(), StringUtils.SPACE);
                    final IssueRef issueRef = this.readIssue(true);

                    builder.append(issueStart).append(LexerConstants.SHARP).append(issueRef.getId());
                    break;
                // case mentionEnd:
                case subjectEnd:
                default:
                    break;
            }

            this.consume();
        }

        this.releaseCommit.setCommitSubject(builder.toString());
    }

    private void readScope() {
        final Token token = this.consume(TokenKind.scope);
        final int pos = token.getValue().lastIndexOf(LexerConstants.SLASH);

        if (pos > -1) {
            final String packageName = token.getValue().substring(0, pos + 1);
            final String scope = token.getValue().substring(pos + 1);

            this.releaseCommit.setCommitPackage(packageName);
            this.releaseCommit.setCommitScope(scope);
        } else {
            this.releaseCommit.setCommitScope(token.getValue());
        }
    }

    private Token consume() {
        final Token old = this.current;

        this.current = this.lexer.next();

        return old;
    }

    private Token consume(final TokenKind kind) {
        final Token old = this.current;

        this.current = this.lexer.next();

        if (old.getKind() != kind) {
            final String message = MessageFormat.format("Invalid token：{0}, kind：{1}.", this.current.getKind(), kind);

            throw new ParseException(this.current.getLine(), this.current.getColumn(), message);
        }

        return old;
    }
}
