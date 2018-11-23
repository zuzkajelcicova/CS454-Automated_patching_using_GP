import AST.ASTHandler;
import AST.NodePair;
import AST.Parser;
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
    private int negPass = 0;
    private int posPass = 0;
    private int numberOfGenerations = 0;
    private Utils utils;
    private Parser parser;
    private ASTHandler astHandler;
    private List<Bug> chosenBugs;
    private ArrayList<Individual> initialPopulation;
    private ArrayList<JavaResult> solutionList = new ArrayList<>();
    private Date startRepairTime;
    private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

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
        HashMap<Integer, NodePair> faultSpace = astHandler.getFaultSpace();
        ArrayList<Integer> faultList = new ArrayList<>();
        int targetNode = -1;
        for (Map.Entry<Integer, NodePair> entry : faultSpace.entrySet()) {
            targetNode = entry.getKey();
            break;
        }
        int i;

        ArrayList<Individual> ListIndividual = new ArrayList<>();
        for (i=0;i<initialPopulationSize;i++){
            Individual population = new Individual();
            int operation = getRandom(random1);
            if (operation == 0){
                population.getAllPatches().add(new Patch(operation, -1, targetNode));
            }
            else{
                int index = new Random().nextInt(candidateList.size());
                int sourceNode = candidateList.get(index);

                targetNode = candidateList.get(index);

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

    public ArrayList<JavaResult> LoopPopulation(ArrayList<Individual> ListIndividual) throws ParseException {

        System.out.println("RUNNING THE TEST");
        //get Time now
        Date date_now_1 = new Date();
        String time1 = formatter.format(date_now_1);
        startRepairTime = formatter.parse(time1);

        ArrayList<JavaResult> ListJavaResult = new ArrayList<>();
        for(Individual individual : ListIndividual){
            ASTHandler modified_AST = new ASTHandler(utils, parser, chosenBugs);
            modified_AST.applyPatches(individual);

            StringBuilder noLinesCode = utils.removeCodeLines();
            utils.saveData(utils.GEN_CANDIDATE_DIRECTORY, utils.TARGET_CODE, noLinesCode);

            JavaResult result = javaCompile(individual);

            if(result.getResult() == utils.PASS){

                Result testPosResult = JUnitCore.runClasses(GCDTestPos.class);

                for (Failure failure : testPosResult.getFailures()) {
                    System.out.println(failure.toString());
                }
                System.out.println("Testing Positive Test case");
                System.out.println("Number of Failure: " + testPosResult.getFailureCount());
                System.out.println("Number of Run: " + testPosResult.getRunCount());
                System.out.println("Run Time: " + testPosResult.getRunTime());

                posPass = utils.NUM_POS_TEST - testPosResult.getFailureCount();

                Result testNegResult = JUnitCore.runClasses(GCDTestNeg.class);

                for (Failure failure : testNegResult.getFailures()) {
                    System.out.println(failure.toString());
                }
                System.out.println("Testing Negative Test case");
                System.out.println("Number of Failure: " + testNegResult.getFailureCount());
                System.out.println("Number of Run: " + testNegResult.getRunCount());
                System.out.println("Run Time: " + testNegResult.getRunTime());

                negPass = utils.NUM_NEG_TEST - testNegResult.getFailureCount();

                double fitness = (utils.WEIGHT_NEG*negPass)/(utils.NUM_NEG_TEST) + (utils.WEIGHT_POS*posPass)/(utils.NUM_POS_TEST);
                System.out.println("Fitness Value: " + fitness);
                result.setFitness(fitness);

                ListJavaResult.add(result);

                if(testNegResult.getFailureCount() == 0 && testPosResult.getFailureCount() == 0){
                    solutionList.add(result);
                    int number_of_sol = solutionList.size();
                    utils.saveData(utils.SOLUTION_DIRECTORY, utils.TARGET_CODE + Integer.toString(number_of_sol), noLinesCode);
                    StringBuilder details = new StringBuilder();
                    List<Patch> ListPatch = result.getIndividual().getAllPatches();
                    // TODO: ADD number of cross over and mutation
                    for (Patch patch : ListPatch){
                        details.append(patch.printPatch());
                        details.append("Number of generations: " + Integer.toString(this.numberOfGenerations) + utils.LINE_SEPARATOR);
                        details.append("******************************" + utils.LINE_SEPARATOR);
                    }

                    utils.saveData(utils.SOLUTION_DIRECTORY, utils.TARGET_CODE + "Patches" + Integer.toString(number_of_sol), details);
                }
            }
            System.out.println("DONE one INDIVIDUAL");
            //CHECK TIME
            Date date_now_2 = new Date();
            String time2 = formatter.format(date_now_2);
            Date date2 = formatter.parse(time2);
            long difference = date2.getTime() - startRepairTime.getTime();
            //difference in milliseconds
            if (difference > maxTimeInMinutes*60*1000 || solutionList.size()>0){
                break;
            }

        }
        System.out.println("TEST ENDED");
        return ListJavaResult;
    }

    public ArrayList<JavaResult> repairProgram(ArrayList<JavaResult> ListJavaResultPassed, ASTHandler astHandler) throws ParseException {
        //todo: implement a loop handling the above three cases for exiting
//        ArrayList<JavaResult> ListJavaResultPassed = LoopPopulation(this.initialPopulation);
        //todo: this is just a manual testing from Main (temporary before the loop is done)


        GeneticOperations gp = new GeneticOperations(utils);

        JavaResult fittest_java_result = gp.getFittest(ListJavaResultPassed);
        List<JavaResult> selected =  gp.tournamentSelection(ListJavaResultPassed);

        //candidate space
        HashMap<Integer, NodePair> candidateSpace = astHandler.getCandidateSpace();
        ArrayList<Integer> candidateList = new ArrayList<>();
        for (Map.Entry<Integer, NodePair> entry : candidateSpace.entrySet()) {
            candidateList.add(entry.getKey());
        }
        //KEEP the old array, cuz after cross over we might have to get rid all of them, so we need to add th eold one
        // overwrite the new one to the old one

        while (true){

            List<Individual> newListIndividual;

            //this is the older one before even doing CROSS OVER and MUTATION
            List<JavaResult> chosenIndividualList = selected;
            //CROSS OVER
            newListIndividual = gp.crossover(selected);
            //MUTATION
            //overwrite individual list after cross over with mutation
            //add fittest one too
            newListIndividual = gp.mutate(newListIndividual, candidateList, fittest_java_result.getIndividual());

//            newListIndividual.add(eachJavaResult.getIndividual());

            ArrayList<JavaResult> ListJavaPassedIndividual_2 = LoopPopulation((ArrayList<Individual>) newListIndividual);


            chosenIndividualList.add(fittest_java_result);
            ListJavaPassedIndividual_2.sort(new SortbyFitness());
            int diff = populationSize - chosenIndividualList.size();

            if(diff < ListJavaPassedIndividual_2.size()) {
                for (int i = 0; i < diff; i++) {
                    chosenIndividualList.add(ListJavaPassedIndividual_2.get(i));
                }
            }
            else{

                //add all passed compilation after CROSS OVER and MUTATION
                chosenIndividualList.addAll(ListJavaPassedIndividual_2);

                diff = populationSize - chosenIndividualList.size();
                //add old population even before tournament selection
                for (int i = 0; i < diff; i++) {
                    chosenIndividualList.add(ListJavaResultPassed.get(i));
                }

            }

            //Now get rid of those having low fitness_value
            //get the best fitness first
            fittest_java_result = gp.getFittest(chosenIndividualList);

            selected =  gp.tournamentSelection(ListJavaPassedIndividual_2);

            Date date_now_2 = new Date();
            String time2 = formatter.format(date_now_2);
            Date date2 = formatter.parse(time2);
            long difference = date2.getTime() - startRepairTime.getTime();
            //difference in milliseconds
            if (difference > maxTimeInMinutes*60*1000 || solutionList.size()>0){
                break;
            }
            this.numberOfGenerations++;
        }

        return solutionList;
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


//        Individual potentialPatch1 = new Individual();
//        // OPERATIONS (set up for LeapYear):
//        potentialPatch1.getAllPatches().add(new Patch(utils.REPLACE, 114, 121));
//        potentialPatch1.getAllPatches().add(new Patch(utils.INSERT, 106, 96));
//        potentialPatch1.getAllPatches().add(new Patch(utils.DELETE, -1, 106));
//        potentialPatch1.getAllPatches().add(new Patch(utils.INSERT, 114, 140));
//        potentialPatch1.getAllPatches().add(new Patch(utils.DELETE, -1, 96));
//        potentialPatch1.getAllPatches().add(new Patch(utils.INSERT, 114, 96));
//
//        astHandler.applyPatches(potentialPatch1);
//        StringBuilder noLinesCode = utils.removeCodeLines();
//        utils.saveData(utils.GEN_CANDIDATE_DIRECTORY, utils.TARGET_CODE, noLinesCode);
//
//        //Test out the second round
//        Individual potentialPatch2 = new Individual();
//        potentialPatch2.getAllPatches().add(new Patch(utils.INSERT, 106, 96));
//        potentialPatch2.getAllPatches().add(new Patch(utils.REPLACE, 140, 96));
//        potentialPatch2.getAllPatches().add(new Patch(utils.INSERT, 106, 96));
//        astHandler.applyPatches(potentialPatch2);
//
//
//        //Remove line numbers to clean the code
//        noLinesCode = utils.removeCodeLines();
//        utils.saveData(utils.GEN_CANDIDATE_DIRECTORY, utils.TARGET_CODE, noLinesCode);
//
//
//        //Test out the 3rd round
//        Individual potentialPatch3 = new Individual();
//        potentialPatch3.getAllPatches().add(new Patch(utils.INSERT, 106, 96));
//        astHandler.applyPatches(potentialPatch3);
//
//        //Remove line numbers to clean the code
//        noLinesCode = utils.removeCodeLines();
//        utils.saveData(utils.GEN_CANDIDATE_DIRECTORY, utils.TARGET_CODE, noLinesCode);
    }
}
