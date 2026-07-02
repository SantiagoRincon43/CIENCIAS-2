package Model;


import java.util.ArrayList;
import java.util.Arrays;

public class IncidenceMatrix implements Graph {


    private static final int NO_INCIDENT  =  0;
    private static final int INCIDENT     =  1;
    private static final int OUTGOING     = -1;
    private static final int INCOMING     =  1;

    private final int     vertexCount;
    private final boolean directed;
    private final ArrayList<Edge> edgeList;


    private int[][] matrix;
    private int edgeCapacity;
    private int nextEdgeId = 0;


    public IncidenceMatrix(int vertexCount, boolean directed) {
        if (vertexCount < 1)
            throw new IllegalArgumentException("El grafo debe tener al menos 1 vértice.");
        this.vertexCount  = vertexCount;
        this.directed     = directed;
        this.edgeList     = new ArrayList<Edge>();
        this.edgeCapacity = 8;
        this.matrix       = new int[vertexCount][edgeCapacity];
    }

    public IncidenceMatrix(int vertexCount) {
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
        int eid = nextEdgeId++;
        Edge e  = new Edge(eid, source, destination, weight);
        edgeList.add(e);


        if (eid >= edgeCapacity) grow();

        if (!directed) {

            matrix[source][eid]      = INCIDENT;
            matrix[destination][eid] = INCIDENT;
        } else {

            matrix[source][eid]      = OUTGOING;
            matrix[destination][eid] = INCOMING;

            if (source == destination)
                matrix[source][eid] = NO_INCIDENT;
        }
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
        for (int eid = 0; eid < edgeList.size(); eid++)
            if (matrix[vertex][eid] != NO_INCIDENT)
                result.add(edgeList.get(eid));
        return result;
    }

    @Override
    public ArrayList<Integer> neighbors(int vertex) {
        validate(vertex);
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (Edge e : edgesOf(vertex)) {
            int other = (e.getSource() == vertex) ? e.getDestination() : e.getSource();
            result.add(other);
        }
        return result;
    }


    public boolean isIncident(int vertex, int edgeId) {
        validate(vertex);
        if (edgeId < 0 || edgeId >= edgeList.size())
            throw new IllegalArgumentException("Arista fuera de rango: " + edgeId);
        return matrix[vertex][edgeId] != NO_INCIDENT;
    }


    public int getCellValue(int vertex, int edgeId) {
        validate(vertex);
        if (edgeId < 0 || edgeId >= edgeList.size())
            throw new IllegalArgumentException("Arista fuera de rango: " + edgeId);
        return matrix[vertex][edgeId];
    }


    public int[][] getRawMatrix() {
        int E = edgeList.size();
        int[][] copy = new int[vertexCount][E];
        for (int v = 0; v < vertexCount; v++)
            copy[v] = Arrays.copyOf(matrix[v], E);
        return copy;
    }


    private void grow() {
        edgeCapacity *= 2;
        int[][] newMatrix = new int[vertexCount][edgeCapacity];
        for (int v = 0; v < vertexCount; v++)
            System.arraycopy(matrix[v], 0, newMatrix[v], 0, matrix[v].length);
        matrix = newMatrix;
    }

    private void validate(int v) {
        if (v < 0 || v >= vertexCount)
            throw new IllegalArgumentException("Vértice fuera de rango: " + v);
    }
}
