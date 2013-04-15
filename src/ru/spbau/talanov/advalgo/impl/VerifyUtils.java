package ru.spbau.talanov.advalgo.impl;

import org.jetbrains.annotations.NotNull;

/**
 * @author Pavel Talanov
 */
public final class VerifyUtils {
    private VerifyUtils() {

    }

    public static <E> boolean verifyProperties(@NotNull Node<E> root) {
        verifyRootIsBlack(root);
        verifyChildrenOfRedNodesAreBlack(root);
        verifyEveryPathToLeafHasEqualNumberOfBlackNodes(root);
        return true;
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
