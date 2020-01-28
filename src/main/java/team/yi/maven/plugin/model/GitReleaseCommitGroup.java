package team.yi.maven.plugin.model;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GitReleaseCommitGroup {
    /*
        feat        Features
        fix         Bug Fixes
        perf        Performance Improvements
        revert      Reverts

        docs        Documentation
        style       Styles
        refactor    Code Refactoring
        test        Tests

        build       Build System
        ci          Continuous Integration

                    BREAKING CHANGES
                    DEPRECATIONS
    */
    public static final String BUG_FIXES = "Bug Fixes";
    public static final String CODE_REFACTORING = "Code Refactoring";
    public static final String FEATURES = "Features";
    public static final String PERFORMANCE_IMPROVEMENTS = "Performance Improvements";
    public static final String DOCUMENTATION = "Documentation";
    public static final String STYLES = "Styles";
    public static final String REVERTS = "Reverts";
    public static final String BREAKING_CHANGE = "BREAKING CHANGES";
    public static final String DEPRECATIONS = "DEPRECATIONS";
    public static final String TESTS = "Tests";
    public static final String BUILD_SYSTEM = "Build System";
    public static final String CONTINUOUS_INTEGRATION = "Continuous Integration";
    public static final String OTHERS = "Others";

    private String title;
    private List<GitReleaseCommit> commits;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<GitReleaseCommit> getCommits() {
        if (commits == null || commits.isEmpty()) return null;

        return commits.stream()
            .distinct()
            .sorted(Comparator.comparing(GitReleaseCommit::getCommitScope, Comparator.nullsLast(String::compareTo)))
            .collect(Collectors.toList());
    }

    public void setCommits(List<GitReleaseCommit> commits) {
        this.commits = commits;
    }
}
