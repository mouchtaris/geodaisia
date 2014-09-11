package org.pseudosystems.geodaisia;

public class ColourfulLine extends ColourfulElement<Line> {
    private ColourfulLine (final Line line, final Colour colour) {
        super(line, colour);
    }

    public static ColourfulLine create (final Line line, final Colour colour) {
        return new ColourfulLine(line, colour);
    }

    public Line getLine () {
        return getElement();
    }

    public static class ColourfulLineFactory implements ColourfulElement.ColourfulElementFactory<Line, ColourfulLine> {
        @Override
        public ColourfulLine create (final Line line, final Colour colour) {
            return ColourfulLine.create(line, colour);
        }
    }
}
