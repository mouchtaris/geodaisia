package org.pseudosystems.geodaisia;

import org.jscience.mathematics.number.LargeInteger;
import org.jscience.mathematics.number.Rational;
import org.jscience.mathematics.number.Real;

import static org.pseudosystems.geodaisia.Constants.comparisonAccuracy;
import static org.pseudosystems.geodaisia.Constants.maxLong;
import static org.pseudosystems.geodaisia.Constants.minLong;

public class Numbers {

    public static long toLong (final LargeInteger li) {
        if (li.isLessThan(minLong) || li.isGreaterThan(maxLong))
            throw new IllegalArgumentException("Cannot be converted: not Long.MIN_VALUE < " + li + " < " + Long.MAX_VALUE);
        return li.longValue();
    }

    public static boolean isGreaterThanOrEqualTo (final LargeInteger a, final LargeInteger b) {
        return a.isGreaterThan(b) || a.equals(b);
    }

    public static boolean isLessThanOrEqualTo (final Rational r, final long v) {
        return isLessThanOrEqualTo(r, toRational(v));
    }

    public static boolean isLessThanOrEqualTo (final Rational r1, final Rational r2) {
        return r1.isLessThan(r2) || r1.equals(r2);
    }

    public static boolean isLessThanOrEqualTo (final LargeInteger r1, final LargeInteger r2) {
        return r1.isLessThan(r2) || r1.equals(r2);
    }

    public static void ensureBetweenZeroAndOne (final Rational u) {
        if (!isBetweenZeroAndOne(u))
            throw new IllegalArgumentException(u + " is not between 0 and 1");
    }

    public static Rational complement (final Rational r) {
        ensureBetweenZeroAndOne(r);

        final LargeInteger divisor = r.getDivisor();
        final LargeInteger dividend = r.getDividend();

        assert(dividend.isLessThan(divisor));

        final Rational result = Rational.valueOf(divisor.minus(dividend), divisor);
        assert(r.plus(result).equals(Rational.ONE));

        return result;
    }

    public static Rational toRational (final LargeInteger n) {
        return Rational.valueOf(n, LargeInteger.ONE);
    }

    public static Rational toRational (final long n) {
        return Rational.valueOf(n, 1l);
    }

    public static boolean isBetweenZeroAndOne (final Rational u) {
        return isLessThanOrEqualTo(u, 1l) && isLessThanOrEqualTo(Rational.ZERO, u);
    }

    public static Rational interpolate (final LargeInteger a, final LargeInteger b, final Rational u) {
        ensureBetweenZeroAndOne(u);
        return complement(u).times(toRational(a)).plus(u.times(toRational(b)));
    }

    public static Rational ratio (final LargeInteger a, final LargeInteger b, final LargeInteger v) {
        if (a.equals(b))
            throw new IllegalArgumentException(a + " equals " + b);
        if (!(isLessThanOrEqualTo(a, v) && isLessThanOrEqualTo(v, b)))
            throw new IllegalArgumentException(v + " is not between " + a + " and " + b);

        return Rational.valueOf(v.minus(a), b.minus(a));
    }

    public static Real toReal (final Rational r) {
        return toReal(r.getDividend()).divide(toReal(r.getDivisor()));
    }

    public static Rational plus (final Rational r, final long n) {
        final Rational result = Rational.valueOf(r.getDividend().plus(r.getDivisor().times(n)), r.getDivisor());
        assert(result.equals(r.plus(toRational(n))));
        return result;
    }

    public static Real toReal (final LargeInteger li) {
        return Real.valueOf(li, 0, 0);
    }

    public static boolean isEqualTo (final Real r, final LargeInteger li) {
        final boolean result = r.round().equals(li) && r.minus(toReal(li)).abs().isLessThan(comparisonAccuracy);
        assert(!result || li.equals(r.round()) && toReal(li).minus(r).abs().isLessThan(comparisonAccuracy));
        assert(result ||!(li.equals(r.round()) && toReal(li).minus(r).abs().isLessThan(comparisonAccuracy)));
        return result;
    }

    public static boolean isRound (final Rational r) {
        return r.getDivisor().equals(LargeInteger.ONE);
    }

    public static LargeInteger toLargeInt (final Rational r) {
        if (!isRound(r))
            throw new IllegalArgumentException(r + " is not round");
        return r.getDividend();
    }

    public static Rational mod (final Rational r, final long m) {
        if (r.isNegative() || m <= 0)
            throw new IllegalArgumentException(String.format("%s is subzero or m is <= 0", r.toString(), m));

        final Rational mr = toRational(m);
        Rational result = r;

        while (result.isGreaterThan(mr))
            result = result.minus(mr);

        return result;
    }

    public static Rational minus (final Rational r, final long n) {
        return plus(r, -n);
    }

    public static Rational minus (final Rational r0, final Rational r1) {
        return r0.minus(r1);
    }

    public static LargeInteger minus (final LargeInteger li0, final LargeInteger li1) {
        return li0.minus(li1);
    }

    public static boolean isLessThan (final Rational r, final long n) {
        return r.isLessThan(toRational(n));
    }

    public static boolean isLessThanOrEqualTo (final long n, final Rational r) {
        return isGreaterThanOrEqualTo(r, n);
    }

    public static boolean isGreaterThanOrEqualTo (final Rational r, final long n) {
        return isGreaterThan(r, n) || isEqualTo(r, n);
    }

    public static boolean isGreaterThan (final Rational r, final long n) {
        return r.isGreaterThan(toRational(n));
    }

    public static boolean isEqualTo (final Rational r, final long n) {
        return r.equals(toRational(n));
    }

    public static int toInt (final Rational r) {
        return toInt(toLargeInt(r));
    }

    public static int toInt (final LargeInteger li) {
        return toInt(li.longValue());
    }

    public static int toInt (final long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE)
            throw new IllegalArgumentException(String.format("%d is not between %d and %d", l, Integer.MIN_VALUE, Integer.MAX_VALUE));
        return (int) l;
    }

    public static boolean isGreaterThanOrEqualTo (final Rational r0, final Rational r1) {
        return r0.isGreaterThan(r1) || r0.equals(r1);
    }

    public static boolean isLessThanOrEqualTo (final Real r, final long l) {
        return isLessThanOrEqualTo(r, toReal(l));
    }

    public static Real toReal (final long l) {
        return Real.valueOf(l);
    }

    public static boolean isLessThanOrEqualTo (final Real r0, final Real r1) {
        return r0.isLessThan(r1) || r0.equals(r1);
    }

    public static Rational distance (final Rational r0, final Rational r1) {
        return r0.minus(r1).abs();
    }

    public static boolean isLessThan (final LargeInteger li, final long l) {
        return li.isLessThan(toLargeInt(l));
    }

    public static LargeInteger toLargeInt (final long l) {
        return LargeInteger.valueOf(l);
    }

    public static boolean isGreaterThan (final LargeInteger li, final long l) {
        return li.isGreaterThan(toLargeInt(l));
    }

    public static boolean isGreaterThan (final LargeInteger li0, final LargeInteger li1) {
        return li0.isGreaterThan(li1);
    }

    public static Rational min (final Rational r0, final Rational r1) {
        return r0.isLessThan(r1)? r0 : r1;
    }

    public static Rational min (final Rational r0, final long l) {
        return min(r0, toRational(l));
    }

    public static Rational max (final Rational r0, final Rational r1) {
        return r0.isGreaterThan(r1)? r0 : r1;
    }

    public static Rational max (final Rational r0, final long l) {
        return max(r0, toRational(l));
    }

    public static Rational times (final Rational r, final LargeInteger li) {
        return r.times(toRational(li));
    }

    public static Rational times (final Rational r0, final Rational r1) {
        return r0.times(r1);
    }

    /**
     * <pre>
     * | x0 x1 |
     * | y0 y1 |
     * </pre>
     * = {@code [x0*y1 - y0*x1]}
     * @param p0
     * @param p1
     * @return
     */
    public static Rational det (final GeneratedPoint p0, final GeneratedPoint p1) {
        return p0.getX().times(p1.getY()).minus(p0.getY().times(p1.getX()));
    }

}
