package se306.team7;

import se306.team7.Digraph.Node;

public class Task {

    private Node _node;
    private int _processor;

    public Task (Node n, int processor) {
        _node = n;
        _processor = processor;
    }

    public Node getNode () {
        return _node;
    }

    public int getProcessor () {
        return _processor;
    }

}