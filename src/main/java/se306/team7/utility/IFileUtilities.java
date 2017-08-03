package se306.team7.utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;

public interface IFileUtilities {
    boolean DoesFileExist(String fileName);
    BufferedReader CreateFileReader(String filename) throws FileNotFoundException;
}
