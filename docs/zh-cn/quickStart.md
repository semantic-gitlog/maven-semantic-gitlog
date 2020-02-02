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
            <useCrazyGrowing>true</useCrazyGrowing>

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
{{#sections}}

{{#version}}## {{version}} ({{#releaseDate}}{{releaseDate.shortDate}}{{/releaseDate}}{{^releaseDate}}{{now.shortDate}}{{/releaseDate}}){{/version}}{{^version}}## {{nextVersion}} (Unreleased, {{#releaseDate}}{{releaseDate.shortDate}}{{/releaseDate}}{{^releaseDate}}{{now.shortDate}}{{/releaseDate}}){{/version}}
{{#description}}

{{description}}
{{/description}}
{{#groups}}

### {{title}}

{{#commits}}
* {{#commitScope}}**{{commitScope}}**: {{/commitScope}}{{commitDescription}}{{#commitIssue}} ([#{{commitIssue.id}}]({{commitIssue.url}})){{/commitIssue}}{{#shortHash}} ([{{shortHash}}]({{commitUrl}})){{/shortHash}}{{#hasCloseIssues}}, closes{{#closeIssues}} [#{{id}}]({{url}}){{/closeIssues}}{{/hasCloseIssues}}
{{/commits}}

{{/groups}}
{{^groups}}

暂无更新说明。

{{/groups}}
{{/sections}}
{{^sections}}

暂无内容。
{{/sections}}
```

## 3. 推送约定式提交（Conventional Commits）

```cc
fix: correct minor typos in code

see the issue for details on the typos fixed

closes issue #12
```

> [!TIP]
> 了解更多： [Conventional Commits](https://conventionalcommits.org).

## 4. 运行插件

```bash
$ mvn semantic-gitlog:changelog
```

然后查看项目根目录下的 `CHANGELOG.md` 文件。
