# Changelog
{{#tags}}

{{#version}}## {{version}} ({{#releaseDate}}{{releaseDate.shortDate}}{{/releaseDate}}{{^releaseDate}}{{now.shortDate}}{{/releaseDate}}){{/version}}{{^version}}## {{nextVersion}} (Unreleased, {{#releaseDate}}{{releaseDate.shortDate}}{{/releaseDate}}{{^releaseDate}}{{now.shortDate}}{{/releaseDate}}){{/version}}
{{#description}}

{{description}}
{{/description}}
{{#sections}}

### {{title}}

{{#commits}}
* {{#commitScope}}**{{commitPackage}}{{commitScope}}**: {{/commitScope}}{{commitSubject}}{{#commitIssue}} ([#{{commitIssue.id}}]({{commitIssue.url}})){{/commitIssue}}{{#hash8}} ([{{hash8}}]({{commitUrl}})){{/hash8}}{{#hasCloseIssues}}, closes{{#closeIssues}} [#{{id}}]({{url}}){{/closeIssues}}{{/hasCloseIssues}}
{{/commits}}

{{/sections}}
{{^sections}}

No update notes.

{{/sections}}
{{/tags}}
{{^tags}}

No contents.
{{/tags}}
