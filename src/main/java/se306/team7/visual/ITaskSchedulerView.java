package se306.team7.visual;

import java.util.HashMap;

import se306.team7.Metrics;

/**
 * An interface that should be implemented by all view/controller maintained by TaskSchedulerGUI.
 */
public interface ITaskSchedulerView {
	
	int _numOfLevels = Metrics.getLevels();
	int _numOfCores = Metrics.getNumOfCores();

    /**
     * Parameters correspond to the fields in the Metrics class.
     * View/Controller may use any number of these info to update itself.
     * @param numOfLevels
     * @param numOfCores
     * @param currentBestCost
     * @param histogram
     * @param coreCurrentLevel
     */
    void update(
                int currentBestCost,
                HashMap<Integer, Integer> histogram,
                HashMap<Integer, Integer> coreCurrentLevel);
}
