# 更新日志

## 0.12.0 (2020-02-06)

### Bug Fixes

- **mojo**: goal `changelog` failed when &quot;fileSets&quot; is empty or invalid ([d8524b78](https://github.com/ymind/maven-semantic-gitlog/commit/d8524b78ab1ee1a44581a6a809233caa06960311))
- **service**: Fix commits outside the Tag scope ([3e73ed67](https://github.com/ymind/maven-semantic-gitlog/commit/3e73ed67d73d781474eddf50bfe2a170ba35ce9b))


### Code Refactoring

- **git-changelog**: change field name `newVersion` to `nextVersion` ([2b80de94](https://github.com/ymind/maven-semantic-gitlog/commit/2b80de941ef1bc245a47f9a1c0920b106b847936))
- **mojo**: replace `useCrazyGrowing` configuration with `ReleaseStrategy` ([5fd40d4e](https://github.com/ymind/maven-semantic-gitlog/commit/5fd40d4eaa57dd9ec5bb36e5e25691e9e1874275))
- code optimize ([db48c20a](https://github.com/ymind/maven-semantic-gitlog/commit/db48c20abca14fce4d5610b0252b519a93214647))
- update `DEFAULT_MESSAGE_PATTERN` ([1d37706f](https://github.com/ymind/maven-semantic-gitlog/commit/1d37706fb04541ef7a2738c23861f158a36ce6cc))
- add `hasQuickActions`, `hasSubjectIssues` and `hasBodyIssues` ([5925f369](https://github.com/ymind/maven-semantic-gitlog/commit/5925f369d1e90cc3a0be8eba663eaa7c0e988d24))
- refactor project ([bc1609f3](https://github.com/ymind/maven-semantic-gitlog/commit/bc1609f3755c8613e02fddb57a1d674ded373d47))


### Features

- **git-changelog**: add `commitIssuePattern` configuration ([edd55e47](https://github.com/ymind/maven-semantic-gitlog/commit/edd55e47eba382f91e3d79dbb0ff3a6b9cfea905))
- **git-changelog**: add `quickActionPattern` configuration ([3d957eda](https://github.com/ymind/maven-semantic-gitlog/commit/3d957edab53e1f50d95cac6d3b0b19b0719efcf6))
- **mojo**: add `forceNextVersion` configuration ([4ef660df](https://github.com/ymind/maven-semantic-gitlog/commit/4ef660df87a484458ed92c1f31eab5a4abfd4408))
- **mojo**: automatically increments from the previous version when the version does not grow ([3a27b663](https://github.com/ymind/maven-semantic-gitlog/commit/3a27b663dc20658afcf02eca8957b0290d50d0ee))
- **mojo**: sorted `ReleaseLog.tags` ([f478ce35](https://github.com/ymind/maven-semantic-gitlog/commit/f478ce3509f4cddbfb3494e6c4ca2175d1aadcea))
- **mojo**: add `hiddenTypes` configuration ([bae9f5af](https://github.com/ymind/maven-semantic-gitlog/commit/bae9f5afaba467f599e5c8a8f4d103853c00dc8a))
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

- **travis**: fix ci ([e6d39850](https://github.com/ymind/maven-semantic-gitlog/commit/e6d39850936933f5f08f253ae314fa0e78ae749c))
- **travis**: add push-back file `CHANGELOG_*.md` ([3eaf171f](https://github.com/ymind/maven-semantic-gitlog/commit/3eaf171f44e52eb174ba17ea1fb8bb99853adfb8))
- **travis**: add travis CI configuration ([0116a713](https://github.com/ymind/maven-semantic-gitlog/commit/0116a713f015f6a05532f11cca17467137dcda81))
- update ci ([0b8cc345](https://github.com/ymind/maven-semantic-gitlog/commit/0b8cc345aed9454846f445206aa3fe564151b9f9))
- update ci ([88cf4838](https://github.com/ymind/maven-semantic-gitlog/commit/88cf483884819310ceeb12560852fcf2b15c8905))
- add `ciManagement` info ([111ce4fe](https://github.com/ymind/maven-semantic-gitlog/commit/111ce4fe4e0de7464aefe070c9a20c04ae72e83f))


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

