package ru.spbau.talanov.advalgo.impl;

import junit.framework.Assert;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.*;

/**
 * @author Pavel Talanov
 */
public final class OperationPerformanceTest {

    @Test
    public void testInsertionOperationPerformance() throws Exception {
        int elementsToAddSize = 10000;
        System.out.println("Testing insertion operation performance:\n" +
                "Number of elements in collection : time took to add an element, measured in nanoseconds " +
                "and averaged over " + elementsToAddSize + " elements");
        Set<Integer> elementsToAdd = randomSetOfSize(elementsToAddSize, Collections.<Integer>emptySet());
        for (int i = 100; i < 2000000; i *= 2) {
            Set<Integer> elementSet = randomSetOfSize(i, elementsToAdd);
            PersistentRedBlackBST<Integer> tree = PersistentRedBlackBST.of(elementSet);
            long start = System.nanoTime();
            for (Integer element : elementsToAdd) {
                PersistentRedBlackBST<Integer> added = tree.add(element);
                Assert.assertNotNull(added);
            }
            long end = System.nanoTime();
            System.gc();
            System.out.println(i + " : " + (end - start) / elementsToAddSize);
        }
    }

    @Test
    public void testDeletionOperationPerformance() throws Exception {
        int elementsToRemoveSize = 10000;
        System.out.println("Testing deletion operation performance:\n" +
                "Number of elements in collection : time took to remove an element, measured in nanoseconds " +
                "and averaged over " + elementsToRemoveSize + " elements");
        Set<Integer> elementsToRemove = randomSetOfSize(elementsToRemoveSize, Collections.<Integer>emptySet());
        for (int i = elementsToRemoveSize * 2; i < 2000000; i *= 2) {
            Set<Integer> elementSet = randomSetOfSize(i - elementsToRemoveSize, elementsToRemove);
            elementSet.addAll(elementsToRemove);
            PersistentRedBlackBST<Integer> tree = PersistentRedBlackBST.of(elementSet);
            long start = System.nanoTime();
            for (Integer element : elementsToRemove) {
                PersistentRedBlackBST<Integer> removed = tree.remove(element);
                Assert.assertNotNull(removed);
            }
            long end = System.nanoTime();
            System.gc();
            System.out.println(i + " : " + (end - start) / elementsToRemoveSize);
        }

    }

    @NotNull
    private Set<Integer> randomSetOfSize(int size, @NotNull Collection<Integer> prohibitedElements) {
        Random random = new Random();
        Set<Integer> result = new HashSet<Integer>();
        while (result.size() < size) {
            int elementToAdd = random.nextInt();
            if (!prohibitedElements.contains(elementToAdd)) {
                result.add(elementToAdd);
            }
        }
        return result;
    }
}
