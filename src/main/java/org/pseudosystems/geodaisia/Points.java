package org.pseudosystems.geodaisia;

import java.math.BigInteger;
import java.util.Comparator;
import org.jscience.mathematics.number.LargeInteger;
import org.jscience.mathematics.number.Rational;
import org.jscience.mathematics.number.Real;

import static org.pseudosystems.geodaisia.Constants.minusOne;
import static org.pseudosystems.geodaisia.Numbers.toReal;
import static org.pseudosystems.geodaisia.Numbers.isGreaterThanOrEqualTo;
import static org.jscience.mathematics.number.LargeInteger.ONE;

public class Points {

    public static boolean isPointValid (final DeclaredPoint p) {
        return    p.getX().isGreaterThan(minusOne) && p.getY().isGreaterThan(minusOne);
    }

    public static boolean isPointValid (final GeneratedPoint p) {
        return isGreaterThanOrEqualTo(p.getX(), Rational.ZERO) && isGreaterThanOrEqualTo(p.getY(), Rational.ZERO);
    }

    public static DeclaredPoint difference (final DeclaredPoint p1, final DeclaredPoint p2) {
        return DeclaredPoint.create(
                p2.getX().minus(p1.getX()),
                p2.getY().minus(p1.getY()),
                p2.getZ().minus(p1.getZ()));
    }

    public static GeneratedPoint difference (final GeneratedPoint p1, final GeneratedPoint p2) {
        return GeneratedPoint.create(
                p2.getX().minus(p1.getX()),
                p2.getY().minus(p1.getY()),
                p2.getZ().minus(p1.getZ()));
    }

    public static Real magnitude (final DeclaredPoint p) {
        return Real.valueOf(p.getX().pow(2).plus(p.getY().pow(2)).plus(p.getZ().pow(2)), 0, 0).sqrt();
    }

    public static Real magnitude (final GeneratedPoint p) {
        Real result;
        final Rational x = p.getX(), y = p.getY(), z = p.getZ();
        if (x.getDivisor().equals(ONE) && y.getDivisor().equals(ONE) && z.getDivisor().equals(ONE)) {
            assert(x.getDividend().equals(x.round()));
            assert(y.getDividend().equals(y.round()));
            assert(z.getDividend().equals(z.round()));
            result = Real.valueOf(x.getDividend().pow(2).plus(y.getDividend().pow(2)).plus(z.getDividend().pow(2)), 0, 0).sqrt();
        }
        else
            result = toReal(x.pow(2).plus(y.pow(2)).plus(z.pow(2))).sqrt();

        return result;
    }

    public static Real distance (final DeclaredPoint p1, final DeclaredPoint p2) {
        final Real result = magnitude(difference(p1, p2));
        assert result.equals(magnitude(difference(p2, p1)));
        return result;
    }

    public static Real distance (final GeneratedPoint p1, final GeneratedPoint p2) {
        final Real result = magnitude(difference(p1, p2));
        assert result.equals(magnitude(difference(p2, p1)));
        return result;
    }

    public static DeclaredPoint lessZ (final DeclaredPoint p1, final DeclaredPoint p2) {
        DeclaredPoint result;
        final LargeInteger z1 = p1.getZ(), z2 = p2.getZ();

        if (z1.isLessThan(z2))
            result = p1;
        else
        if (z2.isLessThan(z1))
            result = p2;
        else {
            assert(z1.equals(z2) && z1.compareTo(z2) == 0);
            result = p1;
        }

        assert(result != null);
        return result;
    }

    public static DeclaredPoint maxZ (final DeclaredPoint p1, final DeclaredPoint p2) {
        DeclaredPoint result = lessZ(p1, p2);

        if (result == p1)
            result = p2;
        else {
            assert(result == p2);
            result = p1;
        }

        return result;
    }

    public static GeneratedPoint lessZ (final GeneratedPoint p1, final GeneratedPoint p2) {
        GeneratedPoint result;
        final Rational z1 = p1.getZ(), z2 = p2.getZ();

        if (z1.isLessThan(z2))
            result = p1;
        else
        if (z2.isLessThan(z1))
            result = p2;
        else {
            assert(z1.equals(z2) && z1.compareTo(z2) == 0);
            result = p1;
        }

        assert(result != null);
        return result;
    }

    public static GeneratedPoint maxZ (final GeneratedPoint p1, final GeneratedPoint p2) {
        GeneratedPoint result = lessZ(p1, p2);

        if (result == p1)
            result = p2;
        else {
            assert(result == p2);
            result = p1;
        }

        return result;
    }

    /**
     * Assuming that u<sub>p1-&gt;p2</sub> is wanted, and that p1 and p2 do not
     * share the same height.
     * @param p1
     * @param p2
     * @param height
     * @return
     */
    public static Rational heightRatio (final DeclaredPoint p1, final DeclaredPoint p2, final LargeInteger height) {
        final LargeInteger a = p1.getZ();
        final LargeInteger b = p2.getZ();

        return Numbers.ratio(a, b, height);
    }

    public static GeneratedPoint interpolate (final DeclaredPoint p1, final DeclaredPoint p2, final Rational u) {
        return GeneratedPoint.create(
                Numbers.interpolate(p1.getX(), p2.getX(), u),
                Numbers.interpolate(p1.getY(), p2.getY(), u),
                Numbers.interpolate(p1.getZ(), p2.getZ(), u)
                );
    }

    public static class ZPointComparator implements Comparator<GeneratedPoint> {
        @Override
        public int compare (final GeneratedPoint p0, final GeneratedPoint p1) {
            return p0.getZ().compareTo(p1.getZ());
        }
    }

    public static GeneratedPoint findPointWithZ (final Iterable<? extends GeneratedPoint> points, final Rational z) {
        for (final GeneratedPoint p: points)
            if (p.getZ().equals(z))
                return p;
        return null;
    }

    public static GeneratedPoint findClosest (final Iterable<? extends GeneratedPoint> points, final GeneratedPoint p) {
        GeneratedPoint closest = points.iterator().next();
        Real mindist = distance(p, closest);

        for (final GeneratedPoint other: points)
            if (other != p) {
                final Real dist = distance(p, other);
                if (dist.isLessThan(mindist)) {
                    mindist = dist;
                    closest = other;
                }
            }

        return closest;
    }

    public static class LineDepthComparator implements Comparator<Line> {
        private final ZPointComparator comp = new ZPointComparator();
        @Override
        public int compare (final Line line0, final Line line1) {
            if (!(line0.isLeveled() && line1.isLeveled()))
                throw new IllegalArgumentException(String.format("line0(%s) or line1(%s) are not leveled", line0.toString(), line1.toString()));
            return comp.compare(line0.getBeginning(), line1.getBeginning());
        }
    }

    public static Rational getHeight (final Line l) {
        if (!l.isLeveled())
            throw new IllegalArgumentException("Line " + l + " is not leveled");
        return getHeight(l.getBeginning());
    }

    public static Rational getHeight (final GeneratedPoint p) {
        return p.getZ();
    }

    public interface HeightGetter<T> {
        Rational getHeight (T o);
    }

    public static class LineHeightGetter implements HeightGetter<Line> {
        @Override
        public Rational getHeight (final Line line) {
            return Points.getHeight(line);
        }
    }

    public static class GeneratedPointHeightGetter implements HeightGetter<GeneratedPoint> {
        @Override
        public Rational getHeight (final GeneratedPoint p) {
            return Points.getHeight(p);
        }
    }

    // Newton Raphson - shift
    private static BigInteger nrsqrt (BigInteger n) {
        // Uses the fact that the square root, any base, usually has 1/2
        // the digits of the number to take root of to get a close first
        // guess. NR converges fast when close to the actual value.
        //
        // If I used the number itself instead of the TWO I started with
        // it would of probably been better since I saw somewhere that
        // initial values close to a minimum can cause it to not converge.
        //
        // All of this was from various mail forum posts I googled.
        // It seems correct and despite not eliminating the one divide it
        // seems fast enough for a BigInteger squareroot.
        //
        // At least for now.
        //
        BigInteger t = n.shiftRight(BigInteger.valueOf(n.bitLength()).shiftRight(1).intValue()),
                result = null;
        for (;;) {
            result = t.add(n.divide(t)).shiftRight(1);
            if (t.compareTo(result) == 0) {
                break;
            }
            t = result;
        }
        return result;
    }
}
