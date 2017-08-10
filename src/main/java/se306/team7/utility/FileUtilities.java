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
     * @return
     */
    public boolean doesFileExist(String filename) {
        File file = new File(filename);
        return file.exists() && file.isFile();
    }

    /**
     * Create BufferedReader for specified file
     * @param filename Relative name of file to load
     * @return Returns BufferedReader to read file
     * @throws FileNotFoundException
     */
    public BufferedReader createFileReader(String filename) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(filename);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        return bufferedReader;
    }

    public void writeToFile(String fileName, List<String> output) {
        Path file = Paths.get(fileName);
        try {
            Files.write(file, output, Charset.forName("UTF-8"));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            logger.error(ex.getMessage());
            System.err.println("Output Failed");
            logger.error("Output Failed");
        }
    }
}
