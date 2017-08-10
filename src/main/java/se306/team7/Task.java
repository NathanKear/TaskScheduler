package se306.team7;

import se306.team7.Digraph.Node;

public class Task {

    private Node _node;
    private int _processor;

    /**
     * Instantiates an instance of Task
     * @param n The node associated with the task
     * @param processor The processor associated with the task
     */
    public Task (Node n, int processor) {
        _node = n;
        _processor = processor;
    }

    /**
     * Gets the node associated with the task
     * @return
     */
    public Node getNode () {
        return _node;
    }

    /**
     * Gets the processor associated with the task
     * @return
     */
    public int getProcessor () {
        return _processor;
    }

}