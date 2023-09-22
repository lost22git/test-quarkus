#!/usr/bin/env bash

./gradlew app:clean && ./gradlew app:fatJar && java -jar app/build/app-0.0.1-runner.jar
