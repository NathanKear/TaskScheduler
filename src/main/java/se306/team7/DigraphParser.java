package se306.team7;


public class DigraphParser implements DigraphParserInterface{
    public Digraph parseLine(Digraph d, String line){
        //split string into nonspace characters and non bracket characters
        //first letter is node
        //if followed by arrow, must be a link
        //weight is after

        String[] splitLine = line.split("\\s+");
        if(splitLine.length<3){ //adding node to digraph
            d.addNode(splitLine[0], Integer.parseInt(splitLine[1].replaceAll("[^0-9]", "")));
        }else{ //adding link to node in digraph
            d.addLink(splitLine[0], splitLine[2],Integer.parseInt(splitLine[1].replaceAll("[^0-9]", "")));
        }
        for(int i = 0; i< splitLine.length;i++){
            splitLine[i]=splitLine[i].replaceAll("[^0-9]", "");
            System.out.println(splitLine[i]);
        }
        //have to determine if its a node or link
        //once thats done, adjust digraph to add node/link
        return null;
    }

    public static void main(String[] args){
        Digraph d = new Digraph();
        String line = new String("a -> b [Weight=2];");
        DigraphParser egg = new DigraphParser();
        egg.parseLine(d, line);
    }
}
