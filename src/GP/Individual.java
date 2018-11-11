package GP;

import java.util.ArrayList;
import java.util.List;

public class Individual {
    private List<Patch> allPatches;

    public Individual() {
        this.allPatches = new ArrayList<>();
    }

    public List<Patch> getAllPatches() {
        return allPatches;
    }
}
