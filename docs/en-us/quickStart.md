# Quick Start

## 1. Installation

```xml
<plugin>
    <groupId>team.yi.maven.plugin</groupId>
    <artifactId>maven-semantic-gitlog</artifactId>
    <version>0.1-SNAPSHOT</version>
    <configuration>
        <templateFile>${project.basedir}/config/gitlog/CHANGELOG.tpl.md</templateFile>

        <releaseLogSettings>
            <lastVersion>0.1.0</lastVersion>
            <strategy>strict</strategy>

            <commitUrlTemplate>https://github.com/${YOUR_ACCOUNT}/${YOUR_PROJECT_NAME}/commit/:commitId</commitUrlTemplate>
            <issueUrlTemplate>https://github.com/${YOUR_ACCOUNT}/${YOUR_PROJECT_NAME}/issues/:issueId</issueUrlTemplate>

            <derivedVersionMark>NEXT_VERSION:==</derivedVersionMark>
        </releaseLogSettings>
    </configuration>
</plugin>
```

## 2. Place template

contents of `CHANGELOG.tpl.md`:

```markdown
# Changelog
{{#tags}}

{{#version}}## {{version}} ({{#releaseDate}}{{releaseDate.shortDate}}{{/releaseDate}}{{^releaseDate}}{{now.shortDate}}{{/releaseDate}}){{/version}}{{^version}}## {{nextVersion}} (Unreleased, {{#releaseDate}}{{releaseDate.shortDate}}{{/releaseDate}}{{^releaseDate}}{{now.shortDate}}{{/releaseDate}}){{/version}}
{{#description}}

{{description}}
{{/description}}
{{#sections}}

### {{title}}

{{#commits}}
- {{#commitScope}}**{{commitPackage}}{{commitScope}}**: {{/commitScope}}{{commitSubject}}{{#subjectIssues}} ([#{{id}}]({{url}})){{/subjectIssues}} ([{{hash8}}]({{commitUrl}})){{#hasCloseIssues}}, closes{{#closeIssues}} [#{{id}}]({{url}}){{/closeIssues}}{{/hasCloseIssues}}
{{/commits}}

{{/sections}}
{{^sections}}

No update notes.

{{/sections}}
{{/tags}}
{{^tags}}

No contents.
{{/tags}}
```

## 3. Push Conventional Commits

Typical example:

````cc
fix!(material/icon/testing): adds size specs to fake icon (#18160) (#18306)

Currently we clear all overlay containers when we create a new one as a way to avoid duplicate content coming in from the server. 
Our current approach is a little too aggressive, because it can pick up containers from different apps #16888. 
These changes add an extra attribute to the container so that we can determine which platform it's coming from.

Fixes #16851.

(cherry picked from commit 29eec77)
-------------------------
BREAKING CHANGE: MAT_CHECKBOX_CLICK_ACTION is deprecated, use MAT_CHECKBOX_DEFAULT_OPTIONS
DEPRECATED: MAT_CHECKBOX_CLICK_ACTION is deprecated, use MAT_CHECKBOX_DEFAULT_OPTIONS

BREAKING CHANGE:

We no longer directly have a direct depedency on `tslib`. Instead it is now listed a `peerDependency`.

Users not using the CLI will need to manually install `tslib` via;
```
yarn add tslib
```
or
```
npm install tslib --save
```

Reference: TOOL-836
````

> [!TIP]
> Find more about [Conventional Commits](https://conventionalcommits.org).

## 4. Execute goal

```bash
$ mvn semantic-gitlog:changelog
```

and then check your `CHANGELOG.md` at root folder of the project.
