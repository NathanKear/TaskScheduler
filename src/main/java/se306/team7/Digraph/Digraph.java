package se306.team7.Digraph;

import java.util.*;

public class Digraph implements IDigraph {

    private String _digraphName;
    private HashMap<String, Node> _nodeMap;
    private List<Node> _topologicalSortedNodes;
    private HashMap<String, Integer> _criticalPathCosts;

    /**
     * Instantiates an instance of Digraph
     * @param digraphName The name of the Digraph
     */
    public Digraph (String digraphName) {
        _digraphName = digraphName;
        _nodeMap = new HashMap<String, Node>();
    }

    /**
     * Creates a new node and adds it to the Digraph
     * @param name The name of the node
     * @param cost The time taken to complete the node's task
     */
    public void addNode (String name, int cost) {
        Node n = new Node(name, cost);
        _nodeMap.put(name, n);
    }

    /**
     * Adds a link between nodes in the digraph
     * @param originName The name of the node where the link originates
     * @param destinationName The name of the node where the link terminates
     * @param cost The cost of transferring the origin node's return value(s) across processors to the destination node
     */
    public void addLink (String originName, String destinationName, int cost) {
        Node originNode = _nodeMap.get(originName);
        Node destinationNode = _nodeMap.get(destinationName);

        originNode.addLink(originNode, originName, destinationNode, cost);
        destinationNode.addLink(originNode, originName, destinationNode, cost);
    }

    /**
     * Calculates which nodes in the digraph are head nodes (nodes at level 0 of the digraph) and adds them to a
     * list of head nodes
     * @return a list of head nodes
     */
    public List<Node> calculateHeadNodes () {
        ArrayList headNodes = new ArrayList<Node>();
        Iterator it = _nodeMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry nodePair = (Map.Entry)it.next();
            Node n = (Node) nodePair.getValue();

            if (n.isHead()) {
                headNodes.add(n);
            }

            it.remove();
        }
        return headNodes;
    }

    /**
     * Get nodes in digraph in some topological order
     * @return
     */
    public List<Node> getNodes() {
        if (_topologicalSortedNodes == null) {
            _topologicalSortedNodes = topologicallySortDigraph(new ArrayList<Node>(_nodeMap.values()));
        }

        return _topologicalSortedNodes;
    }

    /**
     * Calculate critical costs for all nodes in digraph
     * @param topologicalSortedNodes Lists of nodes in digraph sorted topologically
     * @return
     */
    public HashMap<String, Integer> preCalculateCriticalPathCosts(List<Node> topologicalSortedNodes) {
        HashMap<String, Integer> criticalPathCosts = new HashMap<String, Integer>();

        // iterate through topographically sorted nodes in reverse order
        for (int i = _topologicalSortedNodes.size() - 1; i >= 0; i--) {
            Node iNode = _topologicalSortedNodes.get(i);

            // Get maximum critical path of incoming nodes
            int criticalPathCost = 0;

            for (Node outgoingNode : iNode.getOutgoingNodes()) {
                criticalPathCost = Math.max(criticalPathCost, criticalPathCosts.get(outgoingNode.getName()) + outgoingNode.getCost());
            }

            criticalPathCosts.put(iNode.getName(), criticalPathCost);
        }

        return criticalPathCosts;
    }

    /**
     * Get list of nodes in digraph that is topologically ordered
     */
    public List<Node> topologicallySortDigraph(List<Node> digraphNodes) {
        List<Node> topologicalSortedNodes = new ArrayList<Node>();

        List<Node> nodes = new ArrayList<Node>(digraphNodes);

        while (!nodes.isEmpty()) {
            for (Node node : nodes) {

                boolean isFree = true;

                for (Node incomingNode : node.getIncomingNodes()) {
                    if (!topologicalSortedNodes.contains(incomingNode)) {
                        isFree = false;
                    }
                }

                if (isFree) {
                    topologicalSortedNodes.add(node);
                    nodes.remove(node);
                }
            }
        }

        return topologicalSortedNodes;
    }

    public int getCriticalPathCost(Node node) {

        if (_criticalPathCosts == null) {
            _criticalPathCosts = preCalculateCriticalPathCosts(getNodes());
        }

        return _criticalPathCosts.get(node.getName());
    }

    /**
     * Gets a node in the digraph specified by the node's name
     * @param nodeName The name of the node
     * @return
     */
    public Node getNode (String nodeName) {
        return _nodeMap.get(nodeName);
    }

    /**
     * Compares other object to this object, returns true if they are equal, false if not
     * @param other
     * @return boolean
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Digraph)) {
            return false;
        }

        Digraph d = (Digraph) other;

        boolean nameEqual = d._digraphName.equals(this._digraphName);
        boolean nodesEqual = d._nodeMap.equals(this._nodeMap);

        return nameEqual && nodesEqual;
    }
}
