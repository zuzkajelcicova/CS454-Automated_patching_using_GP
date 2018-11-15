import AST.ASTHandler;
import AST.Parser;
import GP.Bug;
import GP.Individual;
import GP.Patch;
import General.Utils;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        //todo: many things here are just temporary until being merged with GA Loop

        //Example of a terminal input:
        //LeapYear.java "C:\\Program Files\\srcML 0.9.5\\bin" -p 50 -f 1000 -t 90

        int initialPopulationSize = 40;
        int fitnessEvaluations = 10;
        int timeInMinutes = 50;
        Utils utils = new Utils();
        Parser parser = new Parser(utils);

        //Fault localization - process the output from GZoltar
        utils.obtainSuspiciousLines();
        List<Bug> chosenBugs = utils.amountOfBugsToFix(6);

        //Terminal input
        if (args.length > 7) {
            try {
                if (args[0] != null) {
                    utils.TARGET_CODE = args[0];
                }
                if (args[1] != null) {
                    utils.SRCML_PATH = args[1];
                }
                if (args[2].equalsIgnoreCase("-p") && args[2] != null) {
                    initialPopulationSize = Integer.parseInt(args[3]);
                }
                if (args[4].equalsIgnoreCase("-f") && args[4] != null) {
                    fitnessEvaluations = Integer.parseInt(args[5]);
                }
                if (args[6].equalsIgnoreCase("-t") && args[6] != null) {
                    timeInMinutes = Integer.parseInt(args[7]);
                }
            } catch (NumberFormatException e) {
                System.err.println("Arguments -p,-f and -t must be integers!");
                System.exit(1);
            }
        }

        //Original AST
        StringBuilder xmlData = parser.parseFile(utils.TARGET_CODE_FILE_PATH);
        utils.saveData(utils.OUTPUT_PARSED_DIRECTORY, utils.FAULTY_XML, xmlData);

        //Testing a potential "fix"
        ASTHandler astHandler = new ASTHandler(utils, parser, chosenBugs);
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
