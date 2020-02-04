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
