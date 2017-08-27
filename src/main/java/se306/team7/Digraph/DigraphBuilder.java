package se306.team7.Digraph;

import java.util.HashMap;

public class DigraphBuilder implements IDigraphBuilder {
    private String _digraphName;
    private HashMap<String, Node> _currentNodes = new HashMap<String, Node>();

    /**
     * Sets name of the digraph
     * @param digraphName
     * @return IDigraphBuilder instance with a set digraph name
     */
    public IDigraphBuilder setName(String digraphName) {
        _digraphName = digraphName;
        return this;
    }

    /**
     * Creates a new node and adds it to the Digraph
     * @param nodeName The name of the node
     * @param cost The time taken to complete the node's task
     * @return IDigraphBuilder instance with an additional node
     */
    public IDigraphBuilder addNode(String nodeName, int cost) {
        Node n = new Node(nodeName, cost);
        _currentNodes.put(nodeName, n);
        return this;
    }

    /**
     * Adds a link between nodes in the digraph
     * @param origin The name of the node where the link originates
     * @param destination The name of the node where the link terminates
     * @param cost The cost of transferring the origin node's return value(s) across processors to the destination node
     * @return IDigraphBuilder instance with an additional link
     */
    public IDigraphBuilder addLink(String origin, String destination, int cost) {
        Node originNode = _currentNodes.get(origin);
        Node destinationNode = _currentNodes.get(destination);

        originNode.addLink(originNode, origin, destinationNode, cost);
        destinationNode.addLink(originNode, origin, destinationNode, cost);
        return this;
    }

    /**
     * Constructs a digraph object using the current state of DigraphBuilder
     * @return IDigraph The constructed immutable Digraph object
     */
    public IDigraph build() {
        IDigraph digraph = new Digraph(_digraphName, _currentNodes);

        return digraph;
    }
}
