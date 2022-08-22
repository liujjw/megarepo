package util.digraph;

/**
 * Associates a weight with a vertex.
 * */
public class WeightedVertex<T> implements Comparable<WeightedVertex<T>> {
    private Vertex<T> v;
    private int weight;

    /**
     * @param v vertex to contain
     * @param weight integer value determining weight
     * */
    public WeightedVertex(Vertex<T> v, int weight) {
        this.v = v;
        this.weight = weight;
    }

    /**
     * @param n new weight
     * */
    public void setWeight(int n) {
        this.weight = n;
    }

    /**
     * @return integer weight
     * */
    public int getWeight() {
        return weight;
    }

    /**
     * @return vertex contained
     * */
    public Vertex<T> getVertex() {
        return v;
    }

    /**
     * Weighted vertices can be equal if they contain the same vertex,
     * and even if they have different weights they are considered the same
     * vertex.
     * */
    @Override
    public boolean equals(Object obj) {
        return this.v.equals(((WeightedVertex<T>) obj).getVertex());
    }

    /**
     * WARNING: inconsistent for use in sorted sets and maps
     * */
    @Override
    public int compareTo(WeightedVertex<T> o) {
        return this.weight - o.weight;
    }
}
