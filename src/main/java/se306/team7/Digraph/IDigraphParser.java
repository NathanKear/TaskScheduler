package se306.team7.Digraph;

import java.io.IOException;

public interface IDigraphParser {
    IDigraph parseDigraph(String filename) throws IOException;
}
