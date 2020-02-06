# 更新日志

## 0.9.1 (Unreleased, 2020-02-05)

### Bug Fixes

- **mojo**: goal `changelog` failed when &quot;fileSets&quot; is empty or invalid ([66dacea8](https://github.com/ymind/maven-semantic-gitlog/commit/66dacea82f976b1c9d9b6e8bb73903a33c95e242))
- **service**: Fix commits outside the Tag scope ([3e73ed67](https://github.com/ymind/maven-semantic-gitlog/commit/3e73ed67d73d781474eddf50bfe2a170ba35ce9b))


### Code Refactoring

- **git-changelog**: change field name `newVersion` to `nextVersion` ([2b80de94](https://github.com/ymind/maven-semantic-gitlog/commit/2b80de941ef1bc245a47f9a1c0920b106b847936))
- **mojo**: replace `useCrazyGrowing` configuration with `ReleaseStrategy` ([5fd40d4e](https://github.com/ymind/maven-semantic-gitlog/commit/5fd40d4eaa57dd9ec5bb36e5e25691e9e1874275))
- update DEFAULT_MESSAGE_PATTERN ([1c752fdd](https://github.com/ymind/maven-semantic-gitlog/commit/1c752fdd0dc3015a37121e03d0f02789df91f8c2))
- add `hasQuickActions`, `hasSubjectIssues` and `hasBodyIssues` ([29fb7e11](https://github.com/ymind/maven-semantic-gitlog/commit/29fb7e119b054fe7a748fca48b6b2556bf4a5de3))
- refactor project ([bc1609f3](https://github.com/ymind/maven-semantic-gitlog/commit/bc1609f3755c8613e02fddb57a1d674ded373d47))


### Features

- **git-changelog**: add `commitIssuePattern` configuration ([edd55e47](https://github.com/ymind/maven-semantic-gitlog/commit/edd55e47eba382f91e3d79dbb0ff3a6b9cfea905))
- **git-changelog**: add `quickActionPattern` configuration ([3d957eda](https://github.com/ymind/maven-semantic-gitlog/commit/3d957edab53e1f50d95cac6d3b0b19b0719efcf6))
- **mojo**: add `hiddenTypes` configuration ([f221744a](https://github.com/ymind/maven-semantic-gitlog/commit/f221744aba00072be3fb432c86aea921e6cea3db))
- **mojo**: add `preRelease` and `buildMetaData` configuration ([bb1da529](https://github.com/ymind/maven-semantic-gitlog/commit/bb1da529c03b0dd572669b127aa4ca056851245a))
- **mojo**: add multi-template filesets support ([24ff4b29](https://github.com/ymind/maven-semantic-gitlog/commit/24ff4b29d63e9fa78f5677bc067fbc9a6d5d485c))
- **mojo**: add `closeIssueActions` configuration ([8671b566](https://github.com/ymind/maven-semantic-gitlog/commit/8671b5665f1e12a758be474d0f68b5c22fdf8371))
- **mojo**: compatible with new `angular-style` commit message format ([1b4efac7](https://github.com/ymind/maven-semantic-gitlog/commit/1b4efac7649ec56f345da1c7735ed40707e4761b))


### Documentation

- **docs**: add favicon ([32953c83](https://github.com/ymind/maven-semantic-gitlog/commit/32953c8327ed12f99d04a3e64acf57d0095d4bc0))
- **docs**: update docs ([34f7d437](https://github.com/ymind/maven-semantic-gitlog/commit/34f7d4379fc69e0d3ba0dfc7d065a8e4c859d7d8))
- **docs**: update `Conventional Commits` badge ([83e42818](https://github.com/ymind/maven-semantic-gitlog/commit/83e42818c8a3693214884787144bce0e99f988d2))


### Styles

- adjust the indent size of `.xml` and `.json` files ([6750c9ef](https://github.com/ymind/maven-semantic-gitlog/commit/6750c9ef5cec141c61defe3d63570b8b4a824de5))


### Continuous Integration

- **travis**: add `ciManagement` info ([521054dd](https://github.com/ymind/maven-semantic-gitlog/commit/521054dd7980f25c3b737e7055f7b322d4507bbc))
- **travis**: fix ci ([e6d39850](https://github.com/ymind/maven-semantic-gitlog/commit/e6d39850936933f5f08f253ae314fa0e78ae749c))
- **travis**: add push-back file `CHANGELOG_*.md` ([3eaf171f](https://github.com/ymind/maven-semantic-gitlog/commit/3eaf171f44e52eb174ba17ea1fb8bb99853adfb8))
- **travis**: add travis CI configuration ([0116a713](https://github.com/ymind/maven-semantic-gitlog/commit/0116a713f015f6a05532f11cca17467137dcda81))
- update ci ([4da86c49](https://github.com/ymind/maven-semantic-gitlog/commit/4da86c49d931f14e2e7d1b0ced0a08ba85e190c7))


### Others

- fix PMD issues ([6c87eabd](https://github.com/ymind/maven-semantic-gitlog/commit/6c87eabd7745045ac08f7a529f304bdc5d2c54cc))


## 0.2.0 (2020-01-29)

### Bug Fixes

- **git-changelog**: fix releaseDate data type error ([545aa511](https://github.com/ymind/maven-semantic-gitlog/commit/545aa511aefb5bbd02b78b73fa37a6f69cf6d1e7))
- **gitReleaseCommit**: fix commit fullHash property ([f765dbb9](https://github.com/ymind/maven-semantic-gitlog/commit/f765dbb9aeb77eb731e5b787e13bb471ad0abbdf))


### Documentation

- **docs**: add changelog page ([fcfe57ae](https://github.com/ymind/maven-semantic-gitlog/commit/fcfe57ae6ba5f79ef132446c55460af582940895))
- **docs**: update README.md ([92909a16](https://github.com/ymind/maven-semantic-gitlog/commit/92909a164b611c242721b2ad0643f5549aabd32e))
- **docs**: add or update badges ([630a1a33](https://github.com/ymind/maven-semantic-gitlog/commit/630a1a33adc4e76370502f0d842a2c50beeb3234))


### Styles

- **checkstyle**: update checkstyle.xml ([a131dcf6](https://github.com/ymind/maven-semantic-gitlog/commit/a131dcf6e3b92af8048e5c7d2dc2546b86633ce2))


### Others

- update pom.xml ([3eb99c35](https://github.com/ymind/maven-semantic-gitlog/commit/3eb99c357e7c2736771a1081aa29f38e62f89885))


## 0.1.0 (2020-01-28)

### Features

- **derive**: add `semantic-gitlog:derive` goal ([4367c63d](https://github.com/ymind/maven-semantic-gitlog/commit/4367c63de29e56fa40044341ac0273a622a4b6b8))
- **git-changelog**: implement main features and challenges ([8d964149](https://github.com/ymind/maven-semantic-gitlog/commit/8d9641496af9c52ef39540c32980e3732c878ca7))


### Documentation

- **docs**: add docs ([1978d19b](https://github.com/ymind/maven-semantic-gitlog/commit/1978d19b1c381784153787fa6a0f5673bdab8336))


### Others

- **deps**: bump pmdVersion from 6.20.0 to 6.21.0 ([2a69b52d](https://github.com/ymind/maven-semantic-gitlog/commit/2a69b52d9052b60c4a515063bcb2c39d6a4f9511))
- **deps**: bump checkstyle from 8.27 to 8.29 ([a6bc1482](https://github.com/ymind/maven-semantic-gitlog/commit/a6bc148222fe161f8a5315f4378c97964df6057d))
- add `CHANGELOG.md` ([b3f41bd4](https://github.com/ymind/maven-semantic-gitlog/commit/b3f41bd49b6b7d02a2ae33f7a8833524e072d590))

