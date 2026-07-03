package Algorithms;

import Model.AdjacencyMatrix;
import java.util.List;

public class BellmanFordDemo {

    public static void main(String[] args) {
        AdjacencyMatrix graph = new AdjacencyMatrix(5, true);

        graph.addEdge(0, 1, 6);
        graph.addEdge(0, 2, 7);
        graph.addEdge(1, 2, 8);
        graph.addEdge(1, 3, 5);
        graph.addEdge(1, 4, -4);
        graph.addEdge(2, 3, -3);
        graph.addEdge(2, 4, 9);
        graph.addEdge(3, 1, -2);
        graph.addEdge(4, 3, 7);
        graph.addEdge(4, 0, 2);

        int startVertex = 0;
        List<Node> result = BellmanFord.bellmanFord(graph, startVertex);

        System.out.println("\nDistancias mínimas desde el vértice " + startVertex + ":");
        for (Node n : result)
            System.out.println("Vértice " + n.getVertex() + " -> " + n.getDistance());
    }
}
