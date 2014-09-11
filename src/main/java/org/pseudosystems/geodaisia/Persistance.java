package org.pseudosystems.geodaisia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.jscience.mathematics.number.LargeInteger;

import static org.pseudosystems.geodaisia.Constants.dot;
import static org.pseudosystems.geodaisia.Constants.minusOne;
import static org.pseudosystems.geodaisia.Constants.ws;

public class Persistance {

    private static final class PointLoading {

        private static final class Restrictions {
            private static final boolean StrictNumbering = true;
            private static final boolean StrictJOrdering = true;
            private static final boolean StrictIOrdering = true;

            public static void checkRestrictions (final DeclaredPoint last, final int n, final int j, final int i) {
                if (hasStrictNumbering() && !(last == null || n == last.getN() + 1))
                    throw new IllegalArgumentException("Point #" + n + " given after point #" + last.getN());
                if (hasStrictJOrdering() && !(last == null || j == last.getJ() || j == last.getJ() + 1))
                    throw new IllegalArgumentException("Point #" + n + " with j=" + j + " given after point #" + last.getN() + " with j=" + last.getJ());
                if (hasStrictIOrdering() && !(last == null || j  > last.getJ() || i == last.getI() + 1))
                    throw new IllegalArgumentException("Point #" + n + " with i=" + i + " given after point #" + last.getN() + " with i=" + last.getI());
            }

            public static boolean hasStrictNumbering () {
                return StrictNumbering;
            }

            public static boolean hasStrictJOrdering () {
                return StrictJOrdering;
            }

            public static boolean hasStrictIOrdering () {
                return StrictIOrdering;
            }
        }

        private static final class RestrictionsExt {
            public static boolean isFullyStrict () {
                return Restrictions.hasStrictNumbering() && Restrictions.hasStrictJOrdering() && Restrictions.hasStrictIOrdering();
            }

            public static boolean allRestrictionsHold (final DeclaredPoint last, final int n, final int j, final int i) {
                boolean hold = true;

                try {
                    Restrictions.checkRestrictions(last, n, j, i);
                }
                catch (final IllegalArgumentException ex) {
                    hold = false;
                }

                return hold;
            }
        }

        private static final class LoadAllPointsResult {
            public final LinkedList<DeclaredPoint>  points;
            public final int                        j;
            public final int                        i;
            public LoadAllPointsResult (final LinkedList<DeclaredPoint> points, final int j, final int i) {
                this.points   = points;
                this.j        = j;
                this.i        = i;
            }
        }

        private interface Adjustment {
            LargeInteger apply (LargeInteger a);
        }

        private static abstract class BasicAdjustment implements Adjustment {
            private final long mod;

            protected BasicAdjustment (long mod) {
                this.mod = mod;
            }

            protected abstract LargeInteger performOn (LargeInteger li, long mod);

            @Override
            public LargeInteger apply (LargeInteger a) {
                return performOn(a, mod);
            }
        }

        private static final class AdditionAdjustment extends BasicAdjustment {
            public AdditionAdjustment (long mod) {
                super(mod);
            }

            @Override
            protected LargeInteger performOn (LargeInteger li, long mod) {
                return li.plus(mod);
            }
        }

        private static final class SubtractionAdjustment extends BasicAdjustment {
            public SubtractionAdjustment (long mod) {
                super(mod);
            }

            @Override
            protected LargeInteger performOn (LargeInteger li, long mod) {
                return li.minus(mod);
            }
        }

        private static final class MultiplicationAdjustment extends BasicAdjustment {
            public MultiplicationAdjustment (long mod) {
                super(mod);
            }

            @Override
            protected LargeInteger performOn (LargeInteger li, long mod) {
                return li.times(mod);
            }
        }

        private static enum AdjustableElement {x, y, z};

        private static final class Adjustments {
            private final Map<AdjustableElement, List<Adjustment>> adjs = new HashMap<>(AdjustableElement.values().length);

            public Adjustments () {
                for (final AdjustableElement adji: AdjustableElement.values())
                    adjs.put(adji, new LinkedList<>());
            }

            public void add (AdjustableElement adji, Adjustment adj) {
                adjs.get(adji).add(adj);
            }

            public List<Adjustment> getX () {
                return adjs.get(AdjustableElement.x);
            }

            public List<Adjustment> getY () {
                return adjs.get(AdjustableElement.y);
            }

            public List<Adjustment> getZ () {
                return adjs.get(AdjustableElement.z);

            }
        }

        private static AdjustableElement translateAdjustableElement (final char c) throws IllegalArgumentException {
            AdjustableElement adji;
            switch (c) {
                case 'x':
                    adji = AdjustableElement.x;
                    break;
                case 'y':
                    adji = AdjustableElement.y;
                    break;
                default:
                    throw new IllegalArgumentException("command is not about x or y (" + c + ")");
            }
            return adji;
        }

        private static Adjustment makeAdjustment (final char c, final int mod) {
            Adjustment op;
            switch (c) {
                case '+':
                    op = new AdditionAdjustment(mod);
                    break;
                case '-':
                    op = new SubtractionAdjustment(mod);
                    break;
                case '*':
                    op = new MultiplicationAdjustment(mod);
                    break;
                default:
                    throw new IllegalArgumentException("command is not add or subtract or multiply (" + c + ")");
            }
            return op;
        }

        private static LargeInteger applyAdjustments (final List<Adjustment> adjs, final LargeInteger a) {
            LargeInteger result = a;
            for (final Adjustment adj: adjs)
                result = adj.apply(result);
            return result;
        }

        private static LoadAllPointsResult loadAllPoints (final Reader pointsReader) throws IOException {
            final Adjustments               adjustments  = new Adjustments();
            final LinkedList<DeclaredPoint> allpoints    = new LinkedList<>();
            final BufferedReader            r            = new BufferedReader(pointsReader);
            int                             maxj         = 0,
                                            maxi         = 0;
            String                          line;

            while ((line = r.readLine()) != null)
                // ignore comments and empty/whitspace lines
                if (line.isEmpty() || line.startsWith("%") || ws.matcher(line).matches())
                    {} // ignore
                // check for a command
                else
                if (line.startsWith("$"))
                    adjustments.add(translateAdjustableElement(line.charAt(1)), makeAdjustment(line.charAt(2), Integer.parseInt(line.substring(3))));
                else
                    for (final String entry: ws.split(line)) {
                        final String[] els = dot.split(entry);
                        if (els.length != 6)
                            throw new IllegalArgumentException("entry is not 6 fields long (" + entry + ")");

                        final int j = Integer.valueOf(els[0]);
                        final int i = Integer.valueOf(els[1]);
                        final int n = Integer.valueOf(els[2]);
                        final LargeInteger x = applyAdjustments(adjustments.getX(), LargeInteger.valueOf(els[3]));
                        final LargeInteger y = applyAdjustments(adjustments.getY(), LargeInteger.valueOf(els[4]));
                        final LargeInteger z = applyAdjustments(adjustments.getZ(), LargeInteger.valueOf(els[5]));

                        Restrictions.checkRestrictions(allpoints.peekLast(), n, j, i);
                        allpoints.addLast(DeclaredPoint.create(x, y, z, j, i, n));

                        maxj = Math.max(maxj, j);
                        maxi = Math.max(maxi, i);
                    }

            return new LoadAllPointsResult(allpoints, maxj, maxi);
        }

        private static ArrayList<ArrayList<DeclaredPoint>> newGrid (final int j, final int i) {
            final ArrayList<ArrayList<DeclaredPoint>> grid = new ArrayList<>(j);
            for (int k = 0; k < j; ++k) {
                final ArrayList<DeclaredPoint> inner = new ArrayList<>(i);
                grid.add(inner);
                for (int l = 0; l < i; ++l)
                    inner.add(DeclaredPoint.create(minusOne, minusOne, minusOne, -1, -1, -1));
            }

            return grid;
        }

        public static List<? extends List<? extends DeclaredPoint>> loadPoints (final Reader pointsReader) throws IOException {
            final LoadAllPointsResult inf = loadAllPoints(pointsReader);
            final ArrayList<ArrayList<DeclaredPoint>> grid = newGrid(inf.j + 1, inf.i + 1);

            DeclaredPoint previousPoint = null;
            for (final DeclaredPoint p: inf.points) {
                assert(previousPoint == null || RestrictionsExt.allRestrictionsHold(previousPoint, p.getN(), p.getJ(), p.getI()));
                grid.get(p.getJ()).set(p.getI(), p);
            }

            return grid;
        }
    }

    public static List<? extends List<? extends DeclaredPoint>> loadPoints (final Reader pointsReader) throws IOException {
        return PointLoading.loadPoints(pointsReader);
    }
}
