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
        String TARGET_CODE = "GCD.java";

        //Terminal input
        if (args.length > 2) {
            try {
                if (args[0] != null) {
                    TARGET_CODE = args[0];
                }
                if (args[1].equalsIgnoreCase("-p") && args[1] != null) {
                    initialPopulationSize = Integer.parseInt(args[2]);
                }
            } catch (NumberFormatException e) {
                System.err.println("Argument " + args[2] + " must be an integer!");
                System.exit(1);
            }
        }

        File TARGET_CODE_FILE = new File(Utils.RESOURCES_DIRECTORY, TARGET_CODE);
        String TARGET_CODE_FILE_PATH = TARGET_CODE_FILE.getAbsolutePath();

        StringBuilder xmlData = Parser.parseFile(TARGET_CODE_FILE_PATH);
        Parser.saveData(Utils.OUTPUT_PARSED_DIRECTORY, Utils.FAULTY_XML, xmlData);
        //System.out.println("Program in AST XML format: \n" + xmlData.toString());

        //StringBuilder codeData = parser.parseFile((new File(OUTPUT_PARSED_DIRECTORY, FAULTY_XML)).getAbsolutePath());
        //FileUtils.saveData(OUTPUT_PARSED_DIRECTORY, TARGET_CODE, codeData);
        //System.out.println("Program in CODE format: \n" + codeData.toString());

        //Testing a potential "fix"
        ASTHandler astHandler = new ASTHandler(Utils.FAULTY_XML_FILE, TARGET_CODE);

        GP_Initialize gp_initialize = new GP_Initialize(TARGET_CODE);
        ArrayList<Individual> ListIndividual = gp_initialize.initialize(initialPopulationSize, astHandler);

        ArrayList<JavaResult> ListJavaResult = gp_initialize.LoopPopulation(ListIndividual);
        //you can use JavaResult.getFitness() to access fitness value

        //parser.runFromCMD(filePath, faultyXml);
        //parser.runFromCMD(faultyXml, outputCode);
    }
}
