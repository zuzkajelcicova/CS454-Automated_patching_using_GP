package GP;

import AST.ASTHandler;
import AST.Parser;
import GP.Individual;
import GP.Patch;
import General.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GP_Initialize {

    private Individual individual;
    private String TARGET_CODE = "";
    private Utils utils;
    private Parser parser;
    public GP_Initialize(String target_code, Utils utils, Parser parser) {

        this.TARGET_CODE = target_code;
        this.utils = utils;
        this.parser = parser;
    }
//Patches here is a class that contains List (or ArrayList) of Insertion, Deletion, Replacement
    public ArrayList<Individual> initialize(int initialPopulationSize, ASTHandler astHandler){

        int[] random1 = new int[]{0,1,2};
        int lenAST = astHandler.getAst().getLength();
        int i;
        ArrayList<Individual> ListIndividual = new ArrayList<>();
        for (i=0;i<initialPopulationSize;i++){
            Individual population = new Individual();
            int operation = getRandom(random1);
            if (operation == 0){
                int targetNode = new Random().nextInt(lenAST);
                population.getAllPatches().add(new Patch(operation, -1, targetNode));
            }
            else{
                int targetNode = new Random().nextInt(lenAST);
                int sourceNode = new Random().nextInt(lenAST);
//                We do not allow the same target node and source node
                while (targetNode == sourceNode){
                    targetNode = new Random().nextInt(lenAST);
                }
                population.getAllPatches().add(new Patch(operation, sourceNode, targetNode));
            }
            ListIndividual.add(population);
        }

        return ListIndividual;
    }
    public ArrayList<JavaResult> LoopPopulation(ArrayList<Individual> ListIndividual){

        ArrayList<JavaResult> ListJavaResult = new ArrayList<>();
        for(Individual individual : ListIndividual){
            ASTHandler modified_AST = new ASTHandler(utils, parser);
            modified_AST.applyPatches(individual);

            JavaResult result = javaCompile(individual);

            if(result.getResult() == utils.PASS){
                ListJavaResult.add(result);
            }

        }


        return ListJavaResult;
    }
    public int getRandom(int[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }
    public JavaResult javaCompile(Individual individual){

//        run the java file
//        use fitness function somehow from JavaResult
        return new JavaResult(TARGET_CODE, utils, individual);
    }

}
