package Model;

import java.util.ArrayList;

public class IncidenceList implements Graph {

    private final int     vertexCount;
    private final boolean directed;

    private final ArrayList<ArrayList<Edge>> inc;
    private final ArrayList<Edge> edgeList;
    private int nextEdgeId = 0;


    public IncidenceList(int vertexCount, boolean directed) {
        if (vertexCount < 1)
            throw new IllegalArgumentException("El grafo debe tener al menos 1 vértice.");
        this.vertexCount = vertexCount;
        this.directed    = directed;
        this.edgeList    = new ArrayList<Edge>();
        this.inc         = new ArrayList<ArrayList<Edge>>(vertexCount);
        for (int i = 0; i < vertexCount; i++) inc.add(new ArrayList<Edge>());
    }

    public IncidenceList(int vertexCount) {
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
        Edge e = new Edge(nextEdgeId++, source, destination, weight);
        edgeList.add(e);

        inc.get(source).add(e);

        if (!directed && source != destination)
            inc.get(destination).add(e);

        else if (directed && source != destination)
            inc.get(destination).add(e);
        return e;
    }

    @Override
    public ArrayList<Edge> edges() {
        return edgeList;
    }


    @Override
    public ArrayList<Edge> edgesOf(int vertex) {
        validate(vertex);
        return inc.get(vertex);
    }


    public ArrayList<Edge> outEdgesOf(int vertex) {
        validate(vertex);
        ArrayList<Edge> result = new ArrayList<Edge>();
        for (Edge e : inc.get(vertex))
            if (e.getSource() == vertex) result.add(e);
        return result;
    }


    public ArrayList<Edge> inEdgesOf(int vertex) {
        validate(vertex);
        ArrayList<Edge> result = new ArrayList<Edge>();
        for (Edge e : inc.get(vertex))
            if (e.getDestination() == vertex) result.add(e);
        return result;
    }

    @Override
    public ArrayList<Integer> neighbors(int vertex) {
        validate(vertex);
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (Edge e : inc.get(vertex)) {
            int other = (e.getSource() == vertex) ? e.getDestination() : e.getSource();
            result.add(other);
        }
        return result;
    }


    public int degree(int vertex) {
        validate(vertex);
        return inc.get(vertex).size();
    }


    private void validate(int v) {
        if (v < 0 || v >= vertexCount)
            throw new IllegalArgumentException("Vértice fuera de rango: " + v);
    }
}
