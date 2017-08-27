package se306.team7.Digraph;

import java.util.*;

public class Digraph implements IDigraph {

    public String _digraphName;
    private HashMap<String, Node> _nodeMap;
    private List<Node> _topologicalSortedNodes;
    private HashMap<String, Integer> _criticalPathCosts;
    private List<Node> _headNodes;

    /**
     * Instantiates an instance of Digraph, to be used by DigraphBuilder only
     * @param digraphName The name of the Digraph
     */
    public Digraph (String digraphName, HashMap<String, Node> nodeMap){
        _digraphName = digraphName;
        _nodeMap = nodeMap;
        _headNodes = calculateHeadNodes();
        _topologicalSortedNodes = topologicallySortDigraph(_nodeMap.values());
        _criticalPathCosts = preCalculateCriticalPathCosts(_topologicalSortedNodes);
    }

    /**
     * Calculates which nodes in the digraph are head nodes (nodes at level 0 of the digraph) and adds them to a
     * list of head nodes
     * @return a list of head nodes
     */
    private List<Node> calculateHeadNodes () {
        ArrayList headNodes = new ArrayList<Node>();
        Iterator it = _nodeMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry nodePair = (Map.Entry)it.next();
            Node n = (Node) nodePair.getValue();

            if (n.isHead()) {
                headNodes.add(n);
            }
        }
        return headNodes;
    }

    /**
     * Get nodes in digraph in some topological order
     * @return List<Node>
     */
    public List<Node> getNodes() {
        return new ArrayList<Node>(_topologicalSortedNodes);
    }

    /**
     * Calculate critical costs for all nodes in digraph
     * @param topologicalSortedNodes Lists of nodes in digraph sorted topologically
     * @return HashMap<String>
     */
    private HashMap<String, Integer> preCalculateCriticalPathCosts(List<Node> topologicalSortedNodes) {
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
     * @param digraphNodes
     * @return List<Node>
     */
    private List<Node> topologicallySortDigraph(Collection<Node> digraphNodes) {
        List<Node> topologicalSortedNodes = new ArrayList<Node>();

        List<Node> nodes = new ArrayList<Node>(digraphNodes);

        while (!nodes.isEmpty()) {
            // Note: Loop definition this way as we are removing elements from the list we are iterating over
            for (Iterator<Node> iterator = nodes.iterator(); iterator.hasNext(); ) {
                Node node = iterator.next();
                boolean isFree = true;

                for (Node incomingNode : node.getIncomingNodes()) {
                    if (!topologicalSortedNodes.contains(incomingNode)) {
                        isFree = false;
                    }
                }

                if (isFree) {
                    topologicalSortedNodes.add(node);
                    iterator.remove();
                }
            }
        }

        return topologicalSortedNodes;
    }

    /**
     * Returns cost of critical path for specifed Node
     * @param node
     * @return int
     */
    public int getCriticalPathCost(Node node) {
        return _criticalPathCosts.get(node.getName());
    }

    /**
     * Gets a node in the digraph specified by the node's name
     * @param nodeName The name of the node
     * @return Node
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

        if(!(d._digraphName.equals(this._digraphName))){
            return false;
        }

        if(d._nodeMap.size()!=this._nodeMap.size()){
            return false;
        }

        if(!(mapsAreEqual(d._nodeMap, this._nodeMap))){
            return false;
        }
        return true;
    }

    /**
     * Helper method to determine whether NodeMaps are equal
     * @param mapA
     * @param mapB
     * @return boolean
     */
    private boolean mapsAreEqual(Map<String, Node> mapA, Map<String, Node> mapB) {

        try{
            for (String k : mapB.keySet())
            {
                if (!mapA.get(k).equals(mapB.get(k))) {
                    return false;
                }
            }
            for (String y : mapA.keySet())
            {
                if (!mapB.containsKey(y)) {
                    return false;
                }
            }
        } catch (NullPointerException np) {
            return false;
        }
        return true;
    }

    /**
     * Override hashCode method so that equals method can compare two Digraph objects accurately
     * @return int
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + _digraphName.hashCode();
        result = 31 * result + _nodeMap.hashCode();
        return result;
    }
}
