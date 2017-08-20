package se306.team7.Algorithm;

import se306.team7.CostEstimatedSchedule;
import se306.team7.Digraph.Node;
import se306.team7.Schedule;

import java.util.List;

public interface IScheduleGenerator {
    List<Schedule> generateSchedules(Schedule current, List<Node> currentHeads);
}
