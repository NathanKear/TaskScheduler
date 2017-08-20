package se306.team7.Algorithm;

import se306.team7.CostEstimatedSchedule;
import se306.team7.Digraph.Digraph;
import se306.team7.Digraph.Node;
import se306.team7.Digraph.Link;
import se306.team7.Schedule;
import se306.team7.Task;

import java.util.*;

import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;

public class AStarAlgorithm implements IAlgorithm {

    private PriorityQueue <CostEstimatedSchedule> _schedules;
    private Map <Schedule, List<Node>> _currentHeads;
    private Digraph _digraph;

    public AStarAlgorithm () {
        _schedules = new PriorityQueue<CostEstimatedSchedule>();
        _currentHeads = new HashMap<Schedule, List<Node>>();
    }

    /**
     * Get the optimal schedule for a given digraph containing tasks and task dependencies
     * @param digraph Represents tasks and task dependencies
     * @param numOfProcessors Processors available to concurrently complete tasks
     * @return Optimal complete schedule
     */
    public Schedule getOptimalSchedule(Digraph digraph, int numOfProcessors) {
        _schedules.clear();
        _currentHeads.clear();
        _digraph = digraph;

        Schedule schedule = new Schedule(numOfProcessors);

        CostEstimatedSchedule emptySchedule = new CostEstimatedSchedule(schedule, Integer.MAX_VALUE);

        _schedules.add(emptySchedule);
        _currentHeads.put(schedule, new ArrayList<Node>());
        List<Node> initialHeads = calculateCurrentHeads(emptySchedule.getSchedule());
        _currentHeads.put(emptySchedule.getSchedule(), initialHeads);

        while(true){
           Schedule mostPromisingSchedule =  _schedules.poll().getSchedule();
           List<Node> possibleNodes = _currentHeads.get(mostPromisingSchedule);

           if(possibleNodes.isEmpty()) {
               return mostPromisingSchedule;
           }

           List<CostEstimatedSchedule> possibleSchedules = generateSchedules(mostPromisingSchedule, possibleNodes);

           for(CostEstimatedSchedule s : possibleSchedules){
               _schedules.add(s);
               List<Node> currentHeads = calculateCurrentHeads(s.getSchedule());
               _currentHeads.put(s.getSchedule(), currentHeads);
           }
        }
    }

    /**
     * Generate schedules that are immediate children of the current schedule
     * @param current The current schedule whos children should be created
     * @param currentHeads The current nodes available to be added to the current schedule.
     *                     This is defined as nodes that are not in the schedule but have all their dependencies in the schedule.
     * @return List of next level schedules who are direct children of the current schedule
     */
    public List<CostEstimatedSchedule> generateSchedules(Schedule current, List<Node> currentHeads) {

        List<CostEstimatedSchedule> generatedSchedules = new ArrayList<CostEstimatedSchedule>();

        for (Node head : currentHeads) {
            for (int i = 0; i < current._numOfProcessors; i++) {
                Schedule newSchedule = new Schedule(current);
                _currentHeads.put(newSchedule, new ArrayList<Node>(currentHeads));
                newSchedule.scheduleTask(i, head);
                int cost = getCostEstimate(newSchedule);
                generatedSchedules.add(new CostEstimatedSchedule(newSchedule, cost));
            }
        }

        return generatedSchedules;
    }

    /**
     * Cost estimate of a schedule is given by the maximum out of (the latest task end time) or
     * (newestAddedTask's start time plus its bottom level)
     * @param schedule
     * @return
     */
    public int getCostEstimate(Schedule schedule) {
        return Math.max(loadBalanceCostEstimate(schedule), criticalPathCostEstimate(schedule));
    }





    /**
     * Updates the list containing which nodes are eligible to be added to a schedule one level below the schedule
     * passed in
     * @param schedule The schedule whose list of current nodes will be updated
     */
    public List<Node> calculateCurrentHeads(Schedule schedule) {
        List<Node> possibleNodes = _currentHeads.get(schedule);
        HashSet<Node> nodesInSchedule = schedule.getNodesInSchedule();
        List<Node> nodes = _digraph.getNodes();

        for (Node n : nodes) {
            if (nodesInSchedule.contains(n)) {
                possibleNodes.remove(n);
            } else if (!possibleNodes.contains(n) && scheduleContainsParentNodes(nodesInSchedule, n)) {
                possibleNodes.add(n);
            }
        }

        return possibleNodes;
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