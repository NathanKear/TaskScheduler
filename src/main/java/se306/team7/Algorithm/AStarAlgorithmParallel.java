package se306.team7.Algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.runtime.*;
import se306.team7.CostEstimatedSchedule;
import se306.team7.Digraph.Digraph;
import se306.team7.Metrics;
import se306.team7.Schedule;

import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.Set;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AStarAlgorithmParallel implements IAlgorithm {

    private static Logger _logger = LoggerFactory.getLogger(AStarAlgorithmParallel.class);
    private static PriorityBlockingQueue<CostEstimatedSchedule> _schedules;
    private static Digraph _digraph;
    private static Set<ICostEstimator> _costEstimators;
    private static IScheduleGenerator _scheduleGenerator;

    private static AtomicReference<Schedule> _bestSchedule;
    private static AtomicInteger _bestCost;
    private static AtomicInteger _numOfProcessors;
    private static AtomicBoolean _foundBestSchedule;
    private int _numOfCores = 4;

    /**
     * Attempt to set the best schedule found so far
     * @param schedule The schedule to try and set as best
     * @return If the schedule provided is the current best schedule
     */
    public static boolean trySetBestSchedule (Schedule schedule) {
        if (schedule.endTime() < _bestCost.get()) {

            _logger.info("Found new best schedule. Cost = " + schedule.endTime());

            _bestCost.set(schedule.endTime());
            _bestSchedule.set(schedule);
            Metrics.setCurrentBestCost(schedule.endTime());

            return true;
        }

        return false;
    }

    /**
     * Instantiate AStarAlgorithmParallel object using a costEstimators set, scheduleGenerator object, a concurrent priority queue
     * and two Atomic fields to reduce concurrency errors
     * @param costEstimators
     * @param scheduleGenerator
     */
    public AStarAlgorithmParallel(Set<ICostEstimator> costEstimators, IScheduleGenerator scheduleGenerator) {
        _schedules = new PriorityBlockingQueue<CostEstimatedSchedule>();
        _costEstimators = costEstimators;
        _scheduleGenerator = scheduleGenerator;
        _bestSchedule = new AtomicReference<Schedule>();
        _bestCost = new AtomicInteger(Integer.MAX_VALUE);
        _foundBestSchedule = new AtomicBoolean(false);
        _numOfProcessors = new AtomicInteger();
    }

    /**
     * Get optimal scheduling of tasks in digraph
     * @param digraph Tasks and dependencies to generate schedule for
     * @param numOfProcessors Number of processors to divide schedules across
     * @return Guaranteed optimal schedule
     */
    public Schedule getOptimalSchedule(Digraph digraph, int numOfProcessors) {
        _numOfProcessors.set(numOfProcessors);
        Schedule schedule = new Schedule(numOfProcessors);
        return getOptimalSchedule(digraph, numOfProcessors, schedule);
    }

    /**
     * Get optimal scheduling of tasks in digraph from the current schedule
     * @param digraph Tasks and dependencies to generate schedule for
     * @param numOfProcessors Number of processors to divide schedules across
     * @param schedule Partial schedule so far
     * @return
     */
    public Schedule getOptimalSchedule(Digraph digraph, int numOfProcessors, Schedule schedule) {
        _digraph = digraph;
        ValidScheduleGenerator v = new ValidScheduleGenerator();
        Schedule knownSchedule = v.generateValidSchedule(digraph, numOfProcessors);
        trySetBestSchedule(knownSchedule);
        CostEstimatedSchedule emptySchedule = new CostEstimatedSchedule(schedule, 0);
        _schedules.add(emptySchedule);

        pollAndGenerateSchedules(10);

        if (_foundBestSchedule.get()) {
            _bestSchedule.get();
        }

        try {
            Method aStarAlgorithmMethod = AStarAlgorithmParallel.class.getMethod("pollAndGenerateSchedules", int.class);

            TaskIDGroup<Void> id = new TaskIDGroup<Void>(_numOfCores);

            for (int i = 0; i < _numOfCores; i++) {
                TaskInfo taskInfo = new TaskInfo();
                taskInfo.setMethod(aStarAlgorithmMethod);
                taskInfo.setParameters(-1);
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
        return _bestSchedule.get();
    }

    /**
     * Cost estimate of a schedule is given by the maximum out of (the latest task end time) or
     * (newestAddedTask's start time plus its bottom level)
     * @param schedule
     * @return int
     */
    public static int getCostEstimate(Schedule schedule) {

        int currentMax = schedule.getLastTaskScheduled().getEndTime();

        for (ICostEstimator costEstimator : _costEstimators) {
            currentMax = Math.max(currentMax, costEstimator.estimateCost(schedule, _digraph));
        }

        return currentMax;
    }

    /**
     * Polls the priority queue for a best schedule so far and generates its children
     * @param loopThreshold Maximum number of times to poll the queue before giving up
     */
    public static void pollAndGenerateSchedules (int loopThreshold) {
        if (CurrentTask.insideTask()) {
            _logger.info("Start thread. Id = " + (CurrentTask.globalID() + 1));
        }

        DfsAlgorithm dfs = new DfsAlgorithm(_costEstimators, _scheduleGenerator);

        while (true) {
            CostEstimatedSchedule polledSchedule = _schedules.poll();

            if (polledSchedule == null) {
                return;
            }

            if (polledSchedule.getEstimatedCost() >= _bestCost.get()) {
                return;
            }

            Schedule mostPromisingSchedule =  polledSchedule.getSchedule();

            Metrics.setCurrentBestCost(polledSchedule.getEstimatedCost());

            if (CurrentTask.insideTask()) {
                Metrics.doneSchedule(polledSchedule, CurrentTask.globalID() + 1);
            } else {
                Metrics.doneSchedule(polledSchedule, 1);
            }

            List<Schedule> possibleSchedules = _scheduleGenerator.generateSchedules(mostPromisingSchedule, _digraph);

            if (possibleSchedules.isEmpty()) { // schedule is complete
                trySetBestSchedule(mostPromisingSchedule);
                _foundBestSchedule.set(true);
                return;

            } else { // schedule is incomplete

                if (_schedules.size() > 1400000) {
                    dfs.setCurrentBestCost(_bestCost.get());
                    Schedule s = dfs.getOptimalSchedule(_digraph, _numOfProcessors.get(), mostPromisingSchedule);
                    if (s != null) {
                        trySetBestSchedule(s);
                    }
                } else {

                    for (Schedule _schedule : possibleSchedules) {

                        int cost = Math.max(getCostEstimate(_schedule), mostPromisingSchedule.endTime());

                        if (cost <= _bestCost.get()) {
                            CostEstimatedSchedule costEstimatedSchedule = new CostEstimatedSchedule(_schedule, cost);
                            _schedules.add(costEstimatedSchedule);
                        }
                    }
                }

                if (loopThreshold >=0) {
                    if (loopThreshold == 0) {
                        return; // Give up search and return
                    }
                    loopThreshold--;
                }
            }
        }
    }

    /**
     * Executes the algorithm, called by TaskScheduler main method
     * @param digraph
     * @param numOfProcessors
     * @param threadCount
     * @return Schedule most optimal schedule
     */
    public Schedule run (Digraph digraph, int numOfProcessors, int threadCount)  {
        _schedules.clear();
        _logger.info("Starting A* search. Parallel threads = " + threadCount);

        Metrics.setAlgorithmTypeUsed(Metrics.AlgorithmType.A_STAR);

        _numOfCores = threadCount;
        Schedule optimalSchedule = getOptimalSchedule(digraph, numOfProcessors);

        _logger.info("Finished A* search. Optimal schedule length = " + optimalSchedule.endTime());

        Metrics.setCurrentBestCost(optimalSchedule.endTime());
        return optimalSchedule;
    }

}
