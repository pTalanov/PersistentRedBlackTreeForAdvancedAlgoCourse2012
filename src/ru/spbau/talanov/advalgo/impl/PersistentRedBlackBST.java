package ru.spbau.talanov.advalgo.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.spbau.talanov.advalgo.api.PersistentNavigableSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.spbau.talanov.advalgo.impl.RBTreeNode.builder;

/**
 * @author Pavel Talanov
 */
public final class PersistentRedBlackBST<E extends Comparable<? super E>> implements PersistentNavigableSet<E, PersistentRedBlackBST<E>> {

    @NotNull
    public static <E extends Comparable<? super E>> PersistentRedBlackBST<E> empty() {
        return new PersistentRedBlackBST<E>(Node.<E>nil());
    }

    @NotNull
    private final Node<E> root;

    private PersistentRedBlackBST(@NotNull Node<E> root) {
        this.root = root;
    }

    @Override
    @Nullable
    public PersistentRedBlackBST<E> add(@NotNull E elementToAdd) {
        LookUpPath<E> lookUpPath = LookUpPath.performLookUp(root, elementToAdd);
        if (!lookUpPath.getResult().isNil()) {
            return null;
        }
        PersistentRedBlackBST<E> result = insertCase1(lookUpPath, RBTreeNode.leafNode(elementToAdd, Color.RED));
        VerifyUtils.verifyProperties(result.root);
        return result;
    }

    @NotNull
    private PersistentRedBlackBST<E> insertCase1(@NotNull LookUpPath<E> lookUpPath, @NotNull Node<E> newNode) {
        assert newNode.isRed();
        if (lookUpPath.isRoot()) {
            return new PersistentRedBlackBST<E>(builder(newNode).changeColor(Color.BLACK).build());
        }
        return insertCase2(lookUpPath, newNode);
    }

    @NotNull
    private PersistentRedBlackBST<E> insertCase2(@NotNull LookUpPath<E> lookUpPath, @NotNull Node<E> newNode) {
        if (lookUpPath.getFather().isBlack()) {
            Node<E> newRoot = PathCopyUtil.replaceNode(lookUpPath, newNode);
            return new PersistentRedBlackBST<E>(newRoot);
        } else {
            return insertCase3(lookUpPath, newNode);
        }
    }

    @NotNull
    private PersistentRedBlackBST<E> insertCase3(@NotNull LookUpPath<E> lookUpPath, @NotNull Node<E> newNode) {
        Node<E> uncle = lookUpPath.getUncle();
        if (uncle.isNil() || uncle.isBlack()) {
            return insertCase4(lookUpPath, newNode);
        }
        Node<E> newUncle = builder(uncle).changeColor(Color.BLACK).build();

        Node<E> newFather = builder(lookUpPath.getFather())
                .child(lookUpPath.getDirectionFromFather(), newNode)
                .changeColor(Color.BLACK)
                .build();

        Node<E> newGrandFather = builder(lookUpPath.getGrandFather())
                .child(lookUpPath.getDirectionFromGrandFather(), newFather)
                .child(lookUpPath.getDirectionFromGrandFather().opposite(), newUncle)
                .changeColor(Color.RED)
                .build();
        lookUpPath.popLast(2);
        return insertCase1(lookUpPath, newGrandFather);
    }

    @NotNull
    private PersistentRedBlackBST<E> insertCase4(@NotNull LookUpPath<E> lookUpPath, @NotNull Node<E> newNode) {
        Node<E> father = lookUpPath.getFather();
        Node<E> grandFather = lookUpPath.getGrandFather();
        Node<E> uncle = lookUpPath.getUncle();
        Direction directionFromGrandFather = lookUpPath.getDirectionFromGrandFather();
        Direction directionFromFather = lookUpPath.getDirectionFromFather();
        Node<E> newFather = builder(father).child(directionFromFather, newNode).build();
        if (directionFromFather == directionFromGrandFather) {
            Node<E> newGrandFather = builder(grandFather).child(directionFromGrandFather, newFather).build();
            return insertCase5(lookUpPath, newNode, newFather, newGrandFather, uncle, directionFromGrandFather);
        }
        Node<E> fatherAfterRotation = rotate(newFather, directionFromGrandFather);
        Node<E> newNodeAfterRotation = fatherAfterRotation.getChild(directionFromGrandFather);
        Node<E> grandFatherAfterRotation = builder(grandFather).child(directionFromGrandFather, fatherAfterRotation).build();
        return insertCase5(lookUpPath, newNodeAfterRotation, fatherAfterRotation, grandFatherAfterRotation, uncle, directionFromGrandFather);
    }

    @NotNull
    private PersistentRedBlackBST<E> insertCase5(@NotNull LookUpPath<E> lookUpPath,
                                                 @NotNull Node<E> newNode,
                                                 @NotNull Node<E> father,
                                                 @NotNull Node<E> grandFather,
                                                 @NotNull Node<E> uncle,
                                                 @NotNull Direction direction
    ) {
        assert grandFather.getChild(direction) == father;
        assert father.getChild(direction) == newNode;
        assert grandFather.getChild(direction.opposite()) == uncle;
        assert grandFather.isBlack();
        assert father.isRed();
        assert uncle.isBlack();
        assert newNode.isRed();

        Node<E> coloredFather = builder(father).changeColor(Color.BLACK).build();
        Node<E> coloredGrandFather = builder(grandFather).child(direction, coloredFather).changeColor(Color.RED).build();
        Node<E> subTreeRootAfterAllOperations = rotate(coloredGrandFather, direction.opposite());
        //we modified grandparent in the last 2 cases so we have to replace our grandparent after we are done
        lookUpPath.popLast(2);
        Node<E> newRoot = PathCopyUtil.replaceNode(lookUpPath, subTreeRootAfterAllOperations);
        return new PersistentRedBlackBST<E>(newRoot);
    }

    @NotNull
    private static <E> Node<E> rotate(
            @NotNull Node<E> node,
            @NotNull Direction direction
    ) {
        Node<E> childInOppositeDirection = node.getChild(direction.opposite());
        assert !childInOppositeDirection.isNil();
        Node<E> rotatedNode = builder(node).child(direction.opposite(), childInOppositeDirection.getChild(direction)).build();
        Node<E> replacementNode = builder(childInOppositeDirection).child(direction, rotatedNode).build();
        return replacementNode;
    }

    @Override
    public PersistentRedBlackBST<E> remove(E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E ceiling(E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E floor(E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E higher(E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E lower(E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(E element) {
        LookUpPath<E> lookUpPath = LookUpPath.performLookUp(root, element);
        return !lookUpPath.getResult().isNil();
    }

    private static interface Callback<E> {
        void exec(E element);
    }

    @NotNull
    public List<E> toList() {
        final List<E> list = new ArrayList<E>();
        traverse(new Callback<E>() {
            @Override
            public void exec(E element) {
                list.add(element);
            }
        });
        return list;
    }

    @NotNull
    public Set<E> toSet() {
        return new HashSet<E>(toList());
    }

    public void traverse(@NotNull Callback<E> callback) {
        recTraverse(callback, root);
    }

    private void recTraverse(@NotNull Callback<E> callback, @NotNull Node<E> node) {
        if (node.isNil()) {
            return;
        }
        recTraverse(callback, node.getLeft());
        callback.exec(node.getValue());
        recTraverse(callback, node.getRight());
    }

}
