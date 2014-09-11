package org.pseudosystems.geodaisia;

public class Point<C> {

    private final C x, y, z;
    private final int j, i, n;

    public C getX() {
        return x;
    }

    public C getY () {
        return y;
    }

    public C getZ () {
        return z;
    }

    public int getJ () {
        return j;
    }

    public int getI () {
        return i;
    }

    public int getN () {
        return n;
    }

    protected Point (final C x, final C y, final C z, int j, int i, int n) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.j = j;
        this.i = i;
        this.n = n;
    }

    @Override
    public String toString () {
        return "#" + n + '[' + j + ',' + i + ']' + toCoordString();
    }

    public String toCoordString () {
        return "(" + x + ',' + y + ',' + z + ')';
    }
}
