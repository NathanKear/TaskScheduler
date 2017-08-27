package se306.team7;

/**
 * Exception that occurs where there is a failure in passing the command line arguments
 */
public class CommandLineArgumentException extends RuntimeException {
    /**
     * Instantiates a CommandLineArgumentException, which is thrown when
     * an input argument is unknown to the program
     * @param message
     */
    public CommandLineArgumentException(String message) {
        super(message);
    }
}
