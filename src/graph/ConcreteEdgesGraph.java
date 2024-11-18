import java.util.*;

public class ConcreteEdgesGraph implements Graph<String> {
    private final Set<String> vertices = new HashSet<>();
    private final List<Edge> edges = new ArrayList<>();

    // Abstraction function:
    //  - vertices represents the set of all vertex labels in the graph.
    //  - edges represents all directed edges between vertices, with weights.
    //
    // Representation invariant:
    //  - edges list should not contain duplicate edges.
    //  - vertices should contain all vertex labels used in edges.
    //
    // Rep exposure:
    //  - vertices and edges are private and final.
    //  - Defensive copies are returned where necessary.

    private void checkRep() {
        for (Edge edge : edges) {
            assert vertices.contains(edge.getSource());
            assert vertices.contains(edge.getTarget());
            assert edge.getWeight() > 0;
        }
    }

    @Override
    public boolean add(String vertex) {
        boolean added = vertices.add(vertex);
        checkRep();
        return added;
    }

    @Override
    public int set(String source, String target, int weight) {
        if (!vertices.contains(source)) vertices.add(source);
        if (!vertices.contains(target)) vertices.add(target);

        for (Edge edge : edges) {
            if (edge.getSource().equals(source) && edge.getTarget().equals(target)) {
                int oldWeight = edge.getWeight();
                if (weight == 0) {
                    edges.remove(edge);
                } else {
                    edges.remove(edge);
                    edges.add(new Edge(source, target, weight));
                }
                checkRep();
                return oldWeight;
            }
        }
        if (weight > 0) {
            edges.add(new Edge(source, target, weight));
        }
        checkRep();
        return 0;
    }

    @Override
    public boolean remove(String vertex) {
        if (!vertices.remove(vertex)) {
            return false;
        }
        edges.removeIf(edge -> edge.getSource().equals(vertex) || edge.getTarget().equals(vertex));
        checkRep();
        return true;
    }

    @Override
    public Set<String> vertices() {
        return new HashSet<>(vertices); // defensive copy
    }

    @Override
    public Map<String, Integer> sources(String target) {
        Map<String, Integer> sources = new HashMap<>();
        for (Edge edge : edges) {
            if (edge.getTarget().equals(target)) {
                sources.put(edge.getSource(), edge.getWeight());
            }
        }
        return sources;
    }

    @Override
    public Map<String, Integer> targets(String source) {
        Map<String, Integer> targets = new HashMap<>();
        for (Edge edge : edges) {
            if (edge.getSource().equals(source)) {
                targets.put(edge.getTarget(), edge.getWeight());
            }
        }
        return targets;
    }

    @Override
    public String toString() {
        return "Vertices: " + vertices + ", Edges: " + edges;
    }

    private static class Edge {
        private final String source;
        private final String target;
        private final int weight;

        // Constructor
        public Edge(String source, String target, int weight) {
            this.source = source;
            this.target = target;
            this.weight = weight;
        }

        public String getSource() { return source; }
        public String getTarget() { return target; }
        public int getWeight() { return weight; }

        @Override
        public String toString() {
            return "(" + source + " -> " + target + ", " + weight + ")";
        }
    }
}
