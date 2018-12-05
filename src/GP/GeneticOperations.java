package GP;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import General.Utils;

public class GeneticOperations {
    private Utils utils;
    private Random random;
    private int targetNodeNumber;
    private List<Integer> candidateList;

    public GeneticOperations(Utils utils, int targetNodeNumber, List<Integer> candidateList) {
        this.utils = utils;
        this.random = new Random();
        this.targetNodeNumber = targetNodeNumber;
        this.candidateList = candidateList;
    }

    public Individual getFittest(List<Individual> population) {
        double maxFit = Double.MIN_VALUE;
        int maxFitIndex = 0;
        for (int i = 0; i < population.size(); i++) {
            if (maxFit <= population.get(i).getFitness()) {
                maxFit = population.get(i).getFitness();
                maxFitIndex = i;
            }
        }
        return population.get(maxFitIndex);
    }


    public List<Individual> tournamentSelection(List<Individual> pop) {
        int tournament_size = pop.size() / 2;
        //int tournament_each = pop.size() / tournament_size;

        List<Individual> tournament;
        List<Individual> selected = new ArrayList<>();
        List<Individual> popCopy = new ArrayList<>();

        //Copy population - individuals will be removed from it as we choose a pair
        for (Individual ind : pop) {
            popCopy.add(ind);
        }

        int currentSizeAvailable = popCopy.size();

        //Always choosing pairs
        for (int i = 0; i < tournament_size; i++) {
            tournament = new ArrayList<>();
            //Get first individual
            int individualAIndex = random.nextInt(currentSizeAvailable);
            Individual individualA = popCopy.get(individualAIndex);
            popCopy.remove(individualA);
            tournament.add(individualA);
            currentSizeAvailable = popCopy.size();

            //Get second individual
            int individualBIndex = random.nextInt(currentSizeAvailable);
            Individual individualB = popCopy.get(individualBIndex);
            popCopy.remove(individualB);
            tournament.add(individualB);
            currentSizeAvailable = popCopy.size();

            //Start the tournament
            selected.add(getFittest(tournament));
        }
        return selected;
    }

    // Crossover of parents
    public List<Individual> crossover(List<Individual> population) {
        List<Individual> newPopulation = new ArrayList<>();

        for (int ii = 0; ii < population.size(); ii++) {
            int p2 = random.nextInt(population.size());

            Individual parent1 = population.get(ii);
            Individual parent2 = population.get(p2);

            parent1.setCtrCrossover(parent1.getCtrCrossover() + 1);
            parent2.setCtrCrossover(parent2.getCtrCrossover() + 1);

            List<Patch> offspring1 = new ArrayList<>();
            List<Patch> offspring2 = new ArrayList<>();

            if (parent1.patchSize() == 1 && parent2.patchSize() == 1) {//Both Parent1 and Parent2 have only one patch
                offspring1.add(new Patch(parent1.getAllPatches().get(0).getOperation(), parent1.getAllPatches().get(0).getSourceNode(), parent1.getAllPatches().get(0).getTargetNode()));
                offspring1.add(new Patch(parent2.getAllPatches().get(0).getOperation(), parent2.getAllPatches().get(0).getSourceNode(), parent2.getAllPatches().get(0).getTargetNode()));

                offspring2.add(new Patch(parent2.getAllPatches().get(0).getOperation(), parent2.getAllPatches().get(0).getSourceNode(), parent2.getAllPatches().get(0).getTargetNode()));
                offspring2.add(new Patch(parent1.getAllPatches().get(0).getOperation(), parent1.getAllPatches().get(0).getSourceNode(), parent1.getAllPatches().get(0).getTargetNode()));

                newPopulation.add(new Individual(offspring1));
                newPopulation.add(new Individual(offspring2));

            } else if (parent1.patchSize() == 1 && parent2.patchSize() > 1) {//Parent1 has only one patch, Parent2 has more
                int cutoffPointParent2 = crossoverPoint(parent2);

                //Offspring1
                offspring1.add(new Patch(parent1.getAllPatches().get(0).getOperation(), parent1.getAllPatches().get(0).getSourceNode(), parent1.getAllPatches().get(0).getTargetNode()));
                for (int i = cutoffPointParent2; i < parent2.getAllPatches().size(); i++) {
                    offspring1.add(new Patch(parent2.getAllPatches().get(i).getOperation(), parent2.getAllPatches().get(i).getSourceNode(), parent2.getAllPatches().get(i).getTargetNode()));
                }

                //Offspring2
                for (int i = 0; i < cutoffPointParent2; i++) {
                    offspring2.add(new Patch(parent2.getAllPatches().get(i).getOperation(), parent2.getAllPatches().get(i).getSourceNode(), parent2.getAllPatches().get(i).getTargetNode()));
                }
                offspring2.add(new Patch(parent1.getAllPatches().get(0).getOperation(), parent1.getAllPatches().get(0).getSourceNode(), parent1.getAllPatches().get(0).getTargetNode()));

                newPopulation.add(new Individual(offspring1));
                newPopulation.add(new Individual(offspring2));

            } else if (parent1.patchSize() > 1 && parent2.patchSize() == 1) {//Parent1 has more patches, Parent2 has 1
                int cutoffPointParent1 = crossoverPoint(parent1);

                //Offspring2
                offspring2.add(new Patch(parent2.getAllPatches().get(0).getOperation(), parent2.getAllPatches().get(0).getSourceNode(), parent2.getAllPatches().get(0).getTargetNode()));
                for (int i = cutoffPointParent1; i < parent1.getAllPatches().size(); i++) {
                    offspring2.add(new Patch(parent1.getAllPatches().get(i).getOperation(), parent1.getAllPatches().get(i).getSourceNode(), parent1.getAllPatches().get(i).getTargetNode()));
                }

                //Offspring1
                for (int i = 0; i < cutoffPointParent1; i++) {
                    offspring1.add(new Patch(parent1.getAllPatches().get(i).getOperation(), parent1.getAllPatches().get(i).getSourceNode(), parent1.getAllPatches().get(i).getTargetNode()));
                }
                offspring1.add(new Patch(parent2.getAllPatches().get(0).getOperation(), parent2.getAllPatches().get(0).getSourceNode(), parent2.getAllPatches().get(0).getTargetNode()));

                newPopulation.add(new Individual(offspring1));
                newPopulation.add(new Individual(offspring2));

            } else {//Both Parent1 and Parent2 has more patches
                int cutoffPointParent1 = crossoverPoint(parent1);
                int cutoffPointParent2 = crossoverPoint(parent2);

                //Offspring1
                for (int i = 0; i < cutoffPointParent1; i++) {
                    offspring1.add(new Patch(parent1.getAllPatches().get(i).getOperation(), parent1.getAllPatches().get(i).getSourceNode(), parent1.getAllPatches().get(i).getTargetNode()));
                }
                for (int i = cutoffPointParent2; i < parent2.getAllPatches().size(); i++) {
                    offspring1.add(new Patch(parent2.getAllPatches().get(i).getOperation(), parent2.getAllPatches().get(i).getSourceNode(), parent2.getAllPatches().get(i).getTargetNode()));
                }

                //Offspring2
                for (int i = 0; i < cutoffPointParent2; i++) {
                    offspring2.add(new Patch(parent2.getAllPatches().get(i).getOperation(), parent2.getAllPatches().get(i).getSourceNode(), parent2.getAllPatches().get(i).getTargetNode()));
                }
                for (int i = cutoffPointParent1; i < parent1.getAllPatches().size(); i++) {
                    offspring2.add(new Patch(parent1.getAllPatches().get(i).getOperation(), parent1.getAllPatches().get(i).getSourceNode(), parent1.getAllPatches().get(i).getTargetNode()));
                }

                newPopulation.add(new Individual(offspring1));
                newPopulation.add(new Individual(offspring2));
            }
        }
        return newPopulation;
    }

    //Mutation operation
    public List<Individual> mutate(List<Individual> population, Individual fittestIndividual) {
        Patch patchToDelete;
        int mutation;
        //Apply mutation to every individual
        int mutationPop = population.size();
        if (mutationPop >= 1) {
            for (int i = 0; i < mutationPop; i++) {
                // select source edit source node
                //int sourceNode = random.nextInt(candidateList.size());
                // Select target edit (randomly)
                int individualIndex = random.nextInt(population.size());
                int patchIndex = random.nextInt(population.get(individualIndex).patchSize());

                mutation = population.get(individualIndex).getCtrMutation();
                mutation++;
                population.get(individualIndex).setCtrMutation(mutation);

                patchToDelete = population.get(individualIndex).getPatch(patchIndex);

                int operation = random.nextInt(3);
                if (operation == utils.DELETE) {
                    // Deleting patch
                    if (population.get(individualIndex).patchSize() > 1)
                        population.get(individualIndex).deleteEdit(patchToDelete);
                }
                else if (operation == utils.INSERT) {
                    // Inserting a completely new patch
                    //population.get(individualIndex).getPatch(patchIndex).setSourceNode(candidateList.get(sourceNode));
                    population.get(individualIndex).getAllPatches().add(generateRandomPatch());
                }
                else if (operation == utils.REPLACE) {
                    // Replacing patch with a completely new code
                    population.get(individualIndex).getAllPatches().set(patchIndex, generateRandomPatch());
                }
            }
            population.add(fittestIndividual);
            return population;
        } else {
            return population;
        }
    }

    private Patch generateRandomPatch() {
        int operation = random.nextInt(3);
        int sourceNodeIndex = random.nextInt(candidateList.size());

        int sourceNode = candidateList.get(sourceNodeIndex);
        return new Patch(operation, sourceNode, targetNodeNumber);
    }

    private int crossoverPoint(Individual in) {
        Random rn = new Random();
        int val1 = rn.nextInt(in.patchSize());
        return val1;
    }
}