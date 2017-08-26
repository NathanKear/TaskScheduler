package se306.team7.visual;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import se306.team7.Metrics;

/**
 * An interface that should be implemented by all view/controller maintained by TaskSchedulerGUI.
 */
public interface ITaskSchedulerView {
	
	int _numOfCores = Metrics.getNumOfCores();
	int _numOfLevels = Metrics.getLevels();

    /**
     * Parameters correspond to the fields in the Metrics class.
     * View/Controller may use any number of these info to update itself.
     * @param currentBestCost
     * @param histogram
     * @param coreCurrentLevel
     */
	void update(
                int currentBestCost,
				ConcurrentHashMap<Integer, Integer> histogram,
				ConcurrentHashMap<Integer, Integer> coreCurrentLevel);
}
