package Algorithms;

import Model.AdjacencyMatrix;
import java.util.ArrayList;
import java.util.List;

public class FordFulkerson {
    private AdjacencyMatrix graph;
    private double[][] residual;
    private int size;
    private String[] vertexData;

    public FordFulkerson(AdjacencyMatrix graph) {
        this(graph, null);
    }

    public FordFulkerson(AdjacencyMatrix graph, String[] vertexData) {
        if (graph == null)
            throw new IllegalArgumentException("El grafo no puede ser null");
        this.graph = graph;
        this.size = graph.vertexCount();
        this.residual = graph.getRawMatrix();
        if (vertexData != null) {
            this.vertexData = vertexData;
        } else {
            this.vertexData = new String[size];
            for (int i = 0; i < size; i++)
                this.vertexData[i] = String.valueOf(i);
        }
    }

    public void validate(int v) {
        if (v < 0 || v >= size)
            throw new IllegalArgumentException("Vértice fuera de rango: " + v);
    }

    public List<Integer> dfs(int s, int t) {
        validate(s); validate(t);
        return dfs(s, t, new boolean[size], new ArrayList<Integer>());
    }

    public List<Integer> dfs(int s, int t, boolean[] visited, List<Integer> path) {
        visited[s] = true;
        List<Integer> currentPath = new ArrayList<Integer>(path);
        currentPath.add(s);

        if (s == t)
            return currentPath;

        for (int ind = 0; ind < size; ind++) {
            double val = residual[s][ind];
            if (!visited[ind] && val > 0) {
                List<Integer> resultPath = dfs(ind, t, visited, currentPath);
                if (resultPath != null)
                    return resultPath;
            }
        }

        return null;
    }

    public double fordFulkerson(int source, int sink) {
        validate(source); validate(sink);
        double maxFlow = 0;

        List<Integer> path = dfs(source, sink);
        while (path != null) {
            double pathFlow = Double.POSITIVE_INFINITY;
            for (int i = 0; i < path.size() - 1; i++) {
                int u = path.get(i);
                int v = path.get(i + 1);
                pathFlow = Math.min(pathFlow, residual[u][v]);
            }

            for (int i = 0; i < path.size() - 1; i++) {
                int u = path.get(i);
                int v = path.get(i + 1);
                residual[u][v] -= pathFlow;
                residual[v][u] += pathFlow;
            }

            maxFlow += pathFlow;

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < path.size(); i++) {
                sb.append(vertexData[path.get(i)]);
                if (i < path.size() - 1)
                    sb.append(" -> ");
            }
            System.out.println("Path: " + sb.toString() + " , Flow: " + pathFlow);

            path = dfs(source, sink);
        }

        return maxFlow;
    }
}
