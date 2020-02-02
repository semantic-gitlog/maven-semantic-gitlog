package team.yi.maven.plugin.model;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class ReleaseCommitGroups {
    public static final Map<String, String> TITLES = new ConcurrentHashMap<>();
    public static final List<String> DEFAULT_ORDER_LIST = new ArrayList<>();

    private static final String BUG_FIXES = "Bug Fixes";
    private static final String CODE_REFACTORING = "Code Refactoring";
    private static final String FEATURES = "Features";
    private static final String PERFORMANCE_IMPROVEMENTS = "Performance Improvements";
    private static final String DOCUMENTATION = "Documentation";
    private static final String STYLES = "Styles";
    private static final String REVERTS = "Reverts";
    private static final String BREAKING_CHANGE = "BREAKING CHANGES";
    private static final String DEPRECATIONS = "DEPRECATIONS";
    private static final String TESTS = "Tests";
    private static final String BUILD_SYSTEM = "Build System";
    private static final String CONTINUOUS_INTEGRATION = "Continuous Integration";
    private static final String OTHERS = "Others";

    static {
        TITLES.put("BUG_FIXES", BUG_FIXES);
        TITLES.put("CODE_REFACTORING", CODE_REFACTORING);
        TITLES.put("FEATURES", FEATURES);
        TITLES.put("PERFORMANCE_IMPROVEMENTS", PERFORMANCE_IMPROVEMENTS);
        TITLES.put("DOCUMENTATION", DOCUMENTATION);
        TITLES.put("STYLES", STYLES);
        TITLES.put("REVERTS", REVERTS);
        TITLES.put("BREAKING_CHANGE", BREAKING_CHANGE);
        TITLES.put("DEPRECATIONS", DEPRECATIONS);
        TITLES.put("TESTS", TESTS);
        TITLES.put("BUILD_SYSTEM", BUILD_SYSTEM);
        TITLES.put("CONTINUOUS_INTEGRATION", CONTINUOUS_INTEGRATION);
        TITLES.put("OTHERS", OTHERS);

        DEFAULT_ORDER_LIST.add(BUG_FIXES);
        DEFAULT_ORDER_LIST.add(CODE_REFACTORING);
        DEFAULT_ORDER_LIST.add(FEATURES);
        DEFAULT_ORDER_LIST.add(PERFORMANCE_IMPROVEMENTS);
        DEFAULT_ORDER_LIST.add(DOCUMENTATION);
        DEFAULT_ORDER_LIST.add(STYLES);
        DEFAULT_ORDER_LIST.add(REVERTS);
        DEFAULT_ORDER_LIST.add(BREAKING_CHANGE);
        DEFAULT_ORDER_LIST.add(DEPRECATIONS);
        DEFAULT_ORDER_LIST.add(TESTS);
        DEFAULT_ORDER_LIST.add(BUILD_SYSTEM);
        DEFAULT_ORDER_LIST.add(CONTINUOUS_INTEGRATION);
        DEFAULT_ORDER_LIST.add(OTHERS);
    }

    public static String fromCommitType(String commitType, boolean breakingChange) {
        if (breakingChange) return BREAKING_CHANGE;

        return fromCommitType(commitType);
    }

    public static String fromCommitType(String commitType) {
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
        */

        if (commitType == null || commitType.isEmpty()) return OTHERS;

        switch (commitType) {
            case "feat":
                return FEATURES;
            case "fix":
                return BUG_FIXES;
            case "perf":
                return PERFORMANCE_IMPROVEMENTS;
            case "revert":
                return REVERTS;
            case "docs":
                return DOCUMENTATION;
            case "style":
                return STYLES;
            case "refactor":
                return CODE_REFACTORING;
            case "test":
                return TESTS;
            case "build":
                return BUILD_SYSTEM;
            case "ci":
                return CONTINUOUS_INTEGRATION;
            default:
                return OTHERS;
        }
    }
}
