#!/usr/bin/env bash
# Switch to script directory
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "${DIR}"

set -e # Stop on first non-null return value

settings="$1"

if [[ -z "$settings" ]]; then
    settings=~/.m2/ossrh.xml
fi

set -u # Stop on uninitialised variable
mvn="mvn -s $settings"

echo " * Perform unit tests..."
$mvn test

echo " * Prepare the release"
$mvn --batch-mode release:prepare -Darguments="-DskipTests"

echo " * Prepare the release"
$mvn release:perform -Darguments="-DskipTests"
