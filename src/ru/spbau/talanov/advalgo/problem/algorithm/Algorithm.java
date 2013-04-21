package ru.spbau.talanov.advalgo.problem.algorithm;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.spbau.talanov.advalgo.api.PersistentNavigableSet;
import ru.spbau.talanov.advalgo.problem.planargraph.Coordinates;
import ru.spbau.talanov.advalgo.problem.planargraph.Edge;
import ru.spbau.talanov.advalgo.problem.planargraph.PlanarGraph;
import ru.spbau.talanov.advalgo.problem.planargraph.Vertex;

import java.util.*;

/**
 * @author Pavel Talanov
 */
public final class Algorithm {
    @NotNull
    private final PlanarGraph graph;
    @NotNull
    private PersistentNavigableSet<EdgeAsFunction, ?> set;
    boolean isReadyForQueries = false;
    @NotNull
    private NavigableMap<Integer, PersistentNavigableSet<EdgeAsFunction, ? extends PersistentNavigableSet>> sets =
            new TreeMap<Integer, PersistentNavigableSet<EdgeAsFunction, ? extends PersistentNavigableSet>>();
    @NotNull
    private XProvider xProvider = new XProvider(0);

    public Algorithm(@NotNull PlanarGraph graph,
                     @NotNull PersistentNavigableSet<EdgeAsFunction, ?> set) {
        this.graph = graph;
        this.set = set;
    }

    public void prepareDataStructure() {
        List<Vertex> verticesSortedByXCoordinate = getVerticesSortedByXCoordinate();
        int verticesProcessed = 0;
        int currentX = verticesSortedByXCoordinate.iterator().next().getCoordinates().getX();
        xProvider.setX(currentX);
        PersistentNavigableSet<EdgeAsFunction, ?> lastSet = null;
        while (verticesProcessed < verticesSortedByXCoordinate.size()) {
            Vertex vertex = verticesSortedByXCoordinate.get(verticesProcessed);
            if (vertex.getX() != currentX) {
                assert vertex.getX() > currentX;
                if (lastSet != set) {
                    sets.put(currentX, set);
                    lastSet = set;
                }
                currentX++;
                xProvider.setX(currentX);
            } else {
                processEdges(vertex);
                verticesProcessed++;
            }
        }
        sets.put(currentX + 1, set);
        isReadyForQueries = true;
    }

    private void processEdges(@NotNull Vertex vertex) {
        for (Edge edge : getEdgesEndingHere(vertex)) {
            PersistentNavigableSet<EdgeAsFunction, ?> setWithoutEdge = set.remove(new EdgeAsFunction(xProvider, edge));
            assert setWithoutEdge != null;
            set = setWithoutEdge;
        }
        for (Edge edge : getEdgesStartingHere(vertex)) {
            PersistentNavigableSet<EdgeAsFunction, ?> setWithNewEdge = set.add(new EdgeAsFunction(xProvider, edge));
            assert setWithNewEdge != null;
            set = setWithNewEdge;
        }
    }

    @NotNull
    public QueryResult answerQuery(int queryX, int queryY) {
        assert isReadyForQueries;
        Coordinates queryCoordinates = new Coordinates(queryX, queryY);
        Vertex vertexAtQueryPoint = graph.getVertex(queryCoordinates);
        if (vertexAtQueryPoint != null) {
            return QueryResult.vertex(vertexAtQueryPoint);
        }
        PersistentNavigableSet<EdgeAsFunction, ? extends PersistentNavigableSet> setToQuery
                = getSetCorrespondingToX(queryX);
        if (setToQuery == null) {
            return QueryResult.NOTHING;
        }
        xProvider.setX(queryX);
        EdgeAsFunction dummyEdge = getDummyEdgeToQuery(queryY);
        EdgeAsFunction ceiling = setToQuery.ceiling(dummyEdge);
        if (setToQuery.contains(dummyEdge)) {
            assert ceiling != null;
            return QueryResult.edge(ceiling.getCorrespondingEdge());
        }
        if (ceiling == null) {
            return QueryResult.NOTHING;
        }
        Edge closestEdgeHigherThanQuery = ceiling.getCorrespondingEdge();
        Vertex endOfEdge = endOfEdgeAtX(closestEdgeHigherThanQuery, queryCoordinates.getX());
        if (endOfEdge != null) {
            Edge verticalEdgePointingDown = findVerticalEdgePointingDown(endOfEdge);
            if (verticalEdgePointingDown != null) {
                return QueryResult.edge(verticalEdgePointingDown);
            }
        }
        List<Vertex> faceUnderEdge = graph.getFaceUnderEdge(closestEdgeHigherThanQuery);
        if (faceUnderEdge != null) {
            return QueryResult.face(faceUnderEdge);
        }
        return QueryResult.NOTHING;
    }

    @Nullable
    private Edge findVerticalEdgePointingDown(Vertex vertex) {
        for (Edge edge : graph.getEdgeList(vertex)) {
            if (edge.isVertical()) {
                if ((edge.getVertex1() == vertex) && (edge.getVertex2().getY() < edge.getVertex1().getY())) {
                    return edge;
                }
                if ((edge.getVertex2() == vertex) && (edge.getVertex1().getY() < edge.getVertex2().getY())) {
                    return edge;
                }
            }
        }
        return null;
    }

    @Nullable
    private Vertex endOfEdgeAtX(@NotNull Edge edge, int x) {
        if (edge.getVertex1().getX() == x) {
            return edge.getVertex1();
        }
        if (edge.getVertex2().getX() == x) {
            return edge.getVertex2();
        }
        return null;
    }

    @Nullable
    private PersistentNavigableSet<EdgeAsFunction, ? extends PersistentNavigableSet> getSetCorrespondingToX(int queryX) {
        Map.Entry<Integer, PersistentNavigableSet<EdgeAsFunction, ? extends PersistentNavigableSet>> floorEntry
                = sets.floorEntry(queryX);
        if (floorEntry == null) {
            return null;
        }
        return floorEntry.getValue();
    }

    @NotNull
    private EdgeAsFunction getDummyEdgeToQuery(int queryY) {
        return new EdgeAsFunction(xProvider, new Edge(new Vertex(-1, new Coordinates(Integer.MIN_VALUE, queryY)),
                new Vertex(-1, new Coordinates(Integer.MAX_VALUE, queryY))));
    }

    @NotNull
    private List<Edge> getEdgesEndingHere(@NotNull Vertex vertex) {
        List<Edge> edgesEndingHere = new ArrayList<Edge>();
        for (Edge edge : graph.getEdgeList(vertex)) {
            if (!edge.isVertical() && edge.getVertexWithGreaterX() == vertex) {
                edgesEndingHere.add(edge);
            }
        }
        return edgesEndingHere;
    }

    @NotNull
    private List<Edge> getEdgesStartingHere(@NotNull Vertex vertex) {
        List<Edge> edgesEndingHere = new ArrayList<Edge>();
        for (Edge edge : graph.getEdgeList(vertex)) {
            if (!edge.isVertical() && edge.getVertexWithLowerX() == vertex) {
                edgesEndingHere.add(edge);
            }
        }
        return edgesEndingHere;
    }

    @NotNull
    private List<Vertex> getVerticesSortedByXCoordinate() {
        List<Vertex> sortedVertices = new ArrayList<Vertex>(graph.getVertices());
        Collections.sort(sortedVertices, new Comparator<Vertex>() {
            @Override
            public int compare(Vertex o1, Vertex o2) {
                return o1.getCoordinates().getX() - o2.getCoordinates().getX();
            }
        });
        return sortedVertices;
    }
}
