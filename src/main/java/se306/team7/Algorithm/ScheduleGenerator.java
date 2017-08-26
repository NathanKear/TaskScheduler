package se306.team7.Algorithm;

import se306.team7.CostEstimatedSchedule;
import se306.team7.Digraph.Link;
import se306.team7.Digraph.Node;
import se306.team7.Digraph.Digraph;
import se306.team7.Schedule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ScheduleGenerator implements IScheduleGenerator {
    /**
     * Generate schedules that are immediate children of the current schedule
     * @param current The current schedule whos children should be created
     * @return List of next level schedules who are direct children of the current schedule
     */
    public List<Schedule> generateSchedules(Schedule current, Digraph digraph) {

        List<Schedule> generatedSchedules = new ArrayList<Schedule>();
        List<Node> currentHeads = calculateCurrentHeads(current, digraph);

        int numOfProcessors = current.getNumberOfProcessorsToScheduleOn();

        for (Node head : currentHeads) {
            for (int i = 0; i <= numOfProcessors; i++) {
                Schedule newSchedule = new Schedule(current);
                //_currentHeads.put(newSchedule, new ArrayList<Node>(currentHeads));
                newSchedule.scheduleTask(i, head);
                //int cost = getCostEstimate(newSchedule);
                generatedSchedules.add(newSchedule);
            }
        }

        return generatedSchedules;
    }


    /**
     * Updates the list containing which nodes are eligible to be added to a schedule one level below the schedule
     * passed in
     * @param schedule The schedule whose list of current nodes will be updated
     */
    public List<Node> calculateCurrentHeads(Schedule schedule, Digraph digraph) {
        List<Node> possibleNodes = new ArrayList<Node>(schedule.getNodesInSchedule());
        HashSet<Node> nodesInSchedule = schedule.getNodesInSchedule();
        List<Node> nodes = digraph.getNodes();

        for (Node n : nodes) {
            if (nodesInSchedule.contains(n)) {
                possibleNodes.remove(n);
            } else if (!possibleNodes.contains(n) && scheduleContainsParentNodes(nodesInSchedule, n)) {
                possibleNodes.add(n);
            }
        }

        return possibleNodes;
    }

    /**
     * Checks to see if all of a node's parent nodes are in a schedule
     * @param nodesInSchedule A hashset of all nodes in a schedule
     * @param n The node to check if its parents are in the hashset
     * @return True if the parent nodes are in the schedule or false otherwise
     */
    private boolean scheduleContainsParentNodes (HashSet<Node> nodesInSchedule, Node n) {
        List<Link> incomingLinks = n.getIncomingLinks();
        for (Link link : incomingLinks) {
            Node parentNode = link.getOriginNode();
            if (!nodesInSchedule.contains(parentNode)) {
                return false;
            }
        }
        return true;
    }
}
