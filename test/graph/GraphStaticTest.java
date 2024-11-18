package graph;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.Set;

import org.junit.Test;
/**
 * Abstract test class for instance methods of Graph<String>.
 * 
 * Tests must use the emptyInstance() method to get fresh empty graphs.
 */
public abstract class GraphStaticTest {

    // Provide an empty instance for testing
    protected abstract Graph<String> emptyInstance();
    
    // Test strategy:
    // - add, remove vertices
    // - add, update, remove edges
    // - check vertices, sources, and targets
    
    @Test
    public void testInitialVerticesEmpty() {
        assertTrue("Expected empty graph", emptyInstance().vertices().isEmpty());
    }

    @Test
    public void testAddVertex() {
        Graph<String> graph = emptyInstance();
        assertTrue("Expected vertex to be added", graph.add("Z"));
        assertTrue("Expected vertices to contain Z", graph.vertices().contains("Z"));
        assertFalse("Vertex already exists, should not add again", graph.add("Z"));
    }

    @Test
    public void testRemoveVertex() {
        Graph<String> graph = emptyInstance();
        graph.add("C");
        assertTrue("Expected vertex C to be removed", graph.remove("C"));
        assertFalse("Expected vertices to not contain C", graph.vertices().contains("C"));
        assertFalse("Non-existent vertex removal should return false", graph.remove("C"));
    }

    @Test
    public void testSetEdgeNewVertices() {
        Graph<String> graph = emptyInstance();
        assertEquals("Expected new edge creation with weight 5", 0, graph.set("C", "D", 4));
        assertTrue("Expected vertices to contain C and D", graph.vertices().contains("C") && graph.vertices().contains("D"));
        assertEquals("Expected weight 4 for edge C -> D", (Integer) 4, graph.targets("C").get("D"));
    }

    @Test
    public void testSetEdgeUpdateWeight() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 5);
        assertEquals("Expected edge weight update from 5 to 3", 5, graph.set("A", "B", 3));
        assertEquals("Expected weight 3 for edge A -> B", (Integer) 3, graph.targets("A").get("B"));
    }

    @Test
    public void testSetEdgeRemove() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 5);
        assertEquals("Expected edge removal by setting weight to 0", 5, graph.set("A", "B", 0));
        assertFalse("Expected no edge from A to B after removal", graph.targets("A").containsKey("B"));
    }

    @Test
    public void testVertices() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        Set<String> vertices = graph.vertices();
        assertEquals("Expected 2 vertices", 2, vertices.size());
        assertTrue("Expected vertices to contain A and B", vertices.contains("A") && vertices.contains("B"));
    }

    @Test
    public void testSources() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 5);
        graph.set("C", "B", 3);
        Map<String, Integer> sources = graph.sources("B");
        assertEquals("Expected 2 sources for vertex B", 2, sources.size());
        assertEquals("Expected source A with weight 5", (Integer) 5, sources.get("A"));
        assertEquals("Expected source C with weight 3", (Integer) 3, sources.get("C"));
    }

    @Test
    public void testTargets() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 5);
        graph.set("A", "C", 3);
        Map<String, Integer> targets = graph.targets("A");
        assertEquals("Expected 2 targets for vertex A", 2, targets.size());
        assertEquals("Expected target B with weight 5", (Integer) 5, targets.get("B"));
        assertEquals("Expected target C with weight 3", (Integer) 3, targets.get("C"));
    }
}
