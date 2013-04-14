package ru.spbau.talanov.advalgo.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pavel Talanov
 */
public final class LookUpPath<E extends Comparable<? super E>> {

    @NotNull
    private List<Node<E>> path = new ArrayList<Node<E>>();
    @NotNull
    private List<Direction> turns = new ArrayList<Direction>();
    @NotNull
    private Node<E> result;

    private int length;

    private LookUpPath(@NotNull Node<E> root, @NotNull E desiredValue) {
        Node<E> currentNode = root;
        result = Node.nil();
        while (!currentNode.isNil()) {
            E currentValue = currentNode.getValue();
            int comparisonResult = desiredValue.compareTo(currentValue);
            if (comparisonResult == 0) {
                result = currentNode;
                break;
            }
            path.add(currentNode);
            if (comparisonResult < 0) {
                turns.add(Direction.LEFT);
                currentNode = currentNode.getLeft();
            } else {
                turns.add(Direction.RIGHT);
                currentNode = currentNode.getRight();
            }
        }
        length = turns.size();
        assert length == path.size();
    }

    @NotNull
    public Node<E> getResult() {
        return result;
    }


    @NotNull
    public Node<E> getFather() {
        if (path.isEmpty()) {
            throw new AssertionError("getFather() called for a node without parent");
        }
        return path.get(length - 1);
    }

    @NotNull
    public Node<E> getGrandFather() {
        return path.get(length - 2);
    }

    @NotNull
    public Direction getDirectionFromFather() {
        return turns.get(length - 1);
    }

    @NotNull
    public Direction getDirectionFromFatherToSibling() {
        return getDirectionFromFather().opposite();
    }

    @NotNull
    public Direction getDirectionFromGrandFather() {
        return turns.get(length - 2);
    }

    @NotNull
    public Node<E> getUncle() {
        return getGrandFather().getChild(getDirectionFromGrandFather().opposite());
    }

    public boolean isRoot() {
        return length == 0;
    }

    @NotNull
    public static <E extends Comparable<? super E>> LookUpPath<E> performLookUp(@NotNull Node<E> root,
                                                                                @NotNull E desiredValue) {
        return new LookUpPath<E>(root, desiredValue);
    }

    /*mutates inner state*/
    public void popLast(int count) {
        result = path.get(length - count);
        for (int i = 1; i <= count; ++i) {
            path.remove(length - i);
            turns.remove(length - i);
        }
        length -= count;
    }


    @NotNull
    private static final Node DUMMY_NODE = new Node() {
        @NotNull
        @Override
        public Color getColor() {
            throw new UnsupportedOperationException();
        }

        @NotNull
        @Override
        public Node getLeft() {
            throw new UnsupportedOperationException();
        }

        @NotNull
        @Override
        public Node getRight() {
            throw new UnsupportedOperationException();
        }

        @NotNull
        @Override
        public Object getValue() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isNil() {
            throw new UnsupportedOperationException();
        }
    };

    public void insertDummy(@NotNull Direction direction) {
        turns.add(direction);
        //noinspection unchecked
        path.add(DUMMY_NODE);
        length += 1;
    }

    @Nullable
    public Node<E> getHigher() {
        int lastTurnLeft = turns.lastIndexOf(Direction.LEFT);
        if (lastTurnLeft == -1) {
            return null;
        }
        return path.get(lastTurnLeft);
    }

    @Nullable
    public Node<E> getLower() {
        int lastTurnRight = turns.lastIndexOf(Direction.RIGHT);
        if (lastTurnRight == -1) {
            return null;
        }
        return path.get(lastTurnRight);
    }

    @NotNull
    public static <E extends Comparable<? super E>> Node<E> replaceNode(@NotNull LookUpPath<E> lookUpPath,
                                                                        @NotNull Node<E> toReplaceWith) {
        return replaceNode(lookUpPath, toReplaceWith, new Mutator<E>() {
            @NotNull
            @Override
            public Node<E> mutate(@NotNull Node<E> node) {
                return node;
            }
        });
    }

    @NotNull
    public static <E extends Comparable<? super E>> Node<E> replaceNode(@NotNull LookUpPath<E> lookUpPath,
                                                                        @NotNull Node<E> toReplaceWith,
                                                                        @Nullable Mutator<E> mutator) {
        if (mutator == null) {
            return replaceNode(lookUpPath, toReplaceWith);
        }
        if (lookUpPath.isRoot()) {
            return mutator.mutate(toReplaceWith);
        }
        assert !lookUpPath.path.isEmpty();
        int height = lookUpPath.path.size() - 1;
        Node<E> currentNode = mutator.mutate(toReplaceWith);
        while (height >= 0) {
            Node<E> parent = lookUpPath.path.get(height);
            Direction direction = lookUpPath.turns.get(height);
            Node<E> newChild = currentNode;
            currentNode = RBTreeNode.builder(parent).child(direction, newChild).build();
            currentNode = mutator.mutate(currentNode);
            height--;
        }
        return currentNode;
    }

}
