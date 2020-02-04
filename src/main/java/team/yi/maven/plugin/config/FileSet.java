package team.yi.maven.plugin.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileSet implements Serializable {
    private static final long serialVersionUID = -3010237214126366130L;

    @Parameter(property = "template", required = true)
    private File template;

    @Parameter(property = "target", required = true)
    private File target;
}
