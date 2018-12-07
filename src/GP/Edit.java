package GP;

public class Edit {
    private int operation;
    private int sourceNode;
    private int targetNode;

    public Edit(int operation, int sNode, int tNode) {
        this.operation = operation;
        this.sourceNode = sNode;
        this.targetNode = tNode;
    }

    public int getOperation() {
        return operation;
    }

    public int getSourceNode() {
        return sourceNode;
    }

    public int getTargetNode() {
        return targetNode;
    }

    public void setSourceNode(int sourceNode) {
        this.sourceNode = sourceNode;
    }

    public String getPatchContent() {
        return "Operation: " + this.operation + ", Source: " + this.sourceNode + ", Target: " + this.targetNode;
    }
}

