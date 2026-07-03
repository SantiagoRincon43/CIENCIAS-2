package Model;

import java.util.ArrayList;
import java.util.Arrays;

public class AdjacencyMatrix implements Graph {

    private final int     vertexCount;
    private final boolean directed;
    private final double[][] matrix;
    private final ArrayList<Edge> edgeList;
    private int nextEdgeId = 0;


    public AdjacencyMatrix(int vertexCount, boolean directed) {
        if (vertexCount < 1)
            throw new IllegalArgumentException("El grafo debe tener al menos 1 vértice.");
        this.vertexCount = vertexCount;
        this.directed    = directed;
        this.matrix      = new double[vertexCount][vertexCount];
        this.edgeList    = new ArrayList<Edge>();
    }

    public AdjacencyMatrix(int vertexCount) {
        this(vertexCount, false);
    }


    @Override public int  vertexCount() { return vertexCount; }
    @Override public int  edgeCount()   { return edgeList.size(); }
    @Override public boolean isDirected(){ return directed; }

    @Override
    public Edge addEdge(int source, int destination) {
        return addEdge(source, destination, 1.0);
    }

    @Override
    public Edge addEdge(int source, int destination, double weight) {
        validate(source); validate(destination);
        if (weight == 0)
            throw new IllegalArgumentException("El peso no puede ser 0 (reservado para 'sin arista').");
        Edge e = new Edge(nextEdgeId++, source, destination, weight);
        edgeList.add(e);
        matrix[source][destination] = weight;
        if (!directed && source != destination)
            matrix[destination][source] = weight;
        return e;
    }

    @Override
    public ArrayList<Edge> edges() {
        return edgeList;
    }

    @Override
    public ArrayList<Edge> edgesOf(int vertex) {
        validate(vertex);
        ArrayList<Edge> result = new ArrayList<Edge>();
        for (Edge e : edgeList)
            if (e.getSource() == vertex || (!directed && e.getDestination() == vertex))
                result.add(e);
        return result;
    }

    @Override
    public ArrayList<Integer> neighbors(int vertex) {
        validate(vertex);
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int j = 0; j < vertexCount; j++)
            if (matrix[vertex][j] != 0) result.add(j);
        return result;
    }


    public boolean hasEdge(int i, int j) {
        validate(i); validate(j);
        return matrix[i][j] != 0;
    }


    public double getWeight(int i, int j) {
        validate(i); validate(j);
        return matrix[i][j];
    }


    public double[][] getRawMatrix() {
        double[][] copy = new double[vertexCount][vertexCount];
        for (int i = 0; i < vertexCount; i++)
            copy[i] = Arrays.copyOf(matrix[i], vertexCount);
        return copy;
    }




    private void validate(int v) {
        if (v < 0 || v >= vertexCount)
            throw new IllegalArgumentException("Vértice fuera de rango: " + v);
    }
}
