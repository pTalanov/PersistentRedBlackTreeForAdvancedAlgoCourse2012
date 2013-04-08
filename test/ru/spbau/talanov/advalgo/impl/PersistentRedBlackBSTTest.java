package ru.spbau.talanov.advalgo.impl;

import junit.framework.Assert;
import org.junit.Test;

import java.util.*;

/**
 * @author Pavel Talanov
 */
public class PersistentRedBlackBSTTest {
    @org.junit.Test
    public void testEmpty() throws Exception {
        PersistentRedBlackBST<Integer> empty = PersistentRedBlackBST.<Integer>empty();
        Assert.assertFalse(empty.contains(1));
        Assert.assertFalse(empty.contains(0));
        Assert.assertFalse(empty.contains(100));
    }

    @Test
    public void testInsertCase1() throws Exception {
        PersistentRedBlackBST<Integer> empty = PersistentRedBlackBST.<Integer>empty();
        PersistentRedBlackBST<Integer> one = empty.add(1);
        Assert.assertNotNull(one);
        Assert.assertFalse(empty.contains(1));
        Assert.assertFalse(empty.contains(0));
        Assert.assertTrue(one.contains(1));
        Assert.assertFalse(one.contains(0));
    }

    @Test
    public void testInsertCase2() throws Exception {
        PersistentRedBlackBST<Integer> empty = PersistentRedBlackBST.<Integer>empty();
        PersistentRedBlackBST<Integer> one = empty.add(1);
        Assert.assertNotNull(one);
        PersistentRedBlackBST<Integer> oneAndZero = one.add(0);
        PersistentRedBlackBST<Integer> oneAndTwo = one.add(2);
        Assert.assertNotNull(oneAndTwo);
        Assert.assertNotNull(oneAndZero);
        Assert.assertTrue(oneAndZero.contains(0));
        Assert.assertTrue(oneAndZero.contains(1));
        Assert.assertFalse(oneAndZero.contains(2));
        Assert.assertFalse(oneAndTwo.contains(0));
        Assert.assertTrue(oneAndTwo.contains(1));
        Assert.assertTrue(oneAndTwo.contains(2));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void testInsertCase3() throws Exception {
        PersistentRedBlackBST<Integer> threeElements = PersistentRedBlackBST.<Integer>empty().add(0).add(-3).add(3);
        Assert.assertNotNull(threeElements);
        PersistentRedBlackBST<Integer> case1 = threeElements.add(-2);
        PersistentRedBlackBST<Integer> case2 = threeElements.add(-4);
        PersistentRedBlackBST<Integer> case3 = threeElements.add(1);
        PersistentRedBlackBST<Integer> case4 = threeElements.add(10);
        List<PersistentRedBlackBST<Integer>> cases = Arrays.asList(case1, case2, case3, case4);
        for (PersistentRedBlackBST<Integer> toCheck : cases) {
            Assert.assertNotNull(toCheck);
            Assert.assertTrue(toCheck.contains(0));
            Assert.assertTrue(toCheck.contains(-3));
            Assert.assertTrue(toCheck.contains(-3));
            Assert.assertFalse(toCheck.contains(-10));
            Assert.assertFalse(toCheck.contains(9));
        }
        Assert.assertTrue(case1.contains(-2));
        Assert.assertTrue(case2.contains(-4));
        Assert.assertTrue(case3.contains(1));
        Assert.assertTrue(case4.contains(10));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void testInsertCase5() throws Exception {
        PersistentRedBlackBST<Integer> insertRight = PersistentRedBlackBST.<Integer>empty().add(0).add(1).add(2);
        PersistentRedBlackBST<Integer> insertLeft = PersistentRedBlackBST.<Integer>empty().add(2).add(1).add(0);
        List<PersistentRedBlackBST<Integer>> cases = Arrays.asList(insertLeft, insertRight);
        for (PersistentRedBlackBST<Integer> toCheck : cases) {
            Assert.assertNotNull(toCheck);
            Assert.assertTrue(toCheck.contains(0));
            Assert.assertTrue(toCheck.contains(1));
            Assert.assertTrue(toCheck.contains(2));
        }
    }

    @Test
    public void testInsertCase4() throws Exception {
        PersistentRedBlackBST<Integer> insertRight = PersistentRedBlackBST.<Integer>empty().add(0).add(2).add(1);
        PersistentRedBlackBST<Integer> insertLeft = PersistentRedBlackBST.<Integer>empty().add(2).add(0).add(1);
        List<PersistentRedBlackBST<Integer>> cases = Arrays.asList(insertLeft, insertRight);
        for (PersistentRedBlackBST<Integer> toCheck : cases) {
            Assert.assertNotNull(toCheck);
            Assert.assertTrue(toCheck.contains(0));
            Assert.assertTrue(toCheck.contains(1));
            Assert.assertTrue(toCheck.contains(2));
        }
    }

    @Test
    public void testToListToSet() throws Exception {
        PersistentRedBlackBST<Integer> someBST = PersistentRedBlackBST.<Integer>empty().add(0).add(2).add(1);
        List<Integer> expected = Arrays.asList(0, 1, 2);
        Assert.assertEquals(expected, someBST.toList());
        Assert.assertEquals(new HashSet<Integer>(expected), someBST.toSet());
    }

    @Test
    public void testMultipleInsertions() throws Exception {
        doTestMultipleInsertions(1000);
        doTestMultipleInsertions(5000);
        doTestMultipleInsertions(10000);
    }

    private void doTestMultipleInsertions(int numberOfInsertions) {
        List<Integer> expected = new ArrayList<Integer>();
        PersistentRedBlackBST<Integer> tree = PersistentRedBlackBST.<Integer>empty();
        for (int i = 0; i < numberOfInsertions; ++i) {
            tree = tree.add(i);
            expected.add(i);
        }
        Assert.assertEquals(expected, tree.toList());
    }

    @Test
    public void testRandomInsertions() throws Exception {
        doTestRandomInsertions(1000);
        doTestRandomInsertions(5000);
        doTestRandomInsertions(10000);
    }

    //TODO: fix random tests
    private void doTestRandomInsertions(int numberOfInsertions) {
        List<Integer> expected = new ArrayList<Integer>();
        Random random = new Random();
        PersistentRedBlackBST<Integer> tree = PersistentRedBlackBST.<Integer>empty();
        for (int i = 0; i < numberOfInsertions; ++i) {
            int randomInt = random.nextInt();
            PersistentRedBlackBST<Integer> newTree = tree.add(randomInt);
            if (newTree != null) {
                tree = newTree;
            }
            expected.add(randomInt);
        }
        Collections.sort(expected);
        Assert.assertEquals(expected, tree.toList());
    }

    @org.junit.Test
    public void testAdd() throws Exception {

    }

    @org.junit.Test
    public void testRemove() throws Exception {

    }

    @SuppressWarnings("ConstantConditions")
    @org.junit.Test
    public void testCeiling() throws Exception {
        PersistentRedBlackBST<Integer> tree = PersistentRedBlackBST.<Integer>empty().add(0).add(10).add(20).add(22).add(23).add(2);
        Assert.assertEquals((Object) 0, tree.ceiling(-1000));
        Assert.assertEquals((Object) 0, tree.ceiling(-1));
        Assert.assertEquals((Object) 0, tree.ceiling(0));
        Assert.assertEquals((Object) 2, tree.ceiling(1));
        Assert.assertEquals((Object) 2, tree.ceiling(2));
        Assert.assertEquals((Object) 10, tree.ceiling(3));
        Assert.assertEquals((Object) 20, tree.ceiling(11));
        Assert.assertEquals((Object) 22, tree.ceiling(21));
        Assert.assertEquals((Object) 22, tree.ceiling(22));
        Assert.assertEquals((Object) 23, tree.ceiling(23));
        Assert.assertEquals(null, tree.ceiling(24));
        Assert.assertEquals(null, tree.ceiling(100));
        Assert.assertEquals(null, tree.ceiling(1000));

    }

    @SuppressWarnings("ConstantConditions")
    @org.junit.Test
    public void testFloor() throws Exception {
        PersistentRedBlackBST<Integer> tree = PersistentRedBlackBST.<Integer>empty().add(0).add(10).add(20).add(22).add(23).add(2);
        Assert.assertEquals(null, tree.floor(-1000));
        Assert.assertEquals(null, tree.floor(-1));
        Assert.assertEquals((Object) 0, tree.floor(0));
        Assert.assertEquals((Object) 0, tree.floor(1));
        Assert.assertEquals((Object) 2, tree.floor(2));
        Assert.assertEquals((Object) 2, tree.floor(3));
        Assert.assertEquals((Object) 10, tree.floor(11));
        Assert.assertEquals((Object) 20, tree.floor(21));
        Assert.assertEquals((Object) 22, tree.floor(22));
        Assert.assertEquals((Object) 23, tree.floor(23));
        Assert.assertEquals((Object) 23, tree.floor(24));
        Assert.assertEquals((Object) 23, tree.floor(100));
    }

    @org.junit.Test
    public void testHigher() throws Exception {
        PersistentRedBlackBST<Integer> tree = PersistentRedBlackBST.<Integer>empty().add(0).add(10).add(20).add(22).add(23).add(2);
        Assert.assertEquals((Object) 0, tree.higher(-1000));
        Assert.assertEquals((Object) 0, tree.higher(-1));
        Assert.assertEquals((Object) 2, tree.higher(0));
        Assert.assertEquals((Object) 2, tree.higher(1));
        Assert.assertEquals((Object) 10, tree.higher(2));
        Assert.assertEquals((Object) 10, tree.higher(3));
        Assert.assertEquals((Object) 20, tree.higher(11));
        Assert.assertEquals((Object) 22, tree.higher(21));
        Assert.assertEquals((Object) 23, tree.higher(22));
        Assert.assertEquals(null, tree.higher(23));
        Assert.assertEquals(null, tree.higher(24));
        Assert.assertEquals(null, tree.higher(100));
        Assert.assertEquals(null, tree.higher(1000));
    }

    @org.junit.Test
    public void testLower() throws Exception {
        PersistentRedBlackBST<Integer> tree = PersistentRedBlackBST.<Integer>empty().add(0).add(10).add(20).add(22).add(23).add(2);
        Assert.assertEquals(null, tree.lower(-1000));
        Assert.assertEquals(null, tree.lower(-1));
        Assert.assertEquals(null, tree.lower(0));
        Assert.assertEquals((Object) 0, tree.lower(1));
        Assert.assertEquals((Object) 0, tree.lower(2));
        Assert.assertEquals((Object) 2, tree.lower(3));
        Assert.assertEquals((Object) 2, tree.lower(10));
        Assert.assertEquals((Object) 10, tree.lower(11));
        Assert.assertEquals((Object) 20, tree.lower(21));
        Assert.assertEquals((Object) 20, tree.lower(22));
        Assert.assertEquals((Object) 22, tree.lower(23));
        Assert.assertEquals((Object) 23, tree.lower(24));
        Assert.assertEquals((Object) 23, tree.lower(100));
    }

    @Test
    public void testOrderingMethods() throws Exception {
        TreeSet<Integer> expected = new TreeSet<Integer>();
        Random random = new Random(3);
        PersistentRedBlackBST<Integer> tree = PersistentRedBlackBST.<Integer>empty();
        int numberOfInsertions = 1000;
        for (int i = 0; i < numberOfInsertions; ++i) {
            int randomInt = random.nextInt(numberOfInsertions * 10);
            PersistentRedBlackBST<Integer> newTree = tree.add(randomInt);
            if (newTree != null) {
                tree = newTree;
            }
            expected.add(randomInt);
        }
        int numberOfQueries = 1000000;
        for (int i = 0; i < numberOfQueries; ++i) {
            int randomInt = random.nextInt(numberOfInsertions * 11);
            Assert.assertEquals(expected.floor(randomInt), tree.floor(randomInt));
            Assert.assertEquals(expected.ceiling(randomInt), tree.ceiling(randomInt));
            Assert.assertEquals(expected.lower(randomInt), tree.lower(randomInt));
            Assert.assertEquals(expected.higher(randomInt), tree.higher(randomInt));
        }
    }
}
