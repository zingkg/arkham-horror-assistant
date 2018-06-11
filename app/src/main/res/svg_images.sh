#!/bin/sh
# This script is meant to convert svg files to the binary (raster) images for Android to utilize.
# This is meant to be run if the files are missing, and the binary files are never to be committed.

PARAMS='-density 1000 -background none'
DONE_SVG=ic_done_white_24px.svg
DONE_ICON=ic_done_white_24dp.png
DRAWABLE_MDPI='drawable-mdpi'
DRAWABLE_HDPI='drawable-hdpi'
DRAWABLE_XHDPI='drawable-xhdpi'
DRAWABLE_XXHDPI='drawable-xxhdpi'
DRAWABLE_XXXHDPI='drawable-xxxhdpi'
mkdir $DRAWABLE_MDPI/ $DRAWABLE_HDPI/ $DRAWABLE_XHDPI/ $DRAWABLE_XXHDPI/ $DRAWABLE_XXXHDPI/
magick convert $PARAMS -resize 24x24 svg-images/$DONE_SVG $DRAWABLE_MDPI/$DONE_ICON
magick convert $PARAMS -resize 36x36 svg-images/$DONE_SVG $DRAWABLE_HDPI/$DONE_ICON
magick convert $PARAMS -resize 48x48 svg-images/$DONE_SVG $DRAWABLE_XHDPI/$DONE_ICON
magick convert $PARAMS -resize 72x72 svg-images/$DONE_SVG $DRAWABLE_XXHDPI/$DONE_ICON
magick convert $PARAMS -resize 96x96 svg-images/$DONE_SVG $DRAWABLE_XXXHDPI/$DONE_ICON

ELDER_SIGN=elder_sign.svg
LAUNCHER_ICON=ic_launcher.png
MIPMAP_MDPI='mipmap-mdpi'
MIPMAP_HDPI='mipmap-hdpi'
MIPMAP_XHDPI='mipmap-xhdpi'
MIPMAP_XXHDPI='mipmap-xxhdpi'
mkdir $MIPMAP_MDPI/ $MIPMAP_HDPI/ $MIPMAP_XHDPI/ $MIPMAP_XXHDPI/
magick convert $PARAMS -resize 48x48 svg-images/$ELDER_SIGN $MIPMAP_MDPI/$LAUNCHER_ICON
magick convert $PARAMS -resize 72x72 svg-images/$ELDER_SIGN $MIPMAP_HDPI/$LAUNCHER_ICON
magick convert $PARAMS -resize 96x96 svg-images/$ELDER_SIGN $MIPMAP_XHDPI/$LAUNCHER_ICON
magick convert $PARAMS -resize 144x144 svg-images/$ELDER_SIGN $MIPMAP_XXHDPI/$LAUNCHER_ICON
