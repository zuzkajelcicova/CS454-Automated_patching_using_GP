package GP;

import GenP.Data;
import GenP.Patch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticOperations {
    //Selection
    public Data selection(List<Data> pop, float fv) {
        //Select the most fittest individual
        int index = 0;
        for (int i = 0; i < pop.size(); i++) {
            if (pop.get(i).getFitnessVal() >= fv)
                index = i;
        }
        return pop.get(index);
    }

    public Data fittestInTournament(List<Data> pop) {
        //Select the most fittest individual
        int index = 0;
        float min = Float.MIN_VALUE;
        for (int i = 0; i < pop.size(); i++) {
            if (pop.get(i).getFitnessVal() >= min) {
                index = i;
                min = pop.get(i).getFitnessVal();
            }
        }
        return pop.get(index);
    }

    public float getFittesFitnessValue(List<Data> pop, float fv) {
        for (int i = 0; i < pop.size(); i++) {
            if (pop.get(i).getFitnessVal() >= fv) {
                fv = pop.get(i).getFitnessVal();
            }
        }
        return fv;
    }

    //Get index of least fittest individual
    public int getLeastFittestIndex(List<Data> pop) {
        float minfit_val = Integer.MAX_VALUE;
        int minfit_index = 0;
        for (int i = 0; i < pop.size(); i++) {
            if (minfit_val >= pop.get(i).pfitness) {
                minfit_val = pop.get(i).pfitness;
                minfit_index = i;
            }
        }
        return minfit_index;
    }

    public List<Data> tournamentSelection(List<Data> pop) {
        int tournament_size = 7;
        int tournament_each = pop.size() / tournament_size;
        List<Data> tourament;
        List<Data> selected = new ArrayList<Data>();
        for (int i = 0; i < tournament_size; i++) {
            tourament = new ArrayList<Data>();
            for (int j = 0; j < tournament_each; j++) {
                Random rn = new Random();
                int data = rn.nextInt(pop.size() - 1) + 1;
                tourament.add(j, pop.get(data));
            }
            selected.add(fittestInTournament(tourament));
        }
        return selected;
    }

    // Crossover of parents
    public List<Data> crossover(Data in1, List<Data> pop) {
        List<Data> newPop = new ArrayList<Data>();
        List<Data> newGen = new ArrayList<Data>();

        for (int ii = 0; ii < pop.size(); ii++) {
            Random rn = new Random();
            int p2 = rn.nextInt(pop.size() - 1) + 1;

            Data parent1 = pop.get(ii);
            Data parent2 = pop.get(p2);

//            for (int j = 0; j < pop.size(); j++) {

            List<Patch> offspring1 = new ArrayList<Patch>();
            List<Patch> offspring2 = new ArrayList<Patch>();
            List<Patch> offspring11 = new ArrayList<Patch>();
            List<Patch> offspring22 = new ArrayList<Patch>();
            List<Patch> offspring12 = new ArrayList<Patch>();
            List<Patch> offspring21 = new ArrayList<Patch>();
            List<Patch> offspring01 = new ArrayList<Patch>();
            List<Patch> offspring02 = new ArrayList<Patch>();

            int cutpoint = crossoverPoint(parent1);

            for (int i = 0; i < cutpoint; i++) {
                offspring1.add(parent1.pdata.get(i));
                offspring2.add(parent2.pdata.get(i));
                offspring01.add(parent1.pdata.get(i));
                offspring02.add(parent2.pdata.get(i));

            }

            for (int k = cutpoint; k < parent1.patchSize(); k++) {
                if (k < parent2.patchSize()) {
                    offspring1.add(parent2.pdata.get(k));
                    offspring22.add(parent2.pdata.get(k));
                    offspring12.add(parent2.pdata.get(k));
                    offspring02.add(parent2.pdata.get(k));
                }

                offspring2.add(parent1.pdata.get(k));
                offspring11.add(parent1.pdata.get(k));


                offspring21.add(parent1.pdata.get(k));

                offspring01.add(parent1.pdata.get(k));


            }
            for (int l = 0; l < cutpoint; l++) {
                offspring01.add(parent2.pdata.get(l));
                offspring02.add(parent1.pdata.get(l));

                offspring11.add(parent2.pdata.get(l));
                offspring22.add(parent1.pdata.get(l));

                offspring12.add(parent1.pdata.get(l));
                offspring21.add(parent2.pdata.get(l));
            }
            int rnfitval = rn.nextInt(10) + 1;

            newPop.add(new Data(offspring1, rnfitval));
            rnfitval = rn.nextInt(10) + 1;
            newPop.add(new Data(offspring2, rnfitval));
            rnfitval = rn.nextInt(10) + 1;
            newPop.add(new Data(offspring11, rnfitval));
            rnfitval = rn.nextInt(10) + 1;
            newPop.add(new Data(offspring22, rnfitval));
            rnfitval = rn.nextInt(10) + 1;
            newPop.add(new Data(offspring12, rnfitval));
            rnfitval = rn.nextInt(10) + 1;
            newPop.add(new Data(offspring21, rnfitval));
            rnfitval = rn.nextInt(10) + 1;
            newPop.add(new Data(offspring01, rnfitval));
            rnfitval = rn.nextInt(10) + 1;
            newPop.add(new Data(offspring02, rnfitval));
//            }

//            newGen = new ArrayList<Data>(newPop);
            newGen.addAll(newPop);

        }
        newGen.add(in1);
        return newGen;
    }

    //Mutation operation
    public Data mutate(List<Data> pop) {
        List<Integer> source_list;


        Data mutated;
        Patch pts = new Patch();
        Random rn = new Random();
        int index = rn.nextInt(pop.get(1).patchSize() - 1) + 1;

        int x, y, z;
        x = rn.nextInt(10) + 1;
        y = rn.nextInt(10) + 1;
        z = rn.nextInt(10) + 1;
        pts.addEdit(x, y, z);
        // Adding new patch
        pop.get(1).addEdit(pts);
        mutated = pop.get(1);
        return mutated;

    }


    //Get the fittest patch
    public Data getFittest(List<Data> pop) {
        float maxFit = Integer.MIN_VALUE;
        int maxFitIndex = 0;
        for (int i = 0; i < pop.size(); i++) {
            if (maxFit <= pop.get(i).pfitness) {
                maxFit = pop.get(i).pfitness;
                maxFitIndex = i;
            }
        }
        return pop.get(maxFitIndex);
    }

    int crossoverPoint(Data in) {
        Random rn = new Random();
        int val1 = rn.nextInt(in.patchSize() - 1);
        return val1;
    }

}
