package se306.team7.Digraph;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;

public class Digraph implements IDigraph {

    private String _digraphName;
    private HashMap<String, Node> _nodeMap;

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

    @Override
    public boolean equals(Object other) {
        if(other == null){return false;}
        if(other == this){return true;}
        if(!(other instanceof Digraph)){return false;}

        Digraph d = (Digraph) other;

        return d._digraphName == this._digraphName &&
                d._nodeMap == this._nodeMap;
    }
}
