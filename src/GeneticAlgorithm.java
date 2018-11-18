import AST.ASTHandler;
import AST.NodePair;
import AST.Parser;
import GP.Bug;
import GP.Individual;
import GP.JavaResult;
import GP.Patch;
import General.Utils;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.*;

public class GeneticAlgorithm {

    private int populationSize;
    private int maxTimeInMinutes;
    private int maxNumberOfFitnessEval;
    private int negPass = 0;
    private int posPass = 0;
    private Utils utils;
    private Parser parser;
    private ASTHandler astHandler;
    private List<Bug> chosenBugs;
    private ArrayList<Individual> initialPopulation;

    public GeneticAlgorithm(int populationSize, int maxTimeInMinutes, int maxNumberOfFitnessEval,
                            Utils utils, Parser parser, ASTHandler astHandler, List<Bug> chosenBugs) {
        this.populationSize = populationSize;
        this.maxTimeInMinutes = maxTimeInMinutes;
        this.maxNumberOfFitnessEval = maxNumberOfFitnessEval;
        this.utils = utils;
        this.parser = parser;
        this.astHandler = astHandler;
        this.chosenBugs = chosenBugs;
        //todo: create an initial population of size populationSize randomly
        this.initialPopulation = initialize(this.populationSize,this.astHandler);
    }

    public ArrayList<Individual> getInitialPopulation() {
        return initialPopulation;
    }

    public void setNegPass(int negPass) {
        this.negPass = negPass;
    }

    public void setPosPass(int posPass) {
        this.posPass = posPass;
    }

    public ArrayList<Individual> initialize(int initialPopulationSize, ASTHandler astHandler){

        int[] random1 = new int[]{0,1,2};
        HashMap<Integer, NodePair> candidateSpace = astHandler.getCandidateSpace();
        ArrayList<Integer> candidateList = new ArrayList<>();
        for (Map.Entry<Integer, NodePair> entry : candidateSpace.entrySet()) {
            candidateList.add(entry.getKey());
        }
        int i;
        ArrayList<Individual> ListIndividual = new ArrayList<>();
        for (i=0;i<initialPopulationSize;i++){
            Individual population = new Individual();
            int operation = getRandom(random1);
            if (operation == 0){
                int index = new Random().nextInt(candidateList.size());
                int targetNode = candidateList.get(index);
                population.getAllPatches().add(new Patch(operation, -1, targetNode));
            }
            else{
                int index = new Random().nextInt(candidateList.size());
                int targetNode = candidateList.get(index);
                index = new Random().nextInt(candidateList.size());
                int sourceNode = candidateList.get(index);
                //We do not allow the same target node and source node
                while (targetNode == sourceNode){
                    index = new Random().nextInt(candidateList.size());
                    targetNode = candidateList.get(index);
                }
                population.getAllPatches().add(new Patch(operation, sourceNode, targetNode));
            }
            ListIndividual.add(population);
        }

        return ListIndividual;
    }

    public int getRandom(int[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }
    public JavaResult javaCompile(Individual individual){

//        run the java file
//        use fitness function somehow from JavaResult
        return new JavaResult(utils, individual);
    }

    public ArrayList<JavaResult> LoopPopulation(ArrayList<Individual> ListIndividual){

        ArrayList<JavaResult> ListJavaResult = new ArrayList<>();
        for(Individual individual : ListIndividual){
            ASTHandler modified_AST = new ASTHandler(utils, parser, chosenBugs);
            modified_AST.applyPatches(individual);

            StringBuilder noLinesCode = utils.removeCodeLines();
            utils.saveData(utils.GEN_CANDIDATE_DIRECTORY, utils.TARGET_CODE, noLinesCode);

            JavaResult result = javaCompile(individual);

            if(result.getResult() == utils.PASS){

                Result testResult = JUnitCore.runClasses(GCDTestPos.class);

                for (Failure failure : testResult.getFailures()) {
                    System.out.println(failure.toString());
                }
                System.out.println("Testing Positive Test case");
                System.out.println("Number of Failure: " + testResult.getFailureCount());
                System.out.println("Number of Run: " + testResult.getRunCount());
                System.out.println("Run Time: " + testResult.getRunTime());

                posPass = utils.NUM_POS_TEST - testResult.getFailureCount();

                testResult = JUnitCore.runClasses(GCDTestNeg.class);

                for (Failure failure : testResult.getFailures()) {
                    System.out.println(failure.toString());
                }
                System.out.println("Testing Negative Test case");
                System.out.println("Number of Failure: " + testResult.getFailureCount());
                System.out.println("Number of Run: " + testResult.getRunCount());
                System.out.println("Run Time: " + testResult.getRunTime());

                negPass = utils.NUM_NEG_TEST - testResult.getFailureCount();

                double fitness = (utils.WEIGHT_NEG*negPass)/(utils.NUM_NEG_TEST) + (utils.WEIGHT_POS*posPass)/(utils.NUM_POS_TEST);
                System.out.println("Fitness Value: " + fitness);
                result.setFitness(fitness);

                ListJavaResult.add(result);
            }

        }

        return ListJavaResult;
    }

    public void repairProgram() {
        //todo: implement a loop handling the above three cases for exiting
        ArrayList<JavaResult> ListJavaPassedIndividual = LoopPopulation(this.initialPopulation);
        //todo: this is just a manual testing from Main (temporary before the loop is done)
        //Testing a potential "fix"
//        JUnitCore junit = new JUnitCore();
//        junit.addListener(new RunListener());
//        Result result = JUnitCore.runClasses(GCDTestPos.class);
//
//        for (Failure failure : result.getFailures()) {
//            System.out.println(failure.toString());
//        }
//        System.out.println(result.wasSuccessful());
//        System.out.println("ENDED RESULT OF TESTING");


        Individual potentialPatch1 = new Individual();
        // OPERATIONS (set up for LeapYear):
        potentialPatch1.getAllPatches().add(new Patch(utils.REPLACE, 114, 121));
        potentialPatch1.getAllPatches().add(new Patch(utils.INSERT, 106, 96));
        potentialPatch1.getAllPatches().add(new Patch(utils.DELETE, -1, 106));
        potentialPatch1.getAllPatches().add(new Patch(utils.INSERT, 114, 140));
        potentialPatch1.getAllPatches().add(new Patch(utils.DELETE, -1, 96));
        potentialPatch1.getAllPatches().add(new Patch(utils.INSERT, 114, 96));

        astHandler.applyPatches(potentialPatch1);
        StringBuilder noLinesCode = utils.removeCodeLines();
        utils.saveData(utils.GEN_CANDIDATE_DIRECTORY, utils.TARGET_CODE, noLinesCode);

        //Test out the second round
        Individual potentialPatch2 = new Individual();
        potentialPatch2.getAllPatches().add(new Patch(utils.INSERT, 106, 96));
        potentialPatch2.getAllPatches().add(new Patch(utils.REPLACE, 140, 96));
        potentialPatch2.getAllPatches().add(new Patch(utils.INSERT, 106, 96));
        astHandler.applyPatches(potentialPatch2);


        //Remove line numbers to clean the code
        noLinesCode = utils.removeCodeLines();
        utils.saveData(utils.GEN_CANDIDATE_DIRECTORY, utils.TARGET_CODE, noLinesCode);


        //Test out the 3rd round
        Individual potentialPatch3 = new Individual();
        potentialPatch3.getAllPatches().add(new Patch(utils.INSERT, 106, 96));
        astHandler.applyPatches(potentialPatch3);

        //Remove line numbers to clean the code
        noLinesCode = utils.removeCodeLines();
        utils.saveData(utils.GEN_CANDIDATE_DIRECTORY, utils.TARGET_CODE, noLinesCode);
    }
}
