package se306.team7.visual;

import java.util.concurrent.ConcurrentHashMap;

import se306.team7.Metrics;

/**
 * An interface that should be implemented by all view/controller maintained by TaskSchedulerGUI.
 */
public interface ITaskSchedulerView {

    /**
     * Parameters correspond to the fields in the Metrics class.
     * View/Controller may use any number of these info to update itself.
     * @param currentBestCost The current best cost found by the algorithm
     * @param histogram Histogram displaying how many schedules have been processed at each level of state space tree
     * @param coreCurrentLevel The level of the current schedule being expanded
     */
	void update(
                int currentBestCost,
				ConcurrentHashMap<Integer, Integer> histogram,
				ConcurrentHashMap<Integer, Integer> coreCurrentLevel);
}
