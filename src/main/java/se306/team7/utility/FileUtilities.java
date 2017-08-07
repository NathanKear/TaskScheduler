package se306.team7.utility;

import java.io.*;

public class FileUtilities implements IFileUtilities {

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
}
