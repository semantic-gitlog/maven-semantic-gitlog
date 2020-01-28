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
