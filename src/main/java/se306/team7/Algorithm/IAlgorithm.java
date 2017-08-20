package se306.team7.Algorithm;

import se306.team7.Digraph.Digraph;
import se306.team7.Schedule;

public interface IAlgorithm {

    public Schedule getOptimalSchedule(Digraph digraph, int numOfProcessors, Schedule schedule);

}
