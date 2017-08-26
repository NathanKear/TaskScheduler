package schedule.valid;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se306.team7.Algorithm.ICostEstimator;
import se306.team7.Algorithm.IScheduleGenerator;
import se306.team7.Algorithm.ScheduleGenerator;
import se306.team7.Schedule;
import se306.team7.Algorithm.AStarAlgorithm;
import se306.team7.Digraph.Digraph;
import se306.team7.Digraph.DigraphParser;
import se306.team7.utility.FileUtilities;
import se306.team7.utility.IFileUtilities;
import org.mockito.Mockito;

/**
 * Tests if the schedule produced is valid.
 *
 */
public class ValidScheduleTest {
	private static Logger _logger = LoggerFactory.getLogger(ValidScheduleTest.class);

	@Mock private FileUtilities _fileUtilities;

	private static final String INPUT_FILE_NAME = "testfile.dot";
	private static final int NUM_OF_SCHEDULE_PROCESSORS = 2;

	@Before
	public void setup() {
		_fileUtilities = Mockito.mock(FileUtilities.class);
	}


	/**
	 * Digraph is simply 3 nodes in linear sequence.
	 * Nodes should be scheduled all in the same processor and in the correct order.
	 * @throws IOException
	 */
	@Test
	public void AStarAlgorithm_Valid_LinearSequentialDigraphInput() throws IOException {
		//Arrange
		when(_fileUtilities.createFileReader(INPUT_FILE_NAME))
		.thenReturn(new BufferedReader(new StringReader(
				"digraph \"Nodes_3_Sequential\" {\n" +
						"	A		[Weight=1];\n" +
						"	B		[Weight=2];\n" +
						"	C		[Weight=3];\n" +
						"	A -> B	[Weight=4];\n" +
						"	A -> C	[Weight=5];\n" +
						"}"
				)));

		DigraphParser digraphParser = new DigraphParser(_fileUtilities);
		Digraph d = (Digraph)digraphParser.parseDigraph(INPUT_FILE_NAME);
		Set<ICostEstimator> costEstimators = new HashSet<ICostEstimator>();
		IScheduleGenerator scheduleGenerator = new ScheduleGenerator();
		AStarAlgorithm a = new AStarAlgorithm(costEstimators, scheduleGenerator);
		Schedule optimalSchedule = a.getOptimalSchedule(d, NUM_OF_SCHEDULE_PROCESSORS);

		List<String> expectedScheduleStringList = new ArrayList<String>();
		expectedScheduleStringList.add("\tA\t\t[Weight=1,Start=0,Processor=0];");
		expectedScheduleStringList.add("\tC\t\t[Weight=3,Start=1,Processor=0];");
		expectedScheduleStringList.add("\tA -> C\t[Weight=5];");
		expectedScheduleStringList.add("\tB\t\t[Weight=2,Start=4,Processor=0];");
		expectedScheduleStringList.add("\tA -> B\t[Weight=4];");

		//Act
		List<String> actualScheduleStringList = optimalSchedule.scheduleToStringList();

		//Assert
		assertEquals(expectedScheduleStringList.size(), actualScheduleStringList.size());
		assertTrue(actualScheduleStringList.containsAll(expectedScheduleStringList));
	}

	/**
	 * Digraph is simply 2 nodes. No edges.
	 * At most one node scheduled per processor.
	 * @throws IOException
	 */
	@Test
	public void AStarAlgorithm_Valid_LoneTasksOnly() throws IOException {
		//Arrange
		when(_fileUtilities.createFileReader("testfile.dot"))
		.thenReturn(new BufferedReader(new StringReader(
				"digraph \"Nodes_2_LoneTasksOnly\" {\n" +
						"	A		[Weight=1];\n" +
						"	B		[Weight=2];\n" +
						"}"
				)));
		DigraphParser digraphParser = new DigraphParser(_fileUtilities);
		Digraph d = (Digraph)digraphParser.parseDigraph(INPUT_FILE_NAME);
		Set<ICostEstimator> costEstimators = new HashSet<ICostEstimator>();
		IScheduleGenerator scheduleGenerator = new ScheduleGenerator();
		AStarAlgorithm a = new AStarAlgorithm(costEstimators, scheduleGenerator);
		Schedule optimalSchedule = a.getOptimalSchedule(d, NUM_OF_SCHEDULE_PROCESSORS);

		List<String> expectedScheduleStringList = new ArrayList<String>();
		expectedScheduleStringList.add("\tA\t\t[Weight=1,Start=0,Processor=0];");
		expectedScheduleStringList.add("\tB\t\t[Weight=2,Start=0,Processor=1];");

		//Act
		List<String> actualScheduleStringList = optimalSchedule.scheduleToStringList();

		//Assert
		assertEquals(expectedScheduleStringList.size(), actualScheduleStringList.size());
		assertTrue(actualScheduleStringList.containsAll(expectedScheduleStringList));
	}

	/**
	 * Digraph consist of 3 nodes. 2 nodes are connected with an edge. The third node is lone.
	 * Nodes with no incoming edges should be scheduled first.
	 * @throws IOException
	 */
	@Test
	public void AStarAlgorithm_Valid_LoneTaskAndDigraph() throws IOException {
		//Arrange
		when(_fileUtilities.createFileReader("testfile.dot"))
		.thenReturn(new BufferedReader(new StringReader(
				"digraph \"Nodes_3_LoneTaskAndDigraph\" {\n" +
						"	A	 [Weight=1];\n" +
						"	B	 [Weight=2];\n" +
						"	C	 [Weight=2];\n" +
						"	A -> B	 [Weight=10];\n" +
						"}"
				)));
		DigraphParser digraphParser = new DigraphParser(_fileUtilities);
		Digraph d = (Digraph)digraphParser.parseDigraph(INPUT_FILE_NAME);
		Set<ICostEstimator> costEstimators = new HashSet<ICostEstimator>();
		IScheduleGenerator scheduleGenerator = new ScheduleGenerator();
		AStarAlgorithm a = new AStarAlgorithm(costEstimators, scheduleGenerator);
		Schedule optimalSchedule = a.getOptimalSchedule(d, NUM_OF_SCHEDULE_PROCESSORS);

		List<String> expectedScheduleStringList = new ArrayList<String>();
		expectedScheduleStringList.add("\tA\t\t[Weight=1,Start=0,Processor=0];");
		expectedScheduleStringList.add("\tC\t\t[Weight=2,Start=0,Processor=1];");
		expectedScheduleStringList.add("\tB\t\t[Weight=2,Start=1,Processor=0];");
		expectedScheduleStringList.add("\tA -> B\t[Weight=10];");
		
		//Act
		List<String> actualScheduleStringList = optimalSchedule.scheduleToStringList();

		//Assert
		assertEquals(expectedScheduleStringList.size(), actualScheduleStringList.size());
		assertTrue(actualScheduleStringList.containsAll(expectedScheduleStringList));
	}

	/**
	 * Digraph consists of 3 nodes. 1 root with 2 child nodes.
	 * Schedule should account for transfer cost when appropriate.
	 * Each task starts at earliest possible start time.
	 * @throws IOException
	 */
	@Test
	public void AStarAlgorithm_Valid_SingleParent() throws IOException {
		//Arrange
		when(_fileUtilities.createFileReader("testfile.dot"))
		.thenReturn(new BufferedReader(new StringReader(
				"digraph \"Nodes_3_SingleParent\" {\n" +
						"	A		[Weight=1];\n" +
						"	B		[Weight=4];\n" +
						"	C		[Weight=5];\n" +
						"	A -> B	[Weight=2];\n" +
						"	A -> C	[Weight=4];\n" +
						"}"
				)));
		DigraphParser digraphParser = new DigraphParser(_fileUtilities);
		Digraph d = (Digraph)digraphParser.parseDigraph(INPUT_FILE_NAME);
		Set<ICostEstimator> costEstimators = new HashSet<ICostEstimator>();
		IScheduleGenerator scheduleGenerator = new ScheduleGenerator();
		AStarAlgorithm a = new AStarAlgorithm(costEstimators, scheduleGenerator);
		Schedule optimalSchedule = a.getOptimalSchedule(d, NUM_OF_SCHEDULE_PROCESSORS);

		List<String> expectedScheduleStringList = new ArrayList<String>();
		expectedScheduleStringList.add("\tA\t\t[Weight=1,Start=0,Processor=0];");
		expectedScheduleStringList.add("\tC\t\t[Weight=5,Start=1,Processor=0];");
		expectedScheduleStringList.add("\tA -> C\t[Weight=4];");
		expectedScheduleStringList.add("\tB\t\t[Weight=4,Start=3,Processor=1];");
		expectedScheduleStringList.add("\tA -> B\t[Weight=2];");

		//Act
		List<String> actualScheduleStringList = optimalSchedule.scheduleToStringList();

		//Assert
		assertEquals(expectedScheduleStringList.size(), actualScheduleStringList.size());
		assertTrue(actualScheduleStringList.containsAll(expectedScheduleStringList));
	}
	
	/**
	 * Digraph with multiple roots.
	 * Roots are to be scheduled independently
	 * Roots should have different critical path values.
	 * @throws IOException
	 */
	@Test
	public void AStarAlgorithm_Valid_MultipleRoots() throws IOException {
		//Arrange
		when(_fileUtilities.createFileReader("testfile.dot"))
		.thenReturn(new BufferedReader(new StringReader(
				"digraph \"Nodes_4_MultipleRoots\" {\n" +
						"	A		[Weight=1];\n" +
						"	B		[Weight=2];\n" +
						"	C		[Weight=3];\n" +
						"	D	 	[Weight=4];\n" +
						"	A -> C	 [Weight=5];\n" +
						"	B -> C	 [Weight=6];\n" +
						"	B -> D	 [Weight=7];\n" +
						"}"
				)));
		
		DigraphParser digraphParser = new DigraphParser(_fileUtilities);
		Digraph d = (Digraph)digraphParser.parseDigraph(INPUT_FILE_NAME);
		Set<ICostEstimator> costEstimators = new HashSet<ICostEstimator>();
		IScheduleGenerator scheduleGenerator = new ScheduleGenerator();
		AStarAlgorithm a = new AStarAlgorithm(costEstimators, scheduleGenerator);
		Schedule optimalSchedule = a.getOptimalSchedule(d, NUM_OF_SCHEDULE_PROCESSORS);

		List<String> expectedScheduleStringList = new ArrayList<String>();
		expectedScheduleStringList.add("\tA\t\t[Weight=1,Start=0,Processor=1];");
		expectedScheduleStringList.add("\tB\t\t[Weight=2,Start=0,Processor=0];");
		expectedScheduleStringList.add("\tD\t\t[Weight=4,Start=2,Processor=0];");
		expectedScheduleStringList.add("\tB -> D\t[Weight=7];");
		expectedScheduleStringList.add("\tC\t\t[Weight=3,Start=6,Processor=0];");
		expectedScheduleStringList.add("\tA -> C\t[Weight=5];");
		expectedScheduleStringList.add("\tB -> C\t[Weight=6];");

		//Act
		List<String> actualScheduleStringList = optimalSchedule.scheduleToStringList();

		//Assert
		assertEquals(expectedScheduleStringList.size(), actualScheduleStringList.size());
		assertTrue(actualScheduleStringList.containsAll(expectedScheduleStringList));
	}
	
	/**
	 * Digraph where a node has multiple parents.
	 * The latest parent should be scheduled on a different processor to the child node.
	 * 
	 * @throws IOException
	 */
	@Test
	public void AStarAlgorithm_Valid_MultipleParents() throws IOException {
		//Arrange
		when(_fileUtilities.createFileReader("testfile.dot"))
		.thenReturn(new BufferedReader(new StringReader(
				"digraph \"Nodes_4_MultipleParents\" {\n" +
						"	A		[Weight=1];\n" +
						"	B		[Weight=2];\n" +
						"	A -> B	[Weight=10];\n" +
						"	C	 [Weight=2];\n" +
						"	A -> C	 [Weight=1];\n" +
						"	D	 [Weight=1];\n" +
						"	B -> D	 [Weight=10];\n" +
						"	C -> D	 [Weight=1];\n" +
						"}"
				)));
				
		DigraphParser digraphParser = new DigraphParser(_fileUtilities);
		Digraph d = (Digraph)digraphParser.parseDigraph(INPUT_FILE_NAME);
		Set<ICostEstimator> costEstimators = new HashSet<ICostEstimator>();
		IScheduleGenerator scheduleGenerator = new ScheduleGenerator();
		AStarAlgorithm a = new AStarAlgorithm(costEstimators, scheduleGenerator);
		Schedule optimalSchedule = a.getOptimalSchedule(d, NUM_OF_SCHEDULE_PROCESSORS);

		List<String> expectedScheduleStringList = new ArrayList<String>();
		expectedScheduleStringList.add("\tA\t\t[Weight=1,Start=0,Processor=1];");
		expectedScheduleStringList.add("\tB\t\t[Weight=2,Start=1,Processor=1];");
		expectedScheduleStringList.add("\tA -> B\t[Weight=10];");
		expectedScheduleStringList.add("\tC\t\t[Weight=2,Start=2,Processor=0];");
		expectedScheduleStringList.add("\tA -> C\t[Weight=1];");
		expectedScheduleStringList.add("\tD\t\t[Weight=1,Start=5,Processor=1];");
		expectedScheduleStringList.add("\tB -> D\t[Weight=10];");
		expectedScheduleStringList.add("\tC -> D\t[Weight=1];");
		
		//Act
		List<String> actualScheduleStringList = optimalSchedule.scheduleToStringList();

		//Assert
		assertEquals(expectedScheduleStringList.size(), actualScheduleStringList.size());
		assertTrue(actualScheduleStringList.containsAll(expectedScheduleStringList));
	}

	/**
	 * Digraph contains a node with multiple parents, where the latest parent should be scheduled on a processor
	 * that is DIFFERENT from the child node. 
	 * Checks that the child node is only scheduled after all its parents have been scheduled.
	 * @throws IOException
	 */
	@Test
	public void AStarAlgorithm_Valid_LatestParentDiffProcessorNoTaskBetween() throws IOException {
		//Arrange
		when(_fileUtilities.createFileReader("testfile.dot"))
		.thenReturn(new BufferedReader(new StringReader(
				"digraph \"Nodes_4_LatestParentDiffProcessorNoTaskBetween\" {\n" +
						"	A		[Weight=1];\n" +
						"	B		[Weight=1];\n" +
						"	A -> B	 [Weight=1];\n" +
						"	C	 [Weight=1];\n" +
						"	A -> C	 [Weight=10];\n" +
						"	D	 [Weight=1];\n" +
						"	B -> D	 [Weight=1];\n" +
						"	C -> D	 [Weight=10];\n" +
						"}"
				)));
		
		DigraphParser digraphParser = new DigraphParser(_fileUtilities);
		Digraph d = (Digraph)digraphParser.parseDigraph(INPUT_FILE_NAME);
		Set<ICostEstimator> costEstimators = new HashSet<ICostEstimator>();
		IScheduleGenerator scheduleGenerator = new ScheduleGenerator();
		AStarAlgorithm a = new AStarAlgorithm(costEstimators, scheduleGenerator);
		Schedule optimalSchedule = a.getOptimalSchedule(d, NUM_OF_SCHEDULE_PROCESSORS);

		List<String> expectedScheduleStringList = new ArrayList<String>();
		expectedScheduleStringList.add("\tA\t\t[Weight=1,Start=0,Processor=0];");
		expectedScheduleStringList.add("\tB\t\t[Weight=1,Start=1,Processor=0];");
		expectedScheduleStringList.add("\tA -> B\t[Weight=1];");
		expectedScheduleStringList.add("\tC\t\t[Weight=1,Start=2,Processor=0];");
		expectedScheduleStringList.add("\tA -> C\t[Weight=10];");
		expectedScheduleStringList.add("\tD\t\t[Weight=1,Start=3,Processor=0];");
		expectedScheduleStringList.add("\tB -> D\t[Weight=1];");
		expectedScheduleStringList.add("\tC -> D\t[Weight=10];");
		
		//Act
		List<String> actualScheduleStringList = optimalSchedule.scheduleToStringList();

		//Assert
		assertEquals(expectedScheduleStringList.size(), actualScheduleStringList.size());
		assertTrue(actualScheduleStringList.containsAll(expectedScheduleStringList));
	}
	
	/**
	 * Digraph where a node has multiple parents, where the latest parent should be scheduled on a processor
	 * that is SAME from the child node.
	 * Checks that the child node is only scheduled after all its parents have been scheduled.
	 * @throws IOException
	 */
	@Test
	public void AStarAlgorithm_Valid_LatestParentSameProcessorNoTaskBetween() throws IOException {
		//Arrange
		when(_fileUtilities.createFileReader("testfile.dot"))
		.thenReturn(new BufferedReader(new StringReader(
				"digraph \"Nodes_4_LatestParentSameProcessorNoTaskBetween\" {\n" +
						"	A	 [Weight=1];\n" +
						"	B	 [Weight=5];\n" +
						"	A -> B	 [Weight=10];\n" +
						"	C	 [Weight=1];\n" +
						"	A -> C	 [Weight=1];\n" +
						"	D	 [Weight=1];\n" +
						"	B -> D	 [Weight=1];\n" +
						"	C -> D	 [Weight=1];\n" +
						"}"
				)));
		
		DigraphParser digraphParser = new DigraphParser(_fileUtilities);
		Digraph d = (Digraph)digraphParser.parseDigraph(INPUT_FILE_NAME);
		Set<ICostEstimator> costEstimators = new HashSet<ICostEstimator>();
		IScheduleGenerator scheduleGenerator = new ScheduleGenerator();
		AStarAlgorithm a = new AStarAlgorithm(costEstimators, scheduleGenerator);
		Schedule optimalSchedule = a.getOptimalSchedule(d, NUM_OF_SCHEDULE_PROCESSORS);

		List<String> expectedScheduleStringList = new ArrayList<String>();
		expectedScheduleStringList.add("\tA\t\t[Weight=1,Start=0,Processor=0];");
		expectedScheduleStringList.add("\tC\t\t[Weight=1,Start=2,Processor=1];");
		expectedScheduleStringList.add("\tA -> C\t[Weight=1];");
		expectedScheduleStringList.add("\tB\t\t[Weight=5,Start=1,Processor=0];");
		expectedScheduleStringList.add("\tA -> B\t[Weight=10];");
		expectedScheduleStringList.add("\tD\t\t[Weight=1,Start=6,Processor=0];");
		expectedScheduleStringList.add("\tB -> D\t[Weight=1];");
		expectedScheduleStringList.add("\tC -> D\t[Weight=1];");
		
		//Act
		List<String> actualScheduleStringList = optimalSchedule.scheduleToStringList();

		//Assert
		assertEquals(expectedScheduleStringList.size(), actualScheduleStringList.size());
		assertTrue(actualScheduleStringList.containsAll(expectedScheduleStringList));
	}
	
	/**
	 * Digraph contains a node with multiple parents, where the latest parent should be scheduled on a processor
	 * that is DIFFERENT from the child node. Furthermore, there IS a task between the latest parent and child.
	 * Checks if the task is 'schedule-aware': the task's start time does not conflict with the latest task on the same processor.
	 * Checks that the child node is only scheduled after all its parents have been scheduled.
	 * @throws IOException
	 */
	@Test
	public void AStarAlgorithm_Valid_LatestParentDiffProcessorTaskBetween() throws IOException {
		//Arrange
		when(_fileUtilities.createFileReader("testfile.dot"))
		.thenReturn(new BufferedReader(new StringReader(
				"digraph \"Nodes_5_LatestParentDiffProcessorTaskBetween\" {\n" +
						"	A	 [Weight=1];\n" +
						"	B	 [Weight=3];\n" +
						"	A -> B	 [Weight=1];\n" +
						"	C	 [Weight=1];\n" +
						"	A -> C	 [Weight=10];\n" +
						"	D	 [Weight=5];\n" +
						"	C -> D	 [Weight=10];\n" +
						"	E	 [Weight=1];\n" +
						"	D -> E	 [Weight=10];\n" +
						"	B -> E	 [Weight=1];\n" +
						"}"
				)));
		
		DigraphParser digraphParser = new DigraphParser(_fileUtilities);
		Digraph d = (Digraph)digraphParser.parseDigraph(INPUT_FILE_NAME);
		Set<ICostEstimator> costEstimators = new HashSet<ICostEstimator>();
		IScheduleGenerator scheduleGenerator = new ScheduleGenerator();
		AStarAlgorithm a = new AStarAlgorithm(costEstimators, scheduleGenerator);
		Schedule optimalSchedule = a.getOptimalSchedule(d, NUM_OF_SCHEDULE_PROCESSORS);

		List<String> expectedScheduleStringList = new ArrayList<String>();
		expectedScheduleStringList.add("\tA\t\t[Weight=1,Start=0,Processor=1];");
		expectedScheduleStringList.add("\tB\t\t[Weight=3,Start=2,Processor=0];");
		expectedScheduleStringList.add("\tA -> B\t[Weight=1];");
		expectedScheduleStringList.add("\tC\t\t[Weight=1,Start=1,Processor=1];");
		expectedScheduleStringList.add("\tA -> C\t[Weight=10];");
		expectedScheduleStringList.add("\tD\t\t[Weight=5,Start=2,Processor=1];");
		expectedScheduleStringList.add("\tC -> D\t[Weight=10];");
		expectedScheduleStringList.add("\tE\t\t[Weight=1,Start=7,Processor=1];");
		expectedScheduleStringList.add("\tD -> E\t[Weight=10];");
		expectedScheduleStringList.add("\tB -> E\t[Weight=1];");
		
		//Act
		List<String> actualScheduleStringList = optimalSchedule.scheduleToStringList();

		//Assert
		assertEquals(expectedScheduleStringList.size(), actualScheduleStringList.size());
		assertTrue(actualScheduleStringList.containsAll(expectedScheduleStringList));
	}
	
	/**
	 * Digraph where a node has multiple parents, where the latest parent should be scheduled on a processor
	 * that is SAME from the child node. Furthermore, there IS a task between the latest parent and child.
	 * Checks if the task is 'schedule-aware': the task's start time does not conflict with the latest task on the same processor.
	 * @throws IOException
	 */
	@Test
	public void AStarAlgorithm_Valid_LatestParentSameProcessorTaskBetween() throws IOException {
		//Arrange
		when(_fileUtilities.createFileReader("testfile.dot"))
		.thenReturn(new BufferedReader(new StringReader(
				"digraph \"Nodes_5_LatestParentSameProcessorTaskBetween\" {\n" +
						"	A		[Weight=1];\n" +
						"	B		[Weight=1];\n" +
						"	A -> B	 [Weight=1];\n" +
						"	C	 [Weight=3];\n" +
						"	A -> C	 [Weight=10];\n" +
						"	D	 [Weight=1];\n" +
						"	C -> D	 [Weight=10];\n" +
						"	E	 [Weight=1];\n" +
						"	D -> E	 [Weight=10];\n" +
						"	B -> E	 [Weight=1];\n" +
						"}"
				)));
		
		DigraphParser digraphParser = new DigraphParser(_fileUtilities);
		Digraph d = (Digraph)digraphParser.parseDigraph(INPUT_FILE_NAME);
		Set<ICostEstimator> costEstimators = new HashSet<ICostEstimator>();
		IScheduleGenerator scheduleGenerator = new ScheduleGenerator();
		AStarAlgorithm a = new AStarAlgorithm(costEstimators, scheduleGenerator);
		Schedule optimalSchedule = a.getOptimalSchedule(d, NUM_OF_SCHEDULE_PROCESSORS);

		List<String> expectedScheduleStringList = new ArrayList<String>();
		expectedScheduleStringList.add("\tA\t\t[Weight=1,Start=0,Processor=1];");
		expectedScheduleStringList.add("\tB\t\t[Weight=1,Start=2,Processor=0];");
		expectedScheduleStringList.add("\tA -> B\t[Weight=1];");
		expectedScheduleStringList.add("\tC\t\t[Weight=3,Start=1,Processor=1];");
		expectedScheduleStringList.add("\tA -> C\t[Weight=10];");
		expectedScheduleStringList.add("\tD\t\t[Weight=1,Start=4,Processor=1];");
		expectedScheduleStringList.add("\tC -> D\t[Weight=10];");
		expectedScheduleStringList.add("\tE\t\t[Weight=1,Start=5,Processor=1];");
		expectedScheduleStringList.add("\tD -> E\t[Weight=10];");
		expectedScheduleStringList.add("\tB -> E\t[Weight=1];");

		//Act
		List<String> actualScheduleStringList = optimalSchedule.scheduleToStringList();

		//Assert
		assertEquals(expectedScheduleStringList.size(), actualScheduleStringList.size());
		assertTrue(actualScheduleStringList.containsAll(expectedScheduleStringList));
	}
}
