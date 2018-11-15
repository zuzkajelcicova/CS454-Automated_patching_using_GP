import AST.ASTHandler;
import AST.Parser;
import GP.Bug;
import GP.GeneticAlgorithm;
import General.Utils;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        int initialPopulationSize = 40;
        int fitnessEvaluations = 10;
        int timeInMinutes = 50;
        Utils utils = new Utils();
        Parser parser = new Parser(utils);

        //An example of a terminal input:
        //LeapYear.java "C:\\Program Files\\srcML 0.9.5\\bin" -p 50 -f 1000 -t 90
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

        //Fault localization - process the output file from GZoltar
        utils.obtainSuspiciousLines();
        List<Bug> chosenBugs = utils.amountOfBugsToFix(6);

        //ASTHandler instance
        ASTHandler astHandler = new ASTHandler(utils, parser, chosenBugs);

        //Genetic algorithm instance
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(initialPopulationSize, timeInMinutes,
                fitnessEvaluations, utils, parser, astHandler);
        geneticAlgorithm.repairProgram();

        System.out.printf("The program has terminated!");
    }
}
