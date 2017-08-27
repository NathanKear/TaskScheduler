package se306.team7.Algorithm;

import pt.runtime.CurrentTask;
import se306.team7.CostEstimatedSchedule;
import se306.team7.Digraph.Digraph;
import se306.team7.Metrics;
import se306.team7.Schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DfsAlgorithm implements IAlgorithm {
    private Digraph _digraph;
    private Set<ICostEstimator> _costEstimators;
    private IScheduleGenerator _scheduleGenerator;
    private int _currentBestCost;

    /**
     * Instantiates DFS algorithm with a costEstimator and scheduleGenerator object and a current best cost value
     * @param costEstimators
     * @param scheduleGenerator
     */
    public DfsAlgorithm (Set<ICostEstimator> costEstimators, IScheduleGenerator scheduleGenerator) {
        _costEstimators = costEstimators;
        _scheduleGenerator = scheduleGenerator;
        _currentBestCost = Integer.MAX_VALUE;
    }

    /**
     * Searches through tree using DFS methodology to generate the optimal schedule in the given digraph
     *
     * Using the input schedule, a list of sub schedules is generated
     * The list is pruned by removing any sub schedule with a cost estimate higher than the current schedule
     *
     * getOptimalSchedule is recursively called on each sub schedule to eventually derive the best schedule
     * @param digraph
     * @param numOfProcessors
     * @param schedule
     * @return Schedule the complete, optimal schedule
     */
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

            if (CurrentTask.insideTask()) {
                Metrics.doneSchedule(nextSchedule, CurrentTask.globalID() + 1);
            } else {
                Metrics.doneSchedule(nextSchedule, 1);
            }

            Schedule s = getOptimalSchedule(digraph, numOfProcessors, nextSchedule.getSchedule());

            if (s == null)
                continue;

            if (bestSchedule == null || s.endTime() < bestSchedule.endTime()) {
                bestSchedule = s;
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
        ValidScheduleGenerator v = new ValidScheduleGenerator();
        Schedule validSchedule = v.generateValidSchedule(digraph, numOfProcessors);
        _currentBestCost = validSchedule.endTime();
        Schedule bestSchedule = getOptimalSchedule(digraph, numOfProcessors, new Schedule(numOfProcessors));
        if (bestSchedule == null) {
            return validSchedule;
        }
        return bestSchedule;
    }

    /**
     * Cost estimate of a schedule is given by the maximum out of (the latest task end time) or
     * (newestAddedTask's start time plus its bottom level)
     * @param schedule
     * @return int cost estimate
     */
    public int getCostEstimate(Schedule schedule) {

        int currentMax = 0;

        for (ICostEstimator costEstimator : _costEstimators) {
            currentMax = Math.max(currentMax, costEstimator.estimateCost(schedule, _digraph));
        }

        return currentMax;
    }

    public void setCurrentBestCost (int cost) {
        _currentBestCost = cost;
    }

}
