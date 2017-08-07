package se306.team7.Algorithm;

import se306.team7.CostEstimatedSchedule;
import se306.team7.Digraph.Digraph;
import se306.team7.Digraph.Node;
import se306.team7.Schedule;

import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class AStarAlgorithm implements IAlgorithm {

    private PriorityQueue <CostEstimatedSchedule> _schedules;
    private Map <Schedule, List<Node>> _currentHeads;
    private Digraph _digraph;

    public AStarAlgorithm () {
        _schedules = new PriorityQueue<CostEstimatedSchedule>();
        _currentHeads = new HashMap<Schedule, List<Node>>();
    }

    public Schedule getOptimalSchedule(Digraph digraph, int numOfProcessors) {
        _schedules.clear();
        _currentHeads.clear();
        _digraph = digraph;

        Schedule schedule = new Schedule(numOfProcessors);

        _schedules.add(new CostEstimatedSchedule(schedule, Integer.MAX_VALUE));

        while(true){
           Schedule mostPromisingSchedule =  _schedules.poll().getSchedule();
           List<Node> possibleNodes = _currentHeads.get(mostPromisingSchedule);

           if(possibleNodes.isEmpty()) {
               return mostPromisingSchedule;
           }

           List<CostEstimatedSchedule> possibleSchedules = generateSchedules(mostPromisingSchedule, possibleNodes);

           for(CostEstimatedSchedule s : possibleSchedules){
               _schedules.add(s);
           }
        }
    }

    public List<CostEstimatedSchedule> generateSchedules(Schedule current, List<Node> currentHeads) {

        List<CostEstimatedSchedule> generatedSchedules = new ArrayList<CostEstimatedSchedule>();

        for (Node head : currentHeads) {
            for (int i = 0; i < current._numOfProcessors; i++) {
                Schedule newSchedule = new Schedule(current);
                newSchedule.scheduleTask(i, head);
                int cost = getCostEstimate(newSchedule);
                generatedSchedules.add(new CostEstimatedSchedule(newSchedule, cost));
            }
        }

        return generatedSchedules;
    }

    public int getCostEstimate(Schedule current) {
        return 0;
    }

    public void calculateCurrentHeads(Schedule schedule) {
        return;
    }

}
