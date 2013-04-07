package ru.spbau.talanov.advalgo.impl;

import org.jetbrains.annotations.NotNull;

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
                return;
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
    public Direction getDirectionFromGrandFather() {
        return turns.get(length - 2);
    }

    @NotNull
    public Node<E> getUncle() {
        return getGrandFather().getChild(turns.get(length - 2).opposite());
    }

    public <D> D traversePathFromBottomToTop(D initialData, TraverseCallback<E, D> callback) {
        assert !path.isEmpty();
        Node<E> current = getResult();
        int height = path.size() - 1;
        D currentData = initialData;
        while (height >= 0) {
            currentData = callback.traverse(current, path.get(height), turns.get(height), currentData);
            current = path.get(height);
            height--;
        }
        return currentData;
    }

    public boolean isRoot() {
        return length == 0;
    }

    public interface TraverseCallback<E, D> {
        D traverse(Node<E> current, Node<E> parent, Direction d, D data);
    }


    @NotNull
    public static <E extends Comparable<? super E>> LookUpPath<E> performLookUp(@NotNull Node<E> root, @NotNull E desiredValue) {
        return new LookUpPath<E>(root, desiredValue);
    }


    /*mutates inner state*/
    public void popLast(int count) {
        result = path.get(length - count);
        for (int i = 1; i <= count; ++i) {
            path.remove(length - i);
            turns.remove(length - i);
        }
        length -= 2;
    }
}
