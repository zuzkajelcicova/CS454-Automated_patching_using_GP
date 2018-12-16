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
            case "Digits00.java":
                positiveTestClass = Digits00NegTest.class;
                negativeTestClass = Digits00PosTest.class;
                System.out.println("JUnitProcess: Digits00.java JUnit tests selected");
                break;
            case "Triangle.java":
                positiveTestClass = TestTriNeg.class;
                negativeTestClass = TestTriPos.class;
                System.out.println("JUnitProcess: Triangle.java JUnit tests selected");
                break;
            case "Digits002.java":
                positiveTestClass = Digits002NegTest.class;
                negativeTestClass = Digits002PosTest.class;
                System.out.println("JUnitProcess: Digits002.java JUnit tests selected");
                break;
            case "Digits5000.java":
                positiveTestClass = Digits5000NegTest.class;
                negativeTestClass = Digits5000PosTest.class;
                System.out.println("JUnitProcess: Digits5000.java JUnit tests selected");
                break;
            case "Digits5003.java":
                positiveTestClass = Digits5003NegTest.class;
                negativeTestClass = Digits5003PosTest.class;
                System.out.println("JUnitProcess: Digits5003.java JUnit tests selected");
                break;
            case "Digits6000.java":
                positiveTestClass = Digits6000NegTest.class;
                negativeTestClass = Digits6000PosTest.class;
                System.out.println("JUnitProcess: Digits6000.java JUnit tests selected");
                break;
            case "Digits10000.java":
                positiveTestClass = Digits10000NegTest.class;
                negativeTestClass = Digits10000PosTest.class;
                System.out.println("JUnitProcess: Digits10000.java JUnit tests selected");
                break;
            case "Digits11004.java":
                positiveTestClass = Digits10004NegTest.class;
                negativeTestClass = Digits10004PosTest.class;
                System.out.println("JUnitProcess: Digits10004.java JUnit tests selected");
                break;
            case "Digits11015.java":
                positiveTestClass = Digits11015NegTest.class;
                negativeTestClass = Digits11015PosTest.class;
                System.out.println("JUnitProcess: Digits11015.java JUnit tests selected");
                break;
            case "Digits12000.java":
                positiveTestClass = Digits12000NegTest.class;
                negativeTestClass = Digits12000PosTest.class;
                System.out.println("JUnitProcess: Digits12000.java JUnit tests selected");
                break;
            case "Digits39000.java":
                positiveTestClass = Digits39000NegTest.class;
                negativeTestClass = Digits39000PosTest.class;
                System.out.println("JUnitProcess: Digits39000.java JUnit tests selected");
                break;
            case "Smallest19.java":
                positiveTestClass = Smallest19NegTest.class;
                negativeTestClass = Smallest19PosTest.class;
                System.out.println("JUnitProcess: Smallest19.java JUnit tests selected");
                break;
            case "NumberUtils.java":
                positiveTestClass = NumberUtilsTestNeg.class;
                negativeTestClass = NumberUtilsTestPos.class;
                System.out.println("JUnitProcess: NumberUtils.java JUnit tests selected");
                break;
            default:
                System.out.println("No matching test classes were found!");
        }
    }
}
