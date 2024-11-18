package poet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import graph.Graph;

/**
 * A graph-based poetry generator.
 */
public class GraphPoet {
    private final Graph<String> graph = Graph.empty();

    // Abstraction function:
    //   The graph represents a word affinity graph where vertices are words and edges
    //   are weighted by the adjacency count of the words in the provided corpus.
    // Representation invariant:
    //   Graph vertices must be non-empty, case-insensitive strings, and edges must have positive weights.
    // Safety from rep exposure:
    //   The graph field is private and final. No mutable references to the graph are exposed.

    /**
     * Create a new poet with the graph from the corpus (as described above).
     *
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
        List<String> words = parseWords(Files.readString(corpus.toPath()));

        for (int i = 0; i < words.size() - 1; i++) {
            String word1 = words.get(i);
            String word2 = words.get(i + 1);

            // Add the vertices to the graph
            graph.add(word1);
            graph.add(word2);

            // Update the weight of the edge
            int currentWeight = graph.targets(word1).getOrDefault(word2, 0);
            graph.set(word1, word2, currentWeight + 1);
        }

        checkRep();
    }

    /**
     * Parse words from the given corpus text.
     *
     * @param corpusText the text to parse
     * @return a list of words in order
     */
    private List<String> parseWords(String corpusText) {
        String[] splitWords = corpusText.split("\\s+");
        List<String> words = new ArrayList<>();
        for (String word : splitWords) {
            if (!word.isBlank()) {
                words.add(word.toLowerCase());
            }
        }
        return words;
    }

    /**
     * Generate a poem.
     *
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input) {
        String[] inputWords = input.split("\\s+");
        StringBuilder poemBuilder = new StringBuilder();

        for (int i = 0; i < inputWords.length - 1; i++) {
            String word1 = inputWords[i].toLowerCase();
            String word2 = inputWords[i + 1].toLowerCase();

            // Add the current word to the poem
            poemBuilder.append(inputWords[i]).append(" ");

            // Find the bridge word
            String bridge = findBridgeWord(word1, word2);
            if (bridge != null) {
                poemBuilder.append(bridge).append(" ");
            }
        }

        // Add the last word
        poemBuilder.append(inputWords[inputWords.length - 1]);
        return poemBuilder.toString();
    }

    /**
     * Find the bridge word between two words.
     *
     * @param word1 the first word
     * @param word2 the second word
     * @return the bridge word, or null if none exists
     */
    private String findBridgeWord(String word1, String word2) {
        int maxWeight = 0;
        String bridge = null;

        for (String candidate : graph.targets(word1).keySet()) {
            if (graph.targets(candidate).containsKey(word2)) {
                int weight = graph.targets(word1).get(candidate) + graph.targets(candidate).get(word2);
                if (weight > maxWeight) {
                    maxWeight = weight;
                    bridge = candidate;
                }
            }
        }

        return bridge;
    }

    /**
     * Check the representation invariant.
     */
    private void checkRep() {
        for (String vertex : graph.vertices()) {
            assert !vertex.isBlank() : "Vertex must be non-empty";
            for (Map.Entry<String, Integer> entry : graph.targets(vertex).entrySet()) {
                assert entry.getValue() > 0 : "Edge weights must be positive";
            }
        }
    }

    @Override
    public String toString() {
        return "GraphPoet using graph: " + graph;
    }
}
