package util.digraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Directed graph on some generic data object.
 */
public class WeightedDigraph<T> {
    // impose some notion of ordering on the set of adjacent vertices with a list
    private HashMap<Vertex<T>, ArrayList<WeightedVertex<T>>> adjacencyList;

    /**
     * Init empty graph.
     * */
    public WeightedDigraph() {
        adjacencyList = new HashMap<>();
    }

    /**
     * Add vertex with degree 0 to graph.
     * Does not overwrite existing vertex.
     *
     * @param t vertex of type T to add
     * @return true if added vertex, false if failure
     * */
    public boolean addVertex(Vertex<T> t) {
        if (containsVertex(t)) return false;
        adjacencyList.put(t, new ArrayList<>());
        return true;
    }

    /**
     * Vertex set membership.
     *
     * @parm t vertex to check
     * @return true or false
     * */
    public boolean containsVertex(Vertex<T> t) {
        return adjacencyList.containsKey(t);
    }

    /**
     * Add a directed edge from one vertex to another, but
     * only if both vertices have been added to the graph.
     *
     * @param a source vertex
     * @param b sink vertex
     * @param weight integer weight to go from a to b
     * @return false if failure, true if success
     * */
    public boolean addEdge(Vertex<T> a, Vertex<T> b, int weight) {
        // source or sink doesnt exist
        if (!containsVertex(a) || !containsVertex(b)) return false;
        ArrayList<WeightedVertex<T>> adjVertices = adjacencyList.get(a);
        WeightedVertex<T> wv = new WeightedVertex<>(b, weight);
        // already have an edge, and cannot have a an edge from a to b with diff weight
        if (adjVertices.contains(wv)) return false;
        adjVertices.add(wv);
        return true;
    }

    /**
     * Get list of weighted vertices adjacent to some vertex, ie
     * get the list of vertices that a vertex points to.
     *
     * @param v vertex to get from
     * @return list of adjacent vertices from v, null if vertex is not in graph
     * */
    public ArrayList<WeightedVertex<T>> getAdjVertices(Vertex<T> v) {
        return adjacencyList.get(v);
    }

    /**
     * The set of vertices in the graph.
     *
     * @return a set object containing the vertices
     * */
    public Set<Vertex<T>> vertexSet() {
        return adjacencyList.keySet(); // changes to the keyset reflect in the map and vice versa
    }
}
