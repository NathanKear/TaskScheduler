package se306.team7.utility;

import java.io.*;

public class FileUtilities implements IFileUtilities {
    public boolean DoesFileExist(String filename) {
        File file = new File(filename);
        return file.exists() && file.isFile();
    }

    public BufferedReader CreateFileReader(String filename) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(filename);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        return bufferedReader;
    }
}
