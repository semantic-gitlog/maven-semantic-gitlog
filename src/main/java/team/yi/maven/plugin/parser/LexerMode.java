package team.yi.maven.plugin.parser;

public enum LexerMode {
    text,
    section,
    sectionLocale,

    scope,
    subject,
    body,

    issueRef,
    actionIssueRef,
    mentionRef
}
