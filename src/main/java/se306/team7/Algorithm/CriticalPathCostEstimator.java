package se306.team7.Algorithm;

import se306.team7.Digraph.IDigraph;
import se306.team7.Schedule;
import se306.team7.Task;

public class CriticalPathCostEstimator {

    /**
     * Get path cost estimate using the critical path estimate.
     * Critical path is the most expensive direct path from any node currently in the schedule
     * to the end of the schedule.
     * @param schedule
     * @return
     */
    public int estimateCost(Schedule schedule, IDigraph digraph) {

        int highestCriticalPath = 0;

        for (Task task : schedule.getTasks()) {
            highestCriticalPath = Math.max(highestCriticalPath, task.getEndTime() + digraph.getCriticalPathCost(task.getNode()));
        }

        return highestCriticalPath;
    }
}
