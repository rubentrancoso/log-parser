# log-parser

The schema is here: https://github.com/rubentrancoso/log-parser/blob/master/scripts/schema.sql

the SQL queries are here: https://github.com/rubentrancoso/log-parser/blob/master/test.sql

There's however a little change that I made to the command line once the application is written over sprint boot.

```
-rw-rw-r--. 1 baduga baduga 20059301 Dec 18 07:10 access.log
-rw-rw-r--. 1 baduga baduga      468 Dec 18 07:11 application.yml
-rw-rw-r--. 1 baduga baduga 23747948 Dec 18 07:10 parser.jar
-rwxrwxr-x. 1 baduga baduga      122 Dec 18 07:11 run.sh
```
inside file run.sh the command became the following:
```
$ java -jar "parser.jar" --accesslog=access.log --startDate=2017-01-01.00:00:11 --duration=hourly --threshold=200
```
the main class was removed from command line since the spring boot app takes care of it.
