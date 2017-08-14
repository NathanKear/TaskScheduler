package se306.team7.utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.List;

public interface IFileUtilities {
    boolean doesFileExist(String fileName);
    BufferedReader createFileReader(String filename) throws FileNotFoundException;
    void writeToFile(String fileName, String digraphName, List<String> output);
}
