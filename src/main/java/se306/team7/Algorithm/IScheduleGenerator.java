package se306.team7.Algorithm;

import se306.team7.Schedule;
import se306.team7.Digraph.Node;

import java.util.List;

public interface IScheduleGenerator {
    List<Schedule> generateSchedules(Schedule current, List<Node> currentHeads);
}
