package GP;

import java.util.Comparator;

public class SortbyFitness implements Comparator<JavaResult> {

        // Used for sorting in descending order of
        // roll number
        public int compare(JavaResult b, JavaResult a)
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
