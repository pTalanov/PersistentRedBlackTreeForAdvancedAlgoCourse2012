package ru.spbau.talanov.advalgo.impl;

import org.jetbrains.annotations.NotNull;

/**
 * @author Pavel Talanov
 */
public final class PathCopyUtil {
    private PathCopyUtil() {
    }

    public static <E extends Comparable<? super E>> Node<E> replaceNode(@NotNull LookUpPath<E> lookUpPath,
                                                                        @NotNull Node<E> toReplaceWith) {
        if (lookUpPath.isRoot()) {
            return toReplaceWith;
        }
        return lookUpPath.traversePathFromBottomToTop(toReplaceWith, new LookUpPath.TraverseCallback<E, Node<E>>() {
            @Override
            public Node<E> traverse(Node<E> current, Node<E> parent, Direction direction, Node<E> newChild) {
                return RBTreeNode.builder(parent).child(direction, newChild).build();
            }
        });
    }
}
