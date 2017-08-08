package se306.team7.Algorithm;

import se306.team7.Digraph.Digraph;
import se306.team7.Digraph.Node;
import se306.team7.Digraph.Link;
import se306.team7.Schedule;
import se306.team7.Task;

import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class AStarAlgorithm implements IAlgorithm {

    private PriorityQueue <Schedule> _schedules;
    private Map <Schedule, List<Node>> _currentHeads;
    private Digraph _digraph;

    public AStarAlgorithm () {
        _schedules = new PriorityQueue<Schedule>();
        _currentHeads = new HashMap<Schedule, List<Node>>();
    }

    public Schedule getOptimalSchedule(Digraph digraph) {
        return new Schedule(0);
    }

    public List<Schedule> generateSchedules(Schedule current, List<Node> currentHeads) {
        return new ArrayList<Schedule>();
    }

    public int getCostEstimate(Schedule current) {
        return 0;
    }

    /**
     * Updates the list containing which nodes are eligible to be added to a schedule one level below the schedule
     * passed in
     * @param schedule The schedule whose list of current nodes will be updated
     */
    public void calculateCurrentHeads(Schedule schedule) {
        List<Node> possibleNodes = _currentHeads.get(schedule);
        HashSet<Node> nodesInSchedule = schedule.getNodesInSchedule();
        HashMap<String, Node> nodeMap = _digraph.getNodeMap();

        Iterator it = nodeMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry nodePair = (Map.Entry)it.next();
            Node n = (Node) nodePair.getValue();
            if (!possibleNodes.contains(n) && scheduleContainsParentNodes(nodesInSchedule, n)) {
                possibleNodes.add(n);
            }
        }

        _currentHeads.put(schedule, possibleNodes);
        return;
    }

    /**
     * Checks to see if all of a node's parent nodes are in a schedule
     * @param nodesInSchedule A hashset of all nodes in a schedule
     * @param n The node to check if its parents are in the hashset
     * @return True if the parent nodes are in the schedule or false otherwise
     */
    private boolean scheduleContainsParentNodes (HashSet<Node> nodesInSchedule, Node n) {
        List<Link> incomingLinks = n.getIncomingLinks();
        for (Link link : incomingLinks) {
            Node parentNode = link.getOriginNode();
            if (!nodesInSchedule.contains(parentNode)) {
                return false;
            }
        }
        return true;
    }

}