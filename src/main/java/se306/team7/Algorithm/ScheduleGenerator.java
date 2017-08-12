package se306.team7.Algorithm;

import se306.team7.CostEstimatedSchedule;
import se306.team7.Digraph.Node;
import se306.team7.Schedule;

import java.util.ArrayList;
import java.util.List;

public class ScheduleGenerator implements IScheduleGenerator {
    /**
     * Generate schedules that are immediate children of the current schedule
     * @param current The current schedule whos children should be created
     * @param currentHeads The current nodes available to be added to the current schedule.
     *                     This is defined as nodes that are not in the schedule but have all their dependencies in the schedule.
     * @return List of next level schedules who are direct children of the current schedule
     */
    public List<Schedule> generateSchedules(Schedule current, List<Node> currentHeads) {

        List<Schedule> generatedSchedules = new ArrayList<Schedule>();

        for (Node head : currentHeads) {
            for (int i = 0; i < current._numOfProcessors; i++) {
                Schedule newSchedule = new Schedule(current);
                newSchedule.scheduleTask(i, head);
                generatedSchedules.add(newSchedule);
            }
        }

        return generatedSchedules;
    }
}
