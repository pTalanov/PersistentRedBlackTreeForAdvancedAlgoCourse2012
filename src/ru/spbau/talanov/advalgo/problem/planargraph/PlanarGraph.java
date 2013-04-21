package ru.spbau.talanov.advalgo.problem.planargraph;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author Pavel Talanov
 *         <p/>
 *         Represents a generic planar graph.
 */
public abstract class PlanarGraph {

    @NotNull
    protected final Map<Coordinates, Vertex> vertices = new HashMap<Coordinates, Vertex>();

    @NotNull
    private final Map<Vertex, List<Edge>> edgeLists = new HashMap<Vertex, List<Edge>>();

    private int nextId = 0;

    public void addVertex(@NotNull Coordinates vertexCoordinates, @NotNull List<Coordinates> adjacentVertexCoordinates) {
        assert !vertices.containsKey(vertexCoordinates);
        Vertex newVertex = new Vertex(nextId++, vertexCoordinates);
        vertices.put(vertexCoordinates, newVertex);
        List<Edge> newVertexEdges = getEdgeList(newVertex);
        for (Coordinates neighbourCoordinates : adjacentVertexCoordinates) {
            Vertex neighbourVertex = vertices.get(neighbourCoordinates);
            assert neighbourVertex != null;
            Edge newEdge = new Edge(newVertex, neighbourVertex);
            newVertexEdges.add(newEdge);
            getEdgeList(neighbourVertex).add(newEdge);
        }
    }

    @NotNull
    public List<Edge> getEdgeList(@NotNull Vertex vertex) {
        List<Edge> result = edgeLists.get(vertex);
        if (result == null) {
            result = new ArrayList<Edge>();
            edgeLists.put(vertex, result);
        }
        return result;
    }

    @Nullable
    public Edge getEdge(@NotNull Coordinates coordinates1, @NotNull Coordinates coordinates2) {
        assert !coordinates1.equals(coordinates2);
        Vertex vertex = vertices.get(coordinates1);
        if (vertex == null) {
            return null;
        }
        List<Edge> edges = getEdgeList(vertex);
        for (Edge edge : edges) {
            if (edge.getVertex1().getCoordinates().equals(coordinates2) || edge.getVertex2().getCoordinates().equals(coordinates2)) {
                return edge;
            }
        }
        return null;
    }

    @Nullable
    public abstract List<Vertex> getFaceUnderEdge(@NotNull Edge edge);

    @NotNull
    public Collection<Vertex> getVertices() {
        return vertices.values();
    }

    @Nullable
    public Vertex getVertex(@NotNull Coordinates coordinates) {
        return vertices.get(coordinates);
    }
}
