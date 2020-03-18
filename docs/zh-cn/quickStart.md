# 入门

## 1. 安装

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
                <target>${project.basedir}/CHANGELOG.zh-cn.md</target>
                <template>${project.basedir}/config/gitlog/CHANGELOG.zh-cn.md.mustache</template>
            </fileSet>
        </fileSets>

        <updateProjectVersion>false</updateProjectVersion>
        <isUnstable>true</isUnstable>
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

## 2. 设置模板

模板 `CHANGELOG.md.mustache` 的内容如下:

```markdown
# 更新日志
{{#tags}}

{{#version}}## {{version}} ({{#releaseDate}}{{#formatDate}}{{releaseDate}}|yyyy-MM-dd{{/formatDate}}{{/releaseDate}}{{^releaseDate}}{{#formatDate}}{{now}}|yyyy-MM-dd{{/formatDate}}{{/releaseDate}}){{/version}}{{^version}}## {{nextVersion}} (Unreleased, {{#releaseDate}}{{#formatDate}}{{releaseDate}}|yyyy-MM-dd{{/formatDate}}{{/releaseDate}}{{^releaseDate}}{{#formatDate}}{{now}}|yyyy-MM-dd{{/formatDate}}{{/releaseDate}}){{/version}}
{{#sections}}

### {{title}}

{{#commits}}
- {{#commitScope}}**{{commitPackage}}{{commitScope}}**: {{/commitScope}}{{#localeMap}}{{& zh-cn.subject}}{{/localeMap}}{{^localeMap}}{{& commitSubject}}{{/localeMap}}{{#subjectIssues}} ([#{{id}}]({{url}})){{/subjectIssues}} ([{{hash8}}]({{commitUrl}})){{#hasCloseIssues}}, closes{{#closeIssues}} [#{{id}}]({{url}}){{/closeIssues}}{{/hasCloseIssues}}
{{/commits}}

{{/sections}}
{{^sections}}

暂无更新说明。

{{/sections}}
{{/tags}}
{{^tags}}

暂无内容。
{{/tags}}
```

## 3. 推送约定式提交（Conventional Commits）

典型示例：

````cc
fix!(@material/icon/testing): adds size specs to `fake icon` (#18160) (#18306)

Currently we clear all overlay containers when we create a new one as a way to avoid duplicate content coming in from the server.
Our current approach is a little too aggressive, because it can pick up containers from different apps #16888.
These changes add an extra attribute to the container so that we can determine which platform it's coming from.

Fixes #16851.

(cherry picked from commit 29eec77)
------------
# Locales
- **[zh-cn]** 添加 `commitLocales` 配置项，支持提交消息的本地化
- **[zh-HK]** xxx
- **[de-DE]** xxx
````

> [!TIP]
> 了解更多： [Conventional Commits](https://conventionalcommits.org).

## 4. 运行插件

```bash
$ mvn semantic-gitlog:changelog
```

然后查看项目根目录下的 `CHANGELOG.md` 文件。
