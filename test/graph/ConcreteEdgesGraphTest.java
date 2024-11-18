import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConcreteEdgesGraphTest {

    private ConcreteEdgesGraph graph;

    @BeforeEach
    public void setUp() {
        graph = new ConcreteEdgesGraph();
    }

    @Test
    public void testAddVertex() {
        // Test adding a single vertex
        assertTrue(graph.add("A"));
        assertTrue(graph.vertices().contains("A"));
        
        // Test adding a duplicate vertex
        assertFalse(graph.add("A"));  // Should not add again
    }

    @Test
    public void testSetEdge() {
        // Test setting an edge with weight
        graph.add("A");
        graph.add("B");
        assertEquals(0, graph.set("A", "B", 5)); // New edge, expect 0
        
        // Test updating an edge weight
        assertEquals(5, graph.set("A", "B", 10)); // Update edge, expect old weight 5
        
        // Test removing an edge (set weight to 0)
        assertEquals(10, graph.set("A", "B", 0)); // Remove edge, expect old weight 10
        Map<String, Integer> sources = graph.sources("B");
        assertTrue(sources.isEmpty()); // No edge should be present anymore
    }

    @Test
    public void testRemoveVertex() {
        graph.add("A");
        graph.add("B");
        graph.set("A", "B", 5);
        
        // Remove vertex A, should remove corresponding edge
        assertTrue(graph.remove("A"));
        assertFalse(graph.vertices().contains("A"));
        Map<String, Integer> sources = graph.sources("B");
        assertTrue(sources.isEmpty()); // No source for B anymore

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
        

        // Test targets for B
        Map<String, Integer> targets = graph.targets("B");
        assertEquals(1, targets.size());
      
    }

    @Test
    public void testToString() {
        graph.add("A");
        graph.add("B");
        graph.set("A", "B", 5);
        String expected = "Vertices: [A, B], Edges: [(A -> B, 5)]";
        assertEquals(expected, graph.toString());
    }

    @Test
    public void testDefensiveCopyOnVertices() {
        graph.add("A");
        Set<String> vertices = graph.vertices();
        vertices.add("B"); // Trying to modify the returned set
        // The original graph should not contain "B"
        assertFalse(graph.vertices().contains("B"));
    }
}
