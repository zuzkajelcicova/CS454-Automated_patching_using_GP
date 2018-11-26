package GP;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import General.Utils;

public class GeneticOperations {
    private Utils utils ;
    public GeneticOperations(Utils utils) {
        this.utils = utils;
    }
    //Select fittest individual which will be maintained
    public Individual getFittest(List<Individual> pop) {
        double maxFit = Double.MIN_VALUE;
        int maxFitIndex = 0;
        for (int i = 0; i < pop.size(); i++) {
            if (maxFit <= pop.get(i).getFitness()) {
                maxFit = pop.get(i).getFitness();
                maxFitIndex = i;
            }
        }
        return pop.get(maxFitIndex);
    }

    //Tournament selection
    public Individual fittestInTournament(List<Individual> pop) {
        //Select the most fittest individual
        int index = 0;
        double min = Double.MIN_VALUE;
        for (int i = 0; i < pop.size(); i++) {
            if (pop.get(i).getFitness() >= min) {
                index = i;
                min = pop.get(i).getFitness();
            }
        }
        return pop.get(index);
    }

    public List<Individual> tournamentSelection(List<Individual> pop) {
        // Arbitrarily selected tournament size, i think we need to be systematic.
        // If the tournament size is larger, weak individuals have a smaller chance to be selected, because,
        // if a weak individual is selected to be in a tournament,
        // there is a higher probability that a stronger individual is also in that tournament.
        int tournament_size = pop.size() / 2;
        int tournament_each = pop.size() / tournament_size;
        List<Individual> tourament;
        List<Individual> selected = new ArrayList<Individual>();
        for (int i = 0; i < tournament_size; i++) {
            tourament = new ArrayList<Individual>();
            for (int j = 0; j < tournament_each; j++) {
                Random rn = new Random();
                int Individual = rn.nextInt(pop.size());
                tourament.add(j, pop.get(Individual));
            }
            selected.add(fittestInTournament(tourament));
        }
        return selected;
    }

    // Crossover of parents
    public List<Individual> crossover(List<Individual> pop, Individual fittestInd) {
        List<Individual> newPop = new ArrayList<Individual>();
        List<Individual> newGen = new ArrayList<Individual>();

        for (int ii = 0; ii < pop.size(); ii++) {
            Random rn = new Random();
            int p2 = rn.nextInt(pop.size());

            Individual parent1 = pop.get(ii);
            Individual parent2 = pop.get(p2);
            int ctrC1 = parent1.getCtrCrossover();
            ctrC1++;
            parent1.setCtrCrossover(ctrC1);
            int ctrC2 = parent2.getCtrCrossover();
            ctrC2++;
            parent1.setCtrMutation(ctrC2);
            List<Patch> offspring1 = new ArrayList<Patch>();
            List<Patch> offspring2 = new ArrayList<Patch>();
            int cutpoint = crossoverPoint(parent1);
            if (parent1.patchSize() == 1 && parent2.patchSize() == 1) {
                offspring1.add(parent1.getAllPatches().get(0));
                offspring2.add(parent2.getAllPatches().get(0));
                offspring1.add(parent2.getAllPatches().get(0));
                offspring2.add(parent1.getAllPatches().get(0));
                newPop.add(new Individual(offspring1));
                newPop.add(new Individual(offspring2));
                newGen.addAll(newPop);
            }
            if (parent1.patchSize() > 1 && parent2.patchSize() > 1) {
                for (int i = 0; i < cutpoint; i++) {
                    offspring1.add(parent1.getAllPatches().get(i));
                    offspring2.add(parent2.getAllPatches().get(i));
                }
                for (int k = cutpoint; k < parent1.patchSize(); k++) {
                    if (k < parent2.patchSize()) {
                        offspring1.add(parent2.getAllPatches().get(k));
                    }
                    offspring2.add(parent1.getAllPatches().get(k));
                }
                newPop.add(new Individual(offspring1));
                newPop.add(new Individual(offspring2));
                newGen.addAll(newPop);
            }
            if (parent1.patchSize() > 1) {
                for (int i = 0; i < cutpoint; i++) {
                    offspring1.add(parent1.getAllPatches().get(i));
                    offspring2.add(parent2.getAllPatches().get(0));
                }
                for (int j = cutpoint; j < parent1.patchSize(); j++) {
                    offspring2.add(parent1.getAllPatches().get(j));
                    offspring1.add(parent2.getAllPatches().get(0));
                }
                newPop.add(new Individual(offspring1));
                newPop.add(new Individual(offspring2));
                newGen.addAll(newPop);
            }
            if (parent2.patchSize() > 1) {
                cutpoint = crossoverPoint(parent2);
                for (int i = 0; i < cutpoint; i++) {
                    offspring1.add(parent2.getAllPatches().get(i));
                    offspring2.add(parent1.getAllPatches().get(0));
                }
                for (int j = cutpoint; j < parent1.patchSize(); j++) {
                    offspring2.add(parent1.getAllPatches().get(0));
                    offspring1.add(parent2.getAllPatches().get(j));
                }
                newPop.add(new Individual(offspring1));
                newPop.add(new Individual(offspring2));
                newGen.addAll(newPop);
            }
        }
        newPop.add(fittestInd);
        return newGen;
    }

    //Mutation operation
    public List<Individual> mutate(List<Individual> pop, List<Integer> sourceList, Individual fittestInd) {
        Patch pts;
        int mut = 0;
        Random rn = new Random();
        int mutationPop = pop.size() / 3;
        if (mutationPop >= 1) {
            for (int i = 0; i < mutationPop; i++) {
                // select source edit source node
                int sn = rn.nextInt(sourceList.size());
                // Select target edit (randomly)
                int patch_index = rn.nextInt(pop.size());
                int edit_index;
                if(pop.get(patch_index).patchSize()> 0)
                    edit_index = rn.nextInt(pop.get(patch_index).patchSize());
                else
                    edit_index = 0;
                mut = pop.get(patch_index).getCtrMutation();
                mut++;
                pop.get(patch_index).setCtrMutation(mut);
                pts = pop.get(patch_index).getPatch(edit_index);
                //choose random operation 0 to delete, 1 to insert, 2 to replace
                int op = rn.nextInt(3);
                if (op == utils.DELETE) {
                    // Deleting edit
                    if (pop.get(patch_index).patchSize() > 1)
                        pop.get(edit_index).deleteEdit(pts);
                }
                if (op == utils.INSERT) {
                    // Inserting new edit
                    pop.get(patch_index).getPatch(edit_index).setSourceNode(sourceList.get(sn));
                }
                if (op == utils.REPLACE) {
                    // Replacing edit (changing source node)
                    pop.get(patch_index).getAllPatches().set(edit_index, pts);
                }
            }
            pop.add(fittestInd);
            return pop;
        } else {
            return pop;
        }
    }

    int crossoverPoint(Individual in) {
        Random rn = new Random();
        int val1 = rn.nextInt(in.patchSize());
        return val1;
    }

}

