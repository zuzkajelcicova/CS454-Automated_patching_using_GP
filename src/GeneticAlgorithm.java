import AST.ASTHandler;
import AST.NodePair;
import GP.*;
import General.Utils;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GeneticAlgorithm {

    private int populationSize;
    private int maxTimeInMinutes;
    private int maxNumberOfFitnessEval;
    private int negPass;
    private int posPass;
    private int numberOfGenerations;
    private Utils utils;
    private ASTHandler astHandler;
    private HashMap<Integer, NodePair> candidateSpace;
    private ArrayList<Integer> candidateList;
    private ArrayList<Individual> initialPopulation;
    private int solutionList;
    //private ArrayList<Individual> solutionList;
    private Date startRepairTime;
    private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    private int solutionCounter;
    private int targetNode;

    public GeneticAlgorithm(int populationSize, int maxTimeInMinutes, int maxNumberOfFitnessEval,
                            Utils utils, ASTHandler astHandler) {
        this.populationSize = populationSize;
        this.maxTimeInMinutes = maxTimeInMinutes;
        this.maxNumberOfFitnessEval = maxNumberOfFitnessEval;
        this.utils = utils;
        this.astHandler = astHandler;
        this.candidateSpace = astHandler.getCandidateSpace();
        this.candidateList = new ArrayList<>();
        this.solutionCounter = 1;
        this.numberOfGenerations = 1;
        this.solutionList = 0;// new ArrayList<>();
        this.targetNode = -1;
    }

    public ArrayList<Individual> initialize(int initialPopulationSize, ASTHandler astHandler) {
        int[] possibleOperations = new int[]{0, 1, 2};
        for (Map.Entry<Integer, NodePair> entry : candidateSpace.entrySet()) {
            candidateList.add(entry.getKey());
        }

        for (Map.Entry<Integer, NodePair> entry : astHandler.getFaultSpace().entrySet()) {
            targetNode = entry.getKey();
            break;
        }

        ArrayList<Individual> population = new ArrayList<>();
        //todo:hardcoded, delete!!!
        List<Patch> patches = new ArrayList<>();
        patches.add(new Patch(1, 156, 75));
        population.add(new Individual(patches));
        //todo

        for (int i = 0; i < initialPopulationSize; i++) {
            Individual individual = new Individual();
            int operation = getRandom(possibleOperations);
            if (operation == 0) {
                individual.getAllPatches().add(new Patch(operation, -1, targetNode));
            } else {
                int index = new Random().nextInt(candidateList.size());
                int sourceNode = candidateList.get(index);
                individual.getAllPatches().add(new Patch(operation, sourceNode, targetNode));
            }
            population.add(individual);
        }
        return population;
    }

    public int getRandom(int[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    private int javaCompile() throws Exception {
        // String command = "javac -d C:\\Users\\admin\\git\\CS454-Automated_patching_using_GP\\out\\production\\CS454_AutomatedPatching\\ src/" + utils.TARGET_CODE;
        //String command = "javac -cp src src/" + utils.TARGET_CODE;
        String command = "FIX!!!";
        Process pro = Runtime.getRuntime().exec(command);
        pro.waitFor();
        System.out.println(command + " exitValue() " + pro.exitValue());
        return pro.exitValue();
    }

    public void repairProgram() throws ParseException {
        System.out.println("Running defect fixing...");
        //todo: run the time in a separate thread
        //todo: create a JUnit test forZunu
        Date date_now_1 = new Date();
        String time1 = formatter.format(date_now_1);
        startRepairTime = formatter.parse(time1);

        GeneticOperations gp = new GeneticOperations(utils);

        //Initialize population - initialize with more to have enough to choose from if a lot does not compile
        this.initialPopulation = initialize(this.populationSize + populationSize / 2, this.astHandler);

        Individual fittestIndividual;
        List<Individual> oldPopulation = new ArrayList<>();
        List<Individual> newPopulation = new ArrayList<>();
        List<Individual> tournamentSelectionParents;
        List<Individual> offsprings;

        //Initial population
        initialPopulation = compileAndTest(initialPopulation);
        if (solutionList > 0) {
            return;
        }

        initialPopulation.sort(new SortByFitness());

        int indiv = 0;
        while (oldPopulation.size() != populationSize) {
            oldPopulation.add(initialPopulation.get(indiv));
            indiv++;
        }
        if (oldPopulation.size() != populationSize) {
            System.out.println("NOT ENOUGH INDIVIDUALS, POPULATION SMALLER!!!");
            this.populationSize = oldPopulation.size();
        }

        fittestIndividual = gp.getFittest(oldPopulation);
        populateList(oldPopulation, newPopulation);

        //CHECK TIME
        Date date_now_2 = new Date();
        String time2 = formatter.format(date_now_2);
        Date date2 = formatter.parse(time2);
        long difference = date2.getTime() - startRepairTime.getTime();

        while (difference < maxTimeInMinutes * 60 * 1000 && solutionList == 0) {
            this.numberOfGenerations++;
            //Tournament selection
            tournamentSelectionParents = gp.tournamentSelection(newPopulation);

            //Crossovers and mutations
            offsprings = gp.crossover(tournamentSelectionParents, fittestIndividual);
            offsprings = gp.mutate(offsprings, candidateList, fittestIndividual);

            offsprings = compileAndTest(offsprings);
            offsprings = selectPopulationIndividuals(oldPopulation, offsprings);

            populateList(newPopulation, oldPopulation);
            //Create new population
            populateList(tournamentSelectionParents, newPopulation);
            populateList(offsprings, newPopulation);
        }
    }

    private List<Individual> selectPopulationIndividuals(List<Individual> oldPopulation, List<Individual> offspringPopulation) {
        int threshold = populationSize / 2;
        List<Individual> result = new ArrayList<>();

        if (offspringPopulation.size() == threshold) {
            return offspringPopulation;
        } else if (offspringPopulation.size() < threshold) {
            populateList(offspringPopulation, result);
            oldPopulation.sort(new SortByFitness());

            int indexCounter = 0;
            while (result.size() != populationSize) {
                result.add(oldPopulation.get(indexCounter));
                indexCounter++;
            }
            return result;
        } else if (offspringPopulation.size() > threshold) {
            offspringPopulation.sort(new SortByFitness());

            int indexCounter = 0;
            while (result.size() != populationSize) {
                result.add(offspringPopulation.get(indexCounter));
                indexCounter++;
            }
            return result;
        }
        return null;
    }

    private ArrayList<Individual> compileAndTest(List<Individual> newPopulation) {
        ArrayList<Individual> candidateResult = new ArrayList<>();

        for (Individual individual : newPopulation) {
            astHandler.applyPatches(individual);

            StringBuilder reformattedCode = utils.removeCodeLines();
            utils.saveData(utils.SRC_DIRECTORY, utils.TARGET_CODE, reformattedCode);
            //Compilation step
            try {
                int compilationResult = javaCompile();

                //Fitness function step
                if (compilationResult == utils.PASS) {
                    Result testNegResult = JUnitCore.runClasses(GCDTestNeg.class);
                    printTestStatistics(testNegResult, "Negative");
                    negPass = utils.NUM_NEG_TEST - testNegResult.getFailureCount();

                    Result testPosResult = JUnitCore.runClasses(GCDTestPos.class);
                    printTestStatistics(testPosResult, "Positive");
                    posPass = utils.NUM_POS_TEST - testPosResult.getFailureCount();

                    double fitness = (utils.WEIGHT_NEG * negPass) + (utils.WEIGHT_POS * posPass);
                    System.out.println("Fitness Value: " + fitness);
                    individual.setFitness(fitness);
                    candidateResult.add(individual);

                    //todo: maybe break here as soon as we find a solution
                    if (testNegResult.getFailureCount() == 0 && testPosResult.getFailureCount() == 0) {
                        //Storing 2 files -> .java and .txt for statistics
                        storeCodeAndStatistics(individual, reformattedCode);
                        this.solutionList++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return candidateResult;
    }

    private void printTestStatistics(Result testResult, String str) {
        for (Failure failure : testResult.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(str + " tests");
        System.out.println("Number of Failure: " + testResult.getFailureCount());
        System.out.println("Number of Run: " + testResult.getRunCount());
        System.out.println("Run Time: " + testResult.getRunTime());
    }

    private void storeCodeAndStatistics(Individual individual, StringBuilder reformattedCode) {
        String javaFile = utils.FIXED_JAVA + solutionCounter + ".java";
        String statistics = utils.FIXED_JAVA_STATISTICS + solutionCounter + ".txt";

        StringBuilder individualStatistics = new StringBuilder();
        individualStatistics.append("Target: ").append(astHandler.getFaultSpace().get(targetNode).getNode().getTextContent()).append(utils.LINE_SEPARATOR);

        individualStatistics.append("Sources: to be added");

        individualStatistics = individual.getAllPatchesContent();
        individualStatistics.append(utils.LINE_SEPARATOR).append(utils.LINE_SEPARATOR);
        //todo: add time
        individualStatistics.append("Time: " + " will be added");
        individualStatistics.append("Generation: " + numberOfGenerations).append(utils.LINE_SEPARATOR);
        individualStatistics.append("Mutations: " + individual.getCtrMutation()).append(utils.LINE_SEPARATOR);
        individualStatistics.append("Crossovers: " + individual.getCtrCrossover()).append(utils.LINE_SEPARATOR);

        utils.saveData(utils.SOLUTION_DIRECTORY, javaFile, reformattedCode);
        utils.saveData(utils.SOLUTION_DIRECTORY, statistics, individualStatistics);
        solutionCounter++;
    }

    private void populateList(List<Individual> sourceList, List<Individual> targetList) {
        for (Individual currentIndividual : sourceList) {
            targetList.add(currentIndividual);
        }
    }
}