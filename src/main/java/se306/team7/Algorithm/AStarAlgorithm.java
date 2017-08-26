package se306.team7.Algorithm;

import se306.team7.CostEstimatedSchedule;
import se306.team7.Metrics;
import se306.team7.Digraph.Digraph;
import se306.team7.Schedule;

import java.util.*;
import java.util.PriorityQueue;
import java.util.List;

public class AStarAlgorithm implements IAlgorithm {

    private PriorityQueue <CostEstimatedSchedule> _schedules;
    private Digraph _digraph;
    private Set<ICostEstimator> _costEstimators;
    private IScheduleGenerator _scheduleGenerator;

    /**
     * Instantiates AStarAlgorithm with a costEstimator and scheduleGenerator object and an empty priority queue
     * of schedules
     * @param costEstimators
     * @param scheduleGenerator
     */
    public AStarAlgorithm (Set<ICostEstimator> costEstimators, IScheduleGenerator scheduleGenerator) {
        _schedules = new PriorityQueue<CostEstimatedSchedule>();
        _costEstimators = costEstimators;
        _scheduleGenerator = scheduleGenerator;
    }

    /**
     * Searches through tree using A* methodology to generate the optimal schedule in the given digraph
     *
     * Using the most promising schedule, which is at the head of the priority queue due to its low estimated cost,
     * a list of possible sub schedules is generated
     *
     * If a sub schedule is better than or the same as the currently best known time, it is added to the priority queue
     * The algorithm continues polling schedules off the priority queue and repeating the schedule generation process
     *
     * Once the algorithm reaches a schedule that can not have any more sub schedules (i.e. is at the bottom most level
     * in the solution tree), this schedule is complete and optimal and is returned
     * @param digraph
     * @param numOfProcessors
     * @param schedule
     * @return Schedule complete optimal schedule
     */
    public Schedule getOptimalSchedule(Digraph digraph, int numOfProcessors, Schedule schedule) {
        _schedules.clear();
        _digraph = digraph;

        ValidScheduleGenerator v = new ValidScheduleGenerator();
        int knownScheduleFinishTime = v.generateValidSchedule(digraph, numOfProcessors).endTime();

        CostEstimatedSchedule emptySchedule = new CostEstimatedSchedule(schedule, 0);

        _schedules.add(emptySchedule);
        
        while(true){
            Schedule mostPromisingSchedule =  _schedules.poll().getSchedule();
            List<Schedule> possibleSchedules = _scheduleGenerator.generateSchedules(mostPromisingSchedule, digraph);

        	Metrics.setCurrentBestCost(mostPromisingSchedule.endTime()); //bogus code
            if(possibleSchedules.isEmpty()) {
                return mostPromisingSchedule;
            }

            for(Schedule _schedule : possibleSchedules){

                int cost = Math.max(getCostEstimate(_schedule), mostPromisingSchedule.endTime());
                if (cost <= knownScheduleFinishTime) {
                    CostEstimatedSchedule costEstimatedSchedule = new CostEstimatedSchedule(_schedule, cost);
                    Metrics.doneSchedule(costEstimatedSchedule, costEstimatedSchedule.getSchedule().getNumberOfProcessors()); // bogus code
                    _schedules.add(costEstimatedSchedule);
                }
            }
        }
    }

    /**
     * Get the optimal schedule for a given digraph containing tasks and task dependencies
     * @param digraph Represents tasks and task dependencies
     * @param numOfProcessors Processors available to concurrently complete tasks
     * @return Schedule Optimal complete schedule
     */
    public Schedule getOptimalSchedule(Digraph digraph, int numOfProcessors) {
        Schedule schedule = new Schedule(numOfProcessors);

        return getOptimalSchedule(digraph, numOfProcessors, schedule);
    }

    /**
     * Cost estimate of a schedule is given by the maximum out of (the latest task end time) or
     * (newestAddedTask's start time plus its bottom level)
     * @param schedule
     * @return int
     */
    public int getCostEstimate(Schedule schedule) {

        int currentMax = schedule.getLastTaskScheduled().getEndTime();

        for (ICostEstimator costEstimator : _costEstimators) {
            currentMax = Math.max(currentMax, costEstimator.estimateCost(schedule, _digraph));
        }

        return currentMax;
    }
}