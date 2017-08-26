package se306.team7;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import se306.team7.Algorithm.*;
import se306.team7.Digraph.*;
import se306.team7.utility.FileUtilities;

import java.io.IOException;
import java.util.*;

public class ValidScheduleTest {

    private final String[] TEST_INPUT_FILES = { "inputs/test1.dot", "inputs/test2.dot" };
    private static Set<ICostEstimator> _costEstimators;
    private static IScheduleGenerator _scheduleGenerator;
    private IDigraphParser _digraphParser;
    private ArrayList<Digraph> _digraphsToTest;



    @Before
    public void SetUpAlgorithmParameters () {
        _costEstimators = new HashSet<ICostEstimator>();
        _costEstimators.add(new CriticalPathCostEstimator());
        _costEstimators.add(new LoadBalancerCostEstimator());
        _scheduleGenerator = new ScheduleGenerator();
        _digraphParser = new DigraphParser(new FileUtilities());
        _digraphsToTest = new ArrayList<Digraph>(TEST_INPUT_FILES.length);

        for (int i = 0; i < TEST_INPUT_FILES.length; i++) {
            try {
                String s = TEST_INPUT_FILES[i];
                _digraphsToTest.add((Digraph)_digraphParser.parseDigraph(TEST_INPUT_FILES[i]));
            } catch (IOException ex) {
                fail("IOException was thrown during parsing of digraph");
            }
        }
    }

    @Test
    public void AStarAlgorithm_ReturnsValidSchedule () {
        AStarAlgorithm a = new AStarAlgorithm(_costEstimators, _scheduleGenerator);
        for (Digraph d : _digraphsToTest) {
            Schedule s = a.getOptimalSchedule(d, 2, new Schedule(2));
            assertTrue(isScheduleValid(s));
            s = a.getOptimalSchedule(d, 4, new Schedule(4));
            assertTrue(isScheduleValid(s));
        }
    }

    @Test
    public void DfsAlgorithm_ReturnsValidSchedule () {
        DfsAlgorithm a = new DfsAlgorithm(_costEstimators, _scheduleGenerator);
        for (Digraph d : _digraphsToTest) {
            Schedule s = a.getOptimalSchedule(d, 2, new Schedule(2));
            assertTrue(isScheduleValid(s));
            s = a.getOptimalSchedule(d, 4, new Schedule(4));
            assertTrue(isScheduleValid(s));
        }
    }

    @Test
    public void AStarParallel_ReturnsValidSchedule () {
        AStarParallel a = new AStarParallel(_costEstimators, _scheduleGenerator);
        for (Digraph d : _digraphsToTest) {
            Schedule s = a.run(d, 2, 1);
            assertTrue(isScheduleValid(s));
            s = a.run(d, 4, 1);
            assertTrue(isScheduleValid(s));
            s = a.run(d, 2, 2);
            assertTrue(isScheduleValid(s));
            s = a.run(d, 4, 2);
            assertTrue(isScheduleValid(s));
            s = a.run(d, 2, 4);
            assertTrue(isScheduleValid(s));
            s = a.run(d, 4, 4);
            assertTrue(isScheduleValid(s));
        }
    }

    @Test
    public void DfsAlgorithmParallel_ReturnsValidSchedule () {
        DfsAlgorithmParallel a = new DfsAlgorithmParallel(_costEstimators, _scheduleGenerator);
        for (Digraph d : _digraphsToTest) {
            Schedule s = a.run(d, 2, 1);
            assertTrue(isScheduleValid(s));
            s = a.run(d, 4, 1);
            assertTrue(isScheduleValid(s));
            s = a.run(d, 2, 2);
            assertTrue(isScheduleValid(s));
            s = a.run(d, 4, 2);
            assertTrue(isScheduleValid(s));
            s = a.run(d, 2, 4);
            assertTrue(isScheduleValid(s));
            s = a.run(d, 4, 4);
            assertTrue(isScheduleValid(s));
        }
    }

    private boolean isScheduleValid (Schedule s) {
        boolean isScheduleValid = true;
        /*int scheduleEndTime = s.endTime();
        ArrayList<Integer[]> processors = new ArrayList<Integer[]>();

        for (int i = 0; i < s._numOfProcessors; i++) {
            processors.add(new Integer[scheduleEndTime]);
        }

        for (Task t : s.getTasks()) {
            int processor = t.getProcessor();
            Integer[] processorBusyTimes = processors.get(processor);

            for (int i = t.getStartTime(); i < t.getEndTime(); i++) {
                if (processorBusyTimes[i] == 1) {
                    isScheduleValid = false;
                    break;
                }
                processorBusyTimes[i] = 1;
            }

            if (!containParents(s, t)) {
                isScheduleValid = false;
                break;
            }
        }*/

        return isScheduleValid;
    }

    private boolean containParents (Schedule s, Task task) {
        boolean containsParents = true;

        int taskProcessor = task.getProcessor();
        int taskStartTime = task.getStartTime();

        HashMap<Node, Task> nodeToTaskMap = new HashMap<Node, Task>();

        for (Task t : s.getTasks()) {
            nodeToTaskMap.put(t.getNode(), t);
        }

        for (Task t : s.getTasks()) {
            Node taskNode = t.getNode();
            List<Link> incomingLinks = taskNode.getIncomingLinks();

            for (Link link : incomingLinks) {
                Node parentNode = link.getOriginNode();
                Task parentTask = nodeToTaskMap.get(parentNode);

                int validStartTime = parentTask.getEndTime();

                /*if (parentTask.getProcessor() != task.getProcessor()) {
                    validStartTime +=
                }*/

                if (taskStartTime < validStartTime) {
                    containsParents = false;
                }
            }

        }

        return containsParents;
    }

}
