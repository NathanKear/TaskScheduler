package se306.team7.Digraph;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private String _name;
    private int _cost;
    private List<Link> _incomingLinks;
    private List<Link> _outgoingLinks;
    private int _bottomLevel;

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
            if (originNodeName.equals(_name)) {
                _outgoingLinks.add(link);
            } else {
                _incomingLinks.add(link);
            }
    }

    /**
     * Checks whether the node is a head node (a node at level 0 of the digraph)
     * @return boolean
     */
    public boolean isHead () {
        return (_incomingLinks.size() == 0);
    }

    /**
     * Returns incoming links for this node
     * @return List<Link>
     */
    public List<Link> getIncomingLinks(){
    	return _incomingLinks;
    }

    /**
     * Returns outgoing links for this node
     * @return List<Link>
     */
    public List<Link> getOutgoingLinks() { return _outgoingLinks; }

    /**
     * Get nodes that have an outgoing link to this node
     * @return List<Node>
     */
    public List<Node> getIncomingNodes() {
        List<Node> nodes = new ArrayList<Node>();

        for (Link link : getIncomingLinks()) {
            nodes.add(link.getOriginNode());
        }

        return nodes;
    }

    /**
     * Get nodes that have an incoming link from this node
     * @return List<Node>
     */
    public List<Node> getOutgoingNodes() {
        List<Node> nodes = new ArrayList<Node>();

        for (Link link : getOutgoingLinks()) {
            nodes.add(link.getDestinationNode());
        }

        return nodes;
    }

    /**
     * Returns weight cost of the node
     * @return int
     */
    public int getCost() {
    	return _cost;
    }
    
    /**
     * Returns bottom level of the node
     * @return int
     */
    public int getBottomLevel(){
    	return _bottomLevel;
    }

    /**
     * Return name of this node
     * @return String
     */
    public String getName() { return _name; }

    /**
     * Override equals method to compare two Node objects
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
        if (!(other instanceof Node)) {
            return false;
        }

        Node n = (Node) other;

        if(!n._name.equals(this._name)){
            return false;
        }
        if(n._cost != this._cost){
            return false;
        }
        if(n._incomingLinks.size() != this._incomingLinks.size()){
            return false;
        }
        if(n._outgoingLinks.size() != this._outgoingLinks.size()) {
            return false;
        }

        int count = 0;
        for(Link l : n._incomingLinks){
            if(this._incomingLinks.contains(l)) {
                count++;
            }
        }
        if(count != n._incomingLinks.size()){
            return false;
        }

        count = 0;
        for(Link l : n._outgoingLinks){
            if(this._outgoingLinks.contains(l)) {
                count++;
            }
        }
        if(count != n._outgoingLinks.size()){
            return false;
        }
        return true;
    }

    /**
     * Override hashCode method so that equals method can compare two Node objects accurately
     * @return int
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + _name.hashCode();
        result = 31 * result + _cost;
        return result;
    }
}
