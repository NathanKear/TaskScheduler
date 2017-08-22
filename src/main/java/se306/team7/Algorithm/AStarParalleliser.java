package se306.team7.Algorithm;

import pt.runtime.*;
import se306.team7.Digraph.Digraph;
import se306.team7.Schedule;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AStarParalleliser {

    public static Schedule bestSchedule;

    public static TaskIDGroup<Schedule> getOptimalScheduleMulti(ConcurrentLinkedQueue<Schedule> schedulesToDo, Digraph digraph, int numOfProcessors){
        Schedule schedule = null;

        Set<ICostEstimator> costEstimators = new HashSet<ICostEstimator>();
        costEstimators.add(new CriticalPathCostEstimator());
        costEstimators.add(new LoadBalancerCostEstimator());
        IScheduleGenerator scheduleGenerator = new ScheduleGenerator();

        AStarAlgorithm a = new AStarAlgorithm(costEstimators, scheduleGenerator);

        while((schedule = schedulesToDo.poll()) != null){
            Schedule bestScheduleInTree = a.getOptimalSchedule(digraph, numOfProcessors, schedule);
            if(bestSchedule == null || bestScheduleInTree.endTime() < bestSchedule.endTime()){
                bestSchedule = bestScheduleInTree;
            }
        }

        TaskIDGroup id = getOptimalScheduleMulti(schedulesToDo, digraph, 4);

        TaskInfo ti = new TaskInfo();
        ti.setParameters(bestSchedule);
        return TaskpoolFactory.getTaskpool().enqueueMulti(ti, 1);
    }
}
