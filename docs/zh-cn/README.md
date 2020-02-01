# maven-semantic-gitlog

一个简单的基于 [Conventional Commits](https://conventionalcommits.org) 的 [semantic Versioning 2.0.0](https://semver.org/) 自动化版本管理工具.
它可以自动推断和管理版本号并生成 `angular-style` 风格的更新日志。

与 [git-changelog-lib](https://github.com/tomasbjerre/git-changelog-lib) 一样, 我们完全使用 [Mustache](http://mustache.github.io/) 模板引擎。它可以：

* 存储到文件中，如CHANGELOG.md。
* 推送到 MediaWiki ([示例](https://github.com/tomasbjerre/git-changelog-lib/tree/screenshots/sandbox))
* 或直接保存到字符串。

本项目的 [CHANGELOG.md](https://github.com/ymind/maven-semantic-gitlog/blob/master/CHANGELOG.md) 便由 [这个模板](https://github.com/ymind/maven-semantic-gitlog/blob/master/config/gitlog/CHANGELOG.tpl.md) 生成。

# Author

[ymind chan](https://github.com/ymind), full stack engineer.

# Related community projects

* [semantic-version](https://github.com/skuzzle/semantic-version)
* [git-changelog-lib](https://github.com/tomasbjerre/git-changelog-lib)

# License

This is open-sourced software licensed under the [MIT license](https://opensource.org/licenses/MIT).
