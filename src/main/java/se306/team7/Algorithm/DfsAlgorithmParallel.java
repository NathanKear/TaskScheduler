package se306.team7.Algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.runtime.*;
import se306.team7.CostEstimatedSchedule;
import se306.team7.Digraph.Digraph;
import se306.team7.Metrics;
import se306.team7.Schedule;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class DfsAlgorithmParallel {

    private static Logger _logger = LoggerFactory.getLogger(DfsAlgorithmParallel.class);
    private static Digraph _digraph;
    private static Set<ICostEstimator> _costEstimators;
    private static IScheduleGenerator _scheduleGenerator;
    private int _processorCount = 1;
    private Method _getOptimalScheduleMethod;
    private static AtomicInteger _bestCost;
    private static AtomicReference<Schedule> _bestSchedule;

    /**
     * Attempt to set the best schedule found so far
     * @param schedule The schedule to try and set as best
     * @return If the schedule provided is the current best schedule
     */
    public static boolean trySetBestSchedule(Schedule schedule) {
        if (schedule.endTime() < _bestCost.get()) {

            _logger.info("Found new best schedule. Cost = " + schedule.endTime());

            _bestCost.set(schedule.endTime());
            _bestSchedule.set(schedule);
            Metrics.setCurrentBestCost(schedule.endTime());

            return true;
        }

        return false;
    }

    public DfsAlgorithmParallel (Set<ICostEstimator> costEstimators, IScheduleGenerator scheduleGenerator) {
        _costEstimators = costEstimators;
        _scheduleGenerator = scheduleGenerator;

        try {
            _getOptimalScheduleMethod = DfsAlgorithmParallel.class.getMethod("getOptimalSchedule", Digraph.class, int.class, Schedule.class);
        } catch (NoSuchMethodException ex) {
            System.exit(1);
        }
    }

    public Schedule run(Digraph digraph, int numOfProcessors, int threadCount) {

        _logger.info("Starting DFS search. Parallel threads = " + threadCount);

        _digraph = digraph;
        _processorCount = threadCount;

        ValidScheduleGenerator validScheduleGenerator = new ValidScheduleGenerator();
        Schedule greedySchedule = validScheduleGenerator.generateValidSchedule(digraph, numOfProcessors);

        _bestCost = new AtomicInteger(greedySchedule.endTime());
        _bestSchedule = new AtomicReference<Schedule>(greedySchedule);


        // Break schedule problem down into several sub-problems
        List<Schedule> topLevelSchedules = decomposeSchedules(digraph, numOfProcessors, threadCount);
        // if decompose has failed to break down problem then the real solution is returned
        if (topLevelSchedules.size() < threadCount) {
            _logger.info("Problem cannot be parallelised across " + threadCount + " threads");
            threadCount = topLevelSchedules.size();
            _logger.info("Thread count reduced to " + threadCount);
        }

        TaskIDGroup taskGroup = new TaskIDGroup(topLevelSchedules.size());

        for (Schedule schedule : topLevelSchedules) {
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setMethod(_getOptimalScheduleMethod);
            taskInfo.setParameters(_digraph, numOfProcessors, schedule);
            TaskID<Void> task = TaskpoolFactory.getTaskpool().enqueue(taskInfo);
            taskGroup.add(task);
        }

        try {
            taskGroup.waitTillFinished();
        } catch (ExecutionException ex) {

        } catch (InterruptedException ex) {

        }

        _logger.info("Finished DFS search. Optimal schedule length = " + _bestCost.get());

        return _bestSchedule.get();
    }

    /**
     * Decompose a problem into a minimum number of smaller scheduling problems to solve
     * @param digraph
     * @param minScheduleCount
     * @return List of schedules that problem has been parallelise over. If the list has only one element then it may be the real solution.
     */
    private static List<Schedule> decomposeSchedules(Digraph digraph, int numOfProcessors, int minScheduleCount) {
        // Get list of schedules at base of subtrees to parallelise
        List<Schedule> topLevelSchedules = new ArrayList<Schedule>();
        List<Schedule> currentDecompisition = new ArrayList<Schedule>();
        currentDecompisition.add(new Schedule(numOfProcessors));

        while (currentDecompisition.size() + topLevelSchedules.size() < minScheduleCount) {
            Schedule schedule = currentDecompisition.remove(0);
            List<Schedule> childSchedules = _scheduleGenerator.generateSchedules(schedule, digraph);

            if (childSchedules == null || childSchedules.isEmpty()) {
                topLevelSchedules.add(schedule);
            } else {
                currentDecompisition.addAll(childSchedules);
            }

            if (currentDecompisition.isEmpty()) {
                return topLevelSchedules;
            }
        }

        topLevelSchedules.addAll(currentDecompisition);
        return topLevelSchedules;
    }



    public static void getOptimalSchedule(Digraph digraph, int numOfProcessors, Schedule schedule) {

        // Generate next schedules
        List<Schedule> nextSchedules = _scheduleGenerator.generateSchedules(schedule, digraph);

        // Base case, at leaf of tree
        if (nextSchedules.isEmpty()) {
            trySetBestSchedule(schedule);
            return;
        }

        // Get cost estimates for next schedules
        PriorityQueue<CostEstimatedSchedule> costEstimatedSchedules = new PriorityQueue<CostEstimatedSchedule>();

        for (Schedule nextSchedule : nextSchedules) {
            int costEstimate = getCostEstimate(nextSchedule);
            if (costEstimate < _bestCost.get())
                costEstimatedSchedules.add(new CostEstimatedSchedule(nextSchedule, costEstimate));
        }

        if (costEstimatedSchedules.isEmpty()) {
            return;
        }

        // Find and return best sub-schedule
        for (CostEstimatedSchedule nextSchedule : costEstimatedSchedules) {

            if (CurrentTask.insideTask()) {
                Metrics.doneSchedule(nextSchedule, CurrentTask.relativeID() + 1);
            }

            getOptimalSchedule(digraph, numOfProcessors, nextSchedule.getSchedule());
        }
    }

    /**
     * Get the optimal schedule for a given digraph containing tasks and task dependencies
     * @param digraph Represents tasks and task dependencies
     * @param numOfProcessors Processors available to concurrently complete tasks
     * @return Optimal complete schedule
     */
    public Schedule getOptimalSchedule(Digraph digraph, int numOfProcessors) {
        //return getOptimalSchedule(digraph, numOfProcessors, new Schedule(numOfProcessors));
        return null;
    }

    /**
     * Cost estimate of a schedule is given by the maximum out of (the latest task end time) or
     * (newestAddedTask's start time plus its bottom level)
     * @param schedule
     * @return
     */
    public static int getCostEstimate(Schedule schedule) {

        int currentMax = 0;

        for (ICostEstimator costEstimator : _costEstimators) {
            currentMax = Math.max(currentMax, costEstimator.estimateCost(schedule, _digraph));
        }

        return currentMax;
    }
}
