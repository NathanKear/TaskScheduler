package se306.team7;

public interface ICommandLineArgumentParser {
    /**
     * Parse the given command line arguments
     * @param args
     * @return Object containing the program configuration specified by the given command line arguments
     */
    CommandLineArgumentConfig parseCommandLineArguments(String[] args);
}
