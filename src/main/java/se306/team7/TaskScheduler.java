package se306.team7;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.runtime.ParaTask;
import se306.team7.Algorithm.*;
import se306.team7.Digraph.Digraph;
import se306.team7.Digraph.DigraphParser;
import se306.team7.utility.FileUtilities;
import se306.team7.utility.IFileUtilities;
import se306.team7.visual.TaskSchedulerGUI;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

public class TaskScheduler
{
	private static IFileUtilities fileUtilities = new FileUtilities();
	private static ICommandLineArgumentParser commandLineArgumentParser = new CommandLineArgumentParser(fileUtilities);
	private static Logger logger = LoggerFactory.getLogger(TaskScheduler.class);
	private static CommandLineArgumentConfig commandLineArgumentConfig;

    /**
     * Establishes ParaTask, Metrics, FileUtilities and DigraphParser program management tools to setup
     * and maintain the program
     *
     * If visualisation is active, launches the GUI before executing the algorithm
     * Otherwise start execution immediately
     *
     * @param args
     */
    public static void main( String[] args ){
        ParaTask.init();

        PropertyConfigurator.configure("src/log4j.properties");

		try {
			commandLineArgumentConfig = commandLineArgumentParser.parseCommandLineArguments(args);

            ParaTask.setThreadPoolSize(ParaTask.ThreadPoolType.MULTI, commandLineArgumentConfig.applicationProcessors());

			FileUtilities fileUtilities = new FileUtilities();
			DigraphParser digraphParser = new DigraphParser(fileUtilities);
			Digraph digraph = (Digraph)digraphParser.parseDigraph(commandLineArgumentConfig.inputFileName());

			if (commandLineArgumentConfig.visualisationOn()){
				TaskSchedulerGUI.LaunchGUI(args, digraph,commandLineArgumentConfig);
			}else {
				executeAlgorithm(digraph);
			}

        } catch (CommandLineArgumentException ex) {
            System.err.println(ex.getMessage());
            logger.error(ex.getMessage());
            printCommandFormatInfo();
            System.exit(1);
        } catch (IOException IOex) {
            System.err.println(IOex.getMessage());
            logger.error(IOex.getMessage());
            System.err.println("Digraph Parsing Failed");
            logger.error("Digraph Parsing Failed");
        }
	}

	/**
	 * Prints helpful message for user about the format the command should be in
	 */
	private static void printCommandFormatInfo() {
		System.err.print("Expected command format:\n" +
				"\n" +
				"java −jar scheduler.jar INPUT.dot P [OPTION]\n" +
				"INPUT.dot a task graph with integer weights in dot format\n" +
				"P number of processors to schedule the INPUT graph on\n" +
				"Optional :\n" +
				"− p N use N cores for execution in parallel (default is sequential)\n" +
				"− v visualise the search\n" +
				"− o OUPUT output file is named OUTPUT (default is INPUT−output.dot)\n");
	}

    /**
     * Initialises the Cost Estimating, Schedule Generating and Algorithm objects to derive the
     * optimal schedule from the digraph, using the command line arguments to configure the search
     *
     * The more suitable algorithm type is selected and executed based on the size of the digraph
     *
     * Once an optimal schedule has been found, it is printed out to the output file
     *
     * @param d
     */
	public static void executeAlgorithm(Digraph d){
		
			Set<ICostEstimator> costEstimators = new HashSet<ICostEstimator>();
			costEstimators.add(new CriticalPathCostEstimator());
			costEstimators.add(new LoadBalancerCostEstimator());
			IScheduleGenerator scheduleGenerator = new ScheduleGenerator();

            int numOfProcessors = commandLineArgumentConfig.scheduleProcessors();
            int applicationProcessors = commandLineArgumentConfig.applicationProcessors();
            Schedule optimalSchedule;

			AStarAlgorithmParallel a = new AStarAlgorithmParallel(costEstimators, scheduleGenerator);
			optimalSchedule = a.run(d, numOfProcessors, applicationProcessors);

            logger.info("Time = " + optimalSchedule.endTime());

            List<String> output = optimalSchedule.scheduleToStringList();

			fileUtilities.writeToFile(commandLineArgumentConfig.outputFileName(), d._digraphName, output);
	}
}
