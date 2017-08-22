package se306.team7.Algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.runtime.*;
import se306.team7.Digraph.Digraph;
import se306.team7.Schedule;

import java.util.HashSet;
import java.util.Set;

public class AStarParalleliser {

    public static Schedule bestSchedule;

    private static Logger logger = LoggerFactory.getLogger(AStarParalleliser.class);

    public static Schedule getSchedule(Schedule schedule, int processors, Digraph digraph) {
        logger.debug("Start ID: " + CurrentTask.globalID());

        Set<ICostEstimator> costEstimators = new HashSet<ICostEstimator>();
        costEstimators.add(new CriticalPathCostEstimator());
        costEstimators.add(new LoadBalancerCostEstimator());
        IScheduleGenerator scheduleGenerator = new ScheduleGenerator();

        AStarAlgorithm a = new AStarAlgorithm(costEstimators, scheduleGenerator);

        Schedule s = a.getOptimalSchedule(digraph, processors, schedule);

        if (bestSchedule == null || s.endTime() < bestSchedule.endTime()) {
            bestSchedule = s;
        }

        logger.debug("Finish ID: " + CurrentTask.globalID());

        return s;
    }

}
