package Algorithms;

import Model.Edge;
import Model.Graph;
import java.util.HashSet;

public class Voraz {

    private final Graph graph;

    public Voraz(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("El grafo no puede ser null");
        }
        this.graph = graph;
    }

    private void validate(int v) {
        if (v < 0 || v >= graph.vertexCount())
            throw new IllegalArgumentException("Vértice fuera de rango: " + v);
    }

    private HashSet<Integer> neighborColors(int vertex, int[] colors) {
        HashSet<Integer> used = new HashSet<Integer>();
        for (Edge e : graph.edgesOf(vertex)) {
            int other = e.getOther(vertex);
            if (other == -1) continue;
            int c = colors[other];
            if (c != -1) used.add(c);
        }
        return used;
    }

    private int smallestAvailableColor(HashSet<Integer> used) {
        int color = 0;
        while (used.contains(color)) color++;
        return color;
    }

    public int[] runAlgorithm() {
        int size = graph.vertexCount();
        int[] colors = new int[size];
        for (int i = 0; i < size; i++) colors[i] = -1;

        for (int vertex = 0; vertex < size; vertex++) {
            HashSet<Integer> used = neighborColors(vertex, colors);
            colors[vertex] = smallestAvailableColor(used);
        }

        return colors;
    }

    public int colorCount(int[] colors) {
        HashSet<Integer> distinct = new HashSet<Integer>();
        for (int c : colors) distinct.add(c);
        return distinct.size();
    }

    public void printResult(int[] colors) {
        System.out.println("Vértice \tColor");
        for (int v = 0; v < colors.length; v++) {
            System.out.println(v + " \t\t" + colors[v]);
        }
        System.out.println("Número cromático (Secuencial Básico): " + colorCount(colors));
    }
}
