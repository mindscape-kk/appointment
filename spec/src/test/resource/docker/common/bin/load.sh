#!/bin/bash

mysql -uroot -puzabase --default-character-set=utf8 < ../data/init_db.sql

for directory in `\find ../data -mindepth 1 -maxdepth 1 -type d`; do
    echo "$directory"

    for doc in `\find $directory -name '*.sql' -mindepth 1 -maxdepth 1 -type f`; do
        CMD="mysql -usystem -psystem --default-character-set=utf8 < $doc"
        echo $CMD
        eval $CMD
        echo ""
    done
done
