package se306.team7.Algorithm;

import pt.runtime.TaskID;
import pt.runtime.TaskIDGroup;
import pt.runtime.TaskInfo;
import pt.runtime.TaskpoolFactory;
import se306.team7.CostEstimatedSchedule;
import se306.team7.Digraph.Digraph;
import se306.team7.Schedule;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class DfsAlgorithmParallel {
    private static Digraph _digraph;
    private static Set<ICostEstimator> _costEstimators;
    private static IScheduleGenerator _scheduleGenerator;
    private int _processorCount = 1;
    private Method _getOptimalScheduleMethod;
    private static AtomicInteger _currentBestCost;
    private static AtomicReference<Schedule> _currentBestSchedule;

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
        _digraph = digraph;
        _processorCount = threadCount;

        ValidScheduleGenerator validScheduleGenerator = new ValidScheduleGenerator();
        Schedule greedySchedule = validScheduleGenerator.generateValidSchedule(digraph, numOfProcessors);

        _currentBestCost = new AtomicInteger(greedySchedule.endTime());
        _currentBestSchedule = new AtomicReference<Schedule>(greedySchedule);

        // Get list of schedules at base of subtrees to parallelise
        List<Schedule> topLevelSchedules = new ArrayList<Schedule>();
        topLevelSchedules.add(new Schedule(numOfProcessors));

        while (topLevelSchedules.size() < _processorCount) {
            List<Schedule> newTopLevelSchedules = new ArrayList<Schedule>();

            for (Schedule topLevelSchedule : topLevelSchedules) {
                newTopLevelSchedules.addAll(_scheduleGenerator.generateSchedules(topLevelSchedule, digraph));
            }

            topLevelSchedules = newTopLevelSchedules;
        }

        //
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

        return _currentBestSchedule.get();
    }



    public static void getOptimalSchedule(Digraph digraph, int numOfProcessors, Schedule schedule) {
        // Generate next schedules
        List<Schedule> nextSchedules = _scheduleGenerator.generateSchedules(schedule, digraph);

        // Base case, at leaf of tree
        if (nextSchedules.isEmpty()) {
            if (schedule.endTime() < _currentBestCost.get()) {
                System.out.println("New best: " + schedule.endTime());
                _currentBestSchedule.set(schedule);
                _currentBestCost.set(schedule.endTime());
            }

            return;
        }

        // Get cost estimates for next schedules
        PriorityQueue<CostEstimatedSchedule> costEstimatedSchedules = new PriorityQueue<CostEstimatedSchedule>();

        for (Schedule nextSchedule : nextSchedules) {
            int costEstimate = getCostEstimate(nextSchedule);
            if (costEstimate < _currentBestCost.get())
                costEstimatedSchedules.add(new CostEstimatedSchedule(nextSchedule, costEstimate));
        }

        if (costEstimatedSchedules.isEmpty()) {
            return;
        }

        // Find and return best sub-schedule
        for (CostEstimatedSchedule nextSchedule : costEstimatedSchedules) {
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
