package GP;

import General.Utils;

public class Patch {
    private int operation;
    private int sourceNode;
    private int targetNode;

    public Patch(int operation, int sNode, int tNode) {
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
    public String printPatch(){
        String string = "";
        Utils utils = new Utils();
        if (this.operation == 0){
           string = string + "Operation: DELETE" + utils.LINE_SEPARATOR;
        }
        else if (this.operation == 1){
            string = string + "Operation: REPLACE" + utils.LINE_SEPARATOR;
        }
        else if (this.operation == 2){
            string = string + "Operation: INSERT" + utils.LINE_SEPARATOR;
        }
        string = string + "Source Node: " + this.sourceNode + utils.LINE_SEPARATOR;
        string = string + "Target Node: " + this.targetNode + utils.LINE_SEPARATOR;
        return string;
//        System.out.println("Source Node: " + this.sourceNode);
//        System.out.println("Target Node: " + this.targetNode);
    }
}
