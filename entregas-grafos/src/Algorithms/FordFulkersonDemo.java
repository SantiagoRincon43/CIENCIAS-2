package Algorithms;

import Model.AdjacencyMatrix;
import Model.Graph;

public class FordFulkersonDemo {
public static void main(String[] args) {
    Graph g = new AdjacencyMatrix(13, true);
    g.addEdge(0, 1, 17); 
    g.addEdge(0, 2, 14); 
    g.addEdge(0, 3, 14);
    g.addEdge(0, 4, 9); 
    g.addEdge(1, 2, 3);
    g.addEdge(1, 5, 5);
    g.addEdge(1, 6, 7);
    g.addEdge(2, 6, 4);
    g.addEdge(2, 7, 5);
    g.addEdge(3, 4, 5);
    g.addEdge(3, 7, 4);
    g.addEdge(3, 8, 7);
    g.addEdge(4, 8, 7);
    g.addEdge(4, 9, 8);
    g.addEdge(5, 10, 17);
    g.addEdge(6, 10, 11);
    g.addEdge(6, 5,10);
    g.addEdge(7, 8, 2);
    g.addEdge(7, 11, 7);
    g.addEdge(8, 11, 15);
    g.addEdge(9, 11, 9);
    g.addEdge(11, 10, 6);
    g.addEdge(10, 12, 25);
    g.addEdge(11, 12, 29);
    FordFulkerson ff = new FordFulkerson((AdjacencyMatrix) g);
    double maxFlow = ff.fordFulkerson(0, 12);
    System.out.println("Max Flow: " + maxFlow);
}

}
