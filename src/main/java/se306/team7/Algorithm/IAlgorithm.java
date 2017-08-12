package se306.team7.Algorithm;

import se306.team7.Digraph.IDigraph;
import se306.team7.Schedule;

public interface IAlgorithm {

    public Schedule getOptimalSchedule(IDigraph digraph, int numOfProcessors);

}
