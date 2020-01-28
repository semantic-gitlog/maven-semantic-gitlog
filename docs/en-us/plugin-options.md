# Plugin Options

| option | description |
| ------ | ----------- |
| `disabled` | Enable or disable `semantic-gitlog:git-changelog` goal. Default is `false`. |
| `useCrazyGrowing` | The version number increases every time when matches the commit. Default is `false`. |
| `lastVersion` | `Tag` as version by default. This option allows you to manually specify the value of `lastVersion`. Default is `0.1.0`. |
| `preRelease` | Set the initial value of `preRelease`. Default is `null`([Understand preRelease](https://github.com/skuzzle/semantic-version#usage)).  |
| `buildMetaData` | Set the initial value of `buildMetaData`. Default is `null`([Understand buildMetaData](https://github.com/skuzzle/semantic-version#usage)). |
| `majorTypes` | Increase major version when these commit types are matched. By default only when **BREAKING CHANGE** is discovered. |
| `minorTypes` | Increase minor version when these commit types are matched. Default is `feat`. |
| `patchTypes` | Increase patch version when these commit types are matched. Default is `fix,perf,revert,refactor`. |
| `preReleaseTypes` | Increase preRelease version when these commit types are matched. Default is `null`. |
| `buildMetaDataTypes` | Increase buildMetaData version when these commit types are matched. Default is `null`. |
| `commitUrlTemplate` | An url string contains placeholder `:commitId` to construct commit link. |
| `issueUrlTemplate` | An url string contains placeholder `:issueId` to construct issue link. |
| `derivedVersionMark` | The value will output as a prefix with the version number when `semantic-gitlog:derive` execute. Default is `null`. |

> [!TIP]
> When `majorTypes`, `minorTypes`, `patchTypes`, `preReleaseTypes` and `buildMetaDataTypes` set a value that dose not match any commit, the corresponding version number dose not change.
> It's useful to use with maven profiles.
