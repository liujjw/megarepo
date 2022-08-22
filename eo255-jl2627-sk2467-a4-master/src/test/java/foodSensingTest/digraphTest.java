package foodSensingTest;

import org.junit.jupiter.api.Test;
import util.digraph.WeightedDigraph;
import util.digraph.Vertex;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class digraphTest {
    @Test
    void testSmallGraph() {
        WeightedDigraph<Integer> d = new WeightedDigraph<>();
        Vertex<Integer> a = new Vertex<Integer>(5);
        Vertex<Integer> b = new Vertex<Integer>(6);
        d.addVertex(a);
        d.addVertex(b);
        d.addEdge(a,b,20);
        d.addEdge(a,b,30);
        d.addEdge(b,a,10);
        assertTrue(d.getAdjVertices(a).size() == 1);
        assertTrue(d.getAdjVertices(b).size() == 1);
        assertTrue(d.getAdjVertices(a).get(0).getWeight() == 20);
        assertTrue(d.getAdjVertices(b).get(0).getWeight() == 10);
    }
}
