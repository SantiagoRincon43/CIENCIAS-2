package Algorithms;

import Model.AdjacencyList;
import Model.Graph;

public class BrelazDemo {

    public static void main(String[] args) {
        Graph graph = new AdjacencyList(6, false);

        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 5);
        graph.addEdge(2, 5);

        Brelaz brelaz = new Brelaz(graph);
        int[] colors = brelaz.runAlgorithm();
        brelaz.printResult(colors);
    }
}
