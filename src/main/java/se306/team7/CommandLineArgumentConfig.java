package se306.team7;

public class CommandLineArgumentConfig {

    private boolean _visualisationOn;
    private String _inputFileName;
    private String _outputFileName;
    private int _scheduleProcessors; // Number of processors that tasks should be distributed over
    private int _applicationProcessors; // Number of processors to distribute this application across

    /**
     * Instantiates the program settings handler to manage the input arguments
     * @param inputFileName
     * @param scheduleProcessors
     * @param applicationProcessors
     * @param visualisationOn
     * @param outputFileName
     */
    public CommandLineArgumentConfig(
            String inputFileName,
            int scheduleProcessors,
            int applicationProcessors,
            boolean visualisationOn,
            String outputFileName) {
        _inputFileName = inputFileName;
        _scheduleProcessors = scheduleProcessors;
        _applicationProcessors = applicationProcessors;
        _visualisationOn = visualisationOn;
        _outputFileName = outputFileName;
    }


    /**
     * Should the application display a visualisation
     * @return
     */
    public boolean visualisationOn() {
        return _visualisationOn;
    }

    /**
     * File name of file containing input digraph
     * @return
     */
    public String inputFileName() {
        return _inputFileName;
    }

    /**
     * File name to print output to
     * @return
     */
    public String outputFileName() {
        return _outputFileName;
    }

    /**
     * Number of processors that tasks in the schedule may be distributed over
     * @return
     */
    public int scheduleProcessors() {
        return _scheduleProcessors;
    }

    /**
     * Number of processors this application should use to calculate schedule
     * @return
     */
    public int applicationProcessors() {
        return _applicationProcessors;
    }
}
