package team.yi.maven.plugin.parser;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Token implements Serializable {
    private static final long serialVersionUID = 1247962245504693908L;

    @EqualsAndHashCode.Include
    @ToString.Include
    private TokenKind kind;

    @EqualsAndHashCode.Include
    @ToString.Include
    private int line;

    @EqualsAndHashCode.Include
    @ToString.Include
    private int column;

    @EqualsAndHashCode.Include
    @ToString.Include
    private String value;

    @EqualsAndHashCode.Include
    private int length;

    public Token(final TokenKind kind, final String value, final int line, final int column) {
        this.kind = kind;
        this.value = value;
        this.line = line;
        this.column = column;
        this.length = StringUtils.length(value);
    }
}
