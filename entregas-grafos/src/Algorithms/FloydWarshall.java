package Algorithms;

import Model.AdjacencyMatrix;

public class FloydWarshall {

    private final double[][] distances;
    private final int[][] next;
    private final int size;

    public FloydWarshall(AdjacencyMatrix graph) {
        if (graph == null)
            throw new IllegalArgumentException("El grafo no puede ser null");

        size = graph.vertexCount();
        distances = new double[size][size];
        next = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    distances[i][j] = 0;
                    next[i][j] = -1;
                } else if (graph.hasEdge(i, j)) {
                    distances[i][j] = graph.getWeight(i, j);
                    next[i][j] = j;
                } else {
                    distances[i][j] = Double.POSITIVE_INFINITY;
                    next[i][j] = -1;
                }
            }
        }

        for (int k = 0; k < size; k++) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (distances[i][k] != Double.POSITIVE_INFINITY
                            && distances[k][j] != Double.POSITIVE_INFINITY
                            && distances[i][k] + distances[k][j] < distances[i][j]) {
                        distances[i][j] = distances[i][k] + distances[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }

        for (int i = 0; i < size; i++)
            if (distances[i][i] < 0)
                throw new IllegalStateException("El grafo contiene un ciclo de peso negativo.");
    }

    public double getDistance(int source, int destination) {
        validate(source);
        validate(destination);
        return distances[source][destination];
    }

    public double[][] getDistances() {
        double[][] copy = new double[size][size];
        for (int i = 0; i < size; i++)
            copy[i] = java.util.Arrays.copyOf(distances[i], size);
        return copy;
    }

    public java.util.List<Integer> getPath(int source, int destination) {
        validate(source);
        validate(destination);

        java.util.List<Integer> path = new java.util.ArrayList<Integer>();
        if (next[source][destination] == -1 && source != destination)
            return path;

        int current = source;
        path.add(current);
        while (current != destination) {
            current = next[current][destination];
            path.add(current);
        }
        return path;
    }

    private void validate(int v) {
        if (v < 0 || v >= size)
            throw new IllegalArgumentException("Vértice fuera de rango: " + v);
    }
}
