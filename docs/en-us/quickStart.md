# Quick Start

## 1. Installation

```xml
<plugin>
    <groupId>team.yi.maven.plugin</groupId>
    <artifactId>maven-semantic-gitlog</artifactId>
    <version>0.1-SNAPSHOT</version>
    <configuration>
        <fileSets>
            <fileSet>
                <target>${project.basedir}/CHANGELOG.md</target>
                <template>${project.basedir}/config/gitlog/CHANGELOG.md.mustache</template>
            </fileSet>
            <fileSet>
                <target>${project.basedir}/CHANGELOG_zh-cn.md</target>
                <template>${project.basedir}/config/gitlog/CHANGELOG_zh-cn.md.mustache</template>
            </fileSet>
        </fileSets>

        <releaseLogSettings>
            <lastVersion>0.1.0</lastVersion>
            <strategy>strict</strategy>

            <commitUrlTemplate>https://github.com/${YOUR_ACCOUNT}/${YOUR_PROJECT_NAME}/commit/:commitId</commitUrlTemplate>
            <issueUrlTemplate>https://github.com/${YOUR_ACCOUNT}/${YOUR_PROJECT_NAME}/issues/:issueId</issueUrlTemplate>
            <mentionUrlTemplate>https://github.com/:username</mentionUrlTemplate>

            <derivedVersionMark>NEXT_VERSION:==</derivedVersionMark>

            <commitLocales>
                <zh-cn>${project.basedir}/config/gitlog/commit-locales.zh-cn.md</zh-cn>
            </commitLocales>
        </releaseLogSettings>
    </configuration>
</plugin>
```

## 2. Place template

contents of `CHANGELOG.md.mustache`:

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
fix!(@material/icon/testing): adds size specs to fake icon (#18160) (#18306)

Currently we clear all overlay containers when we create a new one as a way to avoid duplicate content coming in from the server. 
Our current approach is a little too aggressive, because it can pick up containers from different apps #16888. 
These changes add an extra attribute to the container so that we can determine which platform it's coming from.

Fixes #16851.

(cherry picked from commit 29eec77)

------------
# Locales
- **[zh-cn]** 添加 `commitLocales` 配置项，支持提交消息的本地化
````

> [!TIP]
> Find more about [Conventional Commits](https://conventionalcommits.org).

## 4. Execute goal

```bash
$ mvn semantic-gitlog:changelog
```

and then check your `CHANGELOG.md` at root folder of the project.
