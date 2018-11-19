package GP;

import java.util.ArrayList;
import java.util.List;

public class Individual {
    private List<Patch> allPatches;
    public double fitnessVal;
    public Patch edit;


    public Individual() {
        this.allPatches = new ArrayList<>();
    }

    public List<Patch> getAllPatches() {
        return allPatches;
    }

    void patchData(Patch pt){
        System.out.println(pt.getOperation() + "," + pt.getSourceNode()+ "," + pt.getTargetNode());
    }
    public float getFitnessVal(){
        return this.fitnessVal;
    }

    public void addEdit(Patch patch) {
        this.edit = patch;
    }

    public int patchSize(){
        return pdata.size();
    }

    public List<Patch> getPdata() {
        return pdata;
    }
}
