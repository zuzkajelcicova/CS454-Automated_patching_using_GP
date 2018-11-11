package GP;

import AST.ASTHandler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JavaResult {
    private float fitness = 0;
//    pass or fail
    private int result;
    private ASTHandler astHandler;
    private String TARGET_CODE = "";

    public JavaResult(String TARGET_CODE, ASTHandler astHandler) {
        this.TARGET_CODE = TARGET_CODE;
        this.astHandler = astHandler;
    }

    public float getFitness() {
        return fitness;
    }

    public int getResult() {
        return result;
    }

    public ASTHandler getAstHandler() {
        return astHandler;
    }

    public void run() {

        try {
            System.out.println("**********");
            this.result = runProcess("javac -cp src src/" + this.TARGET_CODE);
            System.out.println("**********");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void printLines(String cmd, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(cmd + " " + line);
        }
    }

    private int runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command);
        printLines(command + " stdout:", pro.getInputStream());
        printLines(command + " stderr:", pro.getErrorStream());
        pro.waitFor();
        System.out.println(command + " exitValue() " + pro.exitValue());
        return pro.exitValue();
    }

    public void fitness_function(ASTHandler modified_AST){



        this.fitness =

    }
}
