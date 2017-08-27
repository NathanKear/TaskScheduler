package se306.team7;

import se306.team7.utility.IFileUtilities;

public class CommandLineArgumentParser implements ICommandLineArgumentParser {

    private IFileUtilities _fileUtilities;

    /**
     * Create a CommandLineArgumentParser using the specified FileUtilities implementation
     * @param fileUtilities
     */
    public CommandLineArgumentParser(IFileUtilities fileUtilities) {
        _fileUtilities = fileUtilities;
    }

    /**
     * Parse the given command line arguments into a data object
     * @param args Command line arguments
     * @return CommandLineArgumentConfig object containing the desire program configuration
     */
    public CommandLineArgumentConfig parseCommandLineArguments(String[] args) {

        boolean visualisationOn = false;
        String inputFileName;
        String outputFileName;
        int scheduleProcessors = 1; // Number of processors that tasks should be distributed over
        int applicationProcessors = 1; // Number of processors to distribute this application across

        // Check required args are present
        if (args.length <= 1) {
            throw new CommandLineArgumentException("Expected at least two arguments");
        }

        inputFileName = args[0];

        // trim .dot off end of file name
        // e.g. input file foo.dot produces output file foo-output.dot
        // and not foo.dot-output.dot
        outputFileName = inputFileName.endsWith(".dot")
                ? inputFileName.substring(0, inputFileName.length() - 4) + "-output.dot"
                : inputFileName + "-output.dot";

        // Check input file exists
        if (!_fileUtilities.doesFileExist(inputFileName)) {
            throw new CommandLineArgumentException(String.format("Unable to find file %s", inputFileName));
        }

        // Check value is positive int
        try {
            scheduleProcessors = Integer.parseInt(args[1]);
        } catch(NumberFormatException ex) {
            throw new CommandLineArgumentException(String.format("Failed to parse argument P to int:\"%s\"", args[1]));
        }

        if (scheduleProcessors < 1) {
            throw new CommandLineArgumentException("Input argument P must not be zero or less");
        }

        // Parse remaining optional args
        for (int i = 2; i < args.length; i++) {
            if (args[i].equals("-v")) {

                visualisationOn = true;

            } else if (args[i].equals("-p")) {

                if (i + 1 >= args.length) {
                    throw new CommandLineArgumentException("Optional arg -p must include the number of processors to use");
                }

                try {
                    applicationProcessors = Integer.parseInt(args[i + 1]);
                } catch(NumberFormatException ex) {
                    throw new CommandLineArgumentException(String.format("Failed to parse argument N to int:\"%s\"", args[i + 1]));
                }

                if (applicationProcessors < 1) {
                    throw new CommandLineArgumentException("Input argument N must not be zero or less");
                }

                i++;

            } else if (args[i].equals("-o")) {

                if (i + 1 >= args.length) {
                    throw new CommandLineArgumentException("Optional arg -o must include the output file name to use");
                }

                String userCustomOutputFileName = args[i + 1];

                outputFileName = userCustomOutputFileName.endsWith(".dot")
                        ? userCustomOutputFileName
                        : userCustomOutputFileName + ".dot";

                i++;

            } else {

                throw new CommandLineArgumentException(String.format("Unrecognized command line arg: %s", args[i]));

            }
        }

        return new CommandLineArgumentConfig(
                inputFileName,
                scheduleProcessors,
                applicationProcessors,
                visualisationOn,
                outputFileName);
    }
}
