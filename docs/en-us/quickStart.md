# Quick Start

## 1. Installation

```xml
<plugin>
    <groupId>team.yi.maven.plugin</groupId>
    <artifactId>maven-semantic-gitlog</artifactId>
    <version>0.17.0</version>
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

        <updateProjectVersion>false</updateProjectVersion>
        <isPreRelease>true</isPreRelease>
        <strategy>${gitlog.releaseStrategy}</strategy>
        <forceNextVersion>${gitlog.forceNextVersion}</forceNextVersion>

        <jsonFile>${project.basedir}/CHANGELOG.json</jsonFile>

        <issueUrlTemplate>${project.scm.url}/issues/:issueId</issueUrlTemplate>
        <commitUrlTemplate>${project.scm.url}/commit/:commitId</commitUrlTemplate>
        <mentionUrlTemplate>https://github.com/:username</mentionUrlTemplate>

        <derivedVersionMark>NEXT_VERSION:==</derivedVersionMark>

        <preRelease>${gitlog.preRelease}</preRelease>

        <commitLocales>
            <en>${project.basedir}/config/gitlog/commit-locales.md</en>
            <zh-cn>${project.basedir}/config/gitlog/commit-locales.zh-cn.md</zh-cn>
        </commitLocales>
    </configuration>
</plugin>
```

## 2. Place template

contents of `CHANGELOG.md.mustache`:

```markdown
# Changelog
{{#tags}}

{{#version}}## {{version}} ({{#releaseDate}}{{#formatDate}}{{releaseDate}}|yyyy-MM-dd{{/formatDate}}{{/releaseDate}}{{^releaseDate}}{{#formatDate}}{{now}}|yyyy-MM-dd{{/formatDate}}{{/releaseDate}}){{/version}}{{^version}}## {{nextVersion}} (Unreleased, {{#releaseDate}}{{#formatDate}}{{releaseDate}}|yyyy-MM-dd{{/formatDate}}{{/releaseDate}}{{^releaseDate}}{{#formatDate}}{{now}}|yyyy-MM-dd{{/formatDate}}{{/releaseDate}}){{/version}}
{{#sections}}

### {{title}}

{{#commits}}
- {{#commitScope}}**{{commitPackage}}{{commitScope}}**: {{/commitScope}}{{& commitSubject}}{{#subjectIssues}} ([#{{id}}]({{url}})){{/subjectIssues}} ([{{hash8}}]({{commitUrl}})){{#hasCloseIssues}}, closes{{#closeIssues}} [#{{id}}]({{url}}){{/closeIssues}}{{/hasCloseIssues}}
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
