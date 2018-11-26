import AST.ASTHandler;
import AST.Parser;
import GP.Bug;
import General.TimeThread;
import General.Utils;

import java.util.List;

import static java.lang.System.exit;

public class Main {

    public static void main(String[] args) {
        int initialPopulationSize = 10;
        int timeInMinutes = 90;
        Utils utils = new Utils();
        Parser parser = new Parser(utils);

        //An example of a terminal input:
        //LeapYear.java "C:\\Program Files\\srcML 0.9.5\\bin" -p 50 -t 90 - cfp "C:\\Users\\admin\\git\\CS454-Automated_patching_using_GP\\out\\production\\CS454_AutomatedPatching" -c GCD.class
        if (args.length > 9) {
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
                if (args[4].equalsIgnoreCase("-t") && args[4] != null) {
                    timeInMinutes = Integer.parseInt(args[5]);
                }
                if (args[6].equalsIgnoreCase("-cfp") && args[6] != null) {
                    utils.DOT_CLASS_FOLDER_PATH = args[7];
                }
                if (args[8].equalsIgnoreCase("-c") && args[8] != null) {
                    utils.TARGET_CLASS = args[9];
                }
            } catch (NumberFormatException e) {
                System.err.println("Arguments -p and -t must be integers!");
                exit(1);
            }
        }

        Thread currThread = Thread.currentThread();
        TimeThread t1 = new TimeThread(timeInMinutes, currThread);
        t1.start();

        long startTime = System.currentTimeMillis();

        //Fault localization - process the output file from GZoltar
        utils.obtainSuspiciousLines();
        List<Bug> chosenBugs = utils.amountOfBugsToFix(1);

        //ASTHandler instance
        ASTHandler astHandler = new ASTHandler(utils, parser, chosenBugs);

        //Genetic algorithm instance
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(initialPopulationSize, utils, astHandler, startTime);

        geneticAlgorithm.repairProgram();
        System.out.println("The program has terminated!");
        exit(0);
    }
}
