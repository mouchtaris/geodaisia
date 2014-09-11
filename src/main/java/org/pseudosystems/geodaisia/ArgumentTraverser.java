package org.pseudosystems.geodaisia;

/**
 *
 * @author Nikos Mouchtaris
 */
public class ArgumentTraverser {

    private final String[] args;
    private int i = 0;

    public ArgumentTraverser (final String[] args) {
        this.args = args;
    }

    public boolean isEnd () {
        return i == args.length;
    }

    public boolean isOption () {
        final String arg = args[i];
        return arg.startsWith("-") && arg.length() > 1;
    }

    public boolean hasArgument () {
        return isOption() && (hasAttachedArgument() || hasSeparateArgument());
    }

    public String getArgument () {
        if (!isOption())
            return null;
        if (hasAttachedArgument())
            return args[i].substring(2);
        if (hasSeparateArgument())
            return args[i + 1];
        return null;
    }

    public String getValue () {
        final String arg = args[i];
        return isOption()? arg.substring(1, 2) : arg;
    }

    public void next () {
        if (isOption() && hasSeparateArgument())
            ++i;
        ++i;
    }

    public int getLength () {
        return args.length;
    }



    private boolean hasSeparateArgument() {
        return i + 1 < args.length;
    }

    private boolean hasAttachedArgument() {
        return args[i].length() > 2;
    }
}
