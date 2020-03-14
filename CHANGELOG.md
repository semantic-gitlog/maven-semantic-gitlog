# Changelog

## 0.19.4 (2020-03-14)

### Bug Fixes

- **mojo**: fix `ChangelogMojo.updatePom` ([21093e4a](https://github.com/ymind/maven-semantic-gitlog/commit/21093e4aa4fed0f85b9552278fd86cde8f117ce2))
- fix `CHANGELOG_zh-cn.md.mustache` defects ([33f0c7e4](https://github.com/ymind/maven-semantic-gitlog/commit/33f0c7e4e396ca675679c0eded2b4a6ee9d2fa5a))


### Code Refactoring

- **mojo**: fix `@Parameter` config ([dfb9fa6f](https://github.com/ymind/maven-semantic-gitlog/commit/dfb9fa6f590e9ed3034409e33d69acc3f6b24e37))


### Chores

- **deps**: bump `semantic-gitlog` to `0.3.2` ([b3b5fa65](https://github.com/ymind/maven-semantic-gitlog/commit/b3b5fa65bbc54d4092fcce9a5ad2b409fcf425d3))


### Continuous Integration

- **travis**: update ci ([2084e7dd](https://github.com/ymind/maven-semantic-gitlog/commit/2084e7dd60984592b058b872d74cfccf04416966))


## 0.19.0 (2020-03-08)

### Features

- **mojo**: add `updateProjectVersion` configuration ([105f9c25](https://github.com/ymind/maven-semantic-gitlog/commit/105f9c25bc121966b31bc3ea15ca5f3be793727e))


### Styles

- **checkstyle**: bumped checkstyle to 8.30 ([9c1d27b2](https://github.com/ymind/maven-semantic-gitlog/commit/9c1d27b24aa6bbf7bc4a6633d098f86f506d0f9a))
- **pmd**: update PMD rules ([c5e66bca](https://github.com/ymind/maven-semantic-gitlog/commit/c5e66bca7ad229006a30df87387269a28aa97ed9))


### BREAKING CHANGES

- upgrade `semantic-gitlog` to 0.2.0 ([7817616b](https://github.com/ymind/maven-semantic-gitlog/commit/7817616b3d4180e5a4a37b273318a29dbbe41d3c))
- upgrade `semantic-gitlog` to 0.2.2 ([cc648429](https://github.com/ymind/maven-semantic-gitlog/commit/cc648429488ae5dccf22c8a788c69370681f359e))


## 0.16.1 (2020-02-27)

### Bug Fixes

- **mojo**: fix mojo settings ([ef908f95](https://github.com/ymind/maven-semantic-gitlog/commit/ef908f95f9d41917b9b7d4bf4b15698ae4a1e952))


### Features

- **mojo**: add `isPreRelease` configuration ([24b145ff](https://github.com/ymind/maven-semantic-gitlog/commit/24b145ff0a1c78fa1297fe0f8b933b762fe084d1))
- **mojo**: add `commitLocales` configuration, support for localization of commit messages ([37ffa021](https://github.com/ymind/maven-semantic-gitlog/commit/37ffa02133cb474bdb3023b87663364eedce6154))
- support `commitType override` from locale profiles ([83796445](https://github.com/ymind/maven-semantic-gitlog/commit/83796445681c0c0d22b2c21c22a757301efef470))


### Documentation

- add `organization` information ([6c8107da](https://github.com/ymind/maven-semantic-gitlog/commit/6c8107da5d7c2ab6ec4a13e4d5e0165287ca360c))


### BREAKING CHANGES

- **parser**: refactor commit message parser ([5a912bf0](https://github.com/ymind/maven-semantic-gitlog/commit/5a912bf0b52692bc7301713dafabd71e2e7698f1))


### Chores

- **deps**: bump `lombok` to `1.18.12` ([0caae3b3](https://github.com/ymind/maven-semantic-gitlog/commit/0caae3b3b3da0d8fbb1bfd3a3b5ee9149468976f))


### Continuous Integration

- **travis**: add push-back file `CHANGELOG.json` ([c3a30910](https://github.com/ymind/maven-semantic-gitlog/commit/c3a30910d13977960b4e24172ee1cd5c9fdf78dc))


## 0.12.0 (2020-02-06)

### Bug Fixes

- **mojo**: goal `changelog` failed when "fileSets" is empty or invalid ([d8524b78](https://github.com/ymind/maven-semantic-gitlog/commit/d8524b78ab1ee1a44581a6a809233caa06960311))
- **service**: Fix commits outside the Tag scope ([3e73ed67](https://github.com/ymind/maven-semantic-gitlog/commit/3e73ed67d73d781474eddf50bfe2a170ba35ce9b))


### Code Refactoring

- **git-changelog**: change field name `newVersion` to `nextVersion` ([2b80de94](https://github.com/ymind/maven-semantic-gitlog/commit/2b80de941ef1bc245a47f9a1c0920b106b847936))
- **mojo**: replace `useCrazyGrowing` configuration with `ReleaseStrategy` ([5fd40d4e](https://github.com/ymind/maven-semantic-gitlog/commit/5fd40d4eaa57dd9ec5bb36e5e25691e9e1874275))
- refactor project ([bc1609f3](https://github.com/ymind/maven-semantic-gitlog/commit/bc1609f3755c8613e02fddb57a1d674ded373d47))
- add `hasQuickActions`, `hasSubjectIssues` and `hasBodyIssues` ([5925f369](https://github.com/ymind/maven-semantic-gitlog/commit/5925f369d1e90cc3a0be8eba663eaa7c0e988d24))
- update `DEFAULT_MESSAGE_PATTERN` ([1d37706f](https://github.com/ymind/maven-semantic-gitlog/commit/1d37706fb04541ef7a2738c23861f158a36ce6cc))
- code optimize ([db48c20a](https://github.com/ymind/maven-semantic-gitlog/commit/db48c20abca14fce4d5610b0252b519a93214647))


### Features

- **git-changelog**: add `quickActionPattern` configuration ([3d957eda](https://github.com/ymind/maven-semantic-gitlog/commit/3d957edab53e1f50d95cac6d3b0b19b0719efcf6))
- **git-changelog**: add `commitIssuePattern` configuration ([edd55e47](https://github.com/ymind/maven-semantic-gitlog/commit/edd55e47eba382f91e3d79dbb0ff3a6b9cfea905))
- **mojo**: compatible with new `angular-style` commit message format ([1b4efac7](https://github.com/ymind/maven-semantic-gitlog/commit/1b4efac7649ec56f345da1c7735ed40707e4761b))
- **mojo**: add `closeIssueActions` configuration ([8671b566](https://github.com/ymind/maven-semantic-gitlog/commit/8671b5665f1e12a758be474d0f68b5c22fdf8371))
- **mojo**: add multi-template filesets support ([24ff4b29](https://github.com/ymind/maven-semantic-gitlog/commit/24ff4b29d63e9fa78f5677bc067fbc9a6d5d485c))
- **mojo**: add `preRelease` and `buildMetaData` configuration ([bb1da529](https://github.com/ymind/maven-semantic-gitlog/commit/bb1da529c03b0dd572669b127aa4ca056851245a))
- **mojo**: add `hiddenTypes` configuration ([bae9f5af](https://github.com/ymind/maven-semantic-gitlog/commit/bae9f5afaba467f599e5c8a8f4d103853c00dc8a))
- **mojo**: sorted `ReleaseLog.tags` ([f478ce35](https://github.com/ymind/maven-semantic-gitlog/commit/f478ce3509f4cddbfb3494e6c4ca2175d1aadcea))
- **mojo**: automatically increments from the previous version when the version does not grow ([3a27b663](https://github.com/ymind/maven-semantic-gitlog/commit/3a27b663dc20658afcf02eca8957b0290d50d0ee))
- **mojo**: add `forceNextVersion` configuration ([4ef660df](https://github.com/ymind/maven-semantic-gitlog/commit/4ef660df87a484458ed92c1f31eab5a4abfd4408))


### Documentation

- **docs**: update `Conventional Commits` badge ([83e42818](https://github.com/ymind/maven-semantic-gitlog/commit/83e42818c8a3693214884787144bce0e99f988d2))
- **docs**: update docs ([34f7d437](https://github.com/ymind/maven-semantic-gitlog/commit/34f7d4379fc69e0d3ba0dfc7d065a8e4c859d7d8))
- **docs**: add favicon ([32953c83](https://github.com/ymind/maven-semantic-gitlog/commit/32953c8327ed12f99d04a3e64acf57d0095d4bc0))


### Styles

- adjust the indent size of `.xml` and `.json` files ([6750c9ef](https://github.com/ymind/maven-semantic-gitlog/commit/6750c9ef5cec141c61defe3d63570b8b4a824de5))


### Chores

- fix PMD issues ([6c87eabd](https://github.com/ymind/maven-semantic-gitlog/commit/6c87eabd7745045ac08f7a529f304bdc5d2c54cc))


### Continuous Integration

- **travis**: add travis CI configuration ([0116a713](https://github.com/ymind/maven-semantic-gitlog/commit/0116a713f015f6a05532f11cca17467137dcda81))
- **travis**: add push-back file `CHANGELOG_*.md` ([3eaf171f](https://github.com/ymind/maven-semantic-gitlog/commit/3eaf171f44e52eb174ba17ea1fb8bb99853adfb8))
- **travis**: fix ci ([e6d39850](https://github.com/ymind/maven-semantic-gitlog/commit/e6d39850936933f5f08f253ae314fa0e78ae749c))
- add `ciManagement` info ([111ce4fe](https://github.com/ymind/maven-semantic-gitlog/commit/111ce4fe4e0de7464aefe070c9a20c04ae72e83f))
- update ci ([88cf4838](https://github.com/ymind/maven-semantic-gitlog/commit/88cf483884819310ceeb12560852fcf2b15c8905))
- update ci ([0b8cc345](https://github.com/ymind/maven-semantic-gitlog/commit/0b8cc345aed9454846f445206aa3fe564151b9f9))


## 0.2.0 (2020-01-29)

### Bug Fixes

- **git-changelog**: fix releaseDate data type error ([545aa511](https://github.com/ymind/maven-semantic-gitlog/commit/545aa511aefb5bbd02b78b73fa37a6f69cf6d1e7))
- **gitReleaseCommit**: fix commit fullHash property ([f765dbb9](https://github.com/ymind/maven-semantic-gitlog/commit/f765dbb9aeb77eb731e5b787e13bb471ad0abbdf))


### Documentation

- **docs**: add or update badges ([630a1a33](https://github.com/ymind/maven-semantic-gitlog/commit/630a1a33adc4e76370502f0d842a2c50beeb3234))
- **docs**: update README.md ([92909a16](https://github.com/ymind/maven-semantic-gitlog/commit/92909a164b611c242721b2ad0643f5549aabd32e))
- **docs**: add changelog page ([fcfe57ae](https://github.com/ymind/maven-semantic-gitlog/commit/fcfe57ae6ba5f79ef132446c55460af582940895))


### Styles

- **checkstyle**: update checkstyle.xml ([a131dcf6](https://github.com/ymind/maven-semantic-gitlog/commit/a131dcf6e3b92af8048e5c7d2dc2546b86633ce2))


### Chores

- update pom.xml ([3eb99c35](https://github.com/ymind/maven-semantic-gitlog/commit/3eb99c357e7c2736771a1081aa29f38e62f89885))


## 0.1.0 (2020-01-28)

### Features

- **derive**: add `semantic-gitlog:derive` goal ([4367c63d](https://github.com/ymind/maven-semantic-gitlog/commit/4367c63de29e56fa40044341ac0273a622a4b6b8))
- **git-changelog**: implement main features and challenges ([8d964149](https://github.com/ymind/maven-semantic-gitlog/commit/8d9641496af9c52ef39540c32980e3732c878ca7))


### Documentation

- **docs**: add docs ([1978d19b](https://github.com/ymind/maven-semantic-gitlog/commit/1978d19b1c381784153787fa6a0f5673bdab8336))


### Chores

- **deps**: bump checkstyle from 8.27 to 8.29 ([a6bc1482](https://github.com/ymind/maven-semantic-gitlog/commit/a6bc148222fe161f8a5315f4378c97964df6057d))
- **deps**: bump pmdVersion from 6.20.0 to 6.21.0 ([2a69b52d](https://github.com/ymind/maven-semantic-gitlog/commit/2a69b52d9052b60c4a515063bcb2c39d6a4f9511))
- add `CHANGELOG.md` ([b3f41bd4](https://github.com/ymind/maven-semantic-gitlog/commit/b3f41bd49b6b7d02a2ae33f7a8833524e072d590))

