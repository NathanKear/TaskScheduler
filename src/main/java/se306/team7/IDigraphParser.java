package se306.team7;

public static interface IDigraphParser {

    static Digraph parseDigraph(String filename);

    static Digraph parseLine(Digraph d, String line);

}
