import AST.ASTHandler;
import AST.NodePair;
import GP.*;
import General.Utils;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.File;
import java.util.*;

public class GeneticAlgorithm {

    private int populationSize;
    private int negPass;
    private int posPass;
    private int numberOfGenerations;
    private Utils utils;
    private ASTHandler astHandler;
    private HashMap<Integer, NodePair> candidateSpace;
    private ArrayList<Integer> candidateList;
    private ArrayList<Individual> initialPopulation;
    private int solutionList;
    private int solutionCounter;
    private int targetNode;
    private long startTime;
    private int posTestsNumber;
    private int negTestNumber;

    public GeneticAlgorithm(int populationSize, Utils utils, ASTHandler astHandler, long startTime) {
        this.populationSize = populationSize;
        this.utils = utils;
        this.astHandler = astHandler;
        this.candidateSpace = astHandler.getCandidateSpace();
        this.candidateList = new ArrayList<>();
        this.solutionCounter = 1;
        this.numberOfGenerations = 1;
        this.solutionList = 0;

        for (Map.Entry<Integer, NodePair> entry : astHandler.getFaultSpace().entrySet()) {
            targetNode = entry.getKey();
            break;
        }

        this.startTime = startTime;
    }

    public ArrayList<Individual> initialize(int initialPopulationSize, ASTHandler astHandler) {
        int[] possibleOperations = new int[]{0, 1, 2};
        for (Map.Entry<Integer, NodePair> entry : candidateSpace.entrySet()) {
            candidateList.add(entry.getKey());
        }

        ArrayList<Individual> population = new ArrayList<>();

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
        File file = new File(utils.DOT_CLASS_FOLDER_PATH, utils.TARGET_CLASS);
        if (file.delete()) {
            System.out.println("File " + utils.TARGET_CLASS + " deleted.");
        } else System.out.println("File " + utils.TARGET_CLASS + " does not exist.");

        String command = "javac -d " + utils.DOT_CLASS_FOLDER_PATH + " src/" + utils.TARGET_CODE;
        Process pro = Runtime.getRuntime().exec(command);
        pro.waitFor();
        System.out.println(command + " exitValue() " + pro.exitValue());
        return pro.exitValue();
    }

    public void repairProgram() {
        System.out.println("Running defect fixing...");

        GeneticOperations gp = new GeneticOperations(utils, targetNode, candidateList);

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
        newPopulation = populateList(oldPopulation);

        while (solutionList == 0) {
            this.numberOfGenerations++;
            //Tournament selection
            tournamentSelectionParents = gp.tournamentSelection(newPopulation);

            //Crossovers and mutations
            offsprings = gp.crossover(tournamentSelectionParents);
            offsprings = gp.mutate(offsprings, fittestIndividual);

            offsprings = compileAndTest(offsprings);
            offsprings = selectPopulationIndividuals(oldPopulation, offsprings);

            oldPopulation = populateList(newPopulation);
            //Create new population
            newPopulation = populateList(tournamentSelectionParents);

            for (Individual individual : populateList(offsprings)) {
                newPopulation.add(individual);
            }
        }
    }

    private List<Individual> selectPopulationIndividuals(List<Individual> oldPopulation, List<Individual> offspringPopulation) {
        int threshold = populationSize / 2;
        List<Individual> result = new ArrayList<>();

        if (offspringPopulation.size() == threshold) {
            return offspringPopulation;
        } else if (offspringPopulation.size() < threshold) {
            result = populateList(offspringPopulation);
            oldPopulation.sort(new SortByFitness());

            int indexCounter = 0;
            while (result.size() != threshold) {
                result.add(oldPopulation.get(indexCounter));
                indexCounter++;
            }
            return result;
        } else if (offspringPopulation.size() > threshold) {
            offspringPopulation.sort(new SortByFitness());

            int indexCounter = 0;
            while (result.size() != threshold) {
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

            //StringBuilder reformattedCode = utils.removeCodeLines();
            StringBuilder codeWithLines = utils.getCodeWithLines();
            utils.saveData(utils.SRC_DIRECTORY, utils.TARGET_CODE, codeWithLines);

            //Compilation step
            try {
                int compilationResult = javaCompile();

                //Fitness function step
                if (compilationResult == utils.PASS) {

                    Result testNegResult = JUnitCore.runClasses(getCorrespondingTests(utils.TARGET_CODE, utils.negative));
                    printTestStatistics(testNegResult, utils.negative);
                    negPass = negTestNumber - testNegResult.getFailureCount();

                    Result testPosResult = JUnitCore.runClasses(getCorrespondingTests(utils.TARGET_CODE, utils.positive));
                    printTestStatistics(testPosResult, utils.positive);
                    posPass = posTestsNumber - testPosResult.getFailureCount();

                    double fitness = (utils.WEIGHT_NEG * negPass) + (utils.WEIGHT_POS * posPass);
                    System.out.println("Fitness Value: " + fitness);
                    individual.setFitness(fitness);
                    candidateResult.add(individual);

                    //We do not finish after finding the very first solution - full generation is finished
                    //to see how many potential patches are available
                    if (testNegResult.getFailureCount() == 0 && testPosResult.getFailureCount() == 0) {
                        //Storing 2 files -> .java and .txt for statistics, Time in seconds
                        long requiredPatchTime = (System.currentTimeMillis() - startTime) / 1000;
                        storeCodeAndStatistics(individual, codeWithLines, requiredPatchTime);
                        this.solutionList++;
                    }
                } else {
                    individual.setFitness(0);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return candidateResult;
    }

    private Class getCorrespondingTests(String testedProgramName, String testType) {
        switch (testedProgramName) {
            case "GCD.java":
                if (testType.equalsIgnoreCase(utils.positive)) {
                    this.posTestsNumber = GCDTestPos.numberOfPositiveTests;
                    return GCDTestPos.class;
                } else {
                    this.negTestNumber = GCDTestNeg.numberOfNegativeTests;
                    return GCDTestNeg.class;
                }
            default:
                return null;
        }
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

    private void storeCodeAndStatistics(Individual individual, StringBuilder reformattedCode, long requiredPatchTime) {
        String javaFile = utils.FIXED_JAVA + solutionCounter + ".java";
        String statistics = utils.FIXED_JAVA_STATISTICS + solutionCounter + ".txt";

        StringBuilder individualStatistics = new StringBuilder();
        individualStatistics.append("Target: ").append(astHandler.getFaultSpace().get(targetNode).getNode().getTextContent()).append(utils.LINE_SEPARATOR);

        individualStatistics.append("Sources: to be added");

        individualStatistics = individual.getAllPatchesContent();
        individualStatistics.append(utils.LINE_SEPARATOR).append(utils.LINE_SEPARATOR);

        individualStatistics.append("Time: " + requiredPatchTime).append(" seconds").append(utils.LINE_SEPARATOR);
        individualStatistics.append("Generation: " + numberOfGenerations).append(utils.LINE_SEPARATOR);
        individualStatistics.append("Mutations: " + individual.getCtrMutation()).append(utils.LINE_SEPARATOR);
        individualStatistics.append("Crossovers: " + individual.getCtrCrossover()).append(utils.LINE_SEPARATOR);

        utils.saveData(utils.SOLUTION_DIRECTORY, javaFile, reformattedCode);
        utils.saveData(utils.SOLUTION_DIRECTORY, statistics, individualStatistics);
        solutionCounter++;
    }

    private List<Individual> populateList(List<Individual> sourceList) {
        List<Individual> targetList = new ArrayList<>();
        for (Individual currentIndividual : sourceList) {
            targetList.add(currentIndividual);
        }
        return targetList;
    }
}