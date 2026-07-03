package Algorithms;

import Model.AdjacencyMatrix;
import java.util.ArrayList;
import java.util.List;

public class BellmanFord {

    public static List<Node> bellmanFord(AdjacencyMatrix graph, int startVertex) {
        int size = graph.vertexCount();

        if (startVertex < 0 || startVertex >= size)
            throw new IllegalArgumentException("Vértice de origen fuera de rango: " + startVertex);

        double[] distances = new double[size];
        for (int i = 0; i < size; i++)
            distances[i] = Double.POSITIVE_INFINITY;
        distances[startVertex] = 0;

        for (int i = 0; i < size - 1; i++) {
            for (int u = 0; u < size; u++) {
                for (int v = 0; v < size; v++) {
                    if (graph.hasEdge(u, v)) {
                        double weight = graph.getWeight(u, v);
                        if (distances[u] != Double.POSITIVE_INFINITY
                                && distances[u] + weight < distances[v]) {
                            distances[v] = distances[u] + weight;
                            System.out.println("Relajando arista " + u + "-" + v
                                    + ", distancia actualizada a " + v + ": " + distances[v]);
                        }
                    }
                }
            }
        }

        for (int u = 0; u < size; u++) {
            for (int v = 0; v < size; v++) {
                if (graph.hasEdge(u, v)) {
                    double weight = graph.getWeight(u, v);
                    if (distances[u] != Double.POSITIVE_INFINITY
                            && distances[u] + weight < distances[v]) {
                        throw new IllegalStateException(
                                "El grafo contiene un ciclo de peso negativo.");
                    }
                }
            }
        }

        List<Node> result = new ArrayList<Node>();
        for (int v = 0; v < size; v++)
            result.add(new Node(v, distances[v]));

        return result;
    }
}
