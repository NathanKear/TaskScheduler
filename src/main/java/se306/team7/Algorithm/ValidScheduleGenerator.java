package se306.team7.Algorithm;

import se306.team7.Digraph.IDigraph;
import se306.team7.Digraph.Node;
import se306.team7.Schedule;

public class ValidScheduleGenerator {

    /**
     * This method calculates a valid schedule using the List Scheduling algorithm
     *
     * @param d An instance of IDigraph
     * @param numOfProcessors The number of processors to can schedule tasks on
     * @return valid Schedule
     */
    public Schedule generateValidSchedule (IDigraph d, int numOfProcessors) {
        Schedule s = new Schedule(numOfProcessors);
        int[] processorStartTimes = new int[numOfProcessors];
        int processorToScheduleOn = 0;

        for (Node node : d.getNodes()) {
            for (int i = 0; i < numOfProcessors; i++) {
                int startTime = s.calculateTaskStartTime(i, node);
                processorStartTimes[i] = startTime;
                if (startTime <= processorStartTimes[processorToScheduleOn]) {
                    processorToScheduleOn = i;
                }
            }
            s.scheduleTask(processorToScheduleOn, node);
            processorStartTimes[processorToScheduleOn] += s.getLastTaskScheduled().getEndTime();
            processorToScheduleOn = 0;
            processorStartTimes = new int[numOfProcessors];
        }
        return s;
    }

}