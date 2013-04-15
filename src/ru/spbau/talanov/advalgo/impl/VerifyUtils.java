package ru.spbau.talanov.advalgo.impl;

import org.jetbrains.annotations.NotNull;

/**
 * @author Pavel Talanov
 */
public final class VerifyUtils {
    private VerifyUtils() {

    }

    public static <E extends Comparable<? super E>> boolean verifyProperties(@NotNull Node<E> root) {
        verifyTreeProperties(root);
        verifyRootIsBlack(root);
        verifyChildrenOfRedNodesAreBlack(root);
        verifyEveryPathToLeafHasEqualNumberOfBlackNodes(root);
        return true;
    }

    private static <E extends Comparable<? super E>> void verifyTreeProperties(@NotNull Node<E> root) {
        if (root.isNil()) {
            return;
        }
        recVerifyTreeProperty(root);
    }

    private static <E extends Comparable<? super E>> void recVerifyTreeProperty(@NotNull Node<E> node) {
        if (!node.getLeft().isNil()) {
            assert node.getLeft().getValue().compareTo(node.getValue()) < 0;
            recVerifyTreeProperty(node.getLeft());
        }
        if (!node.getRight().isNil()) {
            assert node.getRight().getValue().compareTo(node.getValue()) > 0;
            recVerifyTreeProperty(node.getRight());
        }
    }

    private static <E> void verifyChildrenOfRedNodesAreBlack(Node<E> node) {
        recVerifyChildrenOfRedNodesAreBlack(node);
    }

    private static <E> void recVerifyChildrenOfRedNodesAreBlack(@NotNull Node<E> current) {
        if (current.isNil()) {
            return;
        }
        if (current.isRed()) {
            assert current.getLeft().isBlack();
            assert current.getRight().isBlack();
        }
        recVerifyChildrenOfRedNodesAreBlack(current.getLeft());
        recVerifyChildrenOfRedNodesAreBlack(current.getRight());
    }

    private static <E> void verifyEveryPathToLeafHasEqualNumberOfBlackNodes(@NotNull Node<E> node) {
        recVerifyEveryPathToLeafHasEqualNumberOfBlackNodes(node);
    }

    private static <E> int recVerifyEveryPathToLeafHasEqualNumberOfBlackNodes(@NotNull Node<E> current) {
        if (current.isNil()) {
            return 1;
        }
        int leftCount = recVerifyEveryPathToLeafHasEqualNumberOfBlackNodes(current.getLeft());
        int rightCount = recVerifyEveryPathToLeafHasEqualNumberOfBlackNodes(current.getRight());
        assert leftCount == rightCount;
        if (current.isBlack()) {
            return leftCount + 1;
        } else {
            return leftCount;
        }
    }

    private static <E> boolean verifyRootIsBlack(@NotNull Node<E> root) {
        return root.getColor() == Color.BLACK;
    }
}
