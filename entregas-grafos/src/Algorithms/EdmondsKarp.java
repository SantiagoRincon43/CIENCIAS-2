package Algorithms;

import Model.Edge;
import Model.Graph;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class EdmondsKarp {
    private Graph graph;
    private double[][] residual;
    private int size;

    public EdmondsKarp(Graph graph) {
        if (graph == null)
            throw new IllegalArgumentException("El grafo no puede ser null");
        this.graph = graph;
        this.size = graph.vertexCount();
        this.residual = new double[size][size];
        for (Edge e : graph.edges()) {
            residual[e.getSource()][e.getDestination()] += e.getWeight();
            if (!graph.isDirected() && e.getSource() != e.getDestination())
                residual[e.getDestination()][e.getSource()] += e.getWeight();
        }
    }

    public void validate(int v) {
        if (v < 0 || v >= size)
            throw new IllegalArgumentException("Vértice fuera de rango: " + v);
    }

    public boolean bfs(int s, int t, int[] parent) {
        boolean[] visited = new boolean[size];
        Queue<Integer> queue = new LinkedList<Integer>();
        queue.add(s);
        visited[s] = true;

        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (int v = 0; v < size; v++) {
                if (!visited[v] && residual[u][v] > 0) {
                    queue.add(v);
                    visited[v] = true;
                    parent[v] = u;
                }
            }
        }

        return visited[t];
    }

    public double runAlgorithm(int source, int sink) {
        validate(source);
        validate(sink);

        int[] parent = new int[size];
        java.util.Arrays.fill(parent, -1);
        double maxFlow = 0;

        while (bfs(source, sink, parent)) {
            double pathFlow = Double.POSITIVE_INFINITY;
            int s = sink;
            while (s != source) {
                pathFlow = Math.min(pathFlow, residual[parent[s]][s]);
                s = parent[s];
            }

            maxFlow += pathFlow;
            int v = sink;
            while (v != source) {
                int u = parent[v];
                residual[u][v] -= pathFlow;
                residual[v][u] += pathFlow;
                v = parent[v];
            }

            ArrayList<Integer> path = new ArrayList<Integer>();
            v = sink;
            while (v != source) {
                path.add(v);
                v = parent[v];
            }
            path.add(source);
            java.util.Collections.reverse(path);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < path.size(); i++) {
                sb.append(path.get(i));
                if (i < path.size() - 1)
                    sb.append(" -> ");
            }
            System.out.println("Path: " + sb.toString() + ", Flow: " + pathFlow);

            java.util.Arrays.fill(parent, -1);
        }

        return maxFlow;
    }
}
