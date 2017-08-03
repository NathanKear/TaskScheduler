package se306.team7;

public class CommandLineArgumentConfig {

    private boolean _visualisationOn;
    private String _inputFileName;
    private String _outputFileName;
    private int _scheduleProcessors; // Number of processors that tasks should be distributed over
    private int _applicationProcessors; // Number of processors to distribute this application across

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
    public boolean VisualisationOn() {
        return _visualisationOn;
    }

    /**
     * File name of file containing input digraph
     * @return
     */
    public String InputFileName() {
        return _inputFileName;
    }

    /**
     * File name to print output to
     * @return
     */
    public String OutputFileName() {
        return _outputFileName;
    }

    /**
     * Number of processors that tasks in the schedule may be distributed over
     * @return
     */
    public int ScheduleProcessors() {
        return _scheduleProcessors;
    }

    /**
     * Number of processors this application should use to calculate schedule
     * @return
     */
    public int ApplicationProcessors() {
        return _applicationProcessors;
    }
}
