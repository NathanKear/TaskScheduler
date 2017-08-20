package se306.team7;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se306.team7.Algorithm.AStarAlgorithm;
import se306.team7.Digraph.Digraph;
import se306.team7.Digraph.IDigraph;
import se306.team7.Digraph.DigraphParser;
import se306.team7.Digraph.IDigraphBuilder;
import se306.team7.utility.FileUtilities;
import se306.team7.utility.IFileUtilities;

import java.io.*;
import java.util.List;




/**
 * Hello world!
 *
 */
public class TaskScheduler
{
    private static IFileUtilities fileUtilities = new FileUtilities();
    private static ICommandLineArgumentParser commandLineArgumentParser = new CommandLineArgumentParser(fileUtilities);
    private static Logger logger = LoggerFactory.getLogger(TaskScheduler.class);

    public static void main( String[] args )
    {
        PropertyConfigurator.configure("src/log4j.properties");

        CommandLineArgumentConfig commandLineArgumentConfig;

        try {
            commandLineArgumentConfig = commandLineArgumentParser.parseCommandLineArguments(args);
            FileUtilities fileUtilities = new FileUtilities();
            DigraphParser digraphParser = new DigraphParser(fileUtilities);
            Digraph d = (Digraph)digraphParser.parseDigraph(commandLineArgumentConfig.inputFileName());
            AStarAlgorithm a = new AStarAlgorithm();
            Schedule optimalSchedule = a.getOptimalSchedule(d, commandLineArgumentConfig.scheduleProcessors(), null);

            List<String> output = optimalSchedule.scheduleToStringList();

            for (String line : output) {
                logger.info(line);
            }

            fileUtilities.writeToFile(commandLineArgumentConfig.outputFileName(), d._digraphName, output);
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
     * Print helpful message for user about the format the command should be in
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
}
