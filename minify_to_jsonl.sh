#!/bin/bash

MAIN_DIR=ArkhamHorrorFiles
OUTPUT_DIR=minified_files/
for f in $MAIN_DIR/*.json
do
  FILE_NAME=`echo $f | cut -d "/" -f 2 | cut -d "." -f 1`
  echo $FILE_NAME
  jq -c '.[]' $MAIN_DIR/$FILE_NAME.json > $OUTPUT_DIR/$FILE_NAME.jsonl
done
