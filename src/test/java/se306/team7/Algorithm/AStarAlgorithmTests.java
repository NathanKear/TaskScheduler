package se306.team7.Algorithm;

import org.junit.Before;
import org.junit.Test;
import se306.team7.Digraph.Digraph;
import se306.team7.Digraph.IDigraph;
import se306.team7.Digraph.Node;
import se306.team7.Schedule;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AStarAlgorithmTests {
    Set<ICostEstimator> _costEstimators;
    IAlgorithm _algorithm;

    @Before
    public void setupTests() {
        _costEstimators = new HashSet<ICostEstimator>();
        _costEstimators.add(new CriticalPathCostEstimator());
        _costEstimators.add(new LoadBalancerCostEstimator());
        _algorithm = new AStarAlgorithm(_costEstimators, new ScheduleGenerator());
    }

    @Test
    public void getOptimalSchedule_ReturnsSchedule_WhenPassedSimpleDigraphOnOneProcessor() {
        // Arrange

        /**
         * Digraph, cost in brackets
         *
         *     A(1)    B(1)
         *     \      /
         *     (0)  (0)
         *      \  /
         *      C(1)
         */

        IDigraph digraph = new Digraph("testDigraph");
        Node nA = digraph.addNode("A", 1);
        Node nB = digraph.addNode("B", 1);
        Node nC = digraph.addNode("C", 1);
        digraph.addLink("A", "C", 0);
        digraph.addLink("B", "C", 0);

        // Act
        Schedule optimalSchedule = _algorithm.getOptimalSchedule(digraph, 1);

        // Assert
        assertNotNull(optimalSchedule);
        assertEquals(1, optimalSchedule._numOfProcessors);
        assertEquals(3, optimalSchedule.endTime());
    }

    @Test
    public void getOptimalSchedule_ReturnsSchedule_WhenPassedSimpleDigraphOnTwoProcessor() {
        // Arrange

        /**
         * Digraph, cost in brackets
         *
         *     A(1)    B(1)
         *     \      /
         *     (1)  (5)
         *      \  /
         *      C(2)
         */

        IDigraph digraph = new Digraph("testDigraph");
        Node nA = digraph.addNode("A", 1);
        Node nB = digraph.addNode("B", 1);
        Node nC = digraph.addNode("C", 2);
        digraph.addLink("A", "C", 1);
        digraph.addLink("B", "C", 5);

        // Act
        Schedule optimalSchedule = _algorithm.getOptimalSchedule(digraph, 2);

        // Assert
        assertNotNull(optimalSchedule);
        assertEquals(2, optimalSchedule._numOfProcessors);
        assertEquals(4, optimalSchedule.endTime());
    }

    @Test
    public void getOptimalSchedule_ReturnsSchedule_WhenPassedSimpleDigraphOnThreeProcessor() {
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

        // Act
        Schedule optimalSchedule = _algorithm.getOptimalSchedule(digraph, 3);

        // Assert
        assertNotNull(optimalSchedule);
        assertEquals(3, optimalSchedule._numOfProcessors);
        assertEquals(12, optimalSchedule.endTime());
    }

    @Test
    public void getOptimalSchedule_ReturnsSchedule_WhenPassedTreeDigraphOnOneProcessor() {
        // Arrange

        /**
         * Digraph, cost in brackets
         *
         *                 A(5)
         *               /  |  \
         *            (15) (11) (11)
         *            /     |    \
         *          B(6)   C(5)  D(6)
         *        /  |  \
         *     (19) (4) (21)
         *     /     |    \
         *   E(4)   F(7)  G(7)
         */

        IDigraph digraph = new Digraph("testDigraph");
        Node nA = digraph.addNode("A", 5);
        Node nB = digraph.addNode("B", 6);
        Node nC = digraph.addNode("C", 5);
        Node nD = digraph.addNode("D", 6);
        Node nE = digraph.addNode("E", 4);
        Node nF = digraph.addNode("F", 7);
        Node nG = digraph.addNode("G", 7);
        digraph.addLink("A", "B", 15);
        digraph.addLink("A", "C", 11);
        digraph.addLink("A", "D", 11);
        digraph.addLink("B", "E", 19);
        digraph.addLink("B", "F", 4);
        digraph.addLink("B", "G", 21);

        // Act
        Schedule optimalSchedule = _algorithm.getOptimalSchedule(digraph, 1);

        // Assert
        assertNotNull(optimalSchedule);
        assertEquals(1, optimalSchedule._numOfProcessors);
        assertEquals(40, optimalSchedule.endTime());
    }

    @Test
    public void getOptimalSchedule_ReturnsSchedule_WhenPassedTreeDigraphOnTwoProcessors() {
        // Arrange

        /**
         * Digraph, cost in brackets
         *
         *                 A(5)
         *               /  |  \
         *            (15) (11) (11)
         *            /     |    \
         *          B(6)   C(5)  D(6)
         *        /  |  \
         *     (19) (4) (21)
         *     /     |    \
         *   E(4)   F(7)  G(7)
         */

        IDigraph digraph = new Digraph("testDigraph");
        Node nA = digraph.addNode("A", 5);
        Node nB = digraph.addNode("B", 6);
        Node nC = digraph.addNode("C", 5);
        Node nD = digraph.addNode("D", 6);
        Node nE = digraph.addNode("E", 4);
        Node nF = digraph.addNode("F", 7);
        Node nG = digraph.addNode("G", 7);
        digraph.addLink("A", "B", 15);
        digraph.addLink("A", "C", 11);
        digraph.addLink("A", "D", 11);
        digraph.addLink("B", "E", 19);
        digraph.addLink("B", "F", 4);
        digraph.addLink("B", "G", 21);

        // Act
        Schedule optimalSchedule = _algorithm.getOptimalSchedule(digraph, 2);

        // Assert
        assertNotNull(optimalSchedule);
        assertEquals(2, optimalSchedule._numOfProcessors);
        assertEquals(40, optimalSchedule.endTime());
    }
}
