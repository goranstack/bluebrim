#!/usr/bin/env bash

if [ ! -z "$TRAVIS_TAG" ]
then
	mvn versions:set -DnewVersion=$TRAVIS_TAG
else
	echo "not on a tag -> keep snapshot version in pom.xml"
fi

