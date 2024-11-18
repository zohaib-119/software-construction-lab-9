/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteVerticesGraph implements Graph<String> {
    private final List<Vertex> vertices = new ArrayList<>();

    // Abstraction function and representation invariant
    private void checkRep() {
        for (Vertex vertex : vertices) {
            assert vertex != null;
        }
    }

    @Override
    public boolean add(String vertexLabel) {
        for (Vertex vertex : vertices) {
            if (vertex.getLabel().equals(vertexLabel)) {
                return false;
            }
        }
        vertices.add(new Vertex(vertexLabel));
        checkRep();
        return true;
    }

    @Override
    public int set(String sourceLabel, String targetLabel, int weight) {
        Vertex source = getOrCreateVertex(sourceLabel);
        Vertex target = getOrCreateVertex(targetLabel);

        int previousWeight = source.setEdge(targetLabel, weight);
        checkRep();
        return previousWeight;
    }

    @Override
    public boolean remove(String vertexLabel) {
        boolean removed = vertices.removeIf(vertex -> vertex.getLabel().equals(vertexLabel));
        for (Vertex vertex : vertices) {
            vertex.removeEdge(vertexLabel);
        }
        checkRep();
        return removed;
    }

    @Override
    public Set<String> vertices() {
        Set<String> vertexLabels = new HashSet<>();
        for (Vertex vertex : vertices) {
            vertexLabels.add(vertex.getLabel());
        }
        return vertexLabels;
    }

    @Override
    public Map<String, Integer> sources(String targetLabel) {
        Map<String, Integer> sources = new HashMap<>();
        for (Vertex vertex : vertices) {
            Integer weight = vertex.getEdge(targetLabel);
            if (weight != null && weight > 0) {
                sources.put(vertex.getLabel(), weight);
            }
        }
        return sources;
    }

    @Override
    public Map<String, Integer> targets(String sourceLabel) {
        for (Vertex vertex : vertices) {
            if (vertex.getLabel().equals(sourceLabel)) {
                return vertex.getEdges();
            }
        }
        return Collections.emptyMap();
    }

    @Override
    public String toString() {
        return "Vertices: " + vertices;
    }

    private Vertex getOrCreateVertex(String label) {
        for (Vertex vertex : vertices) {
            if (vertex.getLabel().equals(label)) {
                return vertex;
            }
        }
        Vertex newVertex = new Vertex(label);
        vertices.add(newVertex);
        return newVertex;
    }

    private static class Vertex {
        private final String label;
        private final Map<String, Integer> edges = new HashMap<>();

        public Vertex(String label) {
            this.label = label;
        }

        public String getLabel() { return label; }

        public int setEdge(String target, int weight) {
            if (weight == 0) {
                return edges.remove(target) != null ? weight : 0;
            }
            return edges.put(target, weight);
        }

        public void removeEdge(String target) {
            edges.remove(target);
        }

        public Integer getEdge(String target) {
            return edges.get(target);
        }

        public Map<String, Integer> getEdges() {
            return new HashMap<>(edges); // defensive copy
        }

        @Override
        public String toString() {
            return label + " -> " + edges;
        }
    }
}
