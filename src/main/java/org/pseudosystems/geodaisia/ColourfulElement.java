package org.pseudosystems.geodaisia;

public class ColourfulElement<T> {
    private final T         element;
    private final Colour    colour;

    protected ColourfulElement (final T element, final Colour colour) {
        this.element = element;
        this.colour = colour;
    }

    public T getElement () {
        return element;
    }

    public Colour getColour () {
        return colour;
    }

    public interface ColourfulElementFactory<T, RT> {
        RT create (T element, Colour colour);
    }
}
