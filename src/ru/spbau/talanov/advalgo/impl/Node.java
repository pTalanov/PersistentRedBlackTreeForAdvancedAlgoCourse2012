package ru.spbau.talanov.advalgo.impl;

import org.jetbrains.annotations.NotNull;

/**
 * @author Pavel Talanov
 */
public abstract class Node<E> {

    private static Node NIL = new Node() {
        @NotNull
        @Override
        public Color getColor() {
            return Color.BLACK;
        }

        @NotNull
        @Override
        public Node getLeft() {
            return this;
        }

        @NotNull
        @Override
        public Node getRight() {
            return this;
        }

        @NotNull
        @Override
        public Object getValue() {
            throw new UnsupportedOperationException("Node.NIL.getValue()");
        }

        @Override
        public boolean isNil() {
            return true;
        }

        @Override
        public String toString() {
            return "NIL";
        }
    };

    public static <E> Node<E> nil() {
        //noinspection unchecked
        return NIL;
    }

    @NotNull
    public abstract Color getColor();

    //one would argue that those helper methods are redundant
    public boolean isRed() {
        return getColor() == Color.RED;
    }

    public boolean isBlack() {
        return getColor() == Color.BLACK;
    }

    @NotNull
    public abstract Node<E> getLeft();

    @NotNull
    public abstract Node<E> getRight();

    @NotNull
    public Node<E> getChild(@NotNull Direction direction) {
        if (direction == Direction.LEFT) {
            return getLeft();
        } else {
            return getRight();
        }
    }

    @NotNull
    public abstract E getValue();

    public abstract boolean isNil();
}
