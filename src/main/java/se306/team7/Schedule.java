package se306.team7;

import se306.team7.Digraph.Node;

import java.util.LinkedList;
import java.util.Queue;

public class Schedule {

    public int _numOfProcessors;
    private Queue<Task> _tasks;

    /**
     * Instantiates a PartialSchedule instance.
     * @param numOfProcessors the number of processors available for tasks to be scheduled on
     */
    public Schedule(int numOfProcessors) {
        _numOfProcessors = numOfProcessors;
        _tasks = new LinkedList<Task>();
    }

    /**
     * Schedules the specified task on a specified processor.
     * @param processor processor on which the specified task is scheduled
     * @param node task to be scheduled on the specified processor
     */
    public void scheduleTask(int processor, Node node) {

    }

    public Queue<Task> getTasks () {
        return _tasks;
    }

}
