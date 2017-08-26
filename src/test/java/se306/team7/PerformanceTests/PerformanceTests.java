package se306.team7.PerformanceTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se306.team7.TaskScheduler;
import se306.team7.utility.IStopWatch;
import se306.team7.utility.StopWatch;
import se306.team7.utility.TimeUnit;

public class PerformanceTests {

    /**
     * Number of times to repeat test for each test input
     */
    private final int TEST_COUNT = 1;

    /**
     * test input files for performance tests
     */
    private final String[] TEST_INPUT_FILES = { "Nodes_7_OutTree.dot", "Nodes_8_Random.dot",
            "Nodes_9_SeriesParallel.dot", "Nodes_10_Random.dot", "Nodes_11_OutTree.dot", "Nodes_13_Random.dot" };

    private final Logger _logger = LoggerFactory.getLogger(PerformanceTests.class);

    public static void main(String[] args) {
        new PerformanceTests().run();
    }

    private void run() {

        for (String input : TEST_INPUT_FILES) {

            double[] times = new double[TEST_COUNT];
            double totalTime = 0;

            for (int i = 0; i < TEST_COUNT; i++) {

                IStopWatch stopWatch = new StopWatch(TimeUnit.Millisecond);
                stopWatch.Start();
                
                TaskScheduler.main(new String[] { input, "2", "-p", "2" } );

                double time = stopWatch.Stop();

                totalTime += time;
                times[i] = time;

            }

            double meanTime = totalTime / TEST_COUNT;

            System.out.printf("Performance Test: %s ; Mean = %d ms\n", input, (int)meanTime);

            for (int i = 0; i < TEST_COUNT; i++) {
                System.out.printf("Test %s = %d ms\n", i, (int)times[i]);
            }

            System.out.printf("\n");
        }
    }
}
