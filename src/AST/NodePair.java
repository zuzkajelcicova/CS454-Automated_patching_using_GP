package AST;

import org.w3c.dom.Node;

public class NodePair {
    private int lineNumber;
    private Node node;

    public NodePair(int lineNumber, Node node) {
        this.lineNumber = lineNumber;
        this.node = node;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public Node getNode() {
        return node;
    }
}
