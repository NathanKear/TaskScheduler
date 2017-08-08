package se306.team7;

import se306.team7.Digraph.Node;

public class Task {

    private Node _node;
    private int _processor;
    private int _startTime;
    private int _endTime;

    public Task (Node n, int processor, int startTime) {
        _node = n;
        _processor = processor;
        _startTime = startTime;
        _endTime = _startTime + _node.getCost();
    }

    public Node getNode () {
        return _node;
    }

    public int getProcessor () {
        return _processor;
    }
    
    public int getStartTime(){
    	return _startTime;
    }
    
    public int getEndTime(){
    	return _endTime;
    }

}