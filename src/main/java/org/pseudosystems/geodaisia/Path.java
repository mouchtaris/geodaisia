package org.pseudosystems.geodaisia;

import java.util.HashSet;
import java.util.Set;

public class Path<T> {

    public static class Node<T> {
        private Node<T> prev, next;
        private Path<T> parent;
        private final T data;

        public Node (final T data) {
            this.data = data;
        }

        public T getData () {
            return data;
        }

        public boolean isAlone () {
            return parent == null && prev == null && next == null;
        }

        public boolean isFullyRelated () {
            return parent != null && prev != null && next != null;
        }

        public boolean isParent (final Path<T> parent) {
            return this.parent == parent;
        }

        public boolean hasParent () {
            return parent != null;
        }

        public boolean hasPrevious () {
            return prev != null;
        }

        public boolean hasNext () {
            return next != null;
        }

        public boolean isNext (final Node<T> other) {
            assert other != null;
            return next == other;
        }

        public boolean isPrevious (final Node<T> other) {
            assert other != null;
            return prev == other;
        }

        public boolean isRelated (final Node<T> other) {
            assert other != null;
            return prev == other || next == other;
        }

        public boolean hasOnlyOneConnection () {
            return (prev != null || next != null) && !(prev != null && next != null);
        }

        public void connect (final Node<T> to) {
            assert to != null;
            assert hasOnlyOneConnection();
            if (prev == null)
                prev = to;
            else {
                assert next == null;
                next = to;
            }
        }

        public void repay (final Node<T> other) {
            assert other != null;
            assert other.isRelated(this);
            assert !other.isPrevious(this) || next == null;
            assert !other.isNext(this) || prev == null;

            if (other.isPrevious(this))
                next = other;
            else
                prev = other;
        }

        public void disconnect (final Node<T> other) {
            assert other != null;
            assert isPrevious(other) || isNext(other);

            if (isPrevious(other))
                prev = null;
            else
                next = null;
        }

        public void adoptedBy (final Path<T> parent) {
            assert parent != null;
            this.parent = parent;
        }

        public Path<T> getParent () {
            return parent;
        }

        public Node<T> getPrevious () {
            return prev;
        }

        public Node<T> getNext () {
            return next;
        }
    }

    private Node<T> first, last;
    private final Set<Node<T>> nodes = new HashSet<>(50);

    public boolean isMine (final Node<T> n) {
        return nodes.contains(n);
    }

    public void adopt (final Node<T> n) {
        final boolean added = nodes.add(n);
        assert added;
    }
}
