package org.pseudosystems.geodaisia;

import org.jscience.mathematics.number.LargeInteger;

public class DeclaredPoint extends Point<LargeInteger> {

    private DeclaredPoint (final LargeInteger x, final LargeInteger y, final LargeInteger z, final int j, final int i, final int n) {
        super(x, y, z, j, i, n);
    }

    public static DeclaredPoint create (long x, long y, long z, int j, int i, int n) {
        return DeclaredPoint.create(LargeInteger.valueOf(x), LargeInteger.valueOf(y), LargeInteger.valueOf(z), j, i, n);
    }

    public static DeclaredPoint create (long x, long y, long z) {
        return DeclaredPoint.create(LargeInteger.valueOf(x), LargeInteger.valueOf(y), LargeInteger.valueOf(z));
    }

    public static DeclaredPoint create (LargeInteger x, LargeInteger y, LargeInteger z) {
        return DeclaredPoint.create(x, y, z, -1, -1, -1);
    }

    public static DeclaredPoint create (LargeInteger x, LargeInteger y, LargeInteger z, int j, int i, int n) {
        return new DeclaredPoint(x, y, z, j, i, n);
    }

}
