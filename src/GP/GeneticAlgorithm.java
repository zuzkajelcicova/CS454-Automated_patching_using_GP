package GP;

import AST.ASTHandler;
import AST.Parser;
import General.Utils;

public class GeneticAlgorithm {

    private int populationSize;
    private int maxTimeInMinutes;
    private int maxNumberOfFitnessEval;
    private Utils utils;
    private Parser parser;
    private ASTHandler astHandler;

    public GeneticAlgorithm(int populationSize, int maxTimeInMinutes, int maxNumberOfFitnessEval,
                            Utils utils, Parser parser, ASTHandler astHandler) {
        this.populationSize = populationSize;
        this.maxTimeInMinutes = maxTimeInMinutes;
        this.maxNumberOfFitnessEval = maxNumberOfFitnessEval;
        this.utils = utils;
        this.parser = parser;
        this.astHandler = astHandler;
        //todo: create an initial population of size populationSize randomly
    }

    public void repairProgram() {
        //todo: implement a loop handling the above three cases for exiting
        //todo: this is just a manual testing from Main (temporary before the loop is done)
        //Testing a potential "fix"
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
