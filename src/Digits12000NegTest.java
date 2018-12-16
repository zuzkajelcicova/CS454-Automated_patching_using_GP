import General.CompiledClassLoader;
import junit.framework.TestResult;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.lang.System.out;

public class Digits12000NegTest extends TestResult {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);
    public static int numberOfNegativeTests = 1;

    public static Class testClass = null;
    public static Method[] allMethods = null;
    public static Method testMethod = null;
    public static String testedMethodName = "exec";

    @BeforeClass
    public static void setupEnvironment() {
        //Get access to a recompiled class during the runtime
        testClass = Digits12000.class;
        allMethods = CompiledClassLoader.getRecompiledMethods(testClass);

        for (Method m : allMethods) {
            String methodName = m.getName();
            if (methodName.startsWith(testedMethodName)) {
                m.setAccessible(true);
                testMethod = m;
            }
        }
    }
    @Test
    public void test1() {
        try {
            out.format("Invoking %s()%n", testedMethodName, " from test1...");
            String expected = "Enter an integer >  0 That's all, have a nice day!";
            Object o = testMethod.invoke(null, 0);
            String out = o.toString().replace("\n", "").trim();
            Assert.assertEquals(expected.replace(" ", ""),
                    out.replace(" ", ""));
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Test
//    public void test2() {
//        try {
//            out.format("Invoking %s()%n", testedMethodName, " from test1...");
//            String expected = "Enter an integer >  6 7 8 -9 That's all, have a nice day!";
//            Object o = testMethod.invoke(null, -9876);
//            String out = o.toString().replace("\n", "").trim();
//            Assert.assertEquals(expected.replace(" ", ""),
//                    out.replace(" ", ""));
//            out.format("%s() returned %b%n", testedMethodName, o);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    @Test (timeout = 1000) public void test3 () throws Exception {
//        Digits00 mainClass = new Digits00();
//        String expected =
//            "Enter an integer >  2 7 2 0 3 7 3 That's all, have a nice day!";
//        mainClass.scanner = new java.util.Scanner ("3730272");
//        mainClass.exec ();
//        String out = mainClass.output.replace ("\n", " ").trim ();
//        assertEquals (expected.replace (" ", ""), out.replace (" ", ""));
//    }
//    @Test (timeout = 1000) public void test4 () throws Exception {
//        Digits00 mainClass = new Digits00();
//        String expected = "Enter an integer >  8 That's all, have a nice day!";
//        mainClass.scanner = new java.util.Scanner ("8");
//        mainClass.exec ();
//        String out = mainClass.output.replace ("\n", " ").trim ();
//        assertEquals (expected.replace (" ", ""), out.replace (" ", ""));
//    }
//    @Test (timeout = 1000) public void test5 () throws Exception {
//        Digits00 mainClass = new Digits00();
//        String expected =
//            "Enter an integer >  4 7 3 9 8 That's all, have a nice day!";
//        mainClass.scanner = new java.util.Scanner ("89374");
//        mainClass.exec ();
//        String out = mainClass.output.replace ("\n", " ").trim ();
//        assertEquals (expected.replace (" ", ""), out.replace (" ", ""));
//    }
//    @Test (timeout = 1000) public void test6 () throws Exception {
//        Digits00 mainClass = new Digits00();
//        String expected =
//            "Enter an integer >  6 6 6 6 6 6 6 That's all, have a nice day!";
//        mainClass.scanner = new java.util.Scanner ("6666666");
//        mainClass.exec ();
//        String out = mainClass.output.replace ("\n", " ").trim ();
//        assertEquals (expected.replace (" ", ""), out.replace (" ", ""));
//    }
}
