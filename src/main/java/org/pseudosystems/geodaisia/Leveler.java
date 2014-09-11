package org.pseudosystems.geodaisia;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jscience.mathematics.number.Rational;

import static org.pseudosystems.geodaisia.Points.*;
import static org.pseudosystems.geodaisia.Numbers.*;
import static org.pseudosystems.geodaisia.PathExt.*;

public class Leveler {

    private static class LevelWorld {
        public final Set<Path<GeneratedPoint>> paths = new HashSet<>(5);
        public final List<Line> lines = new LinkedList<>();
    }

    private static GeneratedPoint populateAndInitialiseMapAndGetHeighestPoint (final Set<? extends GeneratedPoint> points, final Map<? super Rational, LevelWorld> map) {
        GeneratedPoint highest = points.iterator().next();
        Rational highestHeight = getHeight(highest);
        assert isRound(highestHeight);
        for (final GeneratedPoint point: points) {
            final Rational pointHeight = getHeight(point);
            assert isRound(pointHeight);

            if (pointHeight.isGreaterThan(highestHeight)) {
                highest = point;
                highestHeight = pointHeight;
            }

            if (!map.containsKey(pointHeight))
                map.put(pointHeight, new LevelWorld());
        }

        // Initialise map with the heightest point
        {
            final Path<GeneratedPoint> p = new Path<>();
            map.get(highestHeight).paths.add(p);

            final Path.Node<GeneratedPoint> n = new Path.Node<>(highest);
            adopt(p, n);
            points.remove(highest);
        }

        return highest;
    }

    private static void connectToPath (final GeneratedPoint point, final Path<GeneratedPoint> path) {
    }

    public static Map<? extends Rational, ? extends Set<? extends Line>> generateLevelLines (final Iterable<? extends GeneratedPoint> pointsSource) {
        final Map<Rational, LevelWorld> map = new HashMap<>(20);
        final Set<GeneratedPoint> points = new HashSet<>(Collections.toCollection(pointsSource));

        // Find highest point -- point addition will begin from there.
        // In the same iteration, create all levels.
        final GeneratedPoint highest = populateAndInitialiseMapAndGetHeighestPoint(points, map);


        for (final GeneratedPoint point: points) {
            final Rational level = getHeight(point);
            assert isRound(level);
            final LevelWorld levelWorld = map.get(level);


        }




        return null;

    }

    private Leveler () {
    }

}
