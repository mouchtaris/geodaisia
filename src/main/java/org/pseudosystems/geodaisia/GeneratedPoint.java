package org.pseudosystems.geodaisia;

import org.jscience.mathematics.number.Rational;

public class GeneratedPoint extends Point<Rational> {

    private GeneratedPoint (Rational x, Rational y, Rational z, int j, int i, int n) {
        super(x, y, z, j, i, n);
    }

    public static GeneratedPoint create (long x, long y, long z, int j, int i, int n) {
        return GeneratedPoint.create(Rational.valueOf(x, 1l), Rational.valueOf(y, 1l), Rational.valueOf(z, 1l), j, i, n);
    }

    public static GeneratedPoint create (long x, long y, long z) {
        return GeneratedPoint.create(Rational.valueOf(x, 1l), Rational.valueOf(y, 1l), Rational.valueOf(z, 1l));
    }

    public static GeneratedPoint create (Rational x, Rational y, Rational z) {
        return GeneratedPoint.create(x, y, z, -1, -1, -1);
    }

    public static GeneratedPoint create (Rational x, Rational y, Rational z, int j, int i, int n) {
        return new GeneratedPoint(x, y, z, j, i, n);
    }
}
