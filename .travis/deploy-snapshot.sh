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
echo "not on a tag -> derive version and keep snapshot"

newVersion=`./mvnw ${PLUGIN_CLI_OPTS} \
    -D gitlog.toRef=${TRAVIS_BRANCH} \
    -D gitlog.preRelease='SNAPSHOT' \
    semantic-gitlog:derive -U | grep 'NEXT_VERSION:==' | sed 's/^.*NEXT_VERSION:==//g'`
gitCommit="bumped version to ${newVersion}"

# Print newVersion
echo "newVersion: ${newVersion}"

if [[ -z "${newVersion}" ]]; then
  echo "missing newVersion value" >&2
  exit 1
fi

git remote set-url origin https://${GITHUB_TOKEN}@github.com/${TRAVIS_REPO_SLUG}.git
git fetch
git checkout master
git show-ref

# Run the maven deploy steps
./mvnw ${PLUGIN_CLI_OPTS} -D "newVersion=${newVersion}" versions:set 1>/dev/null 2>/dev/null
./mvnw ${PLUGIN_CLI_OPTS} -D skipTests=true deploy

# Generate and push CHANGELOG.md
./mvnw ${PLUGIN_CLI_OPTS} -D gitlog.toRef=master semantic-gitlog:changelog -U

git add ./CHANGELOG* || true
git add ./pom.xml || true
git commit -m "${gitCommit}" && git push -f || true
