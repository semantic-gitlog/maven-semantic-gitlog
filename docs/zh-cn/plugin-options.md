# 插件配置选项

| option | description |
| ------ | ----------- |
| `disabled` | 打开或关闭 `semantic-gitlog:git-changelog` 功能。默认为 `false`. |
| `useCrazyGrowing` | 疯狂增长模式，当遇到任何匹配的提交时均会增加版本号。默认为 `false`. |
| `lastVersion` | 默认尝试将 `Tag` 转换为版本号。该选项允许您手工指定 `lastVersion` 的值。默认为 `0.1.0`. |
| `preRelease` | 设置 `preRelease` 的初始值。默认为 `null`([Understand preRelease](https://github.com/skuzzle/semantic-version#usage)).  |
| `buildMetaData` | 设置 `buildMetaData` 的初始值。默认为 `null`([Understand buildMetaData](https://github.com/skuzzle/semantic-version#usage)). |
| `majorTypes` | 当匹配到这些提交类型时增加主版本号（major）。默认仅当发现了 **BREAKING CHANGE** 时才生效。 |
| `minorTypes` | 当匹配到这些提交类型时增加次版本号（minor）。默认为 `feat`. |
| `patchTypes` | 当匹配到这些提交类型时增加修订版本号（patch）。默认为 `fix,perf,revert,refactor`. |
| `preReleaseTypes` | 当匹配到这些提交类型时增加预发布版本号（preRelease）。默认为 `null`. |
| `buildMetaDataTypes` | 当匹配到这些提交类型时增加构建（元数据）版本号（buildMetaData）。默认为 `null`. |
| `commitUrlTemplate` | 用于构建提交链接的包含 `:commitId` 占位符的字符串。 |
| `issueUrlTemplate` | 用于构建议题链接的包含 `:issueId` 占位符的字符串。 |
| `derivedVersionMark` | 执行 `semantic-gitlog:derive` 时，该值作为前缀与版本号一起输出。默认为 `null`. |

> [!TIP]
> 当 `majorTypes`、`minorTypes`、`patchTypes`、`preReleaseTypes`和`buildMetaDataTypes` 配置了无法匹配的值， 那么对应的版本号将不会有任何变更。
> 与 maven profiles 结合使用时会非常有用。
