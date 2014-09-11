package org.pseudosystems.geodaisia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.jscience.mathematics.number.LargeInteger;
import org.jscience.mathematics.number.Rational;

import static org.pseudosystems.geodaisia.Constants.ten;
import static org.pseudosystems.geodaisia.Numbers.isGreaterThanOrEqualTo;
import static org.pseudosystems.geodaisia.Numbers.isLessThanOrEqualTo;
import static org.pseudosystems.geodaisia.Numbers.isRound;
import static org.pseudosystems.geodaisia.Numbers.toInt;
import static org.pseudosystems.geodaisia.Numbers.toRational;
import static org.pseudosystems.geodaisia.Numbers.toLargeInt;
import static org.pseudosystems.geodaisia.Numbers.minus;
import static org.pseudosystems.geodaisia.Points.distance;
import static org.pseudosystems.geodaisia.Points.lessZ;
import static org.pseudosystems.geodaisia.Points.maxZ;
import static org.jscience.mathematics.number.LargeInteger.ZERO;

public class IntermediatePointsGenerator {

    public static LargeInteger nextHeightmark (final LargeInteger currentHeightmark) {
        // find remaining distance to next round value
        final LargeInteger remains = currentHeightmark.mod(ten);
        assert(isGreaterThanOrEqualTo(remains, ZERO));

        final LargeInteger stepToNext = ten.minus(remains);
        assert(isLessThanOrEqualTo(stepToNext, ten));
        assert(isGreaterThanOrEqualTo(stepToNext, ZERO));

        final LargeInteger next = currentHeightmark.plus(stepToNext);
        assert(next.mod(ten).equals(ZERO));
        return next;
    }

    public static int generateAllIntermediateHeightPointsBetween (final List<? super GeneratedPoint> into, final DeclaredPoint p1, final DeclaredPoint p2) {
        int result = 0;

        if (p1.getZ().equals(p2.getZ()))
            // our work here is done
            {}
        else {
            final DeclaredPoint first = lessZ(p1, p2), last = maxZ(p1, p2);
            final LargeInteger zfirst = first.getZ(), zlast = last.getZ();

            for (LargeInteger height = zfirst; height.isLessThan(zlast); height = nextHeightmark(height)) {
                GeneratedPoint interpolated = Points.interpolate(first, last, Points.heightRatio(first, last, height));
                assert(isRound(interpolated.getZ()) && interpolated.getZ().getDividend().equals(height));
                into.add(interpolated);
                ++result;
            }
        }

        return result;
    }

    private static DeclaredPoint getPointIfValid (final List<? extends List<? extends DeclaredPoint>> points, final int width, final int height, final int j, final int i) {
        DeclaredPoint result = null;

        assert(width > 1 && height > 1 && points.size() == width && points.get(0).size() == height);

        if (j >= 0 && i >= 0 && j < width && i < height) {
            final DeclaredPoint p = points.get(j).get(i);
            if (Points.isPointValid(p))
                result = p;
        }

        return result;
    }

    public static List<? extends List<? extends List<? extends GeneratedPoint>>> generateAllIntermediateHeightPoints (final List<? extends List<? extends DeclaredPoint>> points) {
        final int width = points.size();
        assert(width > 1);
        final int height = points.get(0).size();
        assert(height > 1);

        final LinkedList<ArrayList<LinkedList<GeneratedPoint>>> result = new LinkedList<>();

        for (int j = 0; j < width - 1; ++j)
            for (int i = 0; i < height - 1; ++i) {
                assert(points.get(j).size() == height);
                final ArrayList<LinkedList<GeneratedPoint>> quartet = new ArrayList<>(5);
                result.add(quartet);

                final DeclaredPoint
                        dealer0 = getPointIfValid(points, width, height, j, i),
                        dealer1 = getPointIfValid(points, width, height, j + 1, i),
                        dealer2 = getPointIfValid(points, width, height, j + 1, i + 1),
                        dealer3 = getPointIfValid(points, width, height, j, i + 1);
                final DeclaredPoint dealers[] = { dealer0, dealer1, dealer2, dealer3 };

                assert(dealers.length == 4);

                for (int k = 0; k < 4; ++k) {
                    final DeclaredPoint p0 = dealers[k];
                    final DeclaredPoint p1 = dealers[(k +1 ) % 4];

                    if (p0 != null && p1 != null) {
                        final LinkedList<GeneratedPoint> intermediates = new LinkedList<>();
                        quartet.add(intermediates);
                        assert(quartet.get(k) == intermediates);
                        final int added = generateAllIntermediateHeightPointsBetween(intermediates, p0, p1);
                        assert(intermediates.size() == added);
                        assert(Collections.isSorted(intermediates, new Points.ZPointComparator()));
                    }
                    else
                        quartet.add(null);
                }

                assert(quartet.size() == 4);

                final LargeInteger diffs[] = {
                    dealer0 != null && dealer2 != null? minus(dealer0.getZ(), dealer2.getZ()) : toLargeInt(0l),
                    dealer1 != null && dealer3 != null? minus(dealer1.getZ(), dealer3.getZ()) : toLargeInt(0l)
                };
                boolean added = false;
                for (int k = 0; !added && k < 2; ++k) {
                    if (isGreaterThanOrEqualTo(diffs[k], diffs[(k + 1) % 2]) && dealers[k] != null && dealers[k + 2] != null) {
                        final LinkedList<GeneratedPoint> intermediates = new LinkedList<>();
                        quartet.add(intermediates);
                        assert(quartet.get(4) == intermediates);
                        final int numadded = generateAllIntermediateHeightPointsBetween(intermediates, dealers[k], dealers[k + 2]);
                        assert(numadded == intermediates.size());
                        assert(Collections.isSorted(intermediates, new Points.ZPointComparator()));
                        added = true;
                    }
                }
            }

        return result;
    }

    public static List<? extends List<? extends List<? extends GeneratedPoint>>> generateAllIntermediateHeightPoints0 (final List<? extends List<? extends DeclaredPoint>> points) {
        final int width = points.size();
        assert(width > 1);
        final int height = points.get(0).size();
        assert(height > 1);

        final LinkedList<ArrayList<LinkedList<GeneratedPoint>>> result = new LinkedList<>();

        for (int j = 0; j < width; ++j)
            for (int i = 0; i < height; ++i) {
                assert(points.get(j).size() == height);
                final DeclaredPoint p = points.get(j).get(i);
                if (Points.isPointValid(p)) {
                    final ArrayList<LinkedList<GeneratedPoint>> octadity = new ArrayList<>(8);
                    result.add(octadity);

                    final DeclaredPoint neighbours[] = {
                        getPointIfValid(points, width, height, j-1, i-1),
                        getPointIfValid(points, width, height, j-1, i),
                        getPointIfValid(points, width, height, j-1, i+1),

                        getPointIfValid(points, width, height, j, i-1),
                        getPointIfValid(points, width, height, j, i+1),

                        getPointIfValid(points, width, height, j+1, i-1),
                        getPointIfValid(points, width, height, j+1, i),
                        getPointIfValid(points, width, height, j+1, i+1),
                    };

                    assert(getPointIfValid(points, width, height, j, i) != null && generateAllIntermediateHeightPointsBetween(new LinkedList<>(), p, getPointIfValid(points, width, height, j, i)) == 0);
                    assert(neighbours.length == 8);

                    for (int k = 0; k < 8; ++k) {
                        final DeclaredPoint neighbour = neighbours[k];
                        if (neighbour != null) {
                            final LinkedList<GeneratedPoint> generatedGroup = new LinkedList<>();
                            octadity.add(generatedGroup);
                            assert(octadity.get(k) == generatedGroup);
                            final int added = generateAllIntermediateHeightPointsBetween(generatedGroup, p, neighbour);
                            assert(generatedGroup.size() == added);
                            assert(Collections.isSorted(generatedGroup, new Points.ZPointComparator()));
                        }
                        else
                            octadity.add(null);
                    }
                }
            }

        return result;
    }

    private static int connectTheLols (final List<? super Line> into, final List<? extends GeneratedPoint> group0, final List<? extends GeneratedPoint> group1) {
        int added = 0;

        for (final GeneratedPoint p: group0) {
            final GeneratedPoint other = Points.findPointWithZ(group1, p.getZ());
            if (other != null) {
                into.add(new Line(p, other));
                ++added;
            }
        }

        return added;
    }

    private static void connectTheGroups (final List<? super Line> into, final List<? extends List<? extends GeneratedPoint>> octadity, final int k0, final int k1) {
        assert(octadity.size() == 8);
        final List<? extends GeneratedPoint> group0 = octadity.get(k0);
        final List<? extends GeneratedPoint> group1 = octadity.get(k1);
        if (group0 != null && group1 != null) {
            final int prevsize = into.size();
            final int added = connectTheLols(into, group0, group1);
            assert(into.size() == prevsize + added);
        }
    }

    public static List<? extends Line> generateHeightLines (final List<? extends List<? extends List<? extends GeneratedPoint>>> generated) {
        final LinkedList<Line> result = new LinkedList<>();

        for (final List<? extends List<? extends GeneratedPoint>> octadity: generated) {
            assert(octadity.size() == 8);
            for (int k = 1; k < 8; ++k)
                connectTheGroups(result, octadity, k-1, k);
            connectTheGroups(result, octadity, 7, 0);
        }

        return result;
    }

    public static Map<? extends Rational, ? extends Set<? extends GeneratedPoint>> makeHeightequalGroups (final Iterable<? extends GeneratedPoint> generated) {
        final Map<Rational, Set<GeneratedPoint>> result = new HashMap<>();

        for (final GeneratedPoint p: generated) {
            Set<GeneratedPoint> heightequals;
            final Rational height = p.getZ();
            if (isRound(height) && !result.containsKey(height)) {
                heightequals = new HashSet<>();
                result.put(height, heightequals);
            }
            else
                heightequals = result.get(height);

            final boolean added = heightequals.add(p);
            assert(added);
        }

        return result;
    }

    private static void connectPointsUnlessTooFarApart (final List<Line> into, final GeneratedPoint p0, final GeneratedPoint p1, final long distanceThreshold) {
        if (isLessThanOrEqualTo(distance(p0, p1), distanceThreshold))
            into.add(new Line(p0, p1));
    }

    public static List<? extends Line> generateHeightLines2 (final List<? extends List<? extends List<? extends GeneratedPoint>>> generatedGroups, final long distanceThreshold)
        { return generateHeightLines2(generatedGroups, distanceThreshold, -1l); }
    public static List<? extends Line> generateHeightLines2 (final List<? extends List<? extends List<? extends GeneratedPoint>>> generatedGroups, final long distanceThreshold, final long forHeight) {
        final List<Line>  result = new LinkedList<>();
        final Iterable<? extends GeneratedPoint> generated = Collections.flattenIterables(Collections.flattenIterables(generatedGroups));
        final Map<? extends Rational, ? extends Set<? extends GeneratedPoint>> groups = makeHeightequalGroups(generated);

        for (final Map.Entry<? extends Rational, ? extends Set<? extends GeneratedPoint>> groupentry: groups.entrySet()) {
            final LinkedList<GeneratedPoint> path = new LinkedList<>();
            final Rational height = groupentry.getKey();
            if (height.compareTo(Numbers.toRational(forHeight)) == 0 || forHeight == -1) {
                final Set<? extends GeneratedPoint> points = groupentry.getValue();

                assert(!points.isEmpty());
                GeneratedPoint last = points.iterator().next();
                assert(last.getZ().equals(height));
                path.add(last);
                points.remove(last);

                while (!points.isEmpty()) {
                    final GeneratedPoint closest = Points.findClosest(points, last);
                    assert(closest != null);
                    assert(points.contains(closest));
                    assert(closest.getZ().equals(height));
                    final boolean added = path.add(closest);
                    assert(added);
                    final boolean removed = points.remove(closest);
                    assert(removed);
                    last = closest;
                }

                assert(!path.isEmpty());
                final Iterator<GeneratedPoint> iter = path.iterator();
                last = path.iterator().next();
                while (iter.hasNext()) {
                    final GeneratedPoint next = iter.next();
                    connectPointsUnlessTooFarApart(result, last, next, distanceThreshold);
                    last = next;
                }
                connectPointsUnlessTooFarApart(result, path.getFirst(), path.getLast(), distanceThreshold);
            }
        }

        return result;
    }

    public static Map<? super Rational, ? extends Colour> makeColourscales (final SortedSet<? extends Rational> levels) {
        final int numberOfLevels = levels.size();
        final Map<Rational, Colour> colours = new HashMap<>(numberOfLevels);
        final Rational    colourstep    = Rational.valueOf(300l, numberOfLevels),
                        _360        = toRational(360l);
        Rational        hue            = toRational(240l);
        for (final Rational level: levels) {
            colours.put(level, Colour.hsv(toInt(hue.round()), 20, 50));
            hue = hue.minus(colourstep);
            if (hue.isNegative())
                hue = hue.plus(_360);
            assert(hue.isLessThan(_360));
        }

        return colours;
    }

    public static <T> SortedSet<? extends Rational> makeLevels (final Iterable<? extends T> unsortedLeveledThings, final Points.HeightGetter<? super T> toHeight) {
        final SortedSet<Rational> levels = new TreeSet<>();
        for (final T levelable: unsortedLeveledThings) {
            final Rational level = toHeight.getHeight(levelable);
            assert(isRound(level));
            levels.add(level);
        }
        return levels;
    }

    public static <VT, T> List<? extends VT> colourise (final Iterable<? extends T> elements, final Points.HeightGetter<? super T> toHeight, final ColourfulElement.ColourfulElementFactory<T, VT> fac) {
        final SortedSet<? extends Rational> levels = makeLevels(elements, toHeight);
        final Map<? super Rational, ? extends Colour> colours = makeColourscales(levels);

        final List<VT> result = new LinkedList<>();
        for (final T element: elements) {
            final Colour c = colours.get(toHeight.getHeight(element));
            assert(c != null);
            result.add(fac.create(element, c));
        }

        return result;
    }

    public static List<? extends ColourfulLine> colouriseLines (final Iterable<? extends Line> unsortedLines) {
        return colourise(unsortedLines, new Points.LineHeightGetter(), new ColourfulLine.ColourfulLineFactory());
    }

    public static List<? extends ColourfulGeneratedPoint> colourisePoints (final Iterable<? extends GeneratedPoint> unsortedPoints) {
        return colourise(unsortedPoints, new Points.GeneratedPointHeightGetter(), new ColourfulGeneratedPoint.ColourfulGeneratedPointFactory());
    }
}
