package ru.spbau.talanov.advalgo.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.spbau.talanov.advalgo.api.PersistentNavigableSet;

import java.util.*;

import static ru.spbau.talanov.advalgo.impl.Direction.LEFT;
import static ru.spbau.talanov.advalgo.impl.Direction.RIGHT;

/**
 * @author Pavel Talanov
 */
@SuppressWarnings("RedundantTypeArguments")
public final class PersistentRedBlackBST<E extends Comparable<? super E>> implements PersistentNavigableSet<E, PersistentRedBlackBST<E>> {

    @NotNull
    public static <E extends Comparable<? super E>> PersistentRedBlackBST<E> empty() {
        return new PersistentRedBlackBST<E>(Node.<E>nil());
    }

    @NotNull
    public static <E extends Comparable<? super E>> PersistentRedBlackBST<E> of(@NotNull Collection<E> elements) {
        PersistentRedBlackBST<E> tree = PersistentRedBlackBST.<E>empty();
        for (E element : elements) {
            if (element == null) {
                throw new NullPointerException();
            }
            PersistentRedBlackBST<E> newTree = tree.add(element);
            if (newTree != null) {
                tree = newTree;
            }
        }
        return tree;
    }

    @NotNull
    public static <E extends Comparable<? super E>> PersistentRedBlackBST<E> of(E... elements) {
        return of(Arrays.asList(elements));
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
        assert VerifyUtils.verifyProperties(result.root);
        return result;
    }

    @NotNull
    private PersistentRedBlackBST<E> insertCase1(@NotNull LookUpPath<E> lookUpPath, @NotNull Node<E> newNode) {
        assert newNode.isRed();
        if (lookUpPath.isRoot()) {
            return new PersistentRedBlackBST<E>(RBTreeNode.builder(newNode).changeColor(Color.BLACK).build());
        }
        return insertCase2(lookUpPath, newNode);
    }

    @NotNull
    private PersistentRedBlackBST<E> insertCase2(@NotNull LookUpPath<E> lookUpPath, @NotNull Node<E> newNode) {
        if (lookUpPath.getFather().isBlack()) {
            Node<E> newRoot = LookUpPath.replaceNode(lookUpPath, newNode);
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
        Node<E> newUncle = RBTreeNode.builder(uncle).changeColor(Color.BLACK).build();

        Node<E> newFather = RBTreeNode.builder(lookUpPath.getFather())
                .child(lookUpPath.getDirectionFromFather(), newNode)
                .changeColor(Color.BLACK)
                .build();

        Node<E> newGrandFather = RBTreeNode.builder(lookUpPath.getGrandFather())
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
        Node<E> newFather = RBTreeNode.builder(father).child(directionFromFather, newNode).build();
        if (directionFromFather == directionFromGrandFather) {
            Node<E> newGrandFather = RBTreeNode.builder(grandFather).child(directionFromGrandFather, newFather).build();
            return insertCase5(lookUpPath, newNode, newFather, newGrandFather, uncle, directionFromGrandFather);
        }
        Node<E> fatherAfterRotation = rotate(newFather, directionFromGrandFather);
        Node<E> newNodeAfterRotation = fatherAfterRotation.getChild(directionFromGrandFather);
        Node<E> grandFatherAfterRotation = RBTreeNode.builder(grandFather).child(directionFromGrandFather, fatherAfterRotation).build();
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

        Node<E> coloredFather = RBTreeNode.builder(father).changeColor(Color.BLACK).build();
        Node<E> coloredGrandFather = RBTreeNode.builder(grandFather).child(direction, coloredFather).changeColor(Color.RED).build();
        Node<E> subTreeRootAfterAllOperations = rotate(coloredGrandFather, direction.opposite());
        //we modified grandparent in the last 2 cases so we have to replace our grandparent after we are done
        lookUpPath.popLast(2);
        Node<E> newRoot = LookUpPath.replaceNode(lookUpPath, subTreeRootAfterAllOperations);
        return new PersistentRedBlackBST<E>(newRoot);
    }

    @NotNull
    private static <E> Node<E> rotate(
            @NotNull Node<E> node,
            @NotNull Direction direction
    ) {
        return rotate(node, direction, null);
    }

    @NotNull
    private static <E> Node<E> rotate(
            @NotNull Node<E> node,
            @NotNull Direction direction,
            @Nullable Mutator<E> mutator
    ) {
        Node<E> childInOppositeDirection = node.getChild(direction.opposite());
        assert !childInOppositeDirection.isNil();
        Node<E> rotatedNode = RBTreeNode.builder(node, mutator)
                .child(direction.opposite(), childInOppositeDirection.getChild(direction))
                .build();
        @SuppressWarnings("UnnecessaryLocalVariable")
        Node<E> replacementNode = RBTreeNode.builder(childInOppositeDirection, mutator)
                .child(direction, rotatedNode)
                .build();
        return replacementNode;
    }

    @Override
    @Nullable
    public PersistentRedBlackBST<E> remove(@NotNull E element) {
        PersistentRedBlackBST<E> resultingTree = doRemove(element);
        if (resultingTree != null) {
            assert VerifyUtils.verifyProperties(resultingTree.root);
        }
        return resultingTree;
    }

    @Nullable
    private PersistentRedBlackBST<E> doRemove(@NotNull E element) {
        LookUpPath<E> pathFromRootToNodeToDelete = LookUpPath.performLookUp(root, element);
        Node<E> toDelete = pathFromRootToNodeToDelete.getResult();
        if (toDelete.isNil()) {
            return null;
        }
        boolean hasNilChild = toDelete.getRight().isNil() || toDelete.getLeft().isNil();
        if (hasNilChild) {
            return new DeleteNodeMethodObject(pathFromRootToNodeToDelete, null).execute();
        }
        return deleteNodeWithTwoChildren(element, pathFromRootToNodeToDelete);
    }

    @NotNull
    private PersistentRedBlackBST<E> deleteNodeWithTwoChildren(@NotNull E element,
                                                               @NotNull LookUpPath<E> pathFromRootToNodeToDelete) {
        final Node<E> toDelete = pathFromRootToNodeToDelete.getResult();
        //TODO: can be faster... but who cares
        E higher = higher(element);
        assert higher != null;
        LookUpPath<E> pathFromRootToReplacementNode = LookUpPath.performLookUp(root, higher);
        final Node<E> replacementNode = pathFromRootToReplacementNode.getResult();
        return new DeleteNodeMethodObject(pathFromRootToReplacementNode, new Mutator<E>() {
            @NotNull
            @Override
            public Node<E> mutate(@NotNull Node<E> node) {
                if (node.isNil() || !node.getValue().equals(toDelete.getValue())) {
                    return node;
                } else {
                    return RBTreeNode.builder(node).value(replacementNode.getValue()).build();
                }
            }
        }).execute();
    }

    private class DeleteNodeMethodObject {
        @NotNull
        private final LookUpPath<E> path;
        @Nullable
        private final Mutator<E> mutator;

        private DeleteNodeMethodObject(@NotNull LookUpPath<E> path, @Nullable Mutator<E> mutator) {
            this.path = path;
            this.mutator = mutator;
        }

        @NotNull
        private PersistentRedBlackBST<E> execute() {
            Node<E> toDelete = path.getResult();
            Node<E> child = getOnlyChild(toDelete);
            //simple cases
            if (toDelete.isRed()) {
                assert child.isBlack();
                Node<E> newRoot = LookUpPath.replaceNode(path, child, mutator);
                return new PersistentRedBlackBST<E>(newRoot);
            }
            if (child.isRed()) {
                assert toDelete.isBlack();
                Node<E> repaintedChild = builder(child).changeColor(Color.BLACK).build();
                Node<E> newRoot = LookUpPath.replaceNode(path, repaintedChild, mutator);
                return new PersistentRedBlackBST<E>(newRoot);
            }
            return complexCases();
        }

        @NotNull
        private Node<E> getOnlyChild(@NotNull Node<E> node) {
            assert node.getLeft().isNil() || node.getRight().isNil();
            return node.getLeft().isNil() ? node.getRight() : node.getLeft();
        }

        @NotNull
        private PersistentRedBlackBST<E> complexCases() {
            Node<E> toDelete = path.getResult();
            return deleteCase1(getOnlyChild(toDelete));

        }

        @NotNull
        private PersistentRedBlackBST<E> deleteCase1(@NotNull Node<E> nodeToReplaceChild) {
            if (!path.isRoot()) {
                Node<E> newFather = builder(path.getFather())
                        .child(path.getDirectionFromFather(), nodeToReplaceChild).build();
                return deleteCase2(newFather);
            }
            return new PersistentRedBlackBST<E>(nodeToReplaceChild);
        }

        @NotNull
        private RBTreeNode.Builder<E> builder(@NotNull Node<E> node) {
            return RBTreeNode.builder(node, mutator);
        }

        @NotNull
        private PersistentRedBlackBST<E> deleteCase2(@NotNull Node<E> father) {
            Direction directionToSibling = path.getDirectionFromFatherToSibling();
            Node<E> sibling = father.getChild(directionToSibling);
            if (sibling.isRed()) {
                Direction directionToRotate = path.getDirectionFromFather();
                Node<E> repaintedSibling = builder(sibling).changeColor(Color.BLACK).build();
                Node<E> repaintedFather = builder(father)
                        .changeColor(Color.RED)
                        .child(directionToSibling, repaintedSibling)
                        .build();
                Node<E> siblingAfterRotation = rotate(repaintedFather, directionToRotate, mutator);
                Node<E> fatherAfterRotation = siblingAfterRotation.getChild(directionToRotate);
                path.popLast(1);
                path.insertNode(directionToRotate, siblingAfterRotation);
                path.insertDummy(directionToRotate);
                return deleteCase3(fatherAfterRotation);
            } else {
                return deleteCase3(father);
            }
        }

        @NotNull
        private PersistentRedBlackBST<E> deleteCase3(@NotNull Node<E> father) {
            Direction directionToSibling = path.getDirectionFromFatherToSibling();
            Node<E> sibling = father.getChild(directionToSibling);
            if (father.isBlack() && sibling.isBlack() && sibling.getLeft().isBlack() && sibling.getRight().isBlack()) {
                Node<E> newSibling = builder(sibling).changeColor(Color.RED).build();
                Node<E> newFather = builder(father).child(directionToSibling, newSibling).build();
                path.popLast(1);
                return deleteCase1(newFather);
            } else {
                return deleteCase4(father);
            }
        }

        @NotNull
        private PersistentRedBlackBST<E> deleteCase4(@NotNull Node<E> father) {
            Direction directionToSibling = path.getDirectionFromFatherToSibling();
            Node<E> sibling = father.getChild(directionToSibling);
            if (sibling.isBlack() && sibling.getLeft().isBlack() && sibling.getRight().isBlack() && father.isRed()) {
                Node<E> repaintedSibling = builder(sibling).changeColor(Color.RED).build();
                Node<E> repaintedFather = builder(father)
                        .changeColor(Color.BLACK)
                        .child(directionToSibling, repaintedSibling)
                        .build();
                path.popLast(1);
                Node<E> newRoot = LookUpPath.replaceNode(path, repaintedFather, mutator);
                return new PersistentRedBlackBST<E>(newRoot);
            } else {
                return deleteCase5(father);
            }

        }

        @NotNull
        private PersistentRedBlackBST<E> deleteCase5(@NotNull Node<E> father) {
            Direction directionToSibling = path.getDirectionFromFatherToSibling();
            Node<E> sibling = father.getChild(directionToSibling);
            if (sibling.getChild(directionToSibling).isBlack() && sibling.getChild(directionToSibling.opposite()).isRed()) {
                Node<E> siblingChild = sibling.getChild(directionToSibling.opposite());
                Node<E> repaintedSiblingChild = builder(siblingChild).changeColor(Color.BLACK).build();
                Node<E> repaintedSibling = builder(sibling).changeColor(Color.RED)
                        .child(directionToSibling.opposite(), repaintedSiblingChild)
                        .build();
                Node<E> siblingAfterRotation = rotate(repaintedSibling, directionToSibling, mutator);
                Node<E> newFather = builder(father).child(directionToSibling, siblingAfterRotation).build();
                return deleteCase6(newFather);
            }
            return deleteCase6(father);
        }

        @NotNull
        private PersistentRedBlackBST<E> deleteCase6(@NotNull Node<E> father) {
            Direction directionToSibling = path.getDirectionFromFatherToSibling();
            Node<E> sibling = father.getChild(directionToSibling);
            Node<E> siblingChild = sibling.getChild(directionToSibling);
            assert siblingChild.isRed();
            Node<E> repaintedSiblingChild = builder(siblingChild).changeColor(Color.BLACK).build();
            Node<E> repaintedSibling = builder(sibling)
                    .color(father.getColor())
                    .child(directionToSibling, repaintedSiblingChild)
                    .build();
            Node<E> repaintedFather = builder(father)
                    .color(Color.BLACK)
                    .child(directionToSibling, repaintedSibling)
                    .build();
            Node<E> newFather = rotate(repaintedFather, directionToSibling.opposite(), mutator);
            path.popLast(1);
            Node<E> newRoot = LookUpPath.replaceNode(path, newFather, mutator);
            return new PersistentRedBlackBST<E>(newRoot);
        }
    }


    @Override
    @Nullable
    public E ceiling(@NotNull E element) {
        LookUpPath<E> lookUpPath = LookUpPath.performLookUp(root, element);
        if (!lookUpPath.getResult().isNil()) {
            return lookUpPath.getResult().getValue();
        }
        Node<E> higherAncestor = lookUpPath.getHigher();
        if (higherAncestor == null) {
            return null;
        }
        return higherAncestor.getValue();
    }

    @Override
    @Nullable
    public E floor(@NotNull E element) {
        LookUpPath<E> lookUpPath = LookUpPath.performLookUp(root, element);
        if (!lookUpPath.getResult().isNil()) {
            return lookUpPath.getResult().getValue();
        }
        Node<E> lowerAncestor = lookUpPath.getLower();
        if (lowerAncestor == null) {
            return null;
        }
        return lowerAncestor.getValue();
    }

    @Override
    @Nullable
    public E higher(@NotNull E element) {
        LookUpPath<E> lookUpPath = LookUpPath.performLookUp(root, element);
        Node<E> higherAncestor = lookUpPath.getHigher();
        Node<E> leastInRightSubTree = findFurthestChildInDirection(lookUpPath.getResult().getRight(), LEFT);
        if (leastInRightSubTree.isNil()) {
            return higherAncestor != null ? higherAncestor.getValue() : null;
        }
        if (higherAncestor == null) {
            return leastInRightSubTree.isNil() ? null : leastInRightSubTree.getValue();
        }
        return higherAncestor.getValue().compareTo(leastInRightSubTree.getValue()) < 0 ? higherAncestor.getValue() : leastInRightSubTree.getValue();
    }

    @Override
    public E lower(@NotNull E element) {
        LookUpPath<E> lookUpPath = LookUpPath.performLookUp(root, element);
        Node<E> lowerAncestor = lookUpPath.getLower();
        Node<E> greatestInLeftSubTree = findFurthestChildInDirection(lookUpPath.getResult().getLeft(), RIGHT);
        if (greatestInLeftSubTree.isNil()) {
            return lowerAncestor != null ? lowerAncestor.getValue() : null;
        }
        if (lowerAncestor == null) {
            return greatestInLeftSubTree.isNil() ? null : greatestInLeftSubTree.getValue();
        }
        return lowerAncestor.getValue().compareTo(greatestInLeftSubTree.getValue()) < 0 ? greatestInLeftSubTree.getValue() : lowerAncestor.getValue();
    }

    @NotNull
    private Node<E> findFurthestChildInDirection(@NotNull Node<E> node, @NotNull Direction direction) {
        Node<E> current = node;
        while (!current.getChild(direction).isNil()) {
            current = current.getChild(direction);
        }
        return current;
    }

    @Override
    public boolean contains(@NotNull E element) {
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

    @Override
    public String toString() {
        return toList().toString();
    }
}
