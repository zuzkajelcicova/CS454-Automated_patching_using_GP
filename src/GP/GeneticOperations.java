package GP;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import General.Utils;

public class GeneticOperations {
    private Utils utils;
    private Random random;
    private List<Integer> targetNodes;
    private List<Integer> candidateList;

    public GeneticOperations(Utils utils, ArrayList<Integer> targetNodes, List<Integer> candidateList) {
        this.utils = utils;
        this.random = new Random();
        this.targetNodes = targetNodes;
        this.candidateList = candidateList;
    }

    public Patch getFittest(List<Patch> population) {
        double maxFit = Double.MIN_VALUE;
        int maxFitIndex = 0;
        try {
            if(population != null && population.size()>0){
                for (int i = 0; i < population.size(); i++) {
                    if (maxFit <= population.get(i).getFitness()) {
                        maxFit = population.get(i).getFitness();
                        maxFitIndex = i;
                    }
                }
            }

        }catch (Exception e) {
                e.printStackTrace();
            }
             return population.get(maxFitIndex);
    }

    public Patch fittestInTournament(List<Patch> pop) {
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

    public List<Patch> tournamentSelection(List<Patch> pop) {
        int tournament_size = pop.size() / 2;
        //int tournament_each = pop.size() / tournament_size;

        List<Patch> tournament;
        List<Patch> selected = new ArrayList<>();
        List<Patch> popCopy = new ArrayList<>();

        //Copy population - individuals will be removed from it as we choose a pair
        for (Patch ind : pop) {
            popCopy.add(ind);
        }

        int currentSizeAvailable = popCopy.size();

        //Always choosing pairs
        for (int i = 0; i < tournament_size; i++) {
            tournament = new ArrayList<>();
            //Get first individual
            int individualAIndex = random.nextInt(currentSizeAvailable);
            Patch patchA = popCopy.get(individualAIndex);
            popCopy.remove(patchA);
            tournament.add(patchA);
            currentSizeAvailable = popCopy.size();

            //Get second individual
            int individualBIndex = random.nextInt(currentSizeAvailable);
            Patch patchB = popCopy.get(individualBIndex);
            popCopy.remove(patchB);
            tournament.add(patchB);
            currentSizeAvailable = popCopy.size();

            //Start the tournament
            selected.add(fittestInTournament(tournament));
        }
        return selected;
    }

    // Crossover of parents
    public List<Patch> crossover(List<Patch> population) {
        List<Patch> newPopulation = new ArrayList<>();

        for (int ii = 0; ii < population.size(); ii++) {
            int p2 = random.nextInt(population.size());

            Patch parent1 = population.get(ii);
            Patch parent2 = population.get(p2);

            parent1.setCtrCrossover(parent1.getCtrCrossover() + 1);
            parent2.setCtrCrossover(parent2.getCtrCrossover() + 1);

            List<Edit> offspring1 = new ArrayList<>();
            List<Edit> offspring2 = new ArrayList<>();

            if (parent1.patchSize() == 1 && parent2.patchSize() == 1) {//Both Parent1 and Parent2 have only one patch
                offspring1.add(new Edit(parent1.getAllEdits().get(0).getOperation(), parent1.getAllEdits().get(0).getSourceNode(), parent1.getAllEdits().get(0).getTargetNode()));
                offspring1.add(new Edit(parent2.getAllEdits().get(0).getOperation(), parent2.getAllEdits().get(0).getSourceNode(), parent2.getAllEdits().get(0).getTargetNode()));

                offspring2.add(new Edit(parent2.getAllEdits().get(0).getOperation(), parent2.getAllEdits().get(0).getSourceNode(), parent2.getAllEdits().get(0).getTargetNode()));
                offspring2.add(new Edit(parent1.getAllEdits().get(0).getOperation(), parent1.getAllEdits().get(0).getSourceNode(), parent1.getAllEdits().get(0).getTargetNode()));

                newPopulation.add(new Patch(offspring1));
                newPopulation.add(new Patch(offspring2));

            } else if (parent1.patchSize() == 1 && parent2.patchSize() > 1) {//Parent1 has only one patch, Parent2 has more
                int cutoffPointParent2 = crossoverPoint(parent2);

                //Offspring1
                offspring1.add(new Edit(parent1.getAllEdits().get(0).getOperation(), parent1.getAllEdits().get(0).getSourceNode(), parent1.getAllEdits().get(0).getTargetNode()));
                for (int i = cutoffPointParent2; i < parent2.getAllEdits().size(); i++) {
                    offspring1.add(new Edit(parent2.getAllEdits().get(i).getOperation(), parent2.getAllEdits().get(i).getSourceNode(), parent2.getAllEdits().get(i).getTargetNode()));
                }

                //Offspring2
                for (int i = 0; i < cutoffPointParent2; i++) {
                    offspring2.add(new Edit(parent2.getAllEdits().get(i).getOperation(), parent2.getAllEdits().get(i).getSourceNode(), parent2.getAllEdits().get(i).getTargetNode()));
                }
                offspring2.add(new Edit(parent1.getAllEdits().get(0).getOperation(), parent1.getAllEdits().get(0).getSourceNode(), parent1.getAllEdits().get(0).getTargetNode()));

                newPopulation.add(new Patch(offspring1));
                newPopulation.add(new Patch(offspring2));

            } else if (parent1.patchSize() > 1 && parent2.patchSize() == 1) {//Parent1 has more patches, Parent2 has 1
                int cutoffPointParent1 = crossoverPoint(parent1);

                //Offspring2
                offspring2.add(new Edit(parent2.getAllEdits().get(0).getOperation(), parent2.getAllEdits().get(0).getSourceNode(), parent2.getAllEdits().get(0).getTargetNode()));
                for (int i = cutoffPointParent1; i < parent1.getAllEdits().size(); i++) {
                    offspring2.add(new Edit(parent1.getAllEdits().get(i).getOperation(), parent1.getAllEdits().get(i).getSourceNode(), parent1.getAllEdits().get(i).getTargetNode()));
                }

                //Offspring1
                for (int i = 0; i < cutoffPointParent1; i++) {
                    offspring1.add(new Edit(parent1.getAllEdits().get(i).getOperation(), parent1.getAllEdits().get(i).getSourceNode(), parent1.getAllEdits().get(i).getTargetNode()));
                }
                offspring1.add(new Edit(parent2.getAllEdits().get(0).getOperation(), parent2.getAllEdits().get(0).getSourceNode(), parent2.getAllEdits().get(0).getTargetNode()));

                newPopulation.add(new Patch(offspring1));
                newPopulation.add(new Patch(offspring2));

            } else {//Both Parent1 and Parent2 has more patches
                int cutoffPointParent1 = crossoverPoint(parent1);
                int cutoffPointParent2 = crossoverPoint(parent2);

                //Offspring1
                for (int i = 0; i < cutoffPointParent1; i++) {
                    offspring1.add(new Edit(parent1.getAllEdits().get(i).getOperation(), parent1.getAllEdits().get(i).getSourceNode(), parent1.getAllEdits().get(i).getTargetNode()));
                }
                for (int i = cutoffPointParent2; i < parent2.getAllEdits().size(); i++) {
                    offspring1.add(new Edit(parent2.getAllEdits().get(i).getOperation(), parent2.getAllEdits().get(i).getSourceNode(), parent2.getAllEdits().get(i).getTargetNode()));
                }

                //Offspring2
                for (int i = 0; i < cutoffPointParent2; i++) {
                    offspring2.add(new Edit(parent2.getAllEdits().get(i).getOperation(), parent2.getAllEdits().get(i).getSourceNode(), parent2.getAllEdits().get(i).getTargetNode()));
                }
                for (int i = cutoffPointParent1; i < parent1.getAllEdits().size(); i++) {
                    offspring2.add(new Edit(parent1.getAllEdits().get(i).getOperation(), parent1.getAllEdits().get(i).getSourceNode(), parent1.getAllEdits().get(i).getTargetNode()));
                }

                newPopulation.add(new Patch(offspring1));
                newPopulation.add(new Patch(offspring2));
            }
        }
        return newPopulation;
    }

    //Mutation operation
    public List<Patch> mutate(List<Patch> population, Patch fittestPatch) {
        Edit editToDelete;
        int mutation;
        //Apply mutation to every individual
        int mutationPop = population.size();
        if (mutationPop >= 1) {
            for (int i = 0; i < mutationPop; i++) {
                int individualIndex = random.nextInt(population.size());
                int patchIndex = random.nextInt(population.get(individualIndex).patchSize());

                mutation = population.get(individualIndex).getCtrMutation();
                mutation++;
                population.get(individualIndex).setCtrMutation(mutation);

                editToDelete = population.get(individualIndex).getPatch(patchIndex);

                int operation = random.nextInt(3);
                if (operation == utils.DELETE) {
                    // Deleting patch
                    if (population.get(individualIndex).patchSize() > 1)
                        population.get(individualIndex).deleteEdit(editToDelete);
                } else if (operation == utils.INSERT) {
                    // Inserting a completely new patch
                    population.get(individualIndex).getAllEdits().add(generateRandomPatch());
                } else if (operation == utils.REPLACE) {
                    // Replacing patch with a completely new code
                    population.get(individualIndex).getAllEdits().set(patchIndex, generateRandomPatch());
                }
            }
            population.add(fittestPatch);
            return population;
        } else {
            return population;
        }
    }

    private Edit generateRandomPatch() {
        int operation = random.nextInt(3);
        int sourceNode = candidateList.get(random.nextInt(candidateList.size()));
        int targetNode = targetNodes.get(random.nextInt(targetNodes.size()));

        return new Edit(operation, sourceNode, targetNode);
    }

    private int crossoverPoint(Patch in) {
        Random rn = new Random();
        int val1 = rn.nextInt(in.patchSize());
        return val1;
    }
}