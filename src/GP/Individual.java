package GP;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Individual {
    private List<Patch> allPatches;
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

    public void setCtrMutation(int ctr_mutataion) {
        this.ctrMutation = ctr_mutataion;
    }

    public Individual() {
        this.allPatches = new ArrayList<>();
        this.ctrCrossover = 0;
        this.ctrMutation= 0;
    }
    public Individual(List<Patch> ind) {
        this.allPatches = ind;
        this.ctrCrossover = 0;
        this.ctrMutation = 0;
    }
    public List<Patch> getAllPatches() {
        return allPatches;
    }

    void patchData(Patch pt){
        System.out.println(pt.getOperation() + "," + pt.getSourceNode()+ "," + pt.getTargetNode());
    }
    public double getFitnessVal(){
        return this.fitnessVal;
    }

    public int patchSize(){
        return allPatches.size();
    }
    public Patch getPatch(int index) {
        return allPatches.get(index);
    }
    public List<Patch> getPdata() {
        return allPatches;
    }

    public void deleteEdit(Patch pts) {
        Iterator<Patch> i = allPatches.iterator();
        while (i.hasNext()) {
            Patch p = i.next();
            if (pts.equals(p)) {
                i.remove();
            }
        }
    }

    public void setAllPatches(List<Patch> allPatches) {
        this.allPatches = allPatches;
    }

    public void setFitnessVal(double fitnessVal) {
        this.fitnessVal = fitnessVal;
    }


}
