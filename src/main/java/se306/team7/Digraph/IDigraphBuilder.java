package se306.team7.Digraph;

public interface IDigraphBuilder {
    IDigraphBuilder setName(String digraphName);
    IDigraphBuilder addNode(String nodeName, int cost);
    IDigraphBuilder addLink(String origin, String destination, int cost);
    IDigraph build();
}
