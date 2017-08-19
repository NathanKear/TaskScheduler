package se306.team7;

import java.util.HashMap;

public class Metrics {

	//the total number of levels in the task scheduler solution tree
	private static int _levels;
	//the total number of cores for execution
	private static int _cores;
	//the cost of the current best partial schedule
	private static int _currentBestCost;
	//the histogram of count of schedules explored for each level
	private static HashMap<Integer, Integer> _histogram;
	//the current level being explored for each core
	private static HashMap<Integer, Integer> _coreCurrentLevel;

	/**
	 * When constructing a Metrics object, the following 2 pieces of info should be known beforehand
	 * and remain unchanged.
	 * 1. The total number of nodes in the digraph.
	 * 		This value corresponds to the total number of levels of the task scheduler solution tree.
	 * 		Each level corresponds to the number of tasks in the partial schedule solution.
	 * 		e.g. Level 3 means that 3 tasks have been scheduled in the partial solution.
	 * 2. The total number of cores for execution
	 * @param numOfNodes
	 * @param numOfCores;
	 */
	public Metrics(int numOfNodes, int numOfCores) {
		_levels = numOfNodes;
		_cores = numOfCores;

		_histogram = new HashMap<Integer, Integer>();
		_coreCurrentLevel = new HashMap<Integer, Integer>();
	}

	/**
	 * Invoked when a schedule has been cost-estimated. Includes the ID of the processor on which
	 * the schedule's cost had been calculated for.
	 *
	 * Updates _histogram by incrementing the count of schedules explored for the appropriate level
	 * Updates the current level being explored by the processor (identified by coreID)
	 * Updates the current best cost (of a complete solution found so far) if appropriate.
	 * @param ces
	 * @param coreID
	 */
	public static void doneSchedule(CostEstimatedSchedule ces, int coreID) {
		//Get the level of the cost-estimated schedule
		Integer levelOfScheduleGiven = Integer.valueOf(ces.getSchedule().getTasks().size());

		//Update histogram
		if (_histogram.containsKey(levelOfScheduleGiven)) {
			Integer newCount = _histogram.get(levelOfScheduleGiven) + 1;
			_histogram.put(levelOfScheduleGiven, newCount);
		} else {
			_histogram.put(levelOfScheduleGiven, 1);
		}

		//Update processor's current level
		_coreCurrentLevel.put(Integer.valueOf(coreID), levelOfScheduleGiven);
	}

	public static int getLevels(){
		return _levels;
	}

	public static int getCurrentBestCost(){
		return _currentBestCost;
	}

	public static void setCurrentBestCost(int cost) {
		_currentBestCost = cost;
	}
}
