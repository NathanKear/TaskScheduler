package se306.team7.Algorithm;

import se306.team7.Digraph.Node;
import se306.team7.Schedule;
import se306.team7.Task;

import java.util.List;
import se306.team7.Digraph.IDigraph;
import se306.team7.Digraph.Node;
import se306.team7.Digraph.Link;

public class LoadBalancerCostEstimator implements ICostEstimator {
    /**
     * Get path cost estimate using the load balance cost estimate.
     * Load balance cost is the cost of all remaining tasks to be scheduled
     * spread perfectly over all threads starting immediately after the current
     * schedule finish time.
     * @param schedule
     * @return
     */
    public int getCostEstimate(IDigraph digraph, Schedule schedule) {

        // Get all nodes
        List<Node> nodesNotInDigraph = digraph.getNodes();

        // Remove nodes already in digraph
        for (Task task : schedule.getTasks()) {
            nodesNotInDigraph.remove(task.getNode());
        }

        int totalCost = 0;

        // Get total cost of all nodes yet to add
        for (Node node : nodesNotInDigraph) {
            totalCost += node.getCost();
        }

        // Get average cost of all nodes yet to add per processor
        int costPerProcessor = (int)Math.ceil(totalCost / (double)schedule._numOfProcessors);

        return schedule.endTime() + costPerProcessor;
    }
}
