# Plugin Goals

## 1. semantic-gitlog:derive

Derive version number and print out.

Example:

```bash
$ mvn semantic-gitlog:derive | grep NEW_VERSION:== | sed 's/NEW_VERSION:==//g'

# will print:
!!! NewVersion=0.28.11-alpha.1
```

## 2. semantic-gitlog:git-changelog

Automatically generate change logs.
