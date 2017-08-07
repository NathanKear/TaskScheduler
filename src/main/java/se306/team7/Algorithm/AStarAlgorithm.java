package se306.team7.Algorithm;

import se306.team7.Digraph.Digraph;
import se306.team7.Digraph.Node;
import se306.team7.Schedule;

import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class AStarAlgorithm implements IAlgorithm {

    private PriorityQueue <Schedule> _schedules;
    private Map <Schedule, List<Node>> _currentHeads;

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

    public void calculateCurrentHeads(Schedule schedule) {
        return;
    }

}
