package se306.team7;

import java.util.Queue;

/**
 * Represents a schedule with an estimated cost attached
 */
public class CostEstimatedSchedule implements Comparable {
    private final Schedule _schedule;
    private final int _estimatedCost;

    /**
     * Get the schedule
     * @return
     */
    public Schedule getSchedule() {
        return _schedule;
    }

    /**
     * Get the estimated cost of this schedule
     * @return
     */
    public int getEstimatedCost() {
        return _estimatedCost;
    }

    /**
     * Instantiates a Schedule object, with an associated estimated cost
     * @param schedule
     * @param estimatedCost
     */
    public CostEstimatedSchedule(Schedule schedule, int estimatedCost) {
        _schedule = schedule;
        _estimatedCost = estimatedCost;
    }

    /**
     * Compares this object to another schedule, to determine which schedule has the lowest cost
     * @param o
     * @return int
     */
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
