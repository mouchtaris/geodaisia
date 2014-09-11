package org.pseudosystems.geodaisia;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import static org.pseudosystems.geodaisia.Collections.flattenIterables;
import static org.pseudosystems.geodaisia.Constants.utf8;
import static org.pseudosystems.geodaisia.IntermediatePointsGenerator.colouriseLines;
import static org.pseudosystems.geodaisia.IntermediatePointsGenerator.generateAllIntermediateHeightPoints;
import static org.pseudosystems.geodaisia.IntermediatePointsGenerator.generateHeightLines2;
import static org.pseudosystems.geodaisia.IntermediatePointsGenerator.colourisePoints;
import static org.pseudosystems.geodaisia.Persistance.loadPoints;
import static org.pseudosystems.geodaisia.Renderer.writeLinesToSvg;
import static org.pseudosystems.geodaisia.Renderer.writePointsToSvg;
import static org.pseudosystems.geodaisia.Renderer.writeSvgFooter;
import static org.pseudosystems.geodaisia.Renderer.writeSvgHeader;
import static java.lang.System.out;

public class Geodaisia {

    private static class Config {
        OutputStream base_outs;
        InputStream base_inps;
    }

    private static final String OPTION_OUTPUT = "o";

    private static Config parseArgs (final String[] args) throws FileNotFoundException {
        final Config config = new Config();
        final ArgumentParser argparser = new ArgumentParser();

        argparser.addOption(OPTION_OUTPUT);

        argparser.parse(args);

        {
            final String output = argparser.getArgument(OPTION_OUTPUT);
            if (output == null || output.equals("-"))
                config.base_outs = System.out;
            else
                config.base_outs = new FileOutputStream(output);

        }

        if (argparser.getArgumentsLength() > 0) {
            final String input = argparser.getArguments().iterator().next();
            if (input.equals("-"))
                config.base_inps = System.in;
            else
                config.base_inps = new FileInputStream(input);
        }
        else
            config.base_inps = System.in;

        return config;
    }

    public static void main (final String[] args) throws IOException {
        final Config config = parseArgs(args);
        try (
                final BufferedOutputStream bouts = new BufferedOutputStream(config.base_outs, 10240);
                final GZIPOutputStream gzouts = new GZIPOutputStream(bouts, 10240, true);
                final OutputStreamWriter w = new OutputStreamWriter(gzouts, utf8);
                final BufferedInputStream bins = new BufferedInputStream(config.base_inps, 10240);
                final InputStreamReader r = new InputStreamReader(bins, utf8);
        ) {
            final List<? extends List<? extends DeclaredPoint>> points = loadPoints(r);
            out.println("points loaded");
            final List<? extends List<? extends List<? extends GeneratedPoint>>> intermediates = generateAllIntermediateHeightPoints(points);
            out.println("intermediates generated");
            writeSvgHeader(w);
            writeLinesToSvg(w, colouriseLines(generateHeightLines2(intermediates, 30)));
            out.println("level lines generated, colourised and written");
            writePointsToSvg(w, flattenIterables(points), colourisePoints(flattenIterables(flattenIterables(intermediates))));
            out.println("intermediate points colourised and written");
            writeSvgFooter(w);
        }
    }

    private Geodaisia () {
    }
}
