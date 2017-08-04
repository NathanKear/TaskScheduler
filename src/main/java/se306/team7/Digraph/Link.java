package se306.team7.Digraph;

public class Link {

    private Node _originNode;
    private Node _destinationNode;
    private int _transferCost;

    public Link (Node originNode, Node destinationNode, int transferCost) {
        _originNode = originNode;
        _destinationNode = destinationNode;
        _transferCost = transferCost;
    }

    public int getTransferCost () {
        return _transferCost;
    }

    public Node getOriginNode () {
        return _originNode;
    }

    public Node getDestinationNode () {
        return _destinationNode;
    }

}
