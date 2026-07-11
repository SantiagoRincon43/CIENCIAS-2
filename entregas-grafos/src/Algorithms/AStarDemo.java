package Algorithms;

import Model.AdjacencyList;
import Model.Graph;
import java.util.List;

public class AStarDemo {
    public static void main(String[] args) {
        Graph g = new AdjacencyList(6, false);
        g.addEdge(0, 1, 2);
        g.addEdge(0, 2, 5);
        g.addEdge(1, 2, 1);
        g.addEdge(1, 3, 6);
        g.addEdge(2, 3, 2);
        g.addEdge(2, 4, 4);
        g.addEdge(3, 5, 1);
        g.addEdge(4, 5, 2);

        double[] heuristic = { 6, 5, 3, 1, 2, 0 };

        AStar aStar = new AStar(g);
        List<Integer> path = aStar.search(0, 5, heuristic);

        if (path != null) {
            System.out.println("Path: " + path);
        } else {
            System.out.println("No se encontró un camino");
        }
    }
}
