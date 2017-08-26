package se306.team7.Algorithm;

import se306.team7.Digraph.IDigraph;
import se306.team7.Digraph.Node;
import se306.team7.Schedule;
import se306.team7.Task;

import java.util.List;

public class LoadBalancerCostEstimator implements ICostEstimator {

    /**
     * Get path cost estimate using the load balance cost estimate.
     * Load balance cost is the cost of all remaining tasks to be scheduled
     * spread perfectly over all threads starting immediately after the current
     * schedule finish time.
     * @param schedule
     * @return int estimated cost
     */
    public int estimateCost(Schedule schedule, IDigraph digraph) {

        // Get all nodes
        List<Node> nodesNotInDigraph = digraph.getNodes();

        int numOfProcessors = schedule.getNumberOfProcessors();
        int[] earliestFinishingTime = new int[numOfProcessors];

        int maxProcessingTime = 0;

        // Remove nodes already in digraph
        for (Task task : schedule.getTasks()) {
            int finishingTime = Math.max(earliestFinishingTime[task.getProcessor()], task.getEndTime());
            earliestFinishingTime[task.getProcessor()] = finishingTime;
            if (finishingTime > maxProcessingTime) {
                maxProcessingTime = finishingTime;
            }
            nodesNotInDigraph.remove(task.getNode());
        }



        int freeTime = 0;
        for(int i = 0; i < numOfProcessors; i++) {
            freeTime += maxProcessingTime - earliestFinishingTime[i];
        }


        int totalCost = 0;

        // Get total cost of all nodes yet to add
        for (Node node : nodesNotInDigraph) {
            totalCost += node.getCost();
        }

        totalCost = Math.max(0, totalCost - freeTime);

        // Get average cost of all nodes yet to add per processor
        int costPerProcessor = (int)Math.ceil(totalCost / (double) schedule._numOfProcessors);

        return schedule.endTime() + costPerProcessor;
    }
}
