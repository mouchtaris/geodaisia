package org.pseudosystems.geodaisia;

import java.io.IOException;
import java.io.Writer;
import org.jscience.mathematics.number.LargeInteger;
import org.jscience.mathematics.number.Rational;
import org.jscience.mathematics.number.Real;

import static org.pseudosystems.geodaisia.Numbers.isEqualTo;
import static org.pseudosystems.geodaisia.Numbers.plus;
import static org.pseudosystems.geodaisia.Numbers.toLong;
import static org.pseudosystems.geodaisia.Numbers.toReal;
import static org.pseudosystems.geodaisia.Numbers.isLessThan;
import static org.pseudosystems.geodaisia.Numbers.toRational;
import static org.pseudosystems.geodaisia.Numbers.isGreaterThan;
import static org.pseudosystems.geodaisia.Numbers.max;
import static org.pseudosystems.geodaisia.Numbers.times;

public class Renderer {

    private static final long    MARGIN_X = 1l,
                                MARGIN_Y = 1l,
                                TEXT_X_OFF = 2l,
                                TEXT_Y_OFF = -4l;
    public static final String SVG_HEADER = ""
            + "<?xml version=\"1.0\" standalone=\"no\"?>\n"
            + "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \n"
            + "  \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n"
            + "<svg version=\"1.1\"\n"
            + "     xmlns=\"http://www.w3.org/2000/svg\">\n"
            + "    <title> The points of a mountain for the Geodaisia-2 course </title>\n"
            + "    <svg x=\"0cm\" y=\"0cm\">\n"
            + "";
    public static final String SVG_FOOTER = "</svg></svg>";
    private final static Rational errorCorrection = Rational.valueOf(107l, 100l).inverse();

    public static String pointIndexToString (final int i) {
        return i == -1? "?" : Integer.toString(i);
    }

    public static String pointToSvg (final DeclaredPoint p) {
        assert(Points.isPointValid(p));
        final StringBuilder bob0        = new StringBuilder(1024),
                            bob1        = new StringBuilder(1024);
        final LargeInteger  x           = p.getX(),
                            y           = p.getY(),
                            z           = p.getZ();
        final Rational      dispx       = plus(times(errorCorrection, x), MARGIN_X),
                            dispy       = plus(times(errorCorrection, y), MARGIN_Y),
                            textx       = plus(dispx, TEXT_X_OFF),
                            texty       = isLessThan(plus(dispy, TEXT_Y_OFF), 4l)? toRational(4l) : plus(dispy, TEXT_Y_OFF);
        final int           n           = p.getN(),
                            j           = p.getJ(),
                            i           = p.getI();
        final String        xstr        = String.format("%03d", toLong(x)),
                            ystr        = String.format("%03d", toLong(y)),
                            zstr        = String.format("%04d", toLong(z)),
                            dispxstr    = toReal(dispx).toString(),
                            dispystr    = toReal(dispy).toString(),
                            textxstr    = toReal(textx).toString(),
                            textystr    = toReal(texty).toString(),
                            nstr        = String.format("%02d", n),
                            jstr        = String.format("%02d", j),
                            istr        = String.format("%02d", i),
                            anchor      = isLessThan(textx, 5l)? "left" : isGreaterThan(textx, 270l)? "right" : "center";

        assert(n >= -1);
        assert(j >= -1);
        assert(i >= -1);
        assert(j == -1 || i > -1);
        assert(i == -1 || j > -1);

        bob0.append("\n\t<circle cx=\"").append(dispxstr).append("mm\" cy=\"").append(dispystr).append("mm\" r=\"0.5mm\"");

        if (n > -1)
            bob1.append("\n\t<title>point[").append(n).append("]</title>");
        if (j > -1)
            bob1.append("\n\t\t<desc>(").append(j).append(',').append(i).append(")</desc>");

        if (bob1.length() > 0)
            bob0.append('>').append(bob1).append("\n\t</circle>");
        else
            bob0.append("/>");

        if (false)
            bob0.append("\n\t<text text-anchor=\"").append(anchor).append("\" font-family=\"monospace\" font-size=\"8px\" x=\"").append(textxstr).append("mm\" y=\"").append(textystr).append("mm\">")
                    .append("<tspan fill=\"#a05050\">").append(nstr).append(":</tspan>")
                    .append("<tspan fill=\"#5050a0\">[").append(jstr).append(',').append(istr).append("]</tspan>")
                    .append("<tspan dy=\"2ex\" dx=\"-7.5em\" fill=\"#305030\">(").append(xstr).append(',').append(ystr).append(',').append(zstr).append(")</tspan>")
                    .append("\n\t</text>");
        else {
            final Rational    textx0 = isGreaterThan(dispx, 270l)? toRational(270l) : dispx,
                            texty0 = isLessThan(dispy, 4l)? toRational(4l) : dispy;
            final String    textx0str =    toReal(textx0).toString(),
                            texty0str = toReal(texty0).toString();
            bob0.append("\n\t<text text-anchor=\"").append(anchor).append("\" font-family=\"sans-serif\" font-size=\"8px\" x=\"").append(textx0str).append("mm\" y=\"").append(texty0str).append("mm\">")
                    .append("<tspan fill=\"#305030\">").append(zstr).append("</tspan></text>");
        }


        return bob0.toString();
    }

    public static String pointToSvg (final GeneratedPoint p) {
        return pointToSvg(p, Colour.rgb(0x604040));
    }
    public static String pointToSvg (final ColourfulGeneratedPoint p) {
        return pointToSvg(p.getPoint(), p.getColour());
    }
    public static String pointToSvg (final GeneratedPoint p, final Colour c) {
        final StringBuilder bob0        = new StringBuilder(1024);
        final Rational      x           = p.getX(),
                            y           = p.getY(),
                            z           = p.getZ(),
                            dispx       = plus(times(x, errorCorrection), MARGIN_X),
                            dispy       = plus(times(y, errorCorrection), MARGIN_Y),
                            textx       = plus(dispx, TEXT_X_OFF),
                            texty       = max(plus(dispy, TEXT_Y_OFF), 0l);
        final Real          realdispx   = toReal(dispx),
                            realdispy   = toReal(dispy),
                            realz       = toReal(z),
                            realtextx   = toReal(textx),
                            realtexty   = toReal(texty);
        // generated points are supposed to have exact heights
        final LargeInteger  intz        = realz.round();
        assert(isEqualTo(realz, intz));
        final long          longz       = toLong(intz);
        final String        strlongz    = String.format("%d", (longz/10)%100);

        if (false) System.err.println(""
                + "x = " + x + "\n"
                + "y = " + y + "\n"
                + "z = " + z + "\n"
                + "dispx = " + dispx + "\n"
                + "dispy = " + dispy + "\n"
                + "textx = " + textx + "\n"
                + "texty = " + texty + "\n"
                + "realdispx = " + realdispx + "\n"
                + "realdispy = " + realdispy + "\n"
                + "realz = " + realz + "\n"
                + "realtextx = " + realtextx + "\n"
                + "realtexty = " + realtexty + "\n"
                + "intz = " + intz + "\n"
                + "longz = " + longz + "\n"
                + "strlongz = " + strlongz + "\n"
                + "assert(realz(" + realz + ") == intz(" + intz + ")) = " + isEqualTo(realz, intz) + "\n"
                + "toReal(intz(" + intz + ")) = " + toReal(intz) + "\n"
                + "assert(realdispy(" + realdispy+ ") == intdispy(" + realdispy.round() + ")) = " + isEqualTo(realdispy, realdispy.round()) + "\n"
                );

        bob0.append("\n\t<circle cx=\"").append(realdispx).append("mm\" cy=\"").append(realdispy).append("mm\" r=\"0.3mm\" fill=\"#").append(c.toRgbString()).append("\" />");

        if (true) {
            bob0.append("\n\t\t<text text-anchor=\"middle\" font-family=\"monospace\" font-size=\"7px\" font-weight=\"lighter\" fill=\"#").append(c.toRgbString()).append("\" x=\"")
                    .append(realtextx).append("mm\" y=\"").append(realtexty).append("mm\" dy=\"4ex\" dx=\"-2em\">")
                    .append(strlongz)
                    .append("</text>");
        }

        return bob0.toString();
    }

    public static String lineToSvg (final ColourfulLine vline) {
        final StringBuilder bob0        = new StringBuilder(1024);
        final Line          line        = vline.getLine();
        final Rational      x1          = line.getBeginning().getX(),
                            y1          = line.getBeginning().getY(),
                            x2          = line.getEnd().getX(),
                            y2          = line.getEnd().getY(),
                            dispx1      = plus(x1, MARGIN_X),
                            dispy1      = plus(y1, MARGIN_Y),
                            dispx2      = plus(x2, MARGIN_X),
                            dispy2      = plus(y2, MARGIN_Y);
        final Real          realdispx1  = toReal(dispx1),
                            realdispy1  = toReal(dispy1),
                            realdispx2  = toReal(dispx2),
                            realdispy2  = toReal(dispy2);

        bob0.append("\n\t<line x1=\"").append(realdispx1)
                .append("mm\" y1=\"").append(realdispy1)
                .append("mm\" x2=\"").append(realdispx2)
                .append("mm\" y2=\"").append(realdispy2)
                .append("mm\" stroke-width=\"0.2mm\" stroke=\"#").append(vline.getColour().toRgbString()).append("\" />");

        return bob0.toString();
    }

    public static void writePointsToSvg (final Writer w, final Iterable<? extends DeclaredPoint> declared, final Iterable<? extends ColourfulGeneratedPoint> generated) throws IOException {
        for (final ColourfulGeneratedPoint p: generated)
            w.append(pointToSvg(p)).append("\n");

        for (final DeclaredPoint p: declared)
            if (Points.isPointValid(p))
                w.append(pointToSvg(p)).append("\n");
    }

    public static void writeLinesToSvg (final Writer w, final Iterable<? extends ColourfulLine> lines) throws IOException {
        for (final ColourfulLine line: lines)
            w.append(lineToSvg(line)).append("\n");
    }

    public static void writeSvgHeader (final Writer w) throws IOException {
        w.write(SVG_HEADER);
    }

    public static void writeSvgFooter(final Writer w) throws IOException {
        w.write(SVG_FOOTER);
    }
}
