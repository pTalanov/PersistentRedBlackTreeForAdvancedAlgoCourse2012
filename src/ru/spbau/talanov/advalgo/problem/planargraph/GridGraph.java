package ru.spbau.talanov.advalgo.problem.planargraph;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Pavel Talanov
 *         <p/>
 *         Represents a uniform grid graph. Each face is a square.
 */
public final class GridGraph extends PlanarGraph {

    private final int yStep;

    public GridGraph(int xStep, int yStep, int xSize, int ySize) {
        this.yStep = yStep;

        for (int i = 0; i < xSize; ++i) {
            for (int j = 0; j < ySize; ++j) {
                int x = i * xStep;
                int y = j * yStep;
                Coordinates vertexCoordinates = new Coordinates(x, y);
                List<Coordinates> adjacentVertices = new ArrayList<Coordinates>();
                if (i > 0) {
                    adjacentVertices.add(new Coordinates((i - 1) * xStep, y));
                }
                if (j > 0) {
                    adjacentVertices.add(new Coordinates(x, (j - 1) * yStep));
                }
                addVertex(vertexCoordinates, adjacentVertices);
            }
        }
    }

    @Nullable
    @Override
    public List<Vertex> getFaceUnderEdge(@NotNull Edge edge) {
        Vertex topLeftCorner = edge.getVertexWithLowerX();
        Vertex topRightCorner = edge.getVertexWithGreaterX();
        Vertex bottomLeftCorner = vertices.get(new Coordinates(topLeftCorner.getX(), topLeftCorner.getY() - yStep));
        Vertex bottomRightCorner = vertices.get(new Coordinates(topRightCorner.getX(), topRightCorner.getY() - yStep));
        if (bottomLeftCorner == null || bottomRightCorner == null) {
            return null;
        }
        return Arrays.asList(topLeftCorner, topRightCorner, bottomRightCorner, bottomLeftCorner);
    }
}
