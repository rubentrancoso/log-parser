#!/bin/sh
java -jar "parser.jar" --accesslog=files/access-short.log --startDate=2017-01-01.00:00:11 --duration=hourly --threshold=200
