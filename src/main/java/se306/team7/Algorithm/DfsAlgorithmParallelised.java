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
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class DfsAlgorithmParallelised implements IAlgorithm{

    private Digraph _digraph;
    private Set<ICostEstimator> _costEstimators;
    private IScheduleGenerator _scheduleGenerator;
    private int _currentBestCost;
    //private List<Schedule> _subTreeSchedules;
    private PriorityQueue<CostEstimatedSchedule> _subTreeSchedules;
    private int _batchSize = 500;

    public DfsAlgorithmParallelised (Set<ICostEstimator> costEstimators, IScheduleGenerator scheduleGenerator) {
        _costEstimators = costEstimators;
        _scheduleGenerator = scheduleGenerator;
        _currentBestCost = Integer.MAX_VALUE;
        _subTreeSchedules = new PriorityQueue<CostEstimatedSchedule>();
    }

    private Schedule runAllSubtrees(List<PriorityQueue<CostEstimatedSchedule>> batchesToRun, Digraph digraph, int processorCount) {
        for (PriorityQueue<CostEstimatedSchedule> batch : batchesToRun) {
            try {

                Method aStarAlgorithmMethod = AStarParalleliser.class.getMethod("getSchedule", Schedule.class, int.class, Digraph.class);

                TaskIDGroup<Void> id = new TaskIDGroup<Void>(batch.size());

                for (CostEstimatedSchedule schedule : batch) {

                    TaskInfo taskInfo = new TaskInfo();
                    taskInfo.setMethod(aStarAlgorithmMethod);
                    taskInfo.setParameters(schedule.getSchedule(), processorCount, digraph);

                    TaskID<Void> task = TaskpoolFactory.getTaskpool().enqueue(taskInfo);

                    id.add(task);
                }

                id.waitTillFinished();

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException ex) {
                System.exit(1); // Something has gone horribly wrong
            }
        }

        return AStarParalleliser.bestSchedule;
    }

    public Schedule getOptimalSchedule(Digraph digraph, int numOfProcessors, Schedule schedule) {
        _digraph = digraph;

        List<Schedule> nextSchedules = _scheduleGenerator.generateSchedules(schedule, digraph);

        if (nextSchedules.isEmpty()) {
            if (schedule.endTime() < _currentBestCost)
                _currentBestCost = Math.min(_currentBestCost, schedule.endTime());

            return schedule;
        }

        List<CostEstimatedSchedule> costEstimatedSchedules = new ArrayList<CostEstimatedSchedule>();

        for (Schedule nextSchedule : nextSchedules) {
            int costEstimate = getCostEstimate(nextSchedule);
            if (costEstimate < _currentBestCost)
                costEstimatedSchedules.add(new CostEstimatedSchedule(nextSchedule, costEstimate));
        }

        if (costEstimatedSchedules.isEmpty()) {
            return null;
        }

        Collections.sort(costEstimatedSchedules);

        Schedule bestSchedule = null;

        for (CostEstimatedSchedule nextSchedule : costEstimatedSchedules) {
            if(digraph.getNodes().size() - schedule.getTasks().size() <= 10){
                Schedule s = nextSchedule.getSchedule();
                _subTreeSchedules.add(new CostEstimatedSchedule(s, s.endTime()));

            }else {
                Schedule s = getOptimalSchedule(digraph, numOfProcessors, nextSchedule.getSchedule());

                if (s == null)
                    continue;

                if (bestSchedule == null || s.endTime() < bestSchedule.endTime()) {
                    bestSchedule = s;
                }
            }
        }

        return bestSchedule;
    }

    /**
     * Get the optimal schedule for a given digraph containing tasks and task dependencies
     * @param digraph Represents tasks and task dependencies
     * @param numOfProcessors Processors available to concurrently complete tasks
     * @return Optimal complete schedule
     */
    public Schedule getOptimalSchedule(Digraph digraph, int numOfProcessors) {
        ValidScheduleGenerator greedyScheduleGenerator = new ValidScheduleGenerator();
        Schedule greedySchedule = greedyScheduleGenerator.generateValidSchedule(digraph, numOfProcessors);
        AStarParalleliser.bestCost.set(greedySchedule.endTime());
        AStarParalleliser.bestSchedule = greedySchedule;
        getOptimalSchedule(digraph, numOfProcessors, new Schedule(numOfProcessors));
        List<PriorityQueue<CostEstimatedSchedule>> batchSchedules = batchSchedules();
        return runAllSubtrees(batchSchedules, digraph, numOfProcessors);
    }

    /**
     * Cost estimate of a schedule is given by the maximum out of (the latest task end time) or
     * (newestAddedTask's start time plus its bottom level)
     * @param schedule
     * @return
     */
    public int getCostEstimate(Schedule schedule) {

        int currentMax = 0;

        for (ICostEstimator costEstimator : _costEstimators) {
            currentMax = Math.max(currentMax, costEstimator.estimateCost(schedule, _digraph));
        }

        return currentMax;
    }

    private List<PriorityQueue<CostEstimatedSchedule>> batchSchedules () {
        List<PriorityQueue<CostEstimatedSchedule>> batchedSchedules = new ArrayList<PriorityQueue<CostEstimatedSchedule>>();

        while (!_subTreeSchedules.isEmpty()) {
            PriorityQueue<CostEstimatedSchedule> batch = new PriorityQueue<CostEstimatedSchedule>();
            for (int i = 0; i < _batchSize; i++) {
                CostEstimatedSchedule s = _subTreeSchedules.poll();
                if (s == null) {
                    break;
                }
                batch.add(_subTreeSchedules.poll());
            }
            batchedSchedules.add(batch);
        }

        return batchedSchedules;
    }

}
