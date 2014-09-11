package org.pseudosystems.geodaisia;

public class ColourfulGeneratedPoint extends ColourfulElement<GeneratedPoint> {

    private ColourfulGeneratedPoint (final GeneratedPoint p, final Colour c) {
        super(p, c);
    }

    public GeneratedPoint getPoint () {
        return getElement();
    }

    public static ColourfulGeneratedPoint create (final GeneratedPoint p, final Colour c) {
        return new ColourfulGeneratedPoint(p, c);
    }

    public static class ColourfulGeneratedPointFactory implements ColourfulElement.ColourfulElementFactory<GeneratedPoint, ColourfulGeneratedPoint> {
        @Override
        public ColourfulGeneratedPoint create (final GeneratedPoint p, final Colour colour) {
            return ColourfulGeneratedPoint.create(p, colour);
        }
    }
}
