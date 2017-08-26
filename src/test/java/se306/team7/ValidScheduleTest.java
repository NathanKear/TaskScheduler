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

    private final String[] TEST_INPUT_FILES = { "inputs/test1.dot", "inputs/test2.dot", "Nodes_7_OutTree.dot", "Nodes_8_Random.dot",
            "Nodes_9_SeriesParallel.dot", "Nodes_10_Random.dot", "Nodes_11_OutTree.dot"};
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
            Metrics.init(d.getNodes().size(), 1);
            Schedule s = a.getOptimalSchedule(d, 2);
            assertTrue(isScheduleValid(s));

            Metrics.init(d.getNodes().size(), 1);
            s = a.getOptimalSchedule(d, 4);
            assertTrue(isScheduleValid(s));
        }
    }

    @Test
    public void DfsAlgorithm_ReturnsValidSchedule () {
        DfsAlgorithm a = new DfsAlgorithm(_costEstimators, _scheduleGenerator);
        for (Digraph d : _digraphsToTest) {
            Metrics.init(d.getNodes().size(), 1);
            Schedule s = a.getOptimalSchedule(d, 2);
            assertTrue(isScheduleValid(s));

            Metrics.init(d.getNodes().size(), 1);
            s = a.getOptimalSchedule(d, 4);
            assertTrue(isScheduleValid(s));
        }
    }

    @Test
    public void AStarParallel_ReturnsValidSchedule () {
        AStarAlgorithmParallel a = new AStarAlgorithmParallel(_costEstimators, _scheduleGenerator);
        for (int i = 0; i < _digraphsToTest.size(); i++) {
            Digraph d = _digraphsToTest.get(i);
            Metrics.init(d.getNodes().size(), 1);
            Schedule s = a.run(d, 2, 1);
            System.out.println("Finished digraph " + TEST_INPUT_FILES[i] + " scheduling on 2 processors and running on 1 core");
            assertTrue(isScheduleValid(s));
            s = a.run(d, 4, 1);
            System.out.println("Finished digraph " + TEST_INPUT_FILES[i] + " scheduling on 4 processors and running on 1 core");
            assertTrue(isScheduleValid(s));

            Metrics.init(d.getNodes().size(), 2);
            s = a.run(d, 2, 2);
            System.out.println("Finished digraph " + TEST_INPUT_FILES[i] + " scheduling on 2 processors and running on 2 cores");
            assertTrue(isScheduleValid(s));
            s = a.run(d, 4, 2);
            System.out.println("Finished digraph " + TEST_INPUT_FILES[i] + " scheduling on 4 processors and running on 2 cores");
            assertTrue(isScheduleValid(s));

            Metrics.init(d.getNodes().size(), 4);
            s = a.run(d, 2, 4);
            System.out.println("Finished digraph " + TEST_INPUT_FILES[i] + " scheduling on 2 processors and running on 4 cores");
            assertTrue(isScheduleValid(s));
            s = a.run(d, 4, 4);
            System.out.println("Finished digraph " + TEST_INPUT_FILES[i] + " scheduling on 4 processors and running on 4 cores");
            assertTrue(isScheduleValid(s));
        }
    }

    @Test
    public void DfsAlgorithmParallel_ReturnsValidSchedule () {
        DfsAlgorithmParallel a = new DfsAlgorithmParallel(_costEstimators, _scheduleGenerator);
        for (Digraph d : _digraphsToTest) {
            Metrics.init(d.getNodes().size(), 1);
            Schedule s = a.run(d, 2, 1);
            assertTrue(isScheduleValid(s));
            s = a.run(d, 4, 1);
            assertTrue(isScheduleValid(s));

            Metrics.init(d.getNodes().size(), 2);
            s = a.run(d, 2, 2);
            assertTrue(isScheduleValid(s));
            s = a.run(d, 4, 2);
            assertTrue(isScheduleValid(s));

            Metrics.init(d.getNodes().size(), 4);
            s = a.run(d, 2, 4);
            assertTrue(isScheduleValid(s));
            s = a.run(d, 4, 4);
            assertTrue(isScheduleValid(s));
        }
    }

    private boolean isScheduleValid (Schedule s) {
        int scheduleEndTime = s.endTime();
        ArrayList<Integer[]> processors = new ArrayList<Integer[]>();

        for (int i = 0; i < s._numOfProcessors; i++) {
            processors.add(new Integer[scheduleEndTime]);
        }

        for (Task t : s.getTasks()) {
            int processor = t.getProcessor();
            Integer[] processorBusyTimes = processors.get(processor);

            for (int i = t.getStartTime(); i < t.getEndTime(); i++) {
                if (processorBusyTimes[i] != null && processorBusyTimes[i] == 1) {
                    return false;
                }
                processorBusyTimes[i] = 1;
            }

            if (!containParents(s, t)) {
                return false;
            }
        }

        return true;
    }

    private boolean containParents (Schedule s, Task task) {
        int taskProcessor = task.getProcessor();
        int taskStartTime = task.getStartTime();

        HashMap<Node, Task> nodeToTaskMap = new HashMap<Node, Task>();

        for (Task t : s.getTasks()) {
            nodeToTaskMap.put(t.getNode(), t);
        }

        List<Link> incomingLinks = task.getNode().getIncomingLinks();

        for (Link link : incomingLinks) {
            Node parentNode = link.getOriginNode();
            Task parentTask = nodeToTaskMap.get(parentNode);

            if (parentTask == null) {
                return false;
            }

            int validStartTime = parentTask.getEndTime();

            if (parentTask.getProcessor() != taskProcessor) {
                validStartTime += link.getTransferCost();
            }

            if (taskStartTime < validStartTime) {
                return false;
            }
        }

        return true;
    }

}