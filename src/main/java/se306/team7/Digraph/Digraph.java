package se306.team7.Digraph;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;

public class Digraph {

    private String _digraphName;
    private List<Node> _firstNodes;
    private HashMap<String, Node> _nodeMap;

    public Digraph (String digraphName) {
        _digraphName = digraphName;
        _firstNodes = new ArrayList<Node>();
        _nodeMap = new HashMap<String, Node>();
    }

    public void AddNode (String name, int cost) {
        Node n = new Node(name, cost);
        _nodeMap.put(name, n);
    }

    public void AddLink (String originName, String destinationName, int cost) {
        Node originNode = _nodeMap.get(originName);
        Node destinationNode = _nodeMap.get(destinationName);

        originNode.addLink(originNode, originName, destinationNode, cost);
        destinationNode.addLink(originNode, originName, destinationNode, cost);
    }

    public void calculateHeadNodes () {
        Iterator it = _nodeMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry nodePair = (Map.Entry)it.next();
            Node n = (Node) nodePair.getValue();

            if (n.isHead()) {
                _firstNodes.add(n);
            }

            it.remove();
        }
    }

}
