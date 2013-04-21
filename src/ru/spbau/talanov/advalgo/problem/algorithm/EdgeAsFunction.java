package ru.spbau.talanov.advalgo.problem.algorithm;

import org.jetbrains.annotations.NotNull;
import ru.spbau.talanov.advalgo.problem.planargraph.Edge;
import ru.spbau.talanov.advalgo.problem.planargraph.Vertex;

/**
 * @author Pavel Talanov
 *         <p/>
 *         This class allows viewing edge of a graph as function which can have different values at different xs.
 *         X is supplied via XProvider class.
 */
public final class EdgeAsFunction implements Comparable<EdgeAsFunction> {

    @NotNull
    private final XProvider xProvider;

    @NotNull
    private final Edge correspondingEdge;

    public EdgeAsFunction(@NotNull XProvider xProvider, @NotNull Edge correspondingEdge) {
        this.xProvider = xProvider;
        this.correspondingEdge = correspondingEdge;
    }


    @Override
    public int compareTo(EdgeAsFunction other) {
        int result = Double.compare(this.calcValue(), other.calcValue());
        if (result == 0) {
            result = determineBasedOnAngle(other);
        }
        return result;
    }

    private int determineBasedOnAngle(EdgeAsFunction other) {
        int result = Double.compare(this.calcKoef(), other.calcKoef());
        if (this.getCorrespondingEdge().getVertexWithLowerX() == other.getCorrespondingEdge().getVertexWithLowerX()) {
            return result;
        } else if (this.getCorrespondingEdge().getVertexWithGreaterX() == other.getCorrespondingEdge().getVertexWithGreaterX()) {
            return -result;
        }
        return 0;
    }

    private double calcKoef() {
        Vertex left = correspondingEdge.getVertexWithLowerX();
        Vertex right = correspondingEdge.getVertexWithGreaterX();
        return ((double) right.getY() - left.getY()) / ((double) right.getX() - left.getX());
    }

    private double calcValue() {
        Vertex left = correspondingEdge.getVertexWithLowerX();
        return left.getY() + calcKoef() * (xProvider.getX() - left.getX());
    }

    @NotNull
    public Edge getCorrespondingEdge() {
        return correspondingEdge;
    }

    @Override
    public String toString() {
        return correspondingEdge.toString();
    }
}
