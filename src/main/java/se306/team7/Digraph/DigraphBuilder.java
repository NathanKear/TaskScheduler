package se306.team7.Digraph;

import java.util.HashMap;

import se306.team7.Metrics;

public class DigraphBuilder implements IDigraphBuilder {
    private String _digraphName;
    private HashMap<String, Node> _currentNodes = new HashMap<String, Node>();

    public IDigraphBuilder setName(String digraphName) {
        _digraphName = digraphName;
        return this;
    }

    /**
     * Creates a new node and adds it to the Digraph
     * @param nodeName The name of the node
     * @param cost The time taken to complete the node's task
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
     */
    public IDigraphBuilder addLink(String origin, String destination, int cost) {
        Node originNode = _currentNodes.get(origin);
        Node destinationNode = _currentNodes.get(destination);

        originNode.addLink(originNode, origin, destinationNode, cost);
        destinationNode.addLink(originNode, origin, destinationNode, cost);
        return this;
    }

    public IDigraph build() {
        IDigraph digraph = new Digraph(_digraphName, _currentNodes);

       new Metrics(digraph.getNodes().size(), 4); //bogus code
        return digraph;
    }

    public void clear() {
        _digraphName = null;
        _currentNodes.clear();
    }
}
