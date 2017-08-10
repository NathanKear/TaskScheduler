package se306.team7.Algorithm;

import se306.team7.Digraph.Digraph;
import se306.team7.Digraph.Node;
import se306.team7.Schedule;
import se306.team7.Task;

import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

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

    /**
     * Cost estimate of a schedule is given by the maximum out of (the latest task end time) or
     * (newestAddedTask's start time plus its bottom level)
     * @param schedule
     * @return
     */
    public int getCostEstimate(Schedule schedule) {
        return Math.max(loadBalanceCostEstimate(schedule), criticalPathCostEstimate(schedule));
    }

    public int loadBalanceCostEstimate(Schedule schedule) {

        // Get all nodes
        List<Node> nodesNotInDigraph = _digraph.getNodes();

        // Remove nodes already in digraph
        for (Task task : schedule.getTasks()) {
            nodesNotInDigraph.remove(task.getNode());
        }

        int totalCost = 0;

        // Get total cost of all nodes yet to add
        for (Node node : nodesNotInDigraph) {
            totalCost += node.getCost();
        }

        // Get average cost of all nodes yet to add per processor
        int costPerProcessor = (int)Math.ceil(totalCost / schedule._numOfProcessors);

        return schedule.endTime() + costPerProcessor;
    }

    public int criticalPathCostEstimate(Schedule schedule) {

        int highestCriticalPath = 0;

        for (Task task : schedule.getTasks()) {
            highestCriticalPath = Math.max(highestCriticalPath, _digraph.getCriticalPathCost(task.getNode()));
        }

        return highestCriticalPath;
    }

    public void calculateCurrentHeads(Schedule schedule) {
        return;
    }

}
