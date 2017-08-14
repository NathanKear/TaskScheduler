package se306.team7;

import se306.team7.Digraph.Digraph;

import java.util.Queue;
import java.util.Comparator;

public class CostEstimatedSchedule implements Comparable {
    private final Schedule _schedule;
    private final int _estimatedCost;

    public Schedule getSchedule() {
        return _schedule;
    }

    public int getEstimatedCost() {
        return _estimatedCost;
    }

    public CostEstimatedSchedule(Schedule schedule, int estimatedCost) {
        _schedule = schedule;
        _estimatedCost = estimatedCost;
    }

    public int compareTo(Object o) {
        if(o == null) {
            throw new NullPointerException();
        }

        if(!o.getClass().equals(CostEstimatedSchedule.class)){
            throw new IllegalArgumentException();
        }

        CostEstimatedSchedule otherSchedule = (CostEstimatedSchedule)(o);
        Queue<Task> thisTasks = _schedule.getTasks();
        Queue<Task> otherTasks = otherSchedule.getSchedule().getTasks();
        int taskDifference = thisTasks.size() - otherTasks.size();

        return  _estimatedCost - otherSchedule._estimatedCost- taskDifference;
    }
}
