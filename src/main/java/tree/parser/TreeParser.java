package tree.parser;

import tree.Node;

import java.util.List;

public interface TreeParser {
    public List<Node> parse(String text) throws IllegalArgumentException;
}
