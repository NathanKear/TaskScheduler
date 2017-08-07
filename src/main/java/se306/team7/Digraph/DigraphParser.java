package se306.team7.Digraph;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import se306.team7.utility.IFileUtilities;

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
     * Returns newly constructed Digraph from input file
     * @param fileName
     * @return
     */
    public Digraph parseDigraph(String fileName) throws IOException{
        Digraph d = null;
        BufferedReader reader = null;


        try {

            reader = _fileUtilities.createFileReader(fileName);

            String name = reader.readLine();
            name = name.split("\"")[1];
            d = new Digraph(name);

            String line;
            while ((line = reader.readLine()) != "}") {
                d = parseLine(d, line);
            }

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }finally{
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return d;
    }

    /**
     * Returns Digraph copy with an added Node/Link object
     * @param d
     * @param line
     * @return
     */
    private Digraph parseLine(Digraph d, String line){

        String[] splitLine = line.split("\\s+");
        if(splitLine.length<3){
            d.addNode(splitLine[0], Integer.parseInt(splitLine[1].replaceAll("[^0-9]", "")));
        }else{
            d.addLink(splitLine[0], splitLine[2],Integer.parseInt(splitLine[1].replaceAll("[^0-9]", "")));
        }

        return d;
    }
}
