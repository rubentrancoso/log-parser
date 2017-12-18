#!/bin/bash
. MYSQL_PASSWORD
mysql -u root -p$MYSQL_PASSWORD -h 127.0.0.1 < schema.sql
