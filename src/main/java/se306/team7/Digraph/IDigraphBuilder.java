package se306.team7.Digraph;

import com.sun.xml.internal.bind.v2.model.core.ID;

public interface IDigraphBuilder {
    IDigraphBuilder setName(String digraphName);
    IDigraphBuilder addNode(String nodeName, int cost);
    IDigraphBuilder addLink(String origin, String destination, int cost);
    IDigraph build();
    void clear();
}
