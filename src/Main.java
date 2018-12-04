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
        int noOfBugsToSolve = 1;

        String targetCode = "";
        String srcMlPath = "";
        String classFolderPath = "";

        //An example of a terminal input:
        //faultyProgramName srcMlPath -p populationSize -t timeInMinutes -b noOfBugsToSolve -cpf classFolderPathOfFaultyProgram
        //LeapYear "C:\\Program Files\\srcML 0.9.5\\bin" -p 50 -t 90 -b 4 -cfp "C:\\Users\\admin\\git\\CS454-Automated_patching_using_GP\\out\\production\\CS454_AutomatedPatching"
        if (args.length > 9) {
            try {
                if (args[0] != null) {
                    targetCode = args[0];
                }
                if (args[1] != null) {
                    srcMlPath = args[1];
                }
                if (args[2].equalsIgnoreCase("-p") && args[2] != null) {
                    initialPopulationSize = Integer.parseInt(args[3]);
                }
                if (args[4].equalsIgnoreCase("-t") && args[4] != null) {
                    timeInMinutes = Integer.parseInt(args[5]);
                }
                if (args[6].equalsIgnoreCase("-b") && args[6] != null) {
                    noOfBugsToSolve = Integer.parseInt(args[7]);
                }
                if (args[8].equalsIgnoreCase("-cfp") && args[8] != null) {
                    classFolderPath = args[9];
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

        //public Utils(String srcMl, String targetCode, String dotClassFolderPath, String targetClass)
        Utils utils = new Utils(srcMlPath, targetCode + ".java", classFolderPath, targetCode + ".class");
        Parser parser = new Parser(utils);


        //Fault localization - process the output file from GZoltar
        utils.obtainSuspiciousLines();
        List<Bug> chosenBugs = utils.amountOfBugsToFix(noOfBugsToSolve);

        //ASTHandler instance
        ASTHandler astHandler = new ASTHandler(utils, parser, chosenBugs);

        //Genetic algorithm instance
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(initialPopulationSize, utils, astHandler, startTime);

        geneticAlgorithm.repairProgram();
        System.out.println("The program has terminated!");
        exit(0);
    }
}
