package se306.team7;

import se306.team7.Digraph.Node;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class Schedule implements Comparable {

    public int _numOfProcessors;
    private Queue<Task> _tasks;
    private HashSet<Node> _nodesInSchedule;
    private int _estimatedCost;

    /**
     * Instantiates a PartialSchedule instance.
     * @param numOfProcessors the number of processors available for tasks to be scheduled on
     */
    public Schedule(int numOfProcessors, int estimatedCost) {
        _numOfProcessors = numOfProcessors;
        _tasks = new LinkedList<Task>();
        _nodesInSchedule = new HashSet<Node>();
        _estimatedCost = estimatedCost;
    }

    /**
     * Schedules the specified task on a specified processor.
     * @param processor processor on which the specified task is scheduled
     * @param node task to be scheduled on the specified processor
     */
    public void scheduleTask(int processor, Node node) {
        _nodesInSchedule.add(node);
    }

    public Queue<Task> getTasks () {
        return _tasks;
    }

    /**
     * Gets the nodes in the schedule
     * @return
     */
    public HashSet<Node> getNodesInSchedule () {
        return _nodesInSchedule;
    }

    public int compareTo(Object o) {
        if(o == null) {
            throw new NullPointerException();
        }

        if(!o.getClass().equals(Schedule.class)){
           throw new IllegalArgumentException();
        }

        Schedule otherSchedule = (Schedule)(o);

       return  _estimatedCost - otherSchedule._estimatedCost;
    }
}
