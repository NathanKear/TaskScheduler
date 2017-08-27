package se306.team7.Digraph;

import java.util.*;

public class Digraph implements IDigraph {

    public String _digraphName;
    private HashMap<String, Node> _nodeMap;
    private List<Node> _topologicalSortedNodes;
    private HashMap<String, Integer> _criticalPathCosts;

    /**
     * Instantiates an instance of Digraph, to be used by DigraphBuilder only
     * @param digraphName The name of the Digraph
     */
    public Digraph (String digraphName, HashMap<String, Node> nodeMap){
        _digraphName = digraphName;
        _nodeMap = nodeMap;
        _topologicalSortedNodes = topologicallySortDigraph(_nodeMap.values());
        _criticalPathCosts = calculateCriticalPathCosts();
    }

    /**
     * Get nodes in digraph in some topological order
     * @return List<Node> The nodes
     */
    public List<Node> getNodes() {
        return new ArrayList<Node>(_topologicalSortedNodes);
    }

    /**
     * Calculate critical costs for all nodes in digraph
     * @return HashMap<String, Integer> A map of critical path costs for each node
     */
    private HashMap<String, Integer> calculateCriticalPathCosts() {
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
     * @return List<Node> The topologically sorted list of nodes
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
     * Returns cost of critical path for specified Node
     * @param node
     * @return int The critical path cost for specified node
     */
    public int getCriticalPathCost(Node node) {
        return _criticalPathCosts.get(node.getName());
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
        }else if (other == this) {
            return true;
        }else if (!(other instanceof Digraph)) {
            return false;
        }

        Digraph d = (Digraph) other;

        if(!(d._digraphName.equals(this._digraphName))){
            return false;
        }else if(d._nodeMap.size()!=this._nodeMap.size()){
            return false;
        }else if(!(mapsAreEqual(d._nodeMap, this._nodeMap))){
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
            for (String k : mapB.keySet()) {
                if (!mapA.get(k).equals(mapB.get(k))) {
                    return false;
                }
            }
            for (String y : mapA.keySet()) {
                if (!mapB.containsKey(y)) {
                    return false;
                }
            }
        } catch (NullPointerException np) { return false; }
        return true;
    }

    /**
     * Override hashCode method so that equals method can compare two Digraph objects accurately
     * @return int Hashcode
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + _digraphName.hashCode();
        result = 31 * result + _nodeMap.hashCode();
        return result;
    }
}
