# Plugin Goals

## 1. semantic-gitlog:derive

根据提交记录推导版本号并打印输出。

例如：
```bash
$ mvn semantic-gitlog:derive | grep NEW_VERSION:== | sed 's/NEW_VERSION:==//g'

# will print:
0.28.11-alpha.1
```

## 2. semantic-gitlog:git-changelog

根据提交记录自动生成新的版本号并生成更新日志。
