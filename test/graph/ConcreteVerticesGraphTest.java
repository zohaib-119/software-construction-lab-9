import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ConcreteVerticesGraphTest {

    private ConcreteVerticesGraph graph;

    @BeforeEach
    public void setUp() {
        graph = new ConcreteVerticesGraph();
    }

    @Test
    public void testAddVertex() {
        // Test adding a single vertex
        assertTrue(graph.add("A"));
        Set<String> vertices = graph.vertices();
        assertTrue(vertices.contains("A"));

        // Test adding a duplicate vertex
        assertFalse(graph.add("A"));  // Should not add again
    }

    @Test
    public void testSetEdge() {
        // Test setting an edge between two vertices
        graph.add("A");
        graph.add("B");
        assertEquals(0, graph.set("A", "B", 5));  // New edge, expect 0
        
        // Test updating an existing edge
        assertEquals(5, graph.set("A", "B", 10));  // Update edge, expect old weight 5
        
        // Test removing an edge (set weight to 0)
        assertEquals(10, graph.set("A", "B", 0));  // Remove edge, expect old weight 10
        Map<String, Integer> sources = graph.sources("B");
        assertTrue(sources.isEmpty());  // No source for B anymore
    }

    @Test
    public void testRemoveVertex() {
        graph.add("A");
        graph.add("B");
        graph.set("A", "B", 5);
        
        // Remove vertex A, should remove corresponding edge
        assertTrue(graph.remove("A"));
        Set<String> vertices = graph.vertices();
        assertFalse(vertices.contains("A"));
        
        // Check if the edge to B from A is removed
        Map<String, Integer> sources = graph.sources("B");
        assertTrue(sources.isEmpty());

        // Try removing a non-existing vertex
        assertFalse(graph.remove("C"));
    }

    @Test
    public void testSourcesAndTargets() {
        graph.add("A");
        graph.add("B");
        graph.add("C");

        // Add edges
        graph.set("A", "B", 3);
        graph.set("B", "C", 5);

        // Test sources for B
        Map<String, Integer> sources = graph.sources("B");
        assertEquals(1, sources.size());
        assertEquals(3, sources.get("A"));

        // Test targets for B
        Map<String, Integer> targets = graph.targets("B");
        assertEquals(1, targets.size());
        assertEquals(5, targets.get("C"));
    }

    @Test
    public void testToString() {
        graph.add("A");
        graph.add("B");
        graph.set("A", "B", 5);
        String expected = "Vertices: [A -> {B=5}, B -> {}]";
        assertEquals(expected, graph.toString());
    }

    @Test
    public void testDefensiveCopyOnEdges() {
        graph.add("A");
        graph.add("B");
        graph.set("A", "B", 5);

        // Get edges of vertex A
        Map<String, Integer> edges = graph.targets("A");
        edges.put("C", 10); // Trying to modify the returned map

        // Ensure the original graph's edges are not affected
        Map<String, Integer> originalEdges = graph.targets("A");
        assertEquals(1, originalEdges.size()); // Should still only have one edge (A -> B)
        assertFalse(originalEdges.containsKey("C"));
    }

    @Test
    public void testGetOrCreateVertex() {
        graph.add("A");
        graph.add("B");

        // Creating a new vertex
        graph.set("A", "B", 5);
        
        // Test if vertex "B" was created and the edge is set correctly
        assertTrue(graph.vertices().contains("B"));
        assertEquals(5, graph.targets("A").get("B"));
    }

    @Test
    public void testEmptyGraph() {
        // Test the behavior of the graph when no vertices have been added
        Set<String> vertices = graph.vertices();
        assertTrue(vertices.isEmpty());

        Map<String, Integer> sources = graph.sources("A");
        assertTrue(sources.isEmpty());

        Map<String, Integer> targets = graph.targets("A");
        assertTrue(targets.isEmpty());
    }
}
