package Algorithms;

import Model.Edge;
import Model.Graph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class WellsPowell {

    private final Graph graph;

    public WellsPowell(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("El grafo no puede ser null");
        }
        this.graph = graph;
    }

    private void validate(int v) {
        if (v < 0 || v >= graph.vertexCount())
            throw new IllegalArgumentException("Vértice fuera de rango: " + v);
    }

    private int degree(int vertex) {
        return graph.edgesOf(vertex).size();
    }

    private ArrayList<Integer> orderByDegreeDescending() {
        ArrayList<Integer> vertices = new ArrayList<Integer>();
        for (int v = 0; v < graph.vertexCount(); v++) vertices.add(v);

        Collections.sort(vertices, new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                return Integer.compare(degree(b), degree(a));
            }
        });

        return vertices;
    }

    private HashSet<Integer> neighborsOf(int vertex) {
        HashSet<Integer> result = new HashSet<Integer>();
        for (int n : graph.neighbors(vertex)) result.add(n);
        return result;
    }

    public int[] runAlgorithm() {
        int size = graph.vertexCount();
        int[] colors = new int[size];
        for (int i = 0; i < size; i++) colors[i] = -1;

        ArrayList<Integer> ordered = orderByDegreeDescending();
        int currentColor = 0;

        for (int vertex : ordered) {
            if (colors[vertex] != -1) continue;

            colors[vertex] = currentColor;
            HashSet<Integer> forbidden = new HashSet<Integer>(neighborsOf(vertex));

            for (int candidate : ordered) {
                if (colors[candidate] != -1) continue;
                if (forbidden.contains(candidate)) continue;

                colors[candidate] = currentColor;
                forbidden.addAll(neighborsOf(candidate));
            }

            currentColor++;
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
        System.out.println("Número cromático (Welsh-Powell): " + colorCount(colors));
    }
}
