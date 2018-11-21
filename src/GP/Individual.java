package GP;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Individual {
    private List<Patch> allPatches;
    private double fitnessVal;
    private Patch edit;


    public Individual() {
        this.allPatches = new ArrayList<>();
    }
    public Individual(List<Patch> ind) {
        this.allPatches = ind;
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

    public void addEdit(Patch patch) {
        this.edit = patch;
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

    public void deletEdit(Patch pts) {
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

    public Patch getEdit() {
        return edit;
    }

    public void setEdit(Patch edit) {
        this.edit = edit;
    }
}
