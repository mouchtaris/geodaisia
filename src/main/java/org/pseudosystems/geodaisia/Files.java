package org.pseudosystems.geodaisia;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;

public class Files {

    public static int countlines (final Path path, final Charset cs) throws IOException {
        final BufferedReader r = java.nio.file.Files.newBufferedReader(path, cs);
        int counter = 0;
        for (int c = r.read(); c != -1; c = r.read())
            if (c == '\n')
                ++counter;
        return counter;
    }
}
