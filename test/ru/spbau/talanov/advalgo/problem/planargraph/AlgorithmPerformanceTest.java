package ru.spbau.talanov.advalgo.problem.planargraph;

import org.junit.Test;
import ru.spbau.talanov.advalgo.api.PersistentNavigableSet;
import ru.spbau.talanov.advalgo.impl.PersistentRedBlackBST;
import ru.spbau.talanov.advalgo.problem.algorithm.Algorithm;
import ru.spbau.talanov.advalgo.problem.algorithm.EdgeAsFunction;
import ru.spbau.talanov.advalgo.problem.comparison.TreeSetBasedPersistentSet;

/**
 * @author Pavel Talanov
 */
public class AlgorithmPerformanceTest {

    @Test
    public void testPerformance() throws Exception {
        System.out.println("Testing performance of building stage of algorithm: \n");
        System.out.println("Uniformly distributed graph:");
        printLegend();
        for (int graphSize = 2; graphSize < 1000000; graphSize *= 2) {
            if (report(graphSize, graphSize, 5000)) break;
        }
        System.out.println("\n'Narrow' graph:");
        printLegend();
        for (int graphSize = 10; graphSize < 1000000; graphSize *= 2) {
            if (report(graphSize, 10, 2000)) break;
        }
        System.out.println("\n'Tall' graph:");
        printLegend();
        for (int graphSize = 10; graphSize < 1000000; graphSize *= 2) {
            if (report(10, graphSize, 10000)) break;
        }

    }

    private void printLegend() {
        System.out.println("(horizontal size of graph) x (vertical size of graph)" +
                " | (time took to build data structure using TreeSetBasedPersistentSet in millis) " +
                ": (time took to build data structure using PersistentRedBlackBST in millis)");
    }

    private boolean report(int graphXSize, int graphYSize, int timeLimit) {
        int graphSparsity = 5;
        long timeSpentUsingPersistentTrees = measureTimeInMillis(graphSparsity, graphXSize, graphYSize,
                PersistentRedBlackBST.<EdgeAsFunction>empty());
        long timeSpentUsingBasicStructures = measureTimeInMillis(graphSparsity, graphXSize, graphYSize,
                TreeSetBasedPersistentSet.<EdgeAsFunction>empty());
        System.out.println(graphXSize + " x " + graphYSize + " | " +
                timeSpentUsingBasicStructures + " : " + timeSpentUsingPersistentTrees);
        return timeSpentUsingBasicStructures >= timeLimit || timeSpentUsingPersistentTrees > timeLimit;
    }

    private long measureTimeInMillis(int graphSparsity, int graphXSize, int graphYSize,
                                     PersistentNavigableSet<EdgeAsFunction, ?> set) {
        GridGraph gridGraph = new GridGraph(graphSparsity, graphSparsity, graphXSize, graphYSize);
        Algorithm algorithm = new Algorithm(gridGraph, set);
        long start = System.nanoTime();
        algorithm.prepareDataStructure();
        long end = System.nanoTime();
        System.gc();
        return (end - start) / 1000000;
    }
}
