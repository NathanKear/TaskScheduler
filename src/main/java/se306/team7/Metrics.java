package se306.team7;

public class Metrics {
	
	//the total number of levels in the task scheduler solution tree
	private int _levels; 
	//the cost of the current best partial schedule
	private int _currentBestCost;	
	
	/**
	 * Receives an integer representing the total number of nodes in the digraph.
	 * This value corresponds to the total number of levels of the task scheduler solution tree.
	 * Each level corresponds to the number of tasks in the partial schedule solution. 
	 * E.g. Level 3 means that 3 tasks have been scheduled in the partial solution.
	 * @param numOfNodes
	 */
	public Metrics(int numOfNodes) {
		_levels = numOfNodes;
	}
	
	public void setCurrentBestCost(int cost) {
		_currentBestCost = cost;
	}
	
	/**
	 * Invoked when a schedule has been cost-estimated. Includes the ID of the processor on which
	 * the schedule's cost had been calculated for. 
	 * @param ces
	 * @param processorID
	 */
	public void doneSchedule(CostEstimatedSchedule ces, int processorID) {
		
	}
}
