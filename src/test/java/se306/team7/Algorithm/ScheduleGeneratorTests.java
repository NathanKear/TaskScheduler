package se306.team7.Algorithm;

import org.junit.Before;
import org.junit.Test;
import se306.team7.Digraph.Digraph;
import se306.team7.Digraph.IDigraph;
import se306.team7.Digraph.Node;
import se306.team7.Schedule;

import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ScheduleGeneratorTests {
    IScheduleGenerator _scheduleGenerator;

    @Before
    public void setupTests() {
        _scheduleGenerator = new ScheduleGenerator();
    }

    @Test
    public void getCostEstimate_ReturnsEmptyList_WhenNoCurrentHeads() {
        // Arrange

        /**
         * Digraph, cost in brackets
         *
         *     A(1)    B(1)
         *     \      /
         *     (1)  (2)
         *      \  /
         *      C(1)
         */

        IDigraph digraph = new Digraph("testDigraph");
        Node nA = digraph.addNode("A", 1);
        Node nB = digraph.addNode("B", 1);
        Node nC = digraph.addNode("C", 1);
        digraph.addLink("A", "C", 1);
        digraph.addLink("B", "C", 2);

        Schedule schedule = new Schedule(2);
        schedule.scheduleTask(0, nA);
        schedule.scheduleTask(1, nB);
        schedule.scheduleTask(1, nC);

        // Act
        List<Schedule> generatedSchedules = _scheduleGenerator.generateSchedules(schedule, new ArrayList<Node>());

        // Assert
        assertNotNull(generatedSchedules);
        assertEquals(0, generatedSchedules.size());
    }

    @Test
    public void getCostEstimate_ReturnsSchedules_WhenOneNodeInCurrentHeads() {
        // Arrange

        /**
         * Digraph, cost in brackets
         *
         *     A(1)    B(1)
         *     \      /
         *     (1)  (2)
         *      \  /
         *      C(4)
         */

        IDigraph digraph = new Digraph("testDigraph");
        Node nA = digraph.addNode("A", 1);
        Node nB = digraph.addNode("B", 1);
        Node nC = digraph.addNode("C", 4);
        digraph.addLink("A", "C", 1);
        digraph.addLink("B", "C", 2);

        Schedule schedule = new Schedule(2);
        schedule.scheduleTask(0, nA);
        schedule.scheduleTask(1, nB);

        List<Node> currentHeads = new ArrayList<Node>();
        currentHeads.add(nC);

        Schedule expected1 = new Schedule(schedule);
        expected1.scheduleTask(0, nC);
        Schedule expected2 = new Schedule(schedule);
        expected2.scheduleTask(1, nC);

        // Act
        List<Schedule> generatedSchedules = _scheduleGenerator.generateSchedules(schedule, currentHeads);

        // Assert
        assertNotNull(generatedSchedules);
        assertEquals(2, generatedSchedules.size());
        assertTrue(generatedSchedules.contains(expected1));
        assertTrue(generatedSchedules.contains(expected2));
    }

    @Test
    public void getCostEstimate_ReturnsSchedules_WhenTwoNodesInCurrentHeads() {
        // Arrange

        /**
         * Digraph, cost in brackets
         *
         *     A(1)    B(1)
         *     \        \
         *     (1)      (1)
         *      \        \
         *      C(4)     D(10)
         */

        IDigraph digraph = new Digraph("testDigraph");
        Node nA = digraph.addNode("A", 1);
        Node nB = digraph.addNode("B", 1);
        Node nC = digraph.addNode("C", 1);
        Node nD = digraph.addNode("D", 10);
        digraph.addLink("A", "C", 1);
        digraph.addLink("B", "C", 2);
        digraph.addLink("B", "D", 1);

        Schedule schedule = new Schedule(2);
        schedule.scheduleTask(0, nA);

        List<Node> currentHeads = new ArrayList<Node>();
        currentHeads.add(nC);
        currentHeads.add(nB);

        Schedule expected1 = new Schedule(schedule);
        expected1.scheduleTask(0, nC);
        Schedule expected2 = new Schedule(schedule);
        expected2.scheduleTask(1, nC);
        Schedule expected3 = new Schedule(schedule);
        expected3.scheduleTask(0, nB);
        Schedule expected4 = new Schedule(schedule);
        expected4.scheduleTask(1, nB);

        // Act
        List<Schedule> generatedSchedules = _scheduleGenerator.generateSchedules(schedule, currentHeads);

        // Assert
        assertNotNull(generatedSchedules);
        assertEquals(4, generatedSchedules.size());
        assertTrue(generatedSchedules.contains(expected1));
        assertTrue(generatedSchedules.contains(expected2));
        assertTrue(generatedSchedules.contains(expected3));
        assertTrue(generatedSchedules.contains(expected4));
    }
}
