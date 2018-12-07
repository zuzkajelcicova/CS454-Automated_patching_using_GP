package GP;

import java.util.ArrayList;
import java.util.List;


public class Patch {
    private List<Edit> allEdits;
    private double fitnessVal;

    private int ctrCrossover;
    private int ctrMutation;

    public int getCtrCrossover() {
        return ctrCrossover;
    }

    public void setCtrCrossover(int ctr_crossover) {
        this.ctrCrossover = ctr_crossover;
    }

    public int getCtrMutation() {
        return ctrMutation;
    }

    public void setCtrMutation(int ctrMutation) {
        this.ctrMutation = ctrMutation;
    }

    public Patch() {
        this.allEdits = new ArrayList<>();
        this.ctrCrossover = 0;
        this.ctrMutation = 0;
    }

    public Patch(List<Edit> ind) {
        this.allEdits = ind;
        this.ctrCrossover = 0;
        this.ctrMutation = 0;
    }

    public List<Edit> getAllEdits() {
        return allEdits;
    }

    public double getFitness() {
        return this.fitnessVal;
    }

    public StringBuilder getAllPatchesContent() {
        StringBuilder content = new StringBuilder();

        for (Edit edit : allEdits) {
            content.append(edit.getPatchContent()).append("\n");
        }
        return content;
    }

    public int patchSize() {
        return allEdits.size();
    }

    public Edit getPatch(int index) {
        return allEdits.get(index);
    }

    public void deleteEdit(Edit pts) {
        for (Edit p : allEdits) {
            if (p.getTargetNode() == pts.getTargetNode() && p.getSourceNode() == pts.getSourceNode() && p.getOperation() == pts.getOperation()) {
                allEdits.remove(p);
                break;
            }
        }
    }

    public void setFitness(double fitnessVal) {
        this.fitnessVal = fitnessVal;
    }
}
