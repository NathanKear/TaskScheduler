package se306.team7.Digraph;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private String _name;
    private int _cost;
    private List<Link> _incomingLinks;
    private List<Link> _outgoingLinks;

    public Node (String name, int cost) {
        _name = name;
        _cost = cost;
        _incomingLinks = new ArrayList<Link>();
        _outgoingLinks = new ArrayList<Link>();
    }

    public void addLink (Node originNode, String originNodeName, Node destinationNode, int weight) {
        Link link = new Link(originNode, destinationNode, weight);
        if (originNodeName == _name) {
            _incomingLinks.add(link);
        } else {
            _outgoingLinks.add(link);
        }
        return;
    }

    public boolean isHead () {
        return (_incomingLinks.size() == 0);
    }

}
