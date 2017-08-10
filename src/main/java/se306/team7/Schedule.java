package se306.team7;

import se306.team7.Digraph.Node;
import se306.team7.Digraph.Link;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Schedule {

    public int _numOfProcessors;
    private Queue<Task> _tasks;
    private HashSet<Node> _nodesInSchedule;

    /**
     * Instantiates a PartialSchedule instance.
     * @param numOfProcessors the number of processors available for tasks to be scheduled on
     */
    public Schedule(int numOfProcessors) {
        _numOfProcessors = numOfProcessors;
        _tasks = new LinkedList<Task>();
        _nodesInSchedule = new HashSet<Node>();
    }

    public Schedule(Schedule schedule) {
        _numOfProcessors = schedule._numOfProcessors;
        _tasks = new LinkedList<Task>(schedule._tasks);
        _nodesInSchedule = new HashSet<Node>();
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

    public List<String> scheduleToStringList() {
        ArrayList<String> output = new ArrayList<String>();
        for (Task task : _tasks) {
            Node n = task.getNode();
            String line = n.getName() + "[ Weight = " + n.getCost() + ", Start = " + task.getStartTime() +
                    ", Processor = " + task.getProcessor() + "];";
            output.add(line);

            List<Link> incomingLinks = n.getIncomingLinks();
            for (Link link : incomingLinks) {
                Node parent = link.getOriginNode();
                Node child = link.getDestinationNode();
                int transferCost = link.getTransferCost();
                String linkString = parent.getName() + " -> " + child.getName() + "    [ Weight = " + transferCost + "];";
                output.add(linkString);
            }
        }
        return output;
    }
}
