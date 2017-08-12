package se306.team7.Digraph;

import java.util.List;

public class DigraphBuilder implements IDigraphBuilder {
    private String _digraphName;
    private List<Node> _currentNodes;
    private List<Link> _currentLinks;

    public IDigraphBuilder setName(String digraphName) {
        _digraphName = digraphName;
        return this;
    }

    public IDigraphBuilder addNode(String nodeName, int cost) {

        return this;
    }

    public IDigraphBuilder addLink(String origin, String destination, int cost) {

        return this;
    }

    public IDigraph build() {

        return null;
    }

    public void clear() {
        _digraphName = null;
        _currentLinks.clear();
        _currentNodes.clear();
    }
}
