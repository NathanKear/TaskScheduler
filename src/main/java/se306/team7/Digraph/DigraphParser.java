package se306.team7.Digraph;

import se306.team7.utility.IFileUtilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DigraphParser implements IDigraphParser {
    IFileUtilities _fileUtilities;

    /**
     * Instantiates an instance of DigraphParser
     * @param fileUtilities
     */
    public DigraphParser(IFileUtilities fileUtilities){
        _fileUtilities = fileUtilities;
    }

    /**
     * Returns Digraph, constructed using the DigraphBuilder, with the information parsed in from input file
     * @param fileName
     * @return IDigraph The constructed, immutable Digraph
     */
    public IDigraph parseDigraph(String fileName) throws IOException{
        IDigraphBuilder db = new DigraphBuilder();
        BufferedReader reader = null;

        try {
            reader = _fileUtilities.createFileReader(fileName);

            //Set name of digraph
            String name = reader.readLine();
            name = name.split("\"")[1];
            db.setName(name);

            //Read line in from input and add link/node accordingly
            String line;
            while (!(line = reader.readLine()).contains("}")) {
                db = parseLine(db, line);
            }
        }
        catch(FileNotFoundException e) { e.printStackTrace(); }
        finally{
            try { reader.close(); }
            catch (IOException e) { e.printStackTrace(); }
        }

        return db.build();
    }

    /**
     * Returns DigraphBuilder with an added Node/Link object
     * @param db
     * @param line
     * @return IDigraphBuilder Instance of DigraphBuilder with additional link/node
     */
    private IDigraphBuilder parseLine(IDigraphBuilder db, String line){
        try {
            line = line.trim();
            String[] splitLine = line.split("\\s+");
            if (splitLine.length < 3) {
                db.addNode(splitLine[0], Integer.parseInt(splitLine[1].replaceAll("[^0-9]", "")));
            } else {
                db.addLink(splitLine[0], splitLine[2], Integer.parseInt(splitLine[3].replaceAll("[^0-9]", "")));
            }
        } catch (Exception ex) {ex.printStackTrace();}
        return db;
    }
}
