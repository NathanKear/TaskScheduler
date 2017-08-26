package se306.team7.Digraph;

public class Link {

    private Node _originNode;
    private Node _destinationNode;
    private int _transferCost;

    /**
     * Instantiates an instance of Link
     * @param originNode The node the link originates at
     * @param destinationNode The node the link terminates at
     * @param transferCost The cost of transferring the origin node's return value(s) across processors to
     *                     the destination node
     */
    public Link (Node originNode, Node destinationNode, int transferCost) {
        _originNode = originNode;
        _destinationNode = destinationNode;
        _transferCost = transferCost;
    }

    /**
     * Returns the cost of transferring the origin node's return value(s) across processors to the destination node
     * @return _transferCost
     */
    public int getTransferCost () {
        return _transferCost;
    }

    /**
     * Returns the origin node of the link
     * @return _originNode
     */
    public Node getOriginNode () {
        return _originNode;
    }

    /**
     * Returns the destination node of the link
     * @return _destinationNode
     */
    public Node getDestinationNode () {
        return _destinationNode;
    }

    /**
     * Overrides equal method to compare two Link objects
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
        if (!(other instanceof Link)) {
            return false;
        }

        Link l = (Link) other;

        if (!l._destinationNode.getName().equals(this._destinationNode.getName())) {
            return false;
        }
        if (!l._originNode.getName().equals(this._originNode.getName())) {
            return false;
        }
        if (l._transferCost != this._transferCost) {
            return false;
        }

        return true;
    }
        /**
         * Override hashCode method so that equals method can compare two Link objects accurately
         * @return int
         * */
        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + _destinationNode.hashCode();
            result = 31 * result + _transferCost;
            result = 31 * result + _originNode.hashCode();
            return result;
        }
}
