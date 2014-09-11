package org.pseudosystems.geodaisia;

public class Line {
    private final GeneratedPoint beginning, end;

    public Line (final GeneratedPoint beginning, final GeneratedPoint end) {
        this.beginning = beginning;
        this.end = end;
    }

    public GeneratedPoint getBeginning () {
        return beginning;
    }

    public GeneratedPoint getEnd () {
        return end;
    }

    public boolean isLeveled () {
        return beginning.getZ().equals(end.getZ());
    }

    @Override
    public String toString () {
        return beginning.toString() + "->" + end;
    }
}
