package team.yi.maven.plugin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileSet implements Serializable {
    private static final long serialVersionUID = -3010237214126366130L;

    private File template;
    private File target;
}
