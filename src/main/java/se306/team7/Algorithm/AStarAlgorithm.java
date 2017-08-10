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

    public Schedule getOptimalSchedule(Digraph digraph, int numOfProcessors) {
        _schedules.clear();
        _currentHeads.clear();
        _digraph = digraph;

        Schedule schedule = new Schedule(numOfProcessors, Integer.MAX_VALUE);

        _schedules.add(schedule);

        while(true){
           Schedule mostPromisingSchedule =  _schedules.poll();
           List<Node> possibleNodes = _currentHeads.get(mostPromisingSchedule);

           if(possibleNodes.isEmpty()) {
               return mostPromisingSchedule;
           }

           List<Schedule> possibleSchedules = generateSchedules(mostPromisingSchedule, possibleNodes);

           for(Schedule s : possibleSchedules){
               _schedules.add(s);
           }
        }
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
        List<Node> nodes = _digraph.getNodesInDigraph();

        for (Node n : nodes) {
            if (nodesInSchedule.contains(n)) {
                possibleNodes.remove(n);
            } else if (!possibleNodes.contains(n) && scheduleContainsParentNodes(nodesInSchedule, n)) {
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