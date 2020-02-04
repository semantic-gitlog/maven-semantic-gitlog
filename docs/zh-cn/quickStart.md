# 入门

## 1. 安装

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

## 2. 设置模板

模板 `CHANGELOG.tpl.md` 的内容如下:

```markdown
# 更新日志
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
> 了解更多： [Conventional Commits](https://conventionalcommits.org).

## 4. 运行插件

```bash
$ mvn semantic-gitlog:changelog
```

然后查看项目根目录下的 `CHANGELOG.md` 文件。
