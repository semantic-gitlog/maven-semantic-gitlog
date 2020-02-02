package team.yi.maven.plugin.config;

import lombok.Data;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.Serializable;

@Data
public class CustomIssue implements Serializable {
    private static final long serialVersionUID = 7029332131038317463L;

    @Parameter(property = "name", required = true)
    private String name;

    @Parameter(property = "pattern", required = true)
    private String pattern;

    @Parameter(property = "link")
    private String link;

    @Parameter(property = "title")
    private String title;
}
