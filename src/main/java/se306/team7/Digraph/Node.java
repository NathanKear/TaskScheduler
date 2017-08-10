package se306.team7.Digraph;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private String _name;
    private int _cost;
    private List<Link> _incomingLinks;
    private List<Link> _outgoingLinks;

    /**
     * Instantiates an instance of Node
     * @param name The name of the node
     * @param cost The time taken to complete the node's task
     */
    public Node (String name, int cost) {
        _name = name;
        _cost = cost;
        _incomingLinks = new ArrayList<Link>();
        _outgoingLinks = new ArrayList<Link>();
    }

    /**
     * Adds a link to the node. The method will calculate whether the link is incoming or outgoing
     * @param originNode The node the link originates from
     * @param originNodeName The name of the node the link originates from
     * @param destinationNode The node the link terminates at
     * @param weight The weight associated with transferring the results of the task across processors
     */
    public void addLink (Node originNode, String originNodeName, Node destinationNode, int weight) {
        Link link = new Link(originNode, destinationNode, weight);
        if (originNodeName == _name) {
            _incomingLinks.add(link);
        } else {
            _outgoingLinks.add(link);
        }
        return;
    }

    /**
     * Checks whether the node is a head node (a node at level 0 of the digraph)
     * @return
     */
    public boolean isHead () {
        return (_incomingLinks.size() == 0);
    }

    /**
     * Gets the outgoing links of the node
     * @return
     */
    public List<Link> getOutgoingLinks () {
        return _outgoingLinks;
    }

    /**
     * Gets the incoming links of the node
     * @return
     */
    public List<Link> getIncomingLinks () {
        return _incomingLinks;
    }

}
