# Plugin Options

| option | description |
| ------ | ----------- |
| `disabled` | Enable or disable `semantic-gitlog` module. Default is `false`. |
| `isPreRelease` | Enable `development-phase`, breaking changes only increases the minor version number. Default is `false`. |
| `strategy` | Release strategy. Optional values: `strict`, `slow`. Default is `strict`. |
| `forceNextVersion` | Allow force increase `nextVersion` when the version dose not grow. Default is `true`. |
| `longDateFormat` | The long date format pattern. Default is `yyyy-MM-dd HH:mm:ss`. |
| `shortDateFormat` | The short date format pattern. Default is `yyyy-MM-dd`. |
| `derivedVersionMark` | The value will output as a prefix with the version number when `semantic-gitlog:derive` execute. Default is `null`. |
| `lastVersion` | `Tag` as version by default. This option allows you to manually specify the value of `lastVersion`. Default is `0.1.0`. |
| `preRelease` | Set the initial value of `preRelease`. Default is `null`([Understand preRelease](https://github.com/skuzzle/semantic-version#usage)).  |
| `buildMetaData` | Set the initial value of `buildMetaData`. Default is `null`([Understand buildMetaData](https://github.com/skuzzle/semantic-version#usage)). |
| `majorTypes` | Increase major version when these commit types are matched. By default only when **BREAKING CHANGE** is discovered. |
| `minorTypes` | Increase minor version when these commit types are matched. Default is `feat`. |
| `patchTypes` | Increase patch version when these commit types are matched. Default is `refactor,perf,fix,chore,revert,docs,build`. |
| `preReleaseTypes` | Increase preRelease version when these commit types are matched. Default is `null`. |
| `buildMetaDataTypes` | Increase buildMetaData version when these commit types are matched. Default is `null`. |
| `hiddenTypes` | These commit types are hidden in the changelog. Default is `release`. |
| `issueUrlTemplate` | An url string contains placeholder `:issueId` to construct issue link. |
| `commitUrlTemplate` | An url string contains placeholder `:commitId` to construct commit link. |
| `mentionUrlTemplate` | An url string contains placeholder `:username` to construct mention link. |
| `commitLocales` | A file set with localized commit messages. Default is `null`. |
| `closeIssueActions` | A string list that detecting close quick actions. Default is `close,closes,closed,fix,fixes,fixed,resolve,resolves,resolved`. |
| `updateProjectVersion` | Enable or disable automatic update the `${project.version}` value in `pom.xml`. Default is `false`. |

> [!TIP]
> When `majorTypes`, `minorTypes`, `patchTypes`, `preReleaseTypes` and `buildMetaDataTypes` set a value that dose not match any commit, the corresponding version number dose not change.
> It's useful to use with maven profiles.
