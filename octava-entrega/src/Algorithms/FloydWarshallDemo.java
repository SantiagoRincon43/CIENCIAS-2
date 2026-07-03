package Algorithms;

import Model.AdjacencyMatrix;
import java.util.List;

public class FloydWarshallDemo {

    public static void main(String[] args) {
        AdjacencyMatrix graph = new AdjacencyMatrix(4, true);

        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 3, 10);
        graph.addEdge(1, 2, 3);
        graph.addEdge(2, 3, 1);

        FloydWarshall fw = new FloydWarshall(graph);

        double[][] distances = fw.getDistances();
        System.out.println("Matriz de distancias mínimas:");
        for (int i = 0; i < graph.vertexCount(); i++) {
            for (int j = 0; j < graph.vertexCount(); j++) {
                if (distances[i][j] == Double.POSITIVE_INFINITY)
                    System.out.print("INF\t");
                else
                    System.out.print(distances[i][j] + "\t");
            }
            System.out.println();
        }

        int source = 0;
        int destination = 3;
        System.out.println("\nDistancia mínima de " + source + " a " + destination + ": "
                + fw.getDistance(source, destination));

        List<Integer> path = fw.getPath(source, destination);
        System.out.println("Camino: " + path);
    }
}
