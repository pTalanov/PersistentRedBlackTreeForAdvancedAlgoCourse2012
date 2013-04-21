package ru.spbau.talanov.advalgo.problem.algorithm;

import org.jetbrains.annotations.NotNull;
import ru.spbau.talanov.advalgo.problem.planargraph.Edge;
import ru.spbau.talanov.advalgo.problem.planargraph.Vertex;

import java.util.List;

/**
 * @author Pavel Talanov
 */
public class QueryResult {

    @NotNull
    public static final QueryResult NOTHING = new QueryResult() {
        @Override
        public String toString() {
            return "Nothing";
        }
    };

    @NotNull
    public static QueryResult vertex(@NotNull Vertex vertex) {
        return new VertexResult(vertex);
    }

    @NotNull
    public static QueryResult edge(@NotNull Edge edge) {
        return new EdgeResult(edge);
    }

    @NotNull
    public static QueryResult face(@NotNull List<Vertex> face) {
        return new FaceResult(face);
    }

    public static class VertexResult extends QueryResult {
        @NotNull
        private final Vertex vertex;

        private VertexResult(@NotNull Vertex vertex) {
            this.vertex = vertex;
        }

        @Override
        public String toString() {
            return "VertexResult{" +
                    "vertex=" + vertex +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            VertexResult that = (VertexResult) o;

            return vertex.equals(that.vertex);
        }

        @Override
        public int hashCode() {
            return vertex.hashCode();
        }

    }

    public static class FaceResult extends QueryResult {
        @NotNull
        private final List<Vertex> face;

        public FaceResult(@NotNull List<Vertex> face) {
            this.face = face;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FaceResult face1 = (FaceResult) o;

            return face.equals(face1.face);
        }

        @Override
        public int hashCode() {
            return face.hashCode();
        }

        @Override
        public String toString() {
            return "FaceResult{" +
                    "face=" + face +
                    '}';
        }
    }

    public static class EdgeResult extends QueryResult {

        @NotNull
        private final Edge edge;

        public EdgeResult(@NotNull Edge edge) {
            this.edge = edge;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            EdgeResult that = (EdgeResult) o;

            return edge.equals(that.edge);
        }

        @Override
        public int hashCode() {
            return edge.hashCode();
        }

        @Override
        public String toString() {
            return "EdgeResult{" +
                    "edge=" + edge +
                    '}';
        }
    }

    protected QueryResult() {
    }

}
