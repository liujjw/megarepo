package util.digraph;

/**
 * A generic graph vertex/node. Contains a piece of data.
 * */
public class Vertex<T> {
    private T data;

    /**
     * @param d the data that the vertex encapsulates
     * */
    public Vertex(T d) {
        data = d;
    }

    /**
     * @return data of type T that the vertex encapsulates
     * */
    public T getData() {
        return data;
    }

    /**
     * Equality if a vertex references the same vertex.
     * */
    @Override
    public boolean equals(Object obj) {
        return (this == ((Vertex) obj));
    }
}
