[![Semantic Versioning 2.0.0](https://img.shields.io/badge/Semantic%20Versioning-2.0.0-brightgreen)](https://semver.org/)
[![Conventional Commits](https://img.shields.io/badge/Conventional%20Commits-1.0.0-fe5196.svg)](https://conventionalcommits.org)
[![MIT](https://img.shields.io/badge/license-MIT-blue.svg)](https://conventionalcommits.org)

# maven-semantic-gitlog

A simple [semantic Versioning 2.0.0](https://semver.org/) management tool based on [Conventional Commits](https://conventionalcommits.org).
It automatically derive and manage version numbers and generate angular-style change logs.

This plugin extended from [git-changelog-lib](https://github.com/tomasbjerre/git-changelog-lib). All of it's features are preserved. 
As same as `git-changelog-lib`, we fully configurable with [Mustache](http://mustache.github.io/) template. That can:

* Be stored to file, like CHANGELOG.md.
* Be posted to MediaWiki ([here](https://github.com/tomasbjerre/git-changelog-lib/tree/screenshots/sandbox) is an example)
* Or just rendered to a String.

The [CHANGELOG.md](https://github.com/ymind/maven-semantic-gitlog/blob/master/CHANGELOG.md) of this project is automatically generated with this [template](https://github.com/ymind/maven-semantic-gitlog/blob/master/config/gitlog/CHANGELOG.tpl.md).

## Usage

### 1. Installation

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
        </gitReleaseLogSettings>
    </configuration>
</plugin>
```

### 2. Place template

contents of `CHANGELOG.tpl.md`:

```markdown
# Changelog

{{#sections}}
{{#version}}## {{version}}{{/version}}{{^version}}## latest{{/version}}{{#releaseDate}} ({{releaseDate.shortDate}}){{/releaseDate}}{{^releaseDate}} ({{now.shortDate}}){{/releaseDate}}
{{#description}}{{description}}{{/description}}
{{#groups}}
### {{title}}

{{#commits}}
* {{#commitScope}}**{{commitScope}}**: {{/commitScope}}{{commitDescription}}{{#firstIssueId}} ([#{{firstIssueId}}]({{firstIssueUrl}})){{/firstIssueId}}{{#shortHash}} ([{{shortHash}}]({{commitUrl}})){{/shortHash}}{{#hasCloseIssues}}, closes{{#closeIssues}} [#{{id}}]({{url}}){{/closeIssues}}{{/hasCloseIssues}}
{{/commits}}
{{^commits}}
No update notes.
{{/commits}}

{{/groups}}

{{/sections}}
```

### 3. Push Conventional Commits

```text
fix: correct minor typos in code

see the issue for details on the typos fixed

closes issue #12
```

> Find more about [Conventional Commits](https://conventionalcommits.org).

### 4. Execute goal

```bash
mvn semantic-gitlog:git-changelog
```

and then check your `CHANGELOG.md` at root folder of the project.

## Configuration options

| option | description |
| ------ | ----------- |
| `disabled` | Enable or disable `semantic-gitlog:git-changelog` goal. Default is `false`. |
| `useCrazyGrowing` | The version number increases every time when matches the commit. Default is `false`. |
| `lastVersion` | Tag as version by default. This option allows you to manually specify the value of lastVersion. Default is `0.1.0`. |
| `preRelease` | Set the initial value of `preRelease`. Default is `null`([Understand preRelease](https://github.com/skuzzle/semantic-version#usage)).  |
| `buildMetaData` | Set the initial value of `buildMetaData`. Default is `null`([Understand buildMetaData](https://github.com/skuzzle/semantic-version#usage)). |
| `majorTypes` | Increase major version when these commit types are matched. By default only when **BREAKING CHANGE** is discovered. |
| `minorTypes` | Increase minor version when these commit types are matched. Default is `feat`. |
| `patchTypes` | Increase patch version when these commit types are matched. Default is `fix,perf,revert,refactor`. |
| `preReleaseTypes` | Increase preRelease version when these commit types are matched. Default is `null`. |
| `buildMetaDataTypes` | Increase buildMetaData version when these commit types are matched. Default is `null`. |
| `commitUrlTemplate` | An url string contains placeholder `:commitId` to construct commit link. |
| `issueUrlTemplate` | An url string contains placeholder `:issueId` to construct issue link. |

> When `majorTypes`, `minorTypes`, `patchTypes`, `preReleaseTypes` and `buildMetaDataTypes` set a value that dose not match any commit, the corresponding version number dose not change.
> It's useful to use with maven profiles.

## Template models

see [here](src/main/java/team/yi/maven/plugin/model) please.

> This plugin extended from [git-changelog-lib](https://github.com/tomasbjerre/git-changelog-lib). All of it's features are preserved.

## Related community projects

* [semantic-version](https://github.com/skuzzle/semantic-version)
* [git-changelog-lib](https://github.com/tomasbjerre/git-changelog-lib)

## License

This is open-sourced software licensed under the [MIT license](https://opensource.org/licenses/MIT).
