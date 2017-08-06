package se306.team7;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se306.team7.utility.FileUtilities;
import se306.team7.utility.IFileUtilities;

import java.io.*;




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
        } catch (CommandLineArgumentException ex) {
            System.err.println(ex.getMessage());
            logger.error(ex.getMessage());
            printCommandFormatInfo();
            System.exit(1);
        }

        logger.trace("trace message");
        logger.debug("debug message");
        logger.info("info message");
        logger.warn("warn message");
        logger.error("error message");
        System.out.println( "Hello World!" );
    }

    private static void /*Digraph*/ readInputFileToDigraph(String inputFileName) {

        // Digraph digraph = new Digraph();

        try {

            BufferedReader bufferedReader = fileUtilities.createFileReader(inputFileName);
            String inputLine = bufferedReader.readLine();

            while (inputLine != null) {
                // Send inputLine to DigraphParser
                // digraph = DigraphParser.ParseLine(inputLine, digraph);
            }

        } catch (FileNotFoundException ex) {
            logger.error("Command in incorrect format");
            System.err.printf("Unable to find file %s", inputFileName);
            System.exit(1);
        } catch (IOException ex) {
            logger.error("Command in incorrect format");
            System.err.printf("Unable to read file %s", inputFileName);
            System.exit(1);
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
