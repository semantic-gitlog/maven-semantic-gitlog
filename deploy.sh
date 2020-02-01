#!/bin/bash
# expects variables to be set:
# - MAVEN_CLI_OPTS
# - OSSRH_USERNAME
# - OSSRH_PASSWORD
# - GPG_KEY_NAME
# - GPG_PASSPHRASE
# expects file to exist:
# - .travis/gpg.asc

set -e

# Check the variables are set
if [[ -z "${MAVEN_CLI_OPTS}" ]]; then
  echo "missing environment value: MAVEN_CLI_OPTS" >&2
  exit 1
fi

# Check the variables are set
if [[ -z "${OSSRH_USERNAME}" ]]; then
  echo "missing environment value: OSSRH_USERNAME" >&2
  exit 1
fi

if [[ -z "${OSSRH_PASSWORD}" ]]; then
  echo "missing environment value: OSSRH_PASSWORD" >&2
  exit 1
fi

if [[ -z "${GPG_KEY_NAME}" ]]; then
  echo "missing environment value: GPG_KEY_NAME" >&2
  exit 1
fi

if [[ -z "${GPG_PASSPHRASE}" ]]; then
  echo "missing environment value: GPG_PASSPHRASE" >&2
  exit 1
fi

# Prepare the local keyring (requires travis to have decrypted the file
# beforehand)
gpg --fast-import .travis/gpg.asc

git config user.name "Travis CI"
git config user.email "travis.ci@yi.team"
git remote set-url origin https://${GITHUB_TOKEN}@github.com/${TRAVIS_REPO_SLUG}.git
git fetch
git checkout master

newVersion=${TRAVIS_TAG}
gitCommit="release ${newVersion}"

if [[ ! -z "${TRAVIS_TAG}" ]]
then
    echo "on a tag -> set pom.xml <version> to ${TRAVIS_TAG}"
else
    echo "not on a tag -> derive version and keep snapshot"

    nextVersion=`./mvnw ${MAVEN_CLI_OPTS} --settings "${TRAVIS_BUILD_DIR}/.travis/mvn-settings.xml" -P release-plugin -U semantic-gitlog:derive | grep 'NEXT_VERSION:==' | sed 's/.\+NEXT_VERSION:==//g'`
    newVersion="${nextVersion}-SNAPSHOT"

    gitCommit="bumped version to ${newVersion}"
fi

# Print newVersion
echo "newVersion: ${newVersion}"

if [[ -z "${newVersion}" ]]; then
  echo "missing newVersion value" >&2
  exit 1
fi

# Run the maven deploy steps
./mvnw ${MAVEN_CLI_OPTS} --settings "${TRAVIS_BUILD_DIR}/.travis/mvn-settings.xml" -P release-plugin versions:set -D "newVersion=${newVersion}" 1>/dev/null 2>/dev/null
./mvnw ${MAVEN_CLI_OPTS} --settings "${TRAVIS_BUILD_DIR}/.travis/mvn-settings.xml" -P release-plugin -DskipTests=true deploy

# Generate and push CHANGELOG.md
./mvnw ${MAVEN_CLI_OPTS} --settings "${TRAVIS_BUILD_DIR}/.travis/mvn-settings.xml" -P release-plugin -U semantic-gitlog:git-changelog
git add ./CHANGELOG.md
git add ./pom.xml
git commit -m "[skip ci] ${gitCommit}" && git push origin || true
