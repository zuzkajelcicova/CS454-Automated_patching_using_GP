import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.List;

public class JUnitProcess {

    public static void main(String[] args) {
        System.out.println("Executing a separate process JUnitProcess...");
        setCorrespondingTests(args[0]);
        runTestInSeparateProcess();
    }

    private static Result testResults;
    private static int noOfNegTestFailures;
    private static int noOfPosTestFailures;
    private static Class positiveTestClass;
    private static Class negativeTestClass;

    private static void runTestInSeparateProcess() {
        //Run JUnit classes and methods in parallel (separate process)
        resetValues();
        Class[] cls = {positiveTestClass, negativeTestClass};
        testResults = JUnitCore.runClasses(new ParallelComputer(true, true), cls);
        printTestStatistics();

        List<Failure> allFailures = testResults.getFailures();

        for (Failure failure : allFailures) {
            if (failure.getDescription().getClassName().contains("Pos")) {
                noOfPosTestFailures++;
            } else {
                noOfNegTestFailures++;
            }
        }

        System.out.println("JUnitProcess_TESTRESULTS_NEG:" + getNoOfNegTestFailures());
        System.out.println("JUnitProcess_TESTRESULTS_POS:" + getNoOfPosTestFailures());
    }

    public static Result getTestResults() {
        return testResults;
    }

    private static void printTestStatistics() {
        for (Failure failure : testResults.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("JUnit Positive and Negative Tests");
        System.out.println("Number of Failure: " + testResults.getFailureCount());
        System.out.println("Number of Run: " + testResults.getRunCount());
        System.out.println("Run Time: " + testResults.getRunTime());
    }

    public static int getNoOfNegTestFailures() {
        return noOfNegTestFailures;
    }

    public static int getNoOfPosTestFailures() {
        return noOfPosTestFailures;
    }

    public static void resetValues() {
        noOfNegTestFailures = 0;
        noOfPosTestFailures = 0;
    }

    private static void setCorrespondingTests(String testedProgramName) {
        System.out.println("Obtained program: " + testedProgramName);
        switch (testedProgramName) {
            case "GCD.java":
                positiveTestClass = GCDTestPos.class;
                negativeTestClass = GCDTestNeg.class;
                System.out.println("JUnitProcess: GCD.java JUnit tests selected");
                break;
            case "Zune.java":
                positiveTestClass = ZuneTestPos.class;
                negativeTestClass = ZuneTestNeg.class;
                System.out.println("JUnitProcess: Zune.java JUnit tests selected");
                break;
            default:
                System.out.println("No matching test classes were found!");
        }
    }
}
