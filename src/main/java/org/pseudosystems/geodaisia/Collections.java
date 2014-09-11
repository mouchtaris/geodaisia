package org.pseudosystems.geodaisia;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class Collections {

    private Collections () {
    }

    public static <T> Iterable<? extends T> flattenIterables (final Iterable<? extends Iterable<? extends T>> itersiterable) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator () {
                return new Iterator<T>() {
                    private final Iterator<? extends Iterable<? extends T>> iterables = itersiterable.iterator();
                    private Iterable<? extends T> currentIterable = null;
                    private Iterator<? extends T> currentIterator = null;

                    private boolean inv () {
                        return    currentIterable != null || currentIterator == null    |
                                currentIterator != null || currentIterable == null;
                    }

                    // Must check invariants -- others rely on this.
                    private void invalidateIteratorIfDoesNotHaveNext () {
                        assert(inv());
                        assert(currentIterator != null);

                        if (!currentIterator.hasNext()) {
                            currentIterator = null;
                            currentIterable = null;
                        }

                        assert(inv());
                    }

                    @Override
                    public boolean hasNext() {
                        assert(inv());

                        while (currentIterable == null && iterables.hasNext()) {
                            currentIterable = iterables.next();
                            if (currentIterable != null) {
                                currentIterator = currentIterable.iterator();
                                invalidateIteratorIfDoesNotHaveNext();
                            }
                        }

                        // assert(inv()) in last call or no op since begining of method
                        return currentIterable != null;
                    }

                    @Override
                    public T next() {
                        assert(inv());
                        assert(currentIterator != null);

                        final T result = currentIterator.next();

                        invalidateIteratorIfDoesNotHaveNext();
                        // assert(inv()) in last method
                        return result;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                };
            }
        };
    }

    public static class DefaultComparator<T extends Comparable<? super T>> implements Comparator<T> {
        @Override
        public int compare (final T o1, final T o2) {
            return o1.compareTo(o2);
        }
    }

    public static <T> boolean isSorted (final Iterable<? extends T> col, final Comparator<? super T> comp) {
        boolean sorted = true;
        final Iterator<? extends T> ite = col.iterator();
        T elem, prev = null;

        for (; sorted && ite.hasNext(); prev = elem) {
            elem = ite.next();
            if (!(prev == null || comp.compare(elem, prev) >= 0))
                sorted = false;
        }

        return sorted;
    }

    public static <T extends Comparable<? super T>> boolean isSorted (final Iterable<? extends T> col) {
        return isSorted(col, new DefaultComparator<T>());
    }

    public static <T> Collection<? extends T> toCollection (final Iterable<T> iterable) {
        return new AbstractCollection<T>() {
            @Override
            public Iterator<T> iterator () {
                return iterable.iterator();
            }

            @Override
            public int size () {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }
}
