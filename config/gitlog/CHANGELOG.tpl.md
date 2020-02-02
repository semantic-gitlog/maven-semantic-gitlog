# Changelog
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

No update notes.

{{/groups}}
{{/sections}}
{{^sections}}

No contents.
{{/sections}}
