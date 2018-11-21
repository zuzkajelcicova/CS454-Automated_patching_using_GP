package GP;

import java.util.Comparator;

public class SortbyFitness implements Comparator<JavaResult> {

        // Used for sorting in ascending order of
        // roll number
        public int compare(JavaResult a, JavaResult b)
        {
            if(a.getFitness() - b.getFitness()>0){
                return 1;
            }
            else if(a.getFitness() - b.getFitness()<0){
                return -1;
            }
            return 0;
        }

}
