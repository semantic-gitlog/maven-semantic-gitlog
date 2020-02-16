package team.yi.maven.plugin.model;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serializable;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MentionRef implements Serializable {
    private static final long serialVersionUID = -2958658493632357049L;

    @EqualsAndHashCode.Include
    private final String username;
    private final String url;
}
