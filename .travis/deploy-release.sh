#!/bin/bash
# expects variables to be set:
# - OSSRH_USERNAME
# - OSSRH_PASSWORD
# - GPG_KEY_NAME
# - GPG_PASSPHRASE
# expects file to exist:
# - .travis/gpg.asc

set -e

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

cd ${TRAVIS_BUILD_DIR}

# Prepare the local keyring (requires travis to have decrypted the file
# beforehand)
gpg --fast-import .travis/gpg.asc

echo "TRAVIS_BRANCH: ${TRAVIS_BRANCH}"
echo "on a tag -> set pom.xml <version> to ${TRAVIS_TAG}"

newVersion=${TRAVIS_TAG}
gitCommit="release: ${newVersion}"

# Print newVersion
echo "newVersion: ${newVersion}"

if [[ -z "${newVersion}" ]]; then
  echo "missing newVersion value" >&2
  exit 1
fi

# Run the maven deploy steps
./mvnw ${PLUGIN_CLI_OPTS} -D "newVersion=${newVersion}" versions:set 1>/dev/null 2>/dev/null
./mvnw ${PLUGIN_CLI_OPTS} -D skipTests=true deploy

# Generate and push CHANGELOG.md - fully
git reset --hard
git remote set-url origin https://${GITHUB_TOKEN}@github.com/${TRAVIS_REPO_SLUG}.git
git remote set-branches --add origin 'master'
git fetch
git checkout master
git show-ref
chmod +x ./mvnw .travis/*.sh

./mvnw ${PLUGIN_CLI_OPTS} -D "newVersion=${newVersion}" versions:set 1>/dev/null 2>/dev/null
./mvnw ${PLUGIN_CLI_OPTS} -D gitlog.toRef=master semantic-gitlog:changelog -U

git add ./CHANGELOG.md
git add ./CHANGELOG_*.md
git add ./pom.xml
git commit --amend -m "${gitCommit}" && git push -f || true
