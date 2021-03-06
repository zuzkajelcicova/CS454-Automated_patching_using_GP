import AST.ASTHandler;
import AST.NodePair;
import GP.*;
import General.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;

public class GeneticAlgorithm {

    private int populationSize;
    private int negPass;
    private int posPass;
    private int numberOfGenerations;
    private Utils utils;
    private ASTHandler astHandler;
    private HashMap<Integer, NodePair> candidateSpace;
    private ArrayList<Integer> candidateList;
    private ArrayList<Patch> initialPopulation;
    private int solutionList;
    private int solutionCounter;
    private ArrayList<Integer> targetNodes;
    private long startTime;
    private int posTestsNumber;
    private int negTestNumber;
    private Random random;

    public GeneticAlgorithm(int populationSize, Utils utils, ASTHandler astHandler, long startTime) {
        this.random = new Random();
        this.populationSize = populationSize;
        this.utils = utils;
        this.astHandler = astHandler;
        this.candidateSpace = astHandler.getCandidateSpace();
        this.candidateList = new ArrayList<>();
        this.solutionCounter = 1;
        this.numberOfGenerations = 1;
        this.solutionList = 0;
        this.targetNodes = new ArrayList<>();

        for (Map.Entry<Integer, NodePair> entry : astHandler.getFaultSpace().entrySet()) {
            targetNodes.add(entry.getKey());
        }
        this.startTime = startTime;
    }

    public ArrayList<Patch> initialize(int initialPopulationSize) {
        int[] possibleOperations = new int[]{0, 1, 2};
        for (Map.Entry<Integer, NodePair> entry : candidateSpace.entrySet()) {
            candidateList.add(entry.getKey());
        }

        ArrayList<Patch> population = new ArrayList<>();

        for (int i = 0; i < initialPopulationSize; i++) {
            Patch patch = new Patch();
            int operation = getRandom(possibleOperations);
            int targetNode = targetNodes.get(random.nextInt(targetNodes.size()));
            if (operation == 0) {
                patch.getAllEdits().add(new Edit(operation, -1, targetNode));
            } else {
                int index = random.nextInt(candidateList.size());
                int sourceNode = candidateList.get(index);
                patch.getAllEdits().add(new Edit(operation, sourceNode, targetNode));
            }
            population.add(patch);
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

        GeneticOperations gp = new GeneticOperations(utils, targetNodes, candidateList);

        //Initialize population - initialize with more to have enough to choose from if a lot does not compile
        this.initialPopulation = initialize(this.populationSize + populationSize / 2);

        Patch fittestPatch;
        List<Patch> oldPopulation = new ArrayList<>();
        List<Patch> newPopulation;
        List<Patch> tournamentSelectionParents;
        List<Patch> offsprings;

        //Initial population
        initialPopulation = compileAndTest(initialPopulation);
        if (solutionList > 0) {
            return;
        }

        initialPopulation.sort(new SortByFitness());

        int indiv = 0;
        while (oldPopulation.size() != populationSize) {
            if (initialPopulation != null && initialPopulation.size() > 0) {
                oldPopulation.add(initialPopulation.get(indiv));
                indiv++;
            } else {
                break;
            }
        }
        if (oldPopulation.size() != populationSize) {
            System.out.println("NOT ENOUGH INDIVIDUALS, POPULATION SMALLER!!!");
            this.populationSize = oldPopulation.size();
        }
        if (oldPopulation.size() > 0) {
            fittestPatch = gp.getFittest(oldPopulation);
        } else fittestPatch = new Patch();
        newPopulation = populateList(oldPopulation);


        while (solutionList == 0) {
            this.numberOfGenerations++;
            //Tournament selection
            tournamentSelectionParents = gp.tournamentSelection(newPopulation);

            //Crossovers and mutations
            offsprings = gp.crossover(tournamentSelectionParents);
            if (fittestPatch.patchSize() > 0)
                offsprings = gp.mutate(offsprings, fittestPatch);

            offsprings = compileAndTest(offsprings);
            if (solutionList > 0) {
                return;
            }

            offsprings = selectPopulationIndividuals(oldPopulation, offsprings);

            oldPopulation = populateList(newPopulation);
            //Create new population
            newPopulation = populateList(tournamentSelectionParents);

            for (Patch patch : populateList(offsprings)) {
                newPopulation.add(patch);
            }
        }
    }

    private List<Patch> selectPopulationIndividuals(List<Patch> oldPopulation, List<Patch> offspringPopulation) {
        int threshold = populationSize / 2;
        List<Patch> result = new ArrayList<>();

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

    private ArrayList<Patch> compileAndTest(List<Patch> newPopulation) {
        ArrayList<Patch> candidateResult = new ArrayList<>();

        for (Patch patch : newPopulation) {
            astHandler.applyPatches(patch);

            StringBuilder codeWithLines = utils.getCodeWithLines();
            utils.saveData(utils.SRC_DIRECTORY, utils.TARGET_CODE, codeWithLines);

            //Compilation step
            try {
                int compilationResult = javaCompile();

                //Fitness function step
                if (compilationResult == utils.PASS) {

                    //Run JUnit in a separate process
                    Process process = new ProcessBuilder(new String[]{"java", "-cp", System.getProperty("java.class.path"), "JUnitProcess", utils.TARGET_CODE}).start();

                    //Read output from a separate process
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                    int noOfNegativeTestsFailed = 0;
                    int noOfPositiveTestsFailed = 0;

                    String line;
                    String splitSign = ":";

                    while ((line = reader.readLine()) != null) {
                        if (line.contains("JUnitProcess_TESTRESULTS_POS")) {
                            noOfPositiveTestsFailed = Integer.parseInt((line.split(splitSign))[1]);
                        } else if (line.contains("JUnitProcess_TESTRESULTS_NEG")) {
                            noOfNegativeTestsFailed = Integer.parseInt((line.split(splitSign))[1]);
                        }
                    }
                    reader.close();

                    process.waitFor();

                    //Set total number of positive and negative tests
                    getCorrespondingNoTests(utils.TARGET_CODE, utils.positive);
                    getCorrespondingNoTests(utils.TARGET_CODE, utils.negative);

                    negPass = negTestNumber - noOfNegativeTestsFailed;
                    posPass = posTestsNumber - noOfPositiveTestsFailed;

                    double fitness = (utils.WEIGHT_NEG * negPass) + (utils.WEIGHT_POS * posPass);
                    System.out.println("Fitness Value: " + fitness);
                    patch.setFitness(fitness);
                    candidateResult.add(patch);

                    //Break after the first fixing Patch is found
                    if (noOfNegativeTestsFailed == 0 && noOfPositiveTestsFailed == 0) {
                        //Storing 2 files -> .java and .txt for statistics, Time in seconds
                        long requiredPatchTime = (System.currentTimeMillis() - startTime) / 1000;
                        storeCodeAndStatistics(patch, codeWithLines, requiredPatchTime);
                        this.solutionList++;
                        break;
                    }
                } else {
                    patch.setFitness(0);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return candidateResult;
    }

    private Class getCorrespondingNoTests(String testedProgramName, String testType) {
        switch (testedProgramName) {
            case "GCD.java":
                if (testType.equalsIgnoreCase(utils.positive)) {
                    this.posTestsNumber = GCDTestPos.numberOfPositiveTests;
                    return GCDTestPos.class;
                } else {
                    this.negTestNumber = GCDTestNeg.numberOfNegativeTests;
                    return GCDTestNeg.class;
                }

            case "Zune.java":
                if (testType.equalsIgnoreCase(utils.positive)) {
                    this.posTestsNumber = ZuneTestPos.numberOfPositiveTests;
                    return GCDTestPos.class;
                } else {
                    this.negTestNumber = ZuneTestNeg.numberOfNegativeTests;
                    return GCDTestNeg.class;
                }
//            case "median.java":
////                if (testType.equalsIgnoreCase(utils.positive)) {
////                    this.posTestsNumber = median_PosTest.numberOfPositiveTests;
////                    return median_PosTest.class;
////                } else {
////                    this.negTestNumber = median_NegTest.numberOfNegativeTests;
////                    return median_NegTest.class;
////                }
            case "Triangle.java":
                if (testType.equalsIgnoreCase(utils.positive)) {
                    this.posTestsNumber = TestTriPos.numberOfPositiveTests;
                    return TestTriPos.class;
                } else {
                    this.negTestNumber = TestTriNeg.numberOfNegativeTests;
                    return TestTriNeg.class;
                }
            case "Digits00.java":
                if (testType.equalsIgnoreCase(utils.positive)) {
                    this.posTestsNumber = Digits00PosTest.numberOfPositiveTests;
                    return Digits00PosTest.class;
                } else {
                    this.negTestNumber = Digits00NegTest.numberOfNegativeTests;
                    return Digits00NegTest.class;
                }
            case "Digits002.java":
                if (testType.equalsIgnoreCase(utils.positive)) {
                    this.posTestsNumber = Digits002PosTest.numberOfPositiveTests;
                    return Digits002PosTest.class;
                } else {
                    this.negTestNumber = Digits002NegTest.numberOfNegativeTests;
                    return Digits002NegTest.class;
                }
            case "Digits5000.java":
                if (testType.equalsIgnoreCase(utils.positive)) {
                    this.posTestsNumber = Digits5000PosTest.numberOfPositiveTests;
                    return Digits5000PosTest.class;
                } else {
                    this.negTestNumber = Digits5000NegTest.numberOfNegativeTests;
                    return Digits5000NegTest.class;
                }
            case "Digits5003.java":
                if (testType.equalsIgnoreCase(utils.positive)) {
                    this.posTestsNumber = Digits5003PosTest.numberOfPositiveTests;
                    return Digits5003PosTest.class;
                } else {
                    this.negTestNumber = Digits5003NegTest.numberOfNegativeTests;
                    return Digits5003NegTest.class;
                }
            case "Digits6000.java":
                if (testType.equalsIgnoreCase(utils.positive)) {
                    this.posTestsNumber = Digits6000PosTest.numberOfPositiveTests;
                    return Digits6000PosTest.class;
                } else {
                    this.negTestNumber = Digits6000NegTest.numberOfNegativeTests;
                    return Digits6000NegTest.class;
                }
            case "Digits10000.java":
                if (testType.equalsIgnoreCase(utils.positive)) {
                    this.posTestsNumber = Digits10000PosTest.numberOfPositiveTests;
                    return Digits10000PosTest.class;
                } else {
                    this.negTestNumber = Digits10000NegTest.numberOfNegativeTests;
                    return Digits10000NegTest.class;
                }
            case "Digits11004.java":
                if (testType.equalsIgnoreCase(utils.positive)) {
                    this.posTestsNumber = Digits11004PosTest.numberOfPositiveTests;
                    return Digits11004PosTest.class;
                } else {
                    this.negTestNumber = Digits11004NegTest.numberOfNegativeTests;
                    return Digits11004NegTest.class;
                }
            case "Digits11015.java":
                if (testType.equalsIgnoreCase(utils.positive)) {
                    this.posTestsNumber = Digits11015PosTest.numberOfPositiveTests;
                    return Digits11015PosTest.class;
                } else {
                    this.negTestNumber = Digits11015NegTest.numberOfNegativeTests;
                    return Digits11015NegTest.class;
                }
            case "Digits12000.java":
                if (testType.equalsIgnoreCase(utils.positive)) {
                    this.posTestsNumber = Digits11015PosTest.numberOfPositiveTests;
                    return Digits11015PosTest.class;
                } else {
                    this.negTestNumber = Digits11015NegTest.numberOfNegativeTests;
                    return Digits11015NegTest.class;
                }
            case "Digits39000.java":
                if (testType.equalsIgnoreCase(utils.positive)) {
                    this.posTestsNumber = Digits39000PosTest.numberOfPositiveTests;
                    return Digits39000PosTest.class;
                } else {
                    this.negTestNumber = Digits39000NegTest.numberOfNegativeTests;
                    return Digits39000NegTest.class;
                }
            case "Smallest19.java":
                if (testType.equalsIgnoreCase(utils.positive)) {
                    this.posTestsNumber = Smallest19PosTest.numberOfPositiveTests;
                    return Smallest19PosTest.class;
                } else {
                    this.negTestNumber = Smallest19NegTest.numberOfNegativeTests;
                    return Smallest19NegTest.class;
                }
            case "NumberUtils2.java":
                if (testType.equalsIgnoreCase(utils.positive)) {
                    this.posTestsNumber = NumberUtilsTestPos.numberOfPositiveTests;
                    return NumberUtilsTestPos.class;
                } else {
                    this.negTestNumber = NumberUtilsTestNeg.numberOfNegativeTests;
                    return NumberUtilsTestNeg.class;
                }
            default:
                return null;
        }
    }

    private void storeCodeAndStatistics(Patch patch, StringBuilder reformattedCode, long requiredPatchTime) {
        String javaFile = utils.FIXED_JAVA + solutionCounter + ".java";
        String statistics = utils.FIXED_JAVA_STATISTICS + solutionCounter + ".txt";

        StringBuilder individualStatistics;

        individualStatistics = patch.getAllPatchesContent();
        individualStatistics.append(utils.LINE_SEPARATOR).append(utils.LINE_SEPARATOR);

        individualStatistics.append("Time: " + requiredPatchTime).append(" seconds").append(utils.LINE_SEPARATOR);
        individualStatistics.append("Generation: " + numberOfGenerations).append(utils.LINE_SEPARATOR);
        individualStatistics.append("Mutations: " + patch.getCtrMutation()).append(utils.LINE_SEPARATOR);
        individualStatistics.append("Crossovers: " + patch.getCtrCrossover()).append(utils.LINE_SEPARATOR);

        utils.saveData(utils.SOLUTION_DIRECTORY, javaFile, reformattedCode);
        utils.saveData(utils.SOLUTION_DIRECTORY, statistics, individualStatistics);
        solutionCounter++;
    }

    private List<Patch> populateList(List<Patch> sourceList) {
        List<Patch> targetList = new ArrayList<>();
        for (Patch currentPatch : sourceList) {
            targetList.add(currentPatch);
        }
        return targetList;
    }
}