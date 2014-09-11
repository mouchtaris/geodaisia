package org.pseudosystems.geodaisia;

import org.pseudosystems.geodaisia.Path.Node;

public class PathExt {

    private PathExt () {
    }

    public static <T> boolean areRelated (final Path<T> path, final Node<T> node) {
        boolean related = path.isMine(node);
        assert !related || node.isParent(path);
        assert related || !node.isParent(path);

        return related;
    }

    public static <T> boolean areRelated (final Node<T> one, final Node<T> two) {
        final boolean related = one.isRelated(two);
        assert !related || two.isRelated(one);
        assert related || !two.isRelated(one);

        return related;
    }

    public static <T> void adopt (final Path<T> path, final Node<T> node) {
        assert !node.hasParent();
        assert !areRelated(path, node);

        path.adopt(node);
        node.adoptedBy(path);
    }

    public static <T> boolean isSaneNode (final Node<T> node) {
        return    areRelated(node.getParent(), node)                                &&
                (!node.hasPrevious() || areRelated(node.getPrevious(), node))    &&
                (!node.hasNext() || areRelated(node.getNext(), node))            &&
                (!node.hasPrevious() || node.isPrevious(node.getPrevious()))    &&
                (!node.hasNext() || node.isNext(node.getNext()));
    }

    public static <T> void connect (final Path<T> path, final Node<T> newcomer, final Node<T> oldman) {
        assert !areRelated(path, oldman);
        assert !areRelated(newcomer, oldman);
        assert newcomer.isAlone();
        assert oldman.hasOnlyOneConnection();

        oldman.connect(newcomer);
        newcomer.repay(oldman);
        path.adopt(newcomer);
        newcomer.adoptedBy(path);
    }

    public static <T> void disconnect (final Path<T> path, final Node<T> guy, final Node<T> from) {
        assert isSaneNode(from);
        assert isSaneNode(guy);
        assert areRelated(path, guy);
        assert areRelated(path, from);
        assert areRelated(guy, from);
        assert from.isFullyRelated();

        guy.disconnect(from);
        from.disconnect(guy);
    }
}
