package team.yi.maven.plugin.model;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serializable;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReleaseCommitLocale implements Serializable {
    private static final long serialVersionUID = 1614994575461203609L;

    @EqualsAndHashCode.Include
    private String lang;
    private String subject;
}
