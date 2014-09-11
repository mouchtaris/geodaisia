#!/bin/sh

find . -iname '*.java' -print0 \
| xargs -0 sed -rn -e '/^.*org\.jscience\.(((\w+)\.?)+).*$/{s//\1/g;s/\s+//g;p}' \
| sort \
| uniq
