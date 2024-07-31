package configs;

import graph.Message;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Node class represents a node in the computational graph. Each node has a name, a list of edges to other nodes,
 * and a message associated with it.
 */
public class Node {

    // Define members
    private String name;
    private List<Node> edges;
    private Message msg;

    /**
     * Constructor to initialize a node with a given name.
     *
     * @param name The name of the node.
     */
    public Node(String name) {
        this.name = name;
        this.edges = new ArrayList<Node>();
    }

    // Getters and Setters

    /**
     * Gets the name of the node.
     *
     * @return The name of the node.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the edges (connections) of the node.
     *
     * @return The list of edges.
     */
    public List<Node> getEdges() {
        return edges;
    }

    /**
     * Gets the message associated with the node.
     *
     * @return The message.
     */
    public Message getMsg() {
        return msg;
    }

    /**
     * Sets the name of the node.
     *
     * @param name The new name of the node.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the edges (connections) of the node.
     *
     * @param edges The new list of edges.
     */
    public void setEdges(List<Node> edges) {
        this.edges = edges;
    }

    /**
     * Sets the message associated with the node.
     *
     * @param msg The new message.
     */
    public void setMsg(Message msg) {
        this.msg = msg;
    }

    /**
     * Adds an edge (connection) to another node.
     *
     * @param n The node to connect to.
     */
    public void addEdge(Node n) {
        this.edges.add(n);
    }

    /**
     * Checks if the graph has cycles starting from this node.
     *
     * @return true if a cycle is detected, false otherwise.
     */
    public boolean hasCycles() {
        Set<Node> visited = new HashSet<>();
        Set<Node> recStack = new HashSet<>();
        return hasCycles(this, visited, recStack);
    }

    /**
     * Helper method to check for cycles in the graph using DFS.
     *
     * @param node     The current node being visited.
     * @param visited  Set of visited nodes.
     * @param recStack Stack of nodes in the current DFS path.
     * @return true if a cycle is detected, false otherwise.
     */
    private boolean hasCycles(Node node, Set<Node> visited, Set<Node> recStack) {
        if (recStack.contains(node)) {
            return true; // Cycle detected
        }

        if (visited.contains(node)) {
            return false; // Node has been completely processed
        }

        visited.add(node);
        recStack.add(node);

        for (Node neighbor : node.getEdges()) {
            if (hasCycles(neighbor, visited, recStack)) {
                return true;
            }
        }

        recStack.remove(node);
        return false;
    }

    /**
     * Adds an input topic to this node.
     *
     * @param sub The name of the input topic.
     */
    public void addInput(String sub) {
        Node inputNode = new Node("T" + sub);
        inputNode.addEdge(this);
    }

    /**
     * Adds an output topic to this node.
     *
     * @param pub The name of the output topic.
     */
    public void addOutput(String pub) {
        Node outputNode = new Node("T" + pub);
        this.addEdge(outputNode);
    }
}
