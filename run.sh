#!/bin/sh
java -jar "build/libs/log-parser-0.0.1-SNAPSHOT.jar" --accesslog=files/access-short.log --startDate=2017-01-01.00:00:11 --duration=hourly --threshold=200
