package se306.team7.Algorithm;

import se306.team7.CostEstimatedSchedule;
import se306.team7.Digraph.Digraph;
import se306.team7.Digraph.Link;
import se306.team7.Digraph.Node;
import se306.team7.Schedule;

import java.util.*;

public class DfsAlgorithm implements IAlgorithm {
    private Digraph _digraph;
    private Set<ICostEstimator> _costEstimators;
    private IScheduleGenerator _scheduleGenerator;
    private int _currentBestCost;

    public DfsAlgorithm (Set<ICostEstimator> costEstimators, IScheduleGenerator scheduleGenerator) {
        _costEstimators = costEstimators;
        _scheduleGenerator = scheduleGenerator;
        _currentBestCost = Integer.MAX_VALUE;
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
        _currentBestCost = v.generateValidSchedule(digraph, numOfProcessors).endTime() + 1;
        return getOptimalSchedule(digraph, numOfProcessors, new Schedule(numOfProcessors));
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

}
