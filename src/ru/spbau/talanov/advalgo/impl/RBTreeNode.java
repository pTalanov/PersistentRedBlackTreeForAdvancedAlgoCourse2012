package ru.spbau.talanov.advalgo.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Pavel Talanov
 */
public final class RBTreeNode<E> extends Node<E> {

    @NotNull
    private final Node<E> left;
    @NotNull
    private final Node<E> right;
    @NotNull
    private final E value;
    @NotNull
    private final Color color;

    public RBTreeNode(@NotNull Node<E> left, @NotNull Node<E> right, @NotNull E value, @NotNull Color color) {
        this.left = left;
        this.right = right;
        this.value = value;
        this.color = color;
    }

    @NotNull
    @Override
    public Color getColor() {
        return color;
    }

    @NotNull
    public Node<E> getLeft() {
        return left;
    }

    @NotNull
    public Node<E> getRight() {
        return right;
    }

    @NotNull
    @Override
    public E getValue() {
        return value;
    }

    public boolean isNil() {
        return false;
    }

    public static class Builder<E> {
        @Nullable
        private Node<E> left = null;
        @Nullable
        private Node<E> right = null;
        @Nullable
        private E value = null;
        @Nullable
        private Color color = null;
        @Nullable
        private Mutator<E> mutator;

        private Builder(@NotNull Node<E> existing, @Nullable Mutator<E> mutator) {
            this.mutator = mutator;
            this.left = existing.getLeft();
            this.right = existing.getRight();
            this.value = existing.getValue();
            this.color = existing.getColor();
        }

        @NotNull
        public Builder<E> left(@NotNull Node<E> left) {
            this.left = left;
            return this;
        }

        @NotNull
        public Builder<E> right(@NotNull Node<E> right) {
            this.right = right;
            return this;
        }

        @NotNull
        public Builder<E> value(@NotNull E value) {
            this.value = value;
            return this;
        }

        @NotNull
        public Builder<E> color(@NotNull Color color) {
            this.color = color;
            return this;
        }

        @NotNull
        public Builder<E> changeColor(@NotNull Color newColor) {
            assert this.color == newColor.other();
            this.color = newColor;
            return this;
        }

        @NotNull
        public Builder<E> child(Direction direction, @NotNull Node<E> child) {
            if (direction == Direction.LEFT) {
                return left(child);
            } else {
                return right(child);
            }
        }

        @NotNull
        public Node<E> build() {
            assert left != null;
            assert right != null;
            assert value != null;
            assert color != null;
            Node<E> result = new RBTreeNode<E>(left, right, value, color);
            if (mutator != null) {
                result = mutator.mutate(result);
            }
            return result;
        }
    }

    @Override
    public String toString() {
        return value.toString() + ":" + color.toString() + "(" + left.toString() + "), (" + right.toString() + ")";
    }

    public static <E> Builder<E> builder(@NotNull Node<E> node) {
        return builder(node, null);
    }

    public static <E> Builder<E> builder(@NotNull Node<E> node, @Nullable Mutator<E> mutator) {
        return new Builder<E>(node, mutator);
    }

    public static <E> Node<E> leafNode(@NotNull E value, @NotNull Color color) {
        return new RBTreeNode<E>(Node.<E>nil(), Node.<E>nil(), value, color);
    }


}
