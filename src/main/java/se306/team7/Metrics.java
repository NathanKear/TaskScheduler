package se306.team7;

import java.util.concurrent.ConcurrentHashMap;

/**
 * This class maintains static state info regarding the latest statistics of the running Task Scheduler.
 */
public class Metrics {

	public enum AlgorithmType {
		A_STAR("A*"),
		DFS("DFS");

		private final String algorithmName;

		AlgorithmType(String name) {
			algorithmName = name;
		}

		public String getAlgorithmName() {
			return algorithmName;
		}
	}

	//the total number of levels in the task scheduler solution tree
	private static int _levels;
	//the total number of cores for execution
	private static int _cores;
	//the cost of the current best partial schedule
	private static int _currentBestCost;
	//the histogram of count of schedules explored for each level
	private static ConcurrentHashMap<Integer, Integer> _histogram;
	//the current level being explored for each core
	private static ConcurrentHashMap<Integer, Integer> _coreCurrentLevel;
	//are metrics currently initialised
	private static boolean _isMetricsAvailable = false;
	//the total number of schedules that have been estimated so far
	private static int _totalSchedulesEstimated;

	public static AlgorithmType _algorithmTypeUsed = AlgorithmType.DFS;

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
	public static void init(int numOfNodes, int numOfCores) {
		_levels = numOfNodes;
		_cores = numOfCores;

		_histogram = new ConcurrentHashMap<Integer, Integer>();
		_coreCurrentLevel = new ConcurrentHashMap<Integer, Integer>();

		_isMetricsAvailable = true;

		_totalSchedulesEstimated = 0;
	}

	/**
	 * Invoked when a schedule has been cost-estimated. Includes the ID of the processor on which
	 * the schedule's cost had been calculated for.
	 *
	 * Updates _histogram by incrementing the count of schedules explored for the appropriate level
	 * Updates the current level being explored by the processor (identified by coreID)
	 * @param ces
	 * @param coreID
	 */
	public static void doneSchedule(CostEstimatedSchedule ces, int coreID) {
		if (!_isMetricsAvailable)
			return;

		//Get the level of the cost-estimated schedule
		Integer levelOfScheduleGiven = Integer.valueOf(ces.getSchedule().getTasks().size());

		//Update histogram
		if (_histogram.containsKey(levelOfScheduleGiven)) {
			Integer newCount = _histogram.get(levelOfScheduleGiven) + 1;
			_histogram.put(levelOfScheduleGiven, newCount);
		} else {
			_histogram.put(levelOfScheduleGiven, 1);
		}
		_totalSchedulesEstimated++;

		//Update the core's current level
		_coreCurrentLevel.put(Integer.valueOf(coreID), levelOfScheduleGiven);
	}

	/**
	 * Gets total number of levels in the solution tree
	 * @return int
	 */
	public static int getLevels(){
		return _levels;
	}

	/**
	 * Gets currently estimated best cost
	 * @return int
	 */
	public static int getCurrentBestCost(){
		return _currentBestCost;
	}

	/**
	 * Sets currently estimated best cost
	 * @param cost
	 */
	public static void setCurrentBestCost(int cost) {
		_currentBestCost = cost;
	}

	/**
	 * Gets number of cores used
	 * @return int
	 */
	public static int getNumOfCores(){
		return _cores;
	}

	/**
	 * Gets histogram hashmap
	 * @return ConcurrentHashMap<Integer, Integer>
	 */
	public static ConcurrentHashMap<Integer, Integer> getHistogram(){
		return _histogram;
	}

	/**
	 * Gets the map referring to the level each core is exploring
	 * @return ConcurrentHashMap<Integer, Integer>
	 */
	public static ConcurrentHashMap<Integer, Integer> getCoreCurrentLevel(){
		return _coreCurrentLevel;
	}

	public static AlgorithmType getAlgorithmTypeUsed() {
		return _algorithmTypeUsed;
	}

	public static void setAlgorithmTypeUsed(AlgorithmType type) {
		_algorithmTypeUsed = type;
	}

	public static int getTotalSchedulesEstimated() {
		return _totalSchedulesEstimated;
	}
}
