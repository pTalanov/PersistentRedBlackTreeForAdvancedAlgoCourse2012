package ru.spbau.talanov.advalgo.problem.planargraph;

import junit.framework.Assert;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;
import ru.spbau.talanov.advalgo.api.PersistentNavigableSet;
import ru.spbau.talanov.advalgo.impl.PersistentRedBlackBST;
import ru.spbau.talanov.advalgo.problem.algorithm.Algorithm;
import ru.spbau.talanov.advalgo.problem.algorithm.EdgeAsFunction;
import ru.spbau.talanov.advalgo.problem.comparison.TreeSetBasedPersistentSet;

import java.util.*;

import static ru.spbau.talanov.advalgo.problem.algorithm.QueryResult.*;

/**
 * @author Pavel Talanov
 */
public class AlgorithmTest {


    private List<Vertex> leftFace;
    private List<Vertex> rightFace;
    private List<Vertex> topFace;
    private List<Vertex> bottomFace;

    @Test
    public void testSimpleGraph() throws Exception {
        testTriangle(PersistentRedBlackBST.<EdgeAsFunction>empty());
        testTriangle(TreeSetBasedPersistentSet.<EdgeAsFunction>empty());
    }

    @SuppressWarnings("ConstantConditions")
    private void testTriangle(@NotNull PersistentNavigableSet<EdgeAsFunction, ?> set) {
        final Coordinates v1 = new Coordinates(-3, 2);
        final Coordinates v2 = new Coordinates(-3, -2);
        final Coordinates v3 = new Coordinates(-1, 0);
        PlanarGraph simpleTriangle = new PlanarGraph() {
            Map<Edge, List<Vertex>> faces = new HashMap<Edge, List<Vertex>>();

            {
                addVertex(v1, Collections.<Coordinates>emptyList());
                addVertex(v2, Collections.singletonList(v1));
                addVertex(v3, Arrays.asList(v1, v2));
                List<Vertex> face = Arrays.asList(vertices.get(v1), vertices.get(v2), vertices.get(v3));
                faces.put(getEdge(v1, v3), face);
            }

            @Nullable
            @Override
            public List<Vertex> getFaceUnderEdge(@NotNull Edge edge) {
                return faces.get(edge);
            }
        };

        Algorithm algorithm = new Algorithm(simpleTriangle, set);
        algorithm.prepareDataStructure();

        Edge edge = simpleTriangle.getEdge(v1, v3);
        Assert.assertNotNull(edge);
        List<Vertex> theOnlyFace = simpleTriangle.getFaceUnderEdge(edge);
        Assert.assertNotNull(theOnlyFace);
        Assert.assertEquals(NOTHING, algorithm.answerQuery(0, 0));
        Assert.assertEquals(NOTHING, algorithm.answerQuery(100, 0));
        Assert.assertEquals(NOTHING, algorithm.answerQuery(-100, 0));
        Vertex vertex1 = simpleTriangle.getVertex(v1);
        Assert.assertNotNull(vertex1);
        Assert.assertEquals(vertex(vertex1), algorithm.answerQuery(-3, 2));
        Vertex vertex2 = simpleTriangle.getVertex(v2);
        Assert.assertNotNull(vertex2);
        Assert.assertEquals(vertex(vertex2), algorithm.answerQuery(-3, -2));
        Assert.assertEquals(edge(simpleTriangle.getEdge(v2, v3)), algorithm.answerQuery(-2, -1));
        Assert.assertEquals(edge(simpleTriangle.getEdge(v1, v3)), algorithm.answerQuery(-2, 1));
        Assert.assertEquals(vertex(simpleTriangle.getVertex(v3)), algorithm.answerQuery(-1, 0));
        Assert.assertEquals(edge(simpleTriangle.getEdge(v1, v2)), algorithm.answerQuery(-3, 0));
        Assert.assertEquals(edge(simpleTriangle.getEdge(v1, v2)), algorithm.answerQuery(-3, 1));
        Assert.assertEquals(edge(simpleTriangle.getEdge(v1, v2)), algorithm.answerQuery(-3, -1));
        Assert.assertEquals(face(theOnlyFace), algorithm.answerQuery(-2, 0));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testFourTrianglesGraph() throws Exception {
        int size = 3;
        final Coordinates topLeft = new Coordinates(-size, size);
        final Coordinates topRight = new Coordinates(size, size);
        final Coordinates bottomRight = new Coordinates(size, -size);
        final Coordinates bottomLeft = new Coordinates(-size, -size);
        final Coordinates center = new Coordinates(0, 0);
        PlanarGraph graph = new PlanarGraph() {
            Map<Edge, List<Vertex>> faces = new HashMap<Edge, List<Vertex>>();

            {
                addVertex(topLeft, Collections.<Coordinates>emptyList());
                addVertex(topRight, Collections.singletonList(topLeft));
                addVertex(bottomRight, Collections.singletonList(topRight));
                addVertex(bottomLeft, Arrays.asList(bottomRight, topLeft));
                addVertex(center, Arrays.asList(topLeft, topRight, bottomLeft, bottomRight));
                topFace = Arrays.asList(vertices.get(topLeft), vertices.get(center), vertices.get(topRight));
                faces.put(getEdge(topLeft, topRight), topFace);
                rightFace = Arrays.asList(vertices.get(topRight), vertices.get(center), vertices.get(bottomRight));
                faces.put(getEdge(center, topRight), rightFace);
                leftFace = Arrays.asList(vertices.get(topLeft), vertices.get(center), vertices.get(bottomLeft));
                faces.put(getEdge(center, topLeft), leftFace);
                bottomFace = Arrays.asList(vertices.get(bottomLeft), vertices.get(center), vertices.get(bottomRight));
                faces.put(getEdge(center, bottomLeft), bottomFace);
                faces.put(getEdge(center, bottomRight), bottomFace);
            }

            @Nullable
            @Override
            public List<Vertex> getFaceUnderEdge(@NotNull Edge edge) {
                return faces.get(edge);
            }
        };

        Algorithm algorithm = new Algorithm(graph, PersistentRedBlackBST.<EdgeAsFunction>empty());
        algorithm.prepareDataStructure();

        Assert.assertEquals(NOTHING, algorithm.answerQuery(100, 0));
        Assert.assertEquals(NOTHING, algorithm.answerQuery(-100, 0));
        Assert.assertEquals(vertex(graph.getVertex(center)), algorithm.answerQuery(0, 0));
        Assert.assertEquals(vertex(graph.getVertex(topLeft)), algorithm.answerQuery(-size, size));
        Assert.assertEquals(vertex(graph.getVertex(topRight)), algorithm.answerQuery(size, size));
        Assert.assertEquals(vertex(graph.getVertex(bottomLeft)), algorithm.answerQuery(-size, -size));

        Assert.assertEquals(face(rightFace), algorithm.answerQuery(size - 1, size - 2));
        Assert.assertEquals(face(rightFace), algorithm.answerQuery(size - 1, -size + 2));
        Assert.assertEquals(face(rightFace), algorithm.answerQuery(size - 1, 0));
        Assert.assertEquals(face(leftFace), algorithm.answerQuery(-size + 1, size - 2));
        Assert.assertEquals(face(leftFace), algorithm.answerQuery(-size + 1, -size + 2));
        Assert.assertEquals(face(leftFace), algorithm.answerQuery(-size + 1, 0));
        Assert.assertEquals(face(topFace), algorithm.answerQuery(size - 2, size - 1));
        Assert.assertEquals(face(topFace), algorithm.answerQuery(-size + 2, size - 1));
        Assert.assertEquals(face(topFace), algorithm.answerQuery(0, size - 1));
        Assert.assertEquals(face(bottomFace), algorithm.answerQuery(size - 2, -size + 1));
        Assert.assertEquals(face(bottomFace), algorithm.answerQuery(-size + 2, -size + 1));
        Assert.assertEquals(face(bottomFace), algorithm.answerQuery(0, -size + 1));


        Assert.assertEquals(edge(graph.getEdge(center, topRight)), algorithm.answerQuery(1, 1));
        Assert.assertEquals(edge(graph.getEdge(center, bottomRight)), algorithm.answerQuery(1, -1));
        Assert.assertEquals(edge(graph.getEdge(center, bottomLeft)), algorithm.answerQuery(-1, -1));
        Assert.assertEquals(edge(graph.getEdge(center, topLeft)), algorithm.answerQuery(-1, 1));
        Assert.assertEquals(edge(graph.getEdge(topLeft, bottomLeft)), algorithm.answerQuery(-size, 0));
        Assert.assertEquals(edge(graph.getEdge(topLeft, topRight)), algorithm.answerQuery(0, size));
        Assert.assertEquals(edge(graph.getEdge(bottomRight, bottomLeft)), algorithm.answerQuery(0, -size));
        Assert.assertEquals(edge(graph.getEdge(topRight, bottomRight)), algorithm.answerQuery(size, 0));
    }
}
