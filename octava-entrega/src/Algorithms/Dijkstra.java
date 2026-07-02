/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Algorithms;

import Model.Edge;
import Model.Graph;

/**
 *
 * @author Familia Guerrero
 */
public class Dijkstra {
    private Graph graph;
    
    public Dijkstra(Graph graph) { 
       if(graph == null) {
           throw new IllegalArgumentException("El grafo no puede ser null");
       }
       this.graph = graph;
    }
    private void validate(int v) {
        if (v < 0 || v >= graph.vertexCount())
            throw new IllegalArgumentException("Vértice fuera de rango: " + v);
    }
    public double[] runAlgorithm(int startVertex) {
        int size = graph.vertexCount();
        validate(startVertex);
        
        double[] distances = new double[size];
        boolean[] visited = new boolean[size];
        java.util.Arrays.fill(distances, Double.POSITIVE_INFINITY);
        distances[startVertex]  = 0;
        
        for(int i = 0; i < size; i++) {
            double minDistance = Double.POSITIVE_INFINITY;
            int u = -1;
            for (int j = 0; j < size; j++) {
                if(!visited[j] && distances[j] < minDistance) {
                    minDistance = distances[j];
                    u = j;
                }
            }
            if(u == -1)  break;   
            
            visited[u] = true;
            for(Edge e: graph.edgesOf(u)) {
                int v = e.getOther(u);
                if(v == -1 || visited[v]) continue;
                
                double weight = e.getWeight();
                double alt = distances[u] + weight;
                if(alt < distances[v]) {
                    distances[v] = alt;
                }
            }
            
        }
        return distances;
    }
    
    
    
}
