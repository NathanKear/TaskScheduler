package se306.team7;

import se306.team7.Digraph.Node;

/**
 * Represents a task(node in the digraph) scheduled on a processor
 */
public class Task {

    private Node _node;
    private int _processor;
    private int _startTime;
    private int _endTime;

    /**
     * Instantiates an instance of Task
     * @param n The node associated with the task
     * @param processor The processor associated with the task
     */
    public Task (Node n, int processor, int startTime) {
        _node = n;
        _processor = processor;
        _startTime = startTime;
        _endTime = _startTime + _node.getCost();
    }

    /**
     * Gets the node associated with the task
     * @return Node
     */
    public Node getNode () {
        return _node;
    }

    /**
     * Gets the processor associated with the task
     * @return int
     */
    public int getProcessor () {
        return _processor;
    }

    /**
     * Gets the start time of the task
     * @return int
     */
    public int getStartTime(){
        return _startTime;
    }

    /**
     * Gets the end time of the task
     * @return int
     */
    public int getEndTime(){
        return _endTime;
    }

}