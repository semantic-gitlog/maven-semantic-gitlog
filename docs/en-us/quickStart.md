# Quick Start

## 1. Installation

```xml
<plugin>
    <groupId>team.yi.maven.plugin</groupId>
    <artifactId>maven-semantic-gitlog</artifactId>
    <version>0.1-SNAPSHOT</version>
    <configuration>
        <templateFile>${project.basedir}/config/gitlog/CHANGELOG.tpl.md</templateFile>

        <gitReleaseLogSettings>
            <lastVersion>0.1.0</lastVersion>
            <useCrazyGrowing>true</useCrazyGrowing>

            <commitUrlTemplate>https://github.com/${YOUR_ACCOUNT}/${YOUR_PROJECT_NAME}/commit/:commitId</commitUrlTemplate>
            <issueUrlTemplate>https://github.com/${YOUR_ACCOUNT}/${YOUR_PROJECT_NAME}/issues/:issueId</issueUrlTemplate>

            <derivedVersionMark>NEW_VERSION:==</derivedVersionMark>
        </gitReleaseLogSettings>
    </configuration>
</plugin>
```

## 2. Place template

contents of `CHANGELOG.tpl.md`:

```markdown
# Changelog

{{#sections}}
{{#version}}## {{version}}{{/version}}{{^version}}## latest{{/version}}{{#releaseDate}} ({{releaseDate.shortDate}}){{/releaseDate}}{{^releaseDate}} ({{now.shortDate}}){{/releaseDate}}
{{#description}}

{{description}}
{{/description}}

{{#groups}}
### {{title}}

{{#commits}}
* {{#commitScope}}**{{commitScope}}**: {{/commitScope}}{{commitDescription}}{{#firstIssueId}} ([#{{firstIssueId}}]({{firstIssueUrl}})){{/firstIssueId}}{{#shortHash}} ([{{shortHash}}]({{commitUrl}})){{/shortHash}}{{#hasCloseIssues}}, closes{{#closeIssues}} [#{{id}}]({{url}}){{/closeIssues}}{{/hasCloseIssues}}
{{/commits}}

{{/groups}}
{{^groups}}

No update notes.
{{/groups}}
{{/sections}}
{{^sections}}
No contents.
{{/sections}}
```

## 3. Push Conventional Commits

```cc
fix: correct minor typos in code

see the issue for details on the typos fixed

closes issue #12
```

> [!TIP]
> Find more about [Conventional Commits](https://conventionalcommits.org).

## 4. Execute goal

```bash
$ mvn semantic-gitlog:git-changelog
```

and then check your `CHANGELOG.md` at root folder of the project.
