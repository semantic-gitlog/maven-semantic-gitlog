# maven-semantic-gitlog

A simple [Semantic Versioning](https://semver.org/) management tool based on [Conventional Commits](https://conventionalcommits.org).
It automatically derive and manage version numbers and generate [angular-style](https://github.com/angular/angular/blob/master/CONTRIBUTING.md) change logs.

As same as [git-changelog-lib](https://github.com/tomasbjerre/git-changelog-lib), we fully configurable with [Mustache](http://mustache.github.io/) template. That can:

* Be stored to file, like CHANGELOG.md.
* Be posted to MediaWiki ([here](https://github.com/tomasbjerre/git-changelog-lib/tree/screenshots/sandbox) is an example)
* Or just rendered to a String.

The [CHANGELOG.md](https://github.com/ymind/maven-semantic-gitlog/blob/master/CHANGELOG.md) of this project is automatically generated with this [template](https://github.com/ymind/maven-semantic-gitlog/blob/master/config/gitlog/CHANGELOG.tpl.md).

# Author

[ymind chan](https://github.com/ymind), full stack engineer.

# Related community projects

* [semantic-version](https://github.com/skuzzle/semantic-version)
* [git-changelog-lib](https://github.com/tomasbjerre/git-changelog-lib)

# License

This is open-sourced software licensed under the [MIT license](https://opensource.org/licenses/MIT).
