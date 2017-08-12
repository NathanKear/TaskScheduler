package se306.team7.Algorithm;

import se306.team7.CostEstimatedSchedule;
import se306.team7.Digraph.IDigraph;
import se306.team7.Digraph.Node;
import se306.team7.Digraph.Link;
import se306.team7.Schedule;

import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class AStarAlgorithm implements IAlgorithm {

    private PriorityQueue <CostEstimatedSchedule> _schedules;
    private Map <Schedule, List<Node>> _currentHeads;
    private IDigraph _digraph;
    private final Set<ICostEstimator> _costEstimators;
    private final IScheduleGenerator _scheduleGenerator;

    public AStarAlgorithm (Set<ICostEstimator> costEstimators, IScheduleGenerator scheduleGenerator) {
        _schedules = new PriorityQueue<CostEstimatedSchedule>();
        _currentHeads = new HashMap<Schedule, List<Node>>();
        _costEstimators = costEstimators;
        _scheduleGenerator = scheduleGenerator;
    }

    /**
     * Get the optimal schedule for a given digraph containing tasks and task dependencies
     * @param digraph Represents tasks and task dependencies
     * @param numOfProcessors Processors available to concurrently complete tasks
     * @return Optimal complete schedule
     */
    public Schedule getOptimalSchedule(IDigraph digraph, int numOfProcessors) {
        _schedules.clear();
        _currentHeads.clear();
        _digraph = digraph;

        Schedule emptySchedule = new Schedule(numOfProcessors);
        CostEstimatedSchedule emptyCostEstimatedSchedule = new CostEstimatedSchedule(emptySchedule, Integer.MAX_VALUE);

        _schedules.add(emptyCostEstimatedSchedule);
        List<Node> initialHeads = calculateCurrentHeads(emptyCostEstimatedSchedule.getSchedule());
        _currentHeads.put(emptyCostEstimatedSchedule.getSchedule(), initialHeads);

        while(true){
           Schedule mostPromisingSchedule =  _schedules.poll().getSchedule();
           List<Node> possibleNodes = _currentHeads.get(mostPromisingSchedule);

           if(possibleNodes.isEmpty()) {
               return mostPromisingSchedule;
           }

           List<Schedule> possibleSchedules = _scheduleGenerator.generateSchedules(mostPromisingSchedule, possibleNodes);

           for(Schedule schedule : possibleSchedules){
               int estimatedCost = getCostEstimate(schedule);
               CostEstimatedSchedule costEstimatedSchedule = new CostEstimatedSchedule(schedule, estimatedCost);

               _schedules.add(costEstimatedSchedule);
               List<Node> currentHeads = calculateCurrentHeads(schedule);
               _currentHeads.put(emptyCostEstimatedSchedule.getSchedule(), currentHeads);
           }
        }
    }


    /**
     * Cost estimate of a schedule is given by the maximum out of (the latest task end time) or
     * (newestAddedTask's start time plus its bottom level)
     * @param schedule
     * @return
     */
    public int getCostEstimate(Schedule schedule) {
        int currentMaxCost = 0;

        for (ICostEstimator costEstimator : _costEstimators) {
            currentMaxCost = Math.max(currentMaxCost, costEstimator.getCostEstimate(_digraph, schedule));
        }

        return currentMaxCost;
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