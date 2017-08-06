package se306.team7;


public class DigraphParser implements IDigraphParser{
    /**
     * Returns Digraph copy with an added Node/Link object
     * @param d
     * @param line
     * @return
     */
    public Digraph parseLine(Digraph d, String line){

        String[] splitLine = line.split("\\s+");
        if(splitLine.length<3){
            d.addNode(splitLine[0], Integer.parseInt(splitLine[1].replaceAll("[^0-9]", "")));
        }else{
            d.addLink(splitLine[0], splitLine[2],Integer.parseInt(splitLine[1].replaceAll("[^0-9]", "")));
        }

        return d;
    }
}
