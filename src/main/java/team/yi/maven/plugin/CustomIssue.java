package team.yi.maven.plugin;

import org.apache.maven.plugins.annotations.Parameter;

public class CustomIssue {
    @Parameter(property = "name", required = true)
    private String name;

    @Parameter(property = "pattern", required = true)
    private String pattern;

    @Parameter(property = "link")
    private String link;

    @Parameter(property = "title")
    private String title;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
