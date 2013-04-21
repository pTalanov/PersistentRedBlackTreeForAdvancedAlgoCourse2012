package ru.spbau.talanov.advalgo.impl;

import junit.framework.Assert;
import org.junit.Test;

import java.util.*;

/**
 * @author Pavel Talanov
 */
@SuppressWarnings({"unchecked", "RedundantTypeArguments"})
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

    @SuppressWarnings("ConstantConditions")
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

    @SuppressWarnings("ConstantConditions")
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
            assert tree != null;
            tree = tree.add(i);
            expected.add(i);
        }
        assert tree != null;
        Assert.assertEquals(expected, tree.toList());
    }

    @Test
    public void testRandomInsertions() throws Exception {
        doTestRandomInsertions(1000);
        doTestRandomInsertions(5000);
        doTestRandomInsertions(10000);
    }

    private void doTestRandomInsertions(int numberOfInsertions) {
        Set<Integer> expected = new HashSet<Integer>();
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
        Assert.assertEquals(expected, tree.toSet());
    }

    @Test
    public void testDeleteSimpleCases() throws Exception {
        PersistentRedBlackBST<Integer> tree = PersistentRedBlackBST.of(1, 2);
        PersistentRedBlackBST<Integer> onlyOne = tree.remove(2);
        Assert.assertNotNull(onlyOne);
        Assert.assertTrue(onlyOne.contains(1));
        Assert.assertFalse(onlyOne.contains(0));
        Assert.assertFalse(onlyOne.contains(2));
        PersistentRedBlackBST<Integer> onlyTwo = tree.remove(1);
        Assert.assertNotNull(onlyTwo);
        Assert.assertTrue(onlyTwo.contains(2));
        Assert.assertFalse(onlyTwo.contains(0));
        Assert.assertFalse(onlyTwo.contains(1));
    }

    @org.junit.Test
    public void testDeleteCase1() throws Exception {
        PersistentRedBlackBST<Integer> tree = PersistentRedBlackBST.of(1);
        PersistentRedBlackBST<Integer> shouldBeEmpty = tree.remove(1);
        Assert.assertNotNull(shouldBeEmpty);
        Assert.assertFalse(shouldBeEmpty.contains(1));
        Assert.assertFalse(shouldBeEmpty.contains(0));
        Assert.assertFalse(shouldBeEmpty.contains(2));
    }

    @org.junit.Test
    public void testDeleteCase3() throws Exception {
        testDeletion(5, 0, -5, 10, 15, 5);
    }

    @org.junit.Test
    public void testDeleteCase6() throws Exception {
        testDeletion(5, 0, -5, 10, 15, 5, 20);
    }

    @org.junit.Test
    public void testDeleteNodeWithTwoChildren() throws Exception {
        testDeletion(15, 0, -5, 10, 15, 5, 20, 12, 17, 19, -2, -10);
    }

    @org.junit.Test
    public void testDeleteX() throws Exception {

    }

    @org.junit.Test
    public void testDeleteCase4() throws Exception {
        testDeletion(37, 31, 18, 37, 2, 47, 14, 10, 45, 43, 49, 38, 41, 26, 29, 44, 8, 28, 12, 9, 6, 20, 3, 15, 25, 13,
                48, 36, 46, 24, 1, 7, 23, 5, 11, 22, 0, 33, 40, 30, 27, 39, 34, 4, 17, 21, 19, 35, 16, 32, 42);
    }

    @Test
    public void testDeleteCase2() throws Exception {
        testDeletion(1679278649, 1259824289, -149733910, 89436858, -613132576, -1359586503, -36776185, 773028180,
                -1417582576, 1807685345, -1789689117, -1071993015, 2033400247, 1679278649, -116515626, -1820092867,
                -734877522, 591411417, 1383018530, 1968347373, 639824947, -1707140770, -976641413, 1495825759, 1847514640,
                -788175674, -1726807352, 882378330, 36468987, 1348222717, -913080277, 45070360, 1167061312, 1251321015,
                -1712337602, -1693535175, 1463425949, -1355666816, 1473737632, -237575941, 386664044, 507189521, -465151057,
                473812904, 1367689479, 1815182052, -2098164586, -341764529, -638194329, 1013444361, 2036033673);
    }

    @Test
    public void testDeleteCase5() throws Exception {
        testDeletion(-126517511, -577670, -1769283912, 1649844156, -1193618445,
                1785435749, 1191997515, 136489090, -341283291, 1033859583, 474774531, -126517511, -233647992, -2139525898,
                537033717, -1181782513, -1671734779, 1234897040, 119214728, 399292322, -1352911824, 2023458287, 1078380,
                -1251219095, 1181700177, -1825464425, -670368309, -1016456171, -685792833, 796972845, 1515808447);
    }

    private void testDeletion(Integer valueToDelete, Integer... initialValues) {
        List<Integer> values = Arrays.asList(initialValues);
        Assert.assertTrue(values.contains(valueToDelete));
        PersistentRedBlackBST<Integer> tree = PersistentRedBlackBST.of(values);
        PersistentRedBlackBST<Integer> modified = tree.remove(valueToDelete);
        Assert.assertNotNull(modified);
        List<Integer> valuesWithoutElement = new ArrayList<Integer>(values);
        valuesWithoutElement.remove(valueToDelete);
        for (Integer value : valuesWithoutElement) {
            Assert.assertTrue(value + "\n" + valueToDelete + " " + values, modified.contains(value));
        }
        Assert.assertFalse(modified.contains(valueToDelete));
    }

    @Test
    public void testRandomDeletion() throws Exception {
        testRandomDeletionOfSize(50);
        testRandomDeletionOfSize(150);
        testRandomDeletionOfSize(350);
    }

    private void testRandomDeletionOfSize(int collectionSize) {
        Set<Integer> values = new HashSet<Integer>();
        Random random = new Random();
        for (int i = 0; i < collectionSize; ++i) {
            values.add(random.nextInt(collectionSize * 15));
        }
        for (Integer value : values) {
            testDeletion(value, values.toArray(new Integer[]{values.size()}));
        }
    }

    @Test
    public void testDeletionSpecialCase1() throws Exception {
        testDeletion(-1565869671, -270891837, -1311255625, 1346889785, 1855759912, 423164483, -131478385, 1958421467,
                627724950, 80416323, -315790821, -1270897959, 1518993084, -604233991, 897004427, -1436607928, -1343122817,
                -1349550357, 985287142, -1680428407, -314671255, -1101337518, 2016952992, 1596831719, -1163834494, -1149779784,
                -531405737, -1024854744, -1363715315, 487161218, 1467678056, -393101515, -814397052, -755499650, -1810364469,
                -2076885779, -1472358672, -2088032186, 826418436, 1428035454, -1469296289, -1008663771, -1772424181, -1537427070,
                -1653481648, -1203210790, -505080690, -1817732333, -1197331015, 752324734, -653091782, 300166285, 241165453, 1606930187,
                -2006819398, 1825617787, 2167468, -1046021138, -35016422, -296260001, 722253476, 1860023190, -2081227342, -24971925, 1254325696,
                1530375042, -1154786012, 1923127965, 1633964393, -39468965, 383774916, 1827389790, 1592128162, 485946834, 726869788, 569938906, 257826911,
                -1910438313, 1083817050, -1923554027, -1565869671, -673659317, 149947135, 1886171969, -655793056, -636185378, 1756923773, 1731068613, -1965986710,
                1102248992, -1662709322, -356870076, 2012112266, -1337093645, 1672041340, -302715426, 1835784200, -210939811, 208623642, -422321483, 909739689);

    }

    @Test
    public void testDeletionSpecialCase2() throws Exception {
        testDeletion(31, 551, 3, 401, 407, 286, 152, 154, 256, 563, 698, 158, 159, 261, 386, 572, 691, 31, 568, 446, 172,
                43, 315, 46, 641, 521, 49, 290, 54, 431, 663, 205, 610, 342, 204, 70, 340, 748, 470, 78, 469, 625, 624,
                632, 328, 372, 578, 378, 710, 246);

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
        PersistentRedBlackBST<Integer> tree = PersistentRedBlackBST.of(0, 10, 20, 22, 23, 2);
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
        PersistentRedBlackBST<Integer> tree = PersistentRedBlackBST.of(0, 10, 20, 22, 23, 2);
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

    @Test
    public void testReturningNullForInvalidOperations() throws Exception {
        PersistentRedBlackBST<Integer> set = PersistentRedBlackBST.of(1, 2, 3);
        Assert.assertNull(set.remove(4));
        Assert.assertNull(set.remove(0));
        Assert.assertNull(set.remove(100));
        Assert.assertNull(set.add(1));
        Assert.assertNull(set.add(2));
        Assert.assertNull(set.add(3));
    }
}
