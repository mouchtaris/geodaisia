#!/bin/sh

find src -iname '*.java' -print0 \
| xargs -0 bash -c '
  for f
  do
    echo processing $f ....
    sed -r -b -i -e s/\\t/\ \ \ \ /g\;s/\\r//g\;s/\\s+\$//g "$f"
  done
'

