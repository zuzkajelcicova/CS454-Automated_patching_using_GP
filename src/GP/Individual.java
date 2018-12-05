package GP;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Individual {
    private List<Patch> allPatches;
    private double fitnessVal;

    private int ctrCrossover;
    private int ctrMutation;

    public Individual(List<Patch> p, double f){
        allPatches.addAll(p);
        fitnessVal = f;
    }
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

    public Individual() {
        this.allPatches = new ArrayList<>();
        this.ctrCrossover = 0;
        this.ctrMutation = 0;
    }

    public Individual(List<Patch> ind) {
        this.allPatches = ind;
        this.ctrCrossover = 0;
        this.ctrMutation = 0;
    }

    public List<Patch> getAllPatches() {
        return allPatches;
    }

    public double getFitness() {
        return this.fitnessVal;
    }

    public StringBuilder getAllPatchesContent() {
        StringBuilder content = new StringBuilder();

        for (Patch patch : allPatches) {
            content.append(patch.getPatchContent()).append("\n");
        }
        return content;
    }

    public int patchSize() {
        return allPatches.size();
    }

    public Patch getPatch(int index) {
        return allPatches.get(index);
    }

    public void deleteEdit(Patch pts) {
        for (Patch p : allPatches) {
            if (p.getTargetNode() == pts.getTargetNode() && p.getSourceNode() == pts.getSourceNode() && p.getOperation() == pts.getOperation()) {
                allPatches.remove(p);
                break;
            }
        }
    }

    public void setFitness(double fitnessVal) {
        this.fitnessVal = fitnessVal;
    }
}
