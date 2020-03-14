# 更新日志

## 0.19.4 (2020-03-14)

### Bug Fixes

- **mojo**: 修复 `ChangelogMojo.updatePom` ([21093e4a](https://github.com/ymind/maven-semantic-gitlog/commit/21093e4aa4fed0f85b9552278fd86cde8f117ce2))
- 修正 `CHANGELOG_zh-cn.md.mustache` 瑕疵 ([33f0c7e4](https://github.com/ymind/maven-semantic-gitlog/commit/33f0c7e4e396ca675679c0eded2b4a6ee9d2fa5a))


### Code Refactoring

- **mojo**: 修正 `@Parameter` 配置 ([dfb9fa6f](https://github.com/ymind/maven-semantic-gitlog/commit/dfb9fa6f590e9ed3034409e33d69acc3f6b24e37))


### Chores

- **deps**: 更新 `semantic-gitlog` 至 `0.3.2` ([b3b5fa65](https://github.com/ymind/maven-semantic-gitlog/commit/b3b5fa65bbc54d4092fcce9a5ad2b409fcf425d3))


### Continuous Integration

- **travis**: 更新 CI ([2084e7dd](https://github.com/ymind/maven-semantic-gitlog/commit/2084e7dd60984592b058b872d74cfccf04416966))


## 0.19.0 (2020-03-08)

### Features

- **mojo**: 添加 `updateProjectVersion` 配置项 ([105f9c25](https://github.com/ymind/maven-semantic-gitlog/commit/105f9c25bc121966b31bc3ea15ca5f3be793727e))


### Styles

- **checkstyle**: 更新 checkstyle 至 8.30 ([9c1d27b2](https://github.com/ymind/maven-semantic-gitlog/commit/9c1d27b24aa6bbf7bc4a6633d098f86f506d0f9a))
- **pmd**: 更新 PMD 规则 ([c5e66bca](https://github.com/ymind/maven-semantic-gitlog/commit/c5e66bca7ad229006a30df87387269a28aa97ed9))


### BREAKING CHANGES

- 升级 `semantic-gitlog` 到 0.2.0 ([7817616b](https://github.com/ymind/maven-semantic-gitlog/commit/7817616b3d4180e5a4a37b273318a29dbbe41d3c))
- 升级 `semantic-gitlog` 到 0.2.2 ([cc648429](https://github.com/ymind/maven-semantic-gitlog/commit/cc648429488ae5dccf22c8a788c69370681f359e))


## 0.16.1 (2020-02-27)

### Bug Fixes

- **mojo**: 修复插件配置 ([ef908f95](https://github.com/ymind/maven-semantic-gitlog/commit/ef908f95f9d41917b9b7d4bf4b15698ae4a1e952))


### Features

- **mojo**: 添加 `isPreRelease` 配置项 ([24b145ff](https://github.com/ymind/maven-semantic-gitlog/commit/24b145ff0a1c78fa1297fe0f8b933b762fe084d1))
- **mojo**: 添加 `commitLocales` 配置项，支持提交消息的本地化 ([37ffa021](https://github.com/ymind/maven-semantic-gitlog/commit/37ffa02133cb474bdb3023b87663364eedce6154))
- 支持从本地化配置文件覆盖提交类型 ([83796445](https://github.com/ymind/maven-semantic-gitlog/commit/83796445681c0c0d22b2c21c22a757301efef470))


### Documentation

- 添加 `organization` 信息 ([6c8107da](https://github.com/ymind/maven-semantic-gitlog/commit/6c8107da5d7c2ab6ec4a13e4d5e0165287ca360c))


### BREAKING CHANGES

- **parser**: 重构提交消息解析器 ([5a912bf0](https://github.com/ymind/maven-semantic-gitlog/commit/5a912bf0b52692bc7301713dafabd71e2e7698f1))


### Chores

- **deps**: 更新 `lombok` 至 `1.18.12` ([0caae3b3](https://github.com/ymind/maven-semantic-gitlog/commit/0caae3b3b3da0d8fbb1bfd3a3b5ee9149468976f))


### Continuous Integration

- **travis**: 添加 `CHANGELOG.json` 文件回推 ([c3a30910](https://github.com/ymind/maven-semantic-gitlog/commit/c3a30910d13977960b4e24172ee1cd5c9fdf78dc))


## 0.12.0 (2020-02-06)

### Bug Fixes

- **mojo**: 修复当 `fileSets` 为空或无效时 `changelog` 目标执行失败的问题 ([d8524b78](https://github.com/ymind/maven-semantic-gitlog/commit/d8524b78ab1ee1a44581a6a809233caa06960311))
- **service**: 修复标记范围外的提交 ([3e73ed67](https://github.com/ymind/maven-semantic-gitlog/commit/3e73ed67d73d781474eddf50bfe2a170ba35ce9b))


### Code Refactoring

- **git-changelog**: 将字段 `newVersion` 重命名为 `nextVersion` ([2b80de94](https://github.com/ymind/maven-semantic-gitlog/commit/2b80de941ef1bc245a47f9a1c0920b106b847936))
- **mojo**: 将配置项 `useCrazyGrowing` 重命名为 `ReleaseStrategy` ([5fd40d4e](https://github.com/ymind/maven-semantic-gitlog/commit/5fd40d4eaa57dd9ec5bb36e5e25691e9e1874275))
- 项目重构 ([bc1609f3](https://github.com/ymind/maven-semantic-gitlog/commit/bc1609f3755c8613e02fddb57a1d674ded373d47))
- 添加 `hasQuickActions`、`hasSubjectIssues` 和 `hasBodyIssues` ([5925f369](https://github.com/ymind/maven-semantic-gitlog/commit/5925f369d1e90cc3a0be8eba663eaa7c0e988d24))
- 更新 `DEFAULT_MESSAGE_PATTERN` ([1d37706f](https://github.com/ymind/maven-semantic-gitlog/commit/1d37706fb04541ef7a2738c23861f158a36ce6cc))
- 代码优化 ([db48c20a](https://github.com/ymind/maven-semantic-gitlog/commit/db48c20abca14fce4d5610b0252b519a93214647))


### Features

- **git-changelog**: 添加 `quickActionPattern` 配置项 ([3d957eda](https://github.com/ymind/maven-semantic-gitlog/commit/3d957edab53e1f50d95cac6d3b0b19b0719efcf6))
- **git-changelog**: 添加 `commitIssuePattern` 配置项 ([edd55e47](https://github.com/ymind/maven-semantic-gitlog/commit/edd55e47eba382f91e3d79dbb0ff3a6b9cfea905))
- **mojo**: 兼容 `angular-style` 的最新格式 ([1b4efac7](https://github.com/ymind/maven-semantic-gitlog/commit/1b4efac7649ec56f345da1c7735ed40707e4761b))
- **mojo**: 添加 `closeIssueActions` 配置项 ([8671b566](https://github.com/ymind/maven-semantic-gitlog/commit/8671b5665f1e12a758be474d0f68b5c22fdf8371))
- **mojo**: 添加多模板支持 ([24ff4b29](https://github.com/ymind/maven-semantic-gitlog/commit/24ff4b29d63e9fa78f5677bc067fbc9a6d5d485c))
- **mojo**: 添加 `preRelease` 和 `buildMetaData` 配置项 ([bb1da529](https://github.com/ymind/maven-semantic-gitlog/commit/bb1da529c03b0dd572669b127aa4ca056851245a))
- **mojo**: 添加 `hiddenTypes` 配置项 ([bae9f5af](https://github.com/ymind/maven-semantic-gitlog/commit/bae9f5afaba467f599e5c8a8f4d103853c00dc8a))
- **mojo**: 为 `ReleaseLog.tags` 字段添加排序支持 ([f478ce35](https://github.com/ymind/maven-semantic-gitlog/commit/f478ce3509f4cddbfb3494e6c4ca2175d1aadcea))
- **mojo**: 当版本不增长时，从早期版本自动递增 ([3a27b663](https://github.com/ymind/maven-semantic-gitlog/commit/3a27b663dc20658afcf02eca8957b0290d50d0ee))
- **mojo**: 添加 `forceNextVersion` 配置项 ([4ef660df](https://github.com/ymind/maven-semantic-gitlog/commit/4ef660df87a484458ed92c1f31eab5a4abfd4408))


### Documentation

- **docs**: 更新 `Conventional Commits` 徽标 ([83e42818](https://github.com/ymind/maven-semantic-gitlog/commit/83e42818c8a3693214884787144bce0e99f988d2))
- **docs**: 更新文档 ([34f7d437](https://github.com/ymind/maven-semantic-gitlog/commit/34f7d4379fc69e0d3ba0dfc7d065a8e4c859d7d8))
- **docs**: 添加 favicon ([32953c83](https://github.com/ymind/maven-semantic-gitlog/commit/32953c8327ed12f99d04a3e64acf57d0095d4bc0))


### Styles

- 调整 `.xml` 和 `.json` 的缩进 ([6750c9ef](https://github.com/ymind/maven-semantic-gitlog/commit/6750c9ef5cec141c61defe3d63570b8b4a824de5))


### Chores

- 修复 PMD 问题事项 ([6c87eabd](https://github.com/ymind/maven-semantic-gitlog/commit/6c87eabd7745045ac08f7a529f304bdc5d2c54cc))


### Continuous Integration

- **travis**: 添加 `travis` 配置 ([0116a713](https://github.com/ymind/maven-semantic-gitlog/commit/0116a713f015f6a05532f11cca17467137dcda81))
- **travis**: 添加 `CHANGELOG_*.md` 文件回推 ([3eaf171f](https://github.com/ymind/maven-semantic-gitlog/commit/3eaf171f44e52eb174ba17ea1fb8bb99853adfb8))
- **travis**: 修正 CI ([e6d39850](https://github.com/ymind/maven-semantic-gitlog/commit/e6d39850936933f5f08f253ae314fa0e78ae749c))
- 添加 `ciManagement` 信息 ([111ce4fe](https://github.com/ymind/maven-semantic-gitlog/commit/111ce4fe4e0de7464aefe070c9a20c04ae72e83f))
- 更新 CI ([88cf4838](https://github.com/ymind/maven-semantic-gitlog/commit/88cf483884819310ceeb12560852fcf2b15c8905))
- 更新 CI ([0b8cc345](https://github.com/ymind/maven-semantic-gitlog/commit/0b8cc345aed9454846f445206aa3fe564151b9f9))


## 0.2.0 (2020-01-29)

### Bug Fixes

- **git-changelog**: 修复 `releaseDate` 数据类型 ([545aa511](https://github.com/ymind/maven-semantic-gitlog/commit/545aa511aefb5bbd02b78b73fa37a6f69cf6d1e7))
- **gitReleaseCommit**: 修正提交记录的 `fullHash` 属性 ([f765dbb9](https://github.com/ymind/maven-semantic-gitlog/commit/f765dbb9aeb77eb731e5b787e13bb471ad0abbdf))


### Documentation

- **docs**: 添加或更新徽标 ([630a1a33](https://github.com/ymind/maven-semantic-gitlog/commit/630a1a33adc4e76370502f0d842a2c50beeb3234))
- **docs**: 更新 `README.md` ([92909a16](https://github.com/ymind/maven-semantic-gitlog/commit/92909a164b611c242721b2ad0643f5549aabd32e))
- **docs**: 添加更新日志页 ([fcfe57ae](https://github.com/ymind/maven-semantic-gitlog/commit/fcfe57ae6ba5f79ef132446c55460af582940895))


### Styles

- **checkstyle**: 更新 `checkstyle.xml` ([a131dcf6](https://github.com/ymind/maven-semantic-gitlog/commit/a131dcf6e3b92af8048e5c7d2dc2546b86633ce2))


### Chores

- 更新 `pom.xml` ([3eb99c35](https://github.com/ymind/maven-semantic-gitlog/commit/3eb99c357e7c2736771a1081aa29f38e62f89885))


## 0.1.0 (2020-01-28)

### Features

- **derive**: 添加 `semantic-gitlog:derive` 目标 ([4367c63d](https://github.com/ymind/maven-semantic-gitlog/commit/4367c63de29e56fa40044341ac0273a622a4b6b8))
- **git-changelog**: 实现主要功能 ([8d964149](https://github.com/ymind/maven-semantic-gitlog/commit/8d9641496af9c52ef39540c32980e3732c878ca7))


### Documentation

- **docs**: 添加文档 ([1978d19b](https://github.com/ymind/maven-semantic-gitlog/commit/1978d19b1c381784153787fa6a0f5673bdab8336))


### Chores

- **deps**: 更新 `checkstyle` 版本 ([a6bc1482](https://github.com/ymind/maven-semantic-gitlog/commit/a6bc148222fe161f8a5315f4378c97964df6057d))
- **deps**: 更新 `pmd` 版本 ([2a69b52d](https://github.com/ymind/maven-semantic-gitlog/commit/2a69b52d9052b60c4a515063bcb2c39d6a4f9511))
- 添加 `CHANGELOG.md` ([b3f41bd4](https://github.com/ymind/maven-semantic-gitlog/commit/b3f41bd49b6b7d02a2ae33f7a8833524e072d590))

