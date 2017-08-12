package se306.team7.Digraph;

import java.util.List;
import se306.team7.Task;

public interface IDigraph {

    /**
     * Creates a new node and adds it to the Digraph
     * @param name The name of the node
     * @param cost The time taken to complete the node's task
     */
    Node addNode (String name, int cost);

    /**
     * Adds a link between nodes in the digraph
     * @param originName The name of the node where the link originates
     * @param destinationName The name of the node where the link terminates
     * @param cost The cost of transferring the origin node's return value(s) across processors to the destination node
     */
    void addLink (String originName, String destinationName, int cost);

    /**
     * Calculates which nodes in the digraph are head nodes (nodes at level 0 of the digraph) and adds them to the
     * list of head nodes (_headNodes)
     */
    List<Node> calculateHeadNodes ();


    /**
     * Get all the nodes in the digraph
     * @return
     */
    List<Node> getNodes();

    /**
     * Get the cost of the critical path for a given node
     * @param node
     * @return
     */
    int getCriticalPathCost(Node node);
}
