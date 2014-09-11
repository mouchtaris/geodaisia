package org.pseudosystems.geodaisia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {
    private final Map<String, String> options = new HashMap<>();
    private final ArrayList<String> arguments = new ArrayList<>(20);

    public void addOption (final String name) {
        options.put(name, null);
    }

    public void parse (final ArgumentTraverser args) {
        arguments.ensureCapacity(args.getLength());

        for (; !args.isEnd(); args.next()) {
            final String value = args.getValue();
            if (args.isOption()) {
                if (!options.containsKey(value))
                    throw new IllegalArgumentException("{ "
                            + "\"type\": \"unrecognized-option\","
                            + "\"name\": \"" + value + "\" }");
                options.put(value, args.getArgument());
            }
            else
                arguments.add(value);
        }
    }

    public void parse (final String[] args) {
        parse(new ArgumentTraverser(args));
    }

    public String getArgument (final String optionName) {
        if (!options.containsKey(optionName))
            throw new IllegalArgumentException("No such option: " + optionName);
        return options.get(optionName);
    }

    public int getArgumentsLength () {
        return arguments.size();
    }

    public Iterable<String> getArguments () {
        return arguments;
    }
}