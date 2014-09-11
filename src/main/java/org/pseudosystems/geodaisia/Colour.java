package org.pseudosystems.geodaisia;

import org.jscience.mathematics.number.Rational;

import static org.pseudosystems.geodaisia.Numbers.isLessThan;
import static org.pseudosystems.geodaisia.Numbers.isLessThanOrEqualTo;
import static org.pseudosystems.geodaisia.Numbers.mod;
import static org.pseudosystems.geodaisia.Numbers.toInt;

public class Colour {

    private final int    rgb;
    private String        rgbstr;

    private boolean inv () {
        return (0xff000000 & rgb) == 0;
    }

    private Colour (final int rgb) {
        this.rgb = rgb;
        assert(inv());
    }

    public String toRgbString () {
        if (rgbstr == null)
            rgbstr = String.format("%06x", rgb);
        return rgbstr;
    }

    public static Colour rgb (final int rgb) {
        if (rgb < 0 || rgb > 0xffffff)
            throw new IllegalArgumentException(String.format("rgb(%#06x) is not between 0x000000 and 0xffffff", rgb));
        return new Colour(rgb);
    }

    public static Colour rgb (final int r, final int g, final int b) {
        if (r > 0xff || r < 0 || g > 0xff || g < 0 || b > 0xff || b < 0)
            throw new IllegalArgumentException(String.format("r(%#02x) or g(%#02x) or b(%#02x) not in [0,0xff]", r, g, b));

        return new Colour((r << 16) | (g << 8) | b);
    }

    public static int rgbUnitToByte (final Rational u) {
        if (!(isLessThanOrEqualTo(Rational.ZERO, u) && isLessThanOrEqualTo(u, Rational.ONE)))
            throw new IllegalArgumentException(String.format("u(%s) is not a unit (0<=u<=1)", u.toString()));
        return toInt(u.times(0xffl).round());
    }

    public static Colour rgb (final Rational r, final Rational g, final Rational b) {
        return rgb(rgbUnitToByte(r), rgbUnitToByte(g), rgbUnitToByte(b));
    }

    public static Colour hsv (final int hdegrees, final int spercent, final int vpercent) {
        if (hdegrees < 0 || hdegrees > 360 || spercent < 0 || spercent > 100 || vpercent < 0 | vpercent > 100)
            throw new IllegalArgumentException(String.format("hue(%d) not in [0,360] or saturation(%d) not in [0,100] or value(%d) not in [0,100]", hdegrees, spercent, vpercent));
        final Rational  s       = Rational.valueOf(spercent, 100),
                        v       = Rational.valueOf(vpercent, 100),
                        chroma  = v.times(s),
                        hpart   = Rational.valueOf(hdegrees, 60l),
                        x       = mod(hpart, 2l).minus(Rational.ONE).abs().opposite().plus(Rational.ONE).times(chroma),
                        m       = v.minus(chroma),
                        r, g, b;

        assert(isLessThanOrEqualTo(Rational.ZERO, hpart) && isLessThanOrEqualTo(hpart, 6l));

        if (isLessThanOrEqualTo(Rational.ZERO, hpart) && hpart.isLessThan(Rational.ONE)) {
            // (c, x, 0)
            r = chroma;
            g = x;
            b = Rational.ZERO;
        }
        else
        if (isLessThanOrEqualTo(Rational.ONE, hpart) && isLessThan(hpart, 2l)) {
            // (x, c, 0)
            r = x;
            g = chroma;
            b = Rational.ZERO;
        }
        else
        if (isLessThanOrEqualTo(2l, hpart) && isLessThan(hpart, 3l)) {
            // (0, c, x)
            r = Rational.ZERO;
            g = chroma;
            b = x;
        }
        else
        if (isLessThanOrEqualTo(3l, hpart) && isLessThan(hpart, 4l)) {
            // (0, x, c)
            r = Rational.ZERO;
            g = x;
            b = chroma;
        }
        else
        if (isLessThanOrEqualTo(4l, hpart) && isLessThan(hpart, 5l)) {
            // (x, 0, c)
            r = x;
            g = Rational.ZERO;
            b = chroma;
        }
        else
        if (isLessThanOrEqualTo(5l, hpart) && isLessThan(hpart, 6l)) {
            // (c, 0, x)
            r = chroma;
            g = Rational.ZERO;
            b = x;
        }
        else
            throw new AssertionError();

        return rgb(r.plus(m), g.plus(m), b.plus(m));
    }

}
