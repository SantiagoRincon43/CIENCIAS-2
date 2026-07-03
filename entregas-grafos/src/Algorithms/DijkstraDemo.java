/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Algorithms;

import Model.AdjacencyList;
import Model.Graph;
import java.util.Arrays;
        

/**
 *
 * @author Familia Guerrero
 */
public class DijkstraDemo {
    public static void main(String[] args) {
        Graph g = new AdjacencyList(4,false);
        g.addEdge(0, 1, 3);
        g.addEdge(0,2,6);
        g.addEdge(0, 3, 7);
        g.addEdge(1, 2, 2);
        g.addEdge(1,3,10);
        g.addEdge(2, 3, 1);
        
        Dijkstra d = new Dijkstra(g);
        double[] distances = d.runAlgorithm(0);
        System.out.println(Arrays.toString(distances));
    }
}
