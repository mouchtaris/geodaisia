# Geodaisia

Geodaisia (Γεωδαισία) is a small utility program which (is supposed to)
figure out and draw equal-height lines (contour lines) on a map. The input for the
program is a list of points, along with their coordinates and their
supposed height. The output is an SVG file which describes/draws these
points, as well as the equal-height lines among them.

This is an experimental/hobby project and the result is not
quite satisfactory yet. Yet, it is well-maintained, and the
architecture is quite good as a starting point.

## Building
After checking out:

    sh bootstrap.sh
    mvn package assembly:single

Maven will report where the generated stand-alone jar file is placed.

One can then run this app as with any jar file, for instance:

    java -jar geodaisia.jar points.txt -o map.svg.gz

Note that the default maven dependency repository is not used because
there appears to be some java-version incompatibility between the
jars in the repos and the latest jdk1.8. If someone feels like trying
it out, the dependencies are still in the pom file, but commented out.

## Input format
The program input is a simple text file, in which every line is an entry.

Lines starting with `%` are comment lines and they are ignored.

Lines starting with `$` are adjustment lines. Adjustment lines apply some kind of
transformation to every point entry found later in the file. Adjustment lines
are accumulative, which means that they are all applied to point entry lines,
in order of appearance.

Every other line is a point entry line.

### Point specification concepts
Points are considered to be laid out on a grid, where each point has
* a column index number,
* a row index number,
* an ordinal number,
* an x-axis coordinate (horizontal offset),
* an y-axis coordinate (vertical offset),
* a z-axis coordinate (height).

Column and row indeces are specified by human interpretation of the
way points are laid out in 2D space. The assumption with these indeces
is that given a point all neighbouring points will be "around" it,
or in rows `i-1` and `i+1` and columns `j-1` and `j+1`.

### Point entries
Point-entry lines have the follow format: _i_._j_._n_._x_._y_._z_,
where
* _i_ is column index,
* _j_ is row index,
* _n_ is ordinal number,
* _x_ is the x coordinate,
* _y_ is the y coordinate, and
* _z_ is the height value.

### Adjustment entries
An adjustment entry stacks an "adjustment" to a list of adjustments which
are applied to every following point-entry. Adjustments can only be appended/defined
and they cannot be deleted later. They can be, however, canceled-out by other
adjustments, since all adjustments are simply mathematical operations.

An adjustment entry has the following format: $_coord_\[+-*/\]_num_.

* *$* is the literal $ character,
* _coord_ is the character `x` or `y`,
* +, -, *, / signify addition, subtraction, multiplication and division, respectively, and
* _num_ is a modifier number.

So, for example, after encoutering the line

    $x+160

every following point specification line will have "160"
added to its `x` value.

Since this cannot be undone (although usually there is no reason),
one can simply apply another adjustment which cancels this one out,
if they want to undo the effect of this one:

    $x-160

and so on and so forth.

## Dependencies
This project depends on JScience for some very basic features, like
`LargeNumber` support. The list of jscience usages can be tracked through
the [find_jscience_usages.sh](https://github.com/mouchtaris/geodaisia/blob/master/scripts/find_jscience_usages.sh)
script.

## Sample output
Check the project website for a sample output.

The output is a gzip compressed SVG file, which usually
is not handled very well by Firefox (this is actually a
know [bug](https://bugzilla.mozilla.org/show_bug.cgi?id=52282)).
If it is not too much trouble, one can simply gunzip it
manually first.

## Maintainer
This project is written and maintained by Nikos Mouchtaris <mouchtaris@gmail.com>.
