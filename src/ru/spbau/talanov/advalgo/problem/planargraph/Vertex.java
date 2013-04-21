package ru.spbau.talanov.advalgo.problem.planargraph;

import org.jetbrains.annotations.NotNull;

/**
 * @author Pavel Talanov
 */
public final class Vertex {

    private final int uniqueId;

    @NotNull
    private final Coordinates coordinates;

    public Vertex(int uniqueId, @NotNull Coordinates coordinates) {
        this.uniqueId = uniqueId;
        this.coordinates = coordinates;
    }

    public int getX() {
        return coordinates.getX();
    }

    public int getY() {
        return getCoordinates().getY();
    }

    @NotNull
    public Coordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex vertex = (Vertex) o;

        return uniqueId == vertex.uniqueId && coordinates.equals(vertex.coordinates);
    }

    @Override
    public int hashCode() {
        int result = uniqueId;
        result = 31 * result + coordinates.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return uniqueId + ":" + getCoordinates().toString();
    }
}
