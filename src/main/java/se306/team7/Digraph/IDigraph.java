package se306.team7.Digraph;

import java.util.List;

public interface IDigraph {
    List<Node> getNodes();

    int getCriticalPathCost(Node node);
}
