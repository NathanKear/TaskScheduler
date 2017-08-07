package se306.team7;

import java.io.IOException;

public interface IDigraphParser {

    Digraph parseDigraph(String filename) throws IOException;

}
