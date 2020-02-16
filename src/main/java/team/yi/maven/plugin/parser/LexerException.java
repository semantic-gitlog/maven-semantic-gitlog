package team.yi.maven.plugin.parser;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString
public class LexerException extends RuntimeException {
    private static final long serialVersionUID = -7877472928281534247L;

    @EqualsAndHashCode.Include
    @ToString.Include
    private int line;

    @EqualsAndHashCode.Include
    @ToString.Include
    private int column;

    public LexerException(final int line, final int column) {
        super();

        this.line = line;
        this.column = column;
    }

    public LexerException(final int line, final int column, final String message) {
        super(message);

        this.line = line;
        this.column = column;
    }

    public LexerException(final int line, final int column, final String message, final Throwable cause) {
        super(message, cause);

        this.line = line;
        this.column = column;
    }

    public LexerException(final int line, final int column, final Throwable cause) {
        super(cause);

        this.line = line;
        this.column = column;
    }

    public LexerException(
        final int line,
        final int column,
        final String message,
        final Throwable cause,
        final boolean enableSuppression,
        final boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);

        this.line = line;
        this.column = column;
    }
}
