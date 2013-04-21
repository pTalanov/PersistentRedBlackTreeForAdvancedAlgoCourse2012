package ru.spbau.talanov.advalgo.problem.planargraph;

import org.jetbrains.annotations.NotNull;

/**
 * @author Pavel Talanov
 */
public final class Edge {

    @NotNull
    private final Vertex vertex1;

    @NotNull
    private final Vertex vertex2;

    public Edge(@NotNull Vertex vertex1, @NotNull Vertex vertex2) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
    }

    public boolean isVertical() {
        return vertex1.getX() == vertex2.getX();
    }

    @NotNull
    public Vertex getVertexWithGreaterX() {
        assert !isVertical();
        return vertex1.getX() > vertex2.getX() ? vertex1 : vertex2;
    }

    @NotNull
    public Vertex getVertexWithLowerX() {
        assert !isVertical();
        return vertex1.getX() > vertex2.getX() ? vertex2 : vertex1;
    }

    @NotNull
    public Vertex getVertex1() {
        return vertex1;
    }

    @NotNull
    public Vertex getVertex2() {
        return vertex2;
    }

    @Override
    public String toString() {
        if (!isVertical()) {
            return getVertexWithLowerX().getCoordinates().toString() + " " +
                    getVertexWithGreaterX().getCoordinates().toString();
        }
        return getVertex1().getCoordinates().toString() + " " + getVertex2().getCoordinates().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        return vertex1.equals(edge.vertex1) && vertex2.equals(edge.vertex2);
    }

    @Override
    public int hashCode() {
        int result = vertex1.hashCode();
        result = 31 * result + vertex2.hashCode();
        return result;
    }
}
