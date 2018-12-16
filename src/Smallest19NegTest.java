import org.junit.Test;
import junit.framework.TestResult;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.lang.System.out;
import static org.junit.Assert.assertEquals;

public class Smallest19NegTest extends TestResult {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(2);
    public static int numberOfNegativeTests = 1;

    public static Class testClass = null;
    public static Method[] allMethods = null;
    public static Method testMethod = null;
    public static String testedMethodName = "exec";

    @BeforeClass
    public static void setupEnvironment() {
        //Get access to a recompiled class during the runtime
        testClass = Smallest19.class;
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
            String expected = "Please enter 4 numbers separated by spaces > 0 is the smallest";
            Object o = testMethod.invoke(null, 0, 0, 0, 0);
            Assert.assertEquals(expected.replace(" ", ""),
                    o.toString().replace(" ", "").trim());
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Test (timeout = 1000) public void test2 () throws Exception {
//        Smallest19 mainClass = new Smallest19 ();
//        String expected =
//            "Please enter 4 numbers separated by spaces > 0 is the smallest";
//        mainClass.scanner = new java.util.Scanner ("0 0 1 2");
//
//        Object o = testMethod.invoke(null, 1108, 1,1);
//        Assert.assertEquals ("INVALID",o.toString());
//
//        mainClass.exec ();
//        String out = mainClass.output.replace ("\n", " ").trim ();
//        assertEquals (expected.replace (" ", ""), out.replace (" ", ""));
//    }
//    @Test (timeout = 1000) public void test3 () throws Exception {
//        Smallest19 mainClass = new Smallest19 ();
//        String expected =
//            "Please enter 4 numbers separated by spaces > 0 is the smallest";
//        mainClass.scanner = new java.util.Scanner ("0 0 1 0");
//        mainClass.exec ();
//        String out = mainClass.output.replace ("\n", " ").trim ();
//        assertEquals (expected.replace (" ", ""), out.replace (" ", ""));
//    }
//    @Test (timeout = 1000) public void test4 () throws Exception {
//        Smallest19 mainClass = new Smallest19 ();
//        String expected =
//            "Please enter 4 numbers separated by spaces > 0 is the smallest";
//        mainClass.scanner = new java.util.Scanner ("0 0 3 1");
//        mainClass.exec ();
//        String out = mainClass.output.replace ("\n", " ").trim ();
//        assertEquals (expected.replace (" ", ""), out.replace (" ", ""));
//    }
//    @Test (timeout = 1000) public void test5 () throws Exception {
//        Smallest19 mainClass = new Smallest19 ();
//        String expected =
//            "Please enter 4 numbers separated by spaces > 0 is the smallest";
//        mainClass.scanner = new java.util.Scanner ("0 1 0 0");
//        mainClass.exec ();
//        String out = mainClass.output.replace ("\n", " ").trim ();
//        assertEquals (expected.replace (" ", ""), out.replace (" ", ""));
//    }
//    @Test (timeout = 1000) public void test6 () throws Exception {
//        Smallest19 mainClass = new Smallest19 ();
//        String expected =
//            "Please enter 4 numbers separated by spaces > 0 is the smallest";
//        mainClass.scanner = new java.util.Scanner ("0 1 1 1");
//        mainClass.exec ();
//        String out = mainClass.output.replace ("\n", " ").trim ();
//        assertEquals (expected.replace (" ", ""), out.replace (" ", ""));
//    }
//    @Test (timeout = 1000) public void test7 () throws Exception {
//        Smallest19 mainClass = new Smallest19 ();
//        String expected =
//            "Please enter 4 numbers separated by spaces > 0 is the smallest";
//        mainClass.scanner = new java.util.Scanner ("0 1 1 0");
//        mainClass.exec ();
//        String out = mainClass.output.replace ("\n", " ").trim ();
//        assertEquals (expected.replace (" ", ""), out.replace (" ", ""));
//    }
//    @Test (timeout = 1000) public void test8 () throws Exception {
//        Smallest19 mainClass = new Smallest19 ();
//        String expected =
//            "Please enter 4 numbers separated by spaces > 0 is the smallest";
//        mainClass.scanner = new java.util.Scanner ("0 1 3 1");
//        mainClass.exec ();
//        String out = mainClass.output.replace ("\n", " ").trim ();
//        assertEquals (expected.replace (" ", ""), out.replace (" ", ""));
//    }
}
