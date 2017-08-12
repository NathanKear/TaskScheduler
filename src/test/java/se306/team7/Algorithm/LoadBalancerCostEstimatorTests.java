package se306.team7.Algorithm;

import org.junit.Before;
import org.junit.Test;
import se306.team7.Digraph.Digraph;
import se306.team7.Digraph.IDigraph;
import se306.team7.Digraph.Node;
import se306.team7.Schedule;

import static org.junit.Assert.assertEquals;

public class LoadBalancerCostEstimatorTests {

    ICostEstimator _costEstimator;

    @Before
    public void setupTests() {
        _costEstimator = new LoadBalancerCostEstimator();
    }

    @Test
    public void getCostEstimate_ReturnsFinalCost_WhenNoNodesLeftToAdd() {
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
        int costEstimate = _costEstimator.getCostEstimate(digraph, schedule);

        // Assert
        assertEquals(3, costEstimate);
    }

    @Test
    public void getCostEstimate_ReturnsEstimatedCost_WhenOneNodeLeftToAdd() {
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

        // Act
        int costEstimate = _costEstimator.getCostEstimate(digraph, schedule);

        // Assert
        assertEquals(3, costEstimate);
    }

    @Test
    public void getCostEstimate_ReturnsEstimatedCost_WhenMultipleNodeLeftToAdd() {
        // Arrange

        /**
         * Digraph, cost in brackets
         *
         *           A(1)
         *            |
         *           (1)
         *            |
         *           B(1)
         *          /   \
         *       (1)   (1)
         *       /       \
         *     C(1)     D(10)
         */

        IDigraph digraph = new Digraph("testDigraph");
        Node nA = digraph.addNode("A", 1);
        Node nB = digraph.addNode("B", 1);
        Node nC = digraph.addNode("C", 1);
        Node nD = digraph.addNode("D", 10);
        digraph.addLink("A", "B", 1);
        digraph.addLink("B", "C", 1);
        digraph.addLink("B", "D", 1);

        Schedule schedule = new Schedule(2);
        schedule.scheduleTask(0, nA);

        // Act
        int costEstimate = _costEstimator.getCostEstimate(digraph, schedule);

        // Assert
        assertEquals(7, costEstimate);
    }

    @Test
    public void getCostEstimate_ReturnsCeilingedCost_WhenMultipleNodeLeftToAddThatDivideImperfectly() {
        // Arrange

        /**
         * Digraph, cost in brackets
         *
         *           A(1)
         *            |
         *           (1)
         *            |
         *           B(1)
         *          /   \
         *       (1)   (1)
         *       /       \
         *     C(1)     D(11)
         */

        IDigraph digraph = new Digraph("testDigraph");
        Node nA = digraph.addNode("A", 1);
        Node nB = digraph.addNode("B", 1);
        Node nC = digraph.addNode("C", 1);
        Node nD = digraph.addNode("D", 11);
        digraph.addLink("A", "B", 1);
        digraph.addLink("B", "C", 1);
        digraph.addLink("B", "D", 1);

        Schedule schedule = new Schedule(2);
        schedule.scheduleTask(0, nA);

        // Act
        int costEstimate = _costEstimator.getCostEstimate(digraph, schedule);

        // Assert
        assertEquals(8, costEstimate);
    }
}
