package se306.team7.Algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.runtime.*;
import se306.team7.Digraph.Digraph;
import se306.team7.Schedule;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class AStarParalleliser {

    public static Schedule bestSchedule;

    public static AtomicInteger bestCost = new AtomicInteger(Integer.MAX_VALUE);

    private static Logger logger = LoggerFactory.getLogger(AStarParalleliser.class);

    public static Schedule getSchedule(Schedule schedule, int processors, Digraph digraph) {
        logger.debug("Start ID: " + CurrentTask.globalID());

        if (schedule.endTime() > bestCost.get()) {
            return null;
        }
        Set<ICostEstimator> costEstimators = new HashSet<ICostEstimator>();
        costEstimators.add(new CriticalPathCostEstimator());
        costEstimators.add(new LoadBalancerCostEstimator());
        IScheduleGenerator scheduleGenerator = new ScheduleGenerator();

        AStarAlgorithm a = new AStarAlgorithm(costEstimators, scheduleGenerator);

        Schedule s = a.getOptimalSchedule(digraph, processors, schedule);

        if (s == null) {
            return null;
        }

        if (bestSchedule == null || s.endTime() < bestSchedule.endTime()) {
            bestSchedule = s;
            bestCost.set(s.endTime());
        }

        logger.debug("Finish ID: " + CurrentTask.globalID());

        return s;
    }

}
