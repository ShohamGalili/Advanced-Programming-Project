package configs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graph.Agent;
import graph.Topic;
import graph.TopicManagerSingleton;
import graph.TopicManagerSingleton.TopicManager;

/**
 * Represents a graph of nodes, where each node corresponds to a topic or an agent.
 * This class provides methods to check for cycles, print the graph, and create the graph from topics.
 */
public class Graph extends ArrayList<Node> {

    /** List of topics in the graph */
    public List<Topic> topics;

    /** Map of node names to node instances */
    private Map<String, Node> nodes;

    /**
     * Constructor for the Graph class.
     */
    public Graph() {
        super();
    }

    /**
     * Checks if the graph has cycles.
     *
     * @return true if the graph has cycles, false otherwise.
     */
    public boolean hasCycles() {
        for (Node node : this) {
            if (node.hasCycles() == true)
                return true;
        }
        return false;
    }

    /**
     * Prints the graph to the standard output, showing the edges of each node.
     */
    public void printGraph() {
        for (Node node : this) {
            System.out.print(node.getName() + " -> ");
            for (Node edge : node.getEdges()) {
                System.out.print(edge.getName() + " ");
            }
            System.out.println();
        }
    }

    /**
     * Creates the graph from the topics managed by the TopicManagerSingleton.
     * Each topic and agent is represented as a node in the graph.
     */
    public void createFromTopics() {
        TopicManager tm = TopicManagerSingleton.get();

        // Get all topics
        Topic[] topics = tm.getTopics().values().toArray(new Topic[0]);

        // Create a hashmap to remember the nodes
        HashMap<String, Node> nodes = new HashMap<>();

        // For each topic, create a node
        for (Topic curTopic : topics) {
            // Create a node for the topic if it doesn't exist
            Node curNode = nodes.get("T" + curTopic.getName());
            if (curNode == null) {
                curNode = new Node("T" + curTopic.getName());
                nodes.put(curNode.getName(), curNode);
                this.add(curNode);
            }

            // Get the subscribers of the topic
            for (Agent agent : curTopic.getSubs()) {
                // Create a node for the subscriber agent if it doesn't exist
                Node subNode = nodes.get("A" + agent.getName());
                if (subNode == null) {
                    subNode = new Node("A" + agent.getName());
                    nodes.put(subNode.getName(), subNode);
                    this.add(subNode);
                }
                // Add an edge from the topic node to the subscriber node
                curNode.addEdge(subNode);
            }

            // Get the publishers of the topic
            for (Agent agent : curTopic.getPubs()) {
                // Create a node for the publisher agent if it doesn't exist
                Node pubNode = nodes.get("A" + agent.getName());
                if (pubNode == null) {
                    pubNode = new Node("A" + agent.getName());
                    nodes.put(pubNode.getName(), pubNode);
                    this.add(pubNode);
                }
                // Add an edge from the publisher node to the topic node
                pubNode.addEdge(curNode);
            }
        }

        this.nodes = nodes;
    }

    /**
     * Retrieves a node from the graph by its name.
     * If the node does not exist, creates a new node and adds it to the graph.
     *
     * @param name The name of the node.
     * @return The node with the given name.
     */
    private Node getNode(String name) {
        for (Node n : this) {
            if (n.getName().equals(name))
                return n;
        }

        Node n = new Node(name);
        this.add(n);
        return n;
    }

    /**
     * Returns the map of nodes in the graph.
     *
     * @return The map of nodes in the graph.
     */
    public Map<String, Node> getNodes() {
        return this.nodes;
    }
}
