package GP;

import AST.ASTHandler;
import General.Utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;

public class JavaResult {
    private double fitness = 0;
//    pass or fail
    private int result;
    private Utils utils;
    private String TARGET_CODE = "";
    private Individual individual;


    public JavaResult(String TARGET_CODE, Utils utils, Individual individual) {
        this.TARGET_CODE = TARGET_CODE;
        this.utils = utils;
        this.individual = individual;
        run();
    }

    public double getFitness() {
        return fitness;
    }

    public int getResult() {
        return result;
    }

    private void run() {
        try {
            System.out.println("**********");
            this.result = runProcess("javac -cp src src/" + this.TARGET_CODE);
            System.out.println("**********");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printLines(String cmd, InputStream ins) throws Exception {
        String line;
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

    private void fitness_function(ArrayList<JavaResult> ListJavaPassedIndividual){
        BigInteger bi1 = new BigInteger("18");
        BigInteger bi2 = new BigInteger("24");
        BigInteger bi3 = bi1.gcd(bi2);

        // assume I have neg and pos test case
        ArrayList<TestCase> negTest = new ArrayList<>();
        ArrayList<TestCase> posTest = new ArrayList<>();
        // mock up test case
        int[] a = new int[]{42,56};
        TestCase testCase1 = new TestCase(a,14);
        int[] b = new int[]{461952,116298};
        TestCase testCase2 = new TestCase(b,18);
        negTest.add(testCase1);
        negTest.add(testCase2);
        int[] c = new int[]{12,0};
        TestCase testCase3 = new TestCase(c,12);
        int[] d = new int[]{0,0};
        TestCase testCase4 = new TestCase(d,0);
        posTest.add(testCase3);
        posTest.add(testCase4);

        // save path(node) that were visited by neg and pos test case
        int negFail = 0;
        int posFail = 0;
        //TODO: Add this later in UNIT test
//        for (TestCase testcase: negTest){
//            int output = run(testcase);
//            negFail = negFail + output;
//            if(output == 1){
//                this.result = utils.FAIL;
//            }
//        }
//        for (TestCase testcase: posTest){
//            int output = run(testcase);
//            posFail = posFail + output;
//            if(output == 1){
//                this.result = utils.FAIL;
//            }
//        }
        int negPass = negTest.size() - negFail;
        int posPass = posTest.size() - posFail;

        //TODO: change formula for fitness function
        this.fitness = (utils.WEIGHT_NEG*negPass) + (utils.WEIGHT_POS*posPass);

    }
}
