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

public class Digits5003PosTest extends TestResult {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(2);
    public static int numberOfPositiveTests = 2;

    public static Class testClass = null;
    public static Method[] allMethods = null;
    public static Method testMethod = null;
    public static String testedMethodName = "exec";

    @BeforeClass
    public static void setupEnvironment() {
        //Get access to a recompiled class during the runtime
        testClass = Digits5003.class;
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

    @Test
    public void test2() {
        try {
            out.format("Invoking %s()%n", testedMethodName, " from test1...");
            String expected = "Enter an integer >  2 6 6 8 6 5 5 0 0 1 That's all, have a nice day!";
            Object o = testMethod.invoke(null, 1005568662);
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

//    @Test (timeout = 1000) public void test3 () throws Exception {
//        Digits00 mainClass = new Digits00();
//        String expected =
//            "Enter an integer >  0 9 6 That's all, have a nice day!";
//        mainClass.scanner = new java.util.Scanner ("690");
//        mainClass.exec ();
//        String out = mainClass.output.replace ("\n", " ").trim ();
//        assertEquals (expected.replace (" ", ""), out.replace (" ", ""));
//    }
//    @Test (timeout = 1000) public void test4 () throws Exception {
//        Digits00 mainClass = new Digits00();
//        String expected =
//            "Enter an integer >  0 2 5 3 That's all, have a nice day!";
//        mainClass.scanner = new java.util.Scanner ("3520");
//        mainClass.exec ();
//        String out = mainClass.output.replace ("\n", " ").trim ();
//        assertEquals (expected.replace (" ", ""), out.replace (" ", ""));
//    }
//    @Test (timeout = 1000) public void test5 () throws Exception {
//        Digits00 mainClass = new Digits00();
//        String expected =
//            "Enter an integer >  8 6 7 2 3 That's all, have a nice day!";
//        mainClass.scanner = new java.util.Scanner ("32768");
//        mainClass.exec ();
//        String out = mainClass.output.replace ("\n", " ").trim ();
//        assertEquals (expected.replace (" ", ""), out.replace (" ", ""));
//    }
//    @Test (timeout = 1000) public void test6 () throws Exception {
//        Digits00 mainClass = new Digits00();
//        String expected =
//            "Enter an integer >  0 0 0 2 1 5 That's all, have a nice day!";
//        mainClass.scanner = new java.util.Scanner ("512000");
//        mainClass.exec ();
//        String out = mainClass.output.replace ("\n", " ").trim ();
//        assertEquals (expected.replace (" ", ""), out.replace (" ", ""));
//    }
//    @Test (timeout = 1000) public void test7 () throws Exception {
//        Digits00 mainClass = new Digits00();
//        String expected =
//            "Enter an integer >  1 5 0 6 2 5 1 That's all, have a nice day!";
//        mainClass.scanner = new java.util.Scanner ("1526051");
//        mainClass.exec ();
//        String out = mainClass.output.replace ("\n", " ").trim ();
//        assertEquals (expected.replace (" ", ""), out.replace (" ", ""));
//    }
//    @Test (timeout = 1000) public void test8 () throws Exception {
//        Digits00 mainClass = new Digits00();
//        String expected =
//            "Enter an integer >  1 3 6 0 6 9 0 4 That's all, have a nice day!";
//        mainClass.scanner = new java.util.Scanner ("40960631");
//        mainClass.exec ();
//        String out = mainClass.output.replace ("\n", " ").trim ();
//        assertEquals (expected.replace (" ", ""), out.replace (" ", ""));
//    }
//    @Test (timeout = 1000) public void test9 () throws Exception {
//        Digits00 mainClass = new Digits00();
//        String expected =
//            "Enter an integer >  0 2 5 9 7 6 0 4 1 That's all, have a nice day!";
//        mainClass.scanner = new java.util.Scanner ("140679520");
//        mainClass.exec ();
//        String out = mainClass.output.replace ("\n", " ").trim ();
//        assertEquals (expected.replace (" ", ""), out.replace (" ", ""));
//    }
}
