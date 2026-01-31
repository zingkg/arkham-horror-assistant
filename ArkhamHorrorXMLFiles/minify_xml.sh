#!/bin/bash

OTHER_DIR=ArkhamHorrorXMLFiles/other/no_formatting
OUTPUT_DIR=minified_files/
for f in $OTHER_DIR/*.xml
do
    FILE_NAME=`echo $f | cut -d "/" -f 4`
    python3 minify_xml.py $OTHER_DIR/$FILE_NAME > $OUTPUT_DIR/$FILE_NAME
done
