import AST.ASTHandler;
import AST.Parser;
import GP.GP_Initialize;
import GP.Individual;
import GP.JavaResult;
import GP.Patch;
import General.Utils;

import java.io.File;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        //todo: hardcode faulty space for now - exclude buggy statements from ingredient space
        //todo: after the faulty space has been created, initial population can be created randomly

        int initialPopulationSize = 40;
        //Buggy program
        //Default program name - will change if different input program provided
        int fitnessEvaluations = 10;
        int timeInminutes = 50;
        Utils utils = new Utils();
        Parser parser = new Parser(utils);

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
                    timeInminutes = Integer.parseInt(args[7]);
                }
            } catch (NumberFormatException e) {
                System.err.println("Arguments -p,-f and -t must be integers!");
                System.exit(1);
            }
        }

        //Original AST
        StringBuilder xmlData = parser.parseFile(utils.TARGET_CODE_FILE_PATH);
        parser.saveData(utils.OUTPUT_PARSED_DIRECTORY, utils.FAULTY_XML, xmlData);
        //System.out.println("Program in AST XML format: \n" + xmlData.toString());

        //StringBuilder codeData = parser.parseFile((new File(OUTPUT_PARSED_DIRECTORY, FAULTY_XML)).getAbsolutePath());
        //FileUtils.saveData(OUTPUT_PARSED_DIRECTORY, TARGET_CODE, codeData);
        //System.out.println("Program in CODE format: \n" + codeData.toString());

        //Testing a potential "fix"
        ASTHandler astHandler = new ASTHandler(utils, parser);

        GP_Initialize gp_initialize = new GP_Initialize(utils.TARGET_CODE, utils, parser);
        ArrayList<Individual> ListIndividual = gp_initialize.initialize(initialPopulationSize, astHandler);

        ArrayList<JavaResult> ListJavaPassedIndividual = gp_initialize.LoopPopulation(ListIndividual);
        //you can use JavaResult.getFitness() to access fitness value

        //Remove line numbers to clean the code
        StringBuilder noLinesCode = astHandler.removeCodeLines();
        parser.saveData(utils.SRC_DIRECTORY, utils.TARGET_CODE, noLinesCode);

        //parser.runFromCMD(filePath, faultyXml);
        //parser.runFromCMD(faultyXml, outputCode);
    }
}

