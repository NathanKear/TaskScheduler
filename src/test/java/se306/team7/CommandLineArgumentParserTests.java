package se306.team7;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import se306.team7.utility.FileUtilities;
import se306.team7.utility.IFileUtilities;

import static org.mockito.Mockito.*;

public class CommandLineArgumentParserTests {

    private ICommandLineArgumentParser _commandLineArgumentParser;
    private IFileUtilities _fileUtilities;

    public CommandLineArgumentParserTests()
    {
        _fileUtilities = mock(FileUtilities.class);
        when(_fileUtilities.DoesFileExist("input.dot")).thenReturn(true);
        when(_fileUtilities.DoesFileExist("not-input.dot")).thenReturn(false);
        _commandLineArgumentParser = new CommandLineArgumentParser(_fileUtilities);
    }

    @Test
    public void ParseCommandLineArguments_Returns_WhenOnlyMinimumArgs()
    {
        // Arrange
        String[] args = new String[] { "input.dot", "4" };

        // Act
        CommandLineArgumentConfig config = _commandLineArgumentParser.parseCommandLineArguments(args);

        // Assert
        assertEquals("input.dot", config.inputFileName());
        assertEquals(4, config.scheduleProcessors());
        assertEquals("input-output.dot", config.outputFileName());
        assertEquals(1, config.applicationProcessors());
        assertEquals(false, config.visualisationOn());
    }

    @Test
    public void ParseCommandLineArguments_Returns_WhenVisualisationOn() {
        // Arrange
        String[] args = new String[] { "input.dot", "4", "-v" };

        // Act
        CommandLineArgumentConfig config = _commandLineArgumentParser.parseCommandLineArguments(args);

        // Assert
        assertEquals(true, config.visualisationOn());
    }

    @Test
    public void ParseCommandLineArguments_Returns_WhenApplicationProcessorsSpecified() {
        // Arrange
        String[] args = new String[] { "input.dot", "4", "-p", "8" };

        // Act
        CommandLineArgumentConfig config = _commandLineArgumentParser.parseCommandLineArguments(args);

        // Assert
        assertEquals(8, config.applicationProcessors());
    }

    @Test
    public void ParseCommandLineArguments_Returns_WhenOutputFileSpecified() {
        // Arrange
        String[] args = new String[] { "input.dot", "4", "-o", "foo.dot" };

        // Act
        CommandLineArgumentConfig config = _commandLineArgumentParser.parseCommandLineArguments(args);

        // Assert
        assertEquals("foo.dot", config.outputFileName());
    }

    @Test
    public void ParseCommandLineArguments_Returns_WhenAllOptionalArgsUsed()
    {
        // Arrange
        String[] args = new String[] { "input.dot", "4", "-v", "-o", "output.dot", "-p", "8" };

        // Act
        CommandLineArgumentConfig config = _commandLineArgumentParser.parseCommandLineArguments(args);

        // Assert
        assertEquals("input.dot", config.inputFileName());
        assertEquals(4, config.scheduleProcessors());
        assertEquals("output.dot", config.outputFileName());
        assertEquals(8, config.applicationProcessors());
        assertEquals(true, config.visualisationOn());
    }

    @Test(expected = CommandLineArgumentException.class)
    public void ParseCommandLineArguments_Throws_WhenUnknownArg() {
        // Arrange
        String[] args = new String[] { "input.dot", "4", "-unknownArg" };

        // Act
        CommandLineArgumentConfig config = _commandLineArgumentParser.parseCommandLineArguments(args);
    }

    @Test(expected = CommandLineArgumentException.class)
    public void ParseCommandLineArguments_Throws_WhenTooFewArgs() {
        // Arrange
        String[] args = new String[] { "input.dot" };

        // Act
        CommandLineArgumentConfig config = _commandLineArgumentParser.parseCommandLineArguments(args);
    }

    @Test(expected = CommandLineArgumentException.class)
    public void ParseCommandLineArguments_Throws_WhenInputFileDoesntExist() {
        // Arrange
        String[] args = new String[] { "not-input.dot", "4" };

        // Act
        CommandLineArgumentConfig config = _commandLineArgumentParser.parseCommandLineArguments(args);
    }

    @Test(expected = CommandLineArgumentException.class)
    public void ParseCommandLineArguments_Throws_WhenScheduleProcessorsNotInt() {
        // Arrange
        String[] args = new String[] { "input.dot", "notAnInt" };

        // Act
        CommandLineArgumentConfig config = _commandLineArgumentParser.parseCommandLineArguments(args);
    }

    @Test(expected = CommandLineArgumentException.class)
    public void ParseCommandLineArguments_Throws_WhenScheduleProcessorsLessThanOne() {
        // Arrange
        String[] args = new String[] { "input.dot", "0" };

        // Act
        CommandLineArgumentConfig config = _commandLineArgumentParser.parseCommandLineArguments(args);
    }

    @Test(expected = CommandLineArgumentException.class)
    public void ParseCommandLineArguments_Throws_WhenApplicationProcessorsNotInt() {
        // Arrange
        String[] args = new String[] { "input.dot", "4", "-p", "notAnInt" };

        // Act
        CommandLineArgumentConfig config = _commandLineArgumentParser.parseCommandLineArguments(args);
    }

    @Test(expected = CommandLineArgumentException.class)
    public void ParseCommandLineArguments_Throws_WhenApplicationProcessorsLessThanOne() {
        // Arrange
        String[] args = new String[] { "input.dot", "4", "-p", "0" };

        // Act
        CommandLineArgumentConfig config = _commandLineArgumentParser.parseCommandLineArguments(args);
    }

    @Test(expected = CommandLineArgumentException.class)
    public void ParseCommandLineArguments_Throws_WhenApplicationProcessorsOptionalArgMissingValue() {
        // Arrange
        String[] args = new String[] { "input.dot", "4", "-p" };

        // Act
        CommandLineArgumentConfig config = _commandLineArgumentParser.parseCommandLineArguments(args);
    }

    @Test(expected = CommandLineArgumentException.class)
    public void ParseCommandLineArguments_Throws_WhenOutputFileNameOptionalArgMissingValue() {
        // Arrange
        String[] args = new String[] { "input.dot", "4", "-o" };

        // Act
        CommandLineArgumentConfig config = _commandLineArgumentParser.parseCommandLineArguments(args);
    }
}
