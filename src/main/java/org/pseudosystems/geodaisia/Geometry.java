package org.pseudosystems.geodaisia;

import java.util.Collection;
import java.util.Iterator;
import org.jscience.mathematics.number.Rational;
import org.jscience.mathematics.number.Real;

import static org.pseudosystems.geodaisia.Constants.half;
import static org.pseudosystems.geodaisia.Numbers.det;
import static org.pseudosystems.geodaisia.Points.distance;

public class Geometry {

    private Geometry () {
    }

    private static Rational processPair (final Collection<Real> sides, final GeneratedPoint p0, final GeneratedPoint p1) {
        final Real d = distance(p0, p1);
        final Rational a = det(p0, p1);
        System.out.println("[" + p0.toCoordString() + "," + p1.toCoordString() + "]: A=" + half.times(a) + " d=" + d);
        sides.add(d);
        return a;
    }

    public static Rational area (final Collection<Real> sides, final Iterable<? extends GeneratedPoint> points) {
        final Iterator<? extends GeneratedPoint> i = points.iterator();
        GeneratedPoint prev = i.next(), curr = i.next(), last = curr;
        final GeneratedPoint first = prev;

        Rational area = processPair(sides, prev, curr);
        while (i.hasNext()) {
            prev = curr;
            curr = i.next();
            last = curr;

            area = area.plus(processPair(sides, prev, curr));
        }

        area = area.plus(processPair(sides, last, first));

        return half.times(area);
    }

//    public static boolean intersect (final Line l1, final Line l2) {
//
//    }

}
