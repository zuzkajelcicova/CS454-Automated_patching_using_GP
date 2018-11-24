package GP;

import java.util.Comparator;

public class SortByFitness implements Comparator<Individual> {

    public int compare(Individual b, Individual a) {
        if (a.getFitness() - b.getFitness() > 0) {
            return 1;
        } else if (a.getFitness() - b.getFitness() < 0) {
            return -1;
        }
        return 0;
    }
}
