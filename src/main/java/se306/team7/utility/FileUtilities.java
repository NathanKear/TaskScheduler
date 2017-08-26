package se306.team7.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se306.team7.TaskScheduler;

import java.io.*;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.Charset;


public class FileUtilities implements IFileUtilities {

    private static Logger logger = LoggerFactory.getLogger(FileUtilities.class);

    /**
     * Checks if the file does exists and is not a directory
     * @param filename Relative name of file to load
     * @return boolean
     */
    public boolean doesFileExist(String filename) {
        File file = new File(filename);
        return file.exists() && file.isFile();
    }

    /**
     * Create BufferedReader for specified file
     * @param filename Relative name of file to load
     * @return BufferedReader to read file
     * @throws FileNotFoundException
     */
    public BufferedReader createFileReader(String filename) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(filename);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        return bufferedReader;
    }

    /**
     * Writes the derived optimal schedule to an output file, using the correct notation, as specified
     * in the project documentation
     * @param fileName
     * @param digraphName
     * @param output
     */
    public void writeToFile(String fileName, String digraphName, List<String> output) {
        Path file = Paths.get(fileName);
        try {
        	List<String> fileContents = output;
        	fileContents.add(0, "digraph \"output_" + digraphName + "\" {\n");
        	fileContents.add("}");
            Files.write(file, fileContents, Charset.forName("UTF-8"));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            logger.error(ex.getMessage());
            System.err.println("Output Failed");
            logger.error("Output Failed");
        }
    }
}
