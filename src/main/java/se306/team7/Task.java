package se306.team7;

import se306.team7.Digraph.Node;

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

    /**
     * Gets the start time of the task
     * @return
     */
    public int getStartTime(){
        return _startTime;
    }

    /**
     * Gets the end time of the task
     * @return
     */
    public int getEndTime(){
        return _endTime;
    }

    public boolean equals(Object other) {
        if (other == null)
            return false;

        if (other.getClass() != Task.class)
            return false;

        Task otherTask = (Task)other;

        if (otherTask._endTime != _endTime ||
            otherTask._startTime != _startTime ||
            otherTask._processor != _processor ||
            otherTask._node != _node)
            return false;

        return true;
    }
}