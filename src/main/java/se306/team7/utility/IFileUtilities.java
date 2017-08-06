package se306.team7.utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;

public interface IFileUtilities {
    boolean doesFileExist(String fileName);
    BufferedReader createFileReader(String filename) throws FileNotFoundException;
}
