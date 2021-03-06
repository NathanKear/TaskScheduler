package se306.team7.utility;

import java.io.*;
import java.util.List;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.Charset;

/**
 * Provides utility methods related to finding, reading and writing to files
 */
public class FileUtilities implements IFileUtilities {

    /**
     * Checks if the file does exists and is not a directory
     * @param filename Relative name of file
     * @return boolean True if file exists
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
        	fileContents.add(0, "digraph \"output" + digraphName + "\" {");
        	fileContents.add("}");
            Files.write(file, fileContents, Charset.forName("UTF-8"));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            System.err.println("Output Failed");
        }
    }
}
