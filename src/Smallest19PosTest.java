import General.CompiledClassLoader;
import junit.framework.TestResult;
import org.junit.Test;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

public class Smallest19PosTest extends TestResult {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);
    public static int numberOfPositiveTests = 2;

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
            String expected = "Please enter 4 numbers separated by spaces > 1 is the smallest";
            Object o = testMethod.invoke(null, 1, 2, 3, 4);
            Assert.assertEquals(expected.replace(" ", ""),
                    o.toString().replace(" ", "").trim());
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
            String expected = "Please enter 4 numbers separated by spaces > 1 is the smallest";
            Object o = testMethod.invoke(null, 4, 3, 2, 1);
            Assert.assertEquals(expected.replace(" ", ""),
                    o.toString().replace(" ", "").trim());
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
//            "Please enter 4 numbers separated by spaces > 1 is the smallest";
//        mainClass.scanner = new java.util.Scanner ("4 3 2 1");
//        mainClass.exec ();
//        String out = mainClass.output.replace ("\n", " ").trim ();
//        assertEquals (expected.replace (" ", ""), out.replace (" ", ""));
//    }
//    @Test (timeout = 1000) public void test3 () throws Exception {
//        Smallest19 mainClass = new Smallest19 ();
//        String expected =
//            "Please enter 4 numbers separated by spaces > 1 is the smallest";
//        mainClass.scanner = new java.util.Scanner ("3 4 2 1");
//        mainClass.exec ();
//        String out = mainClass.output.replace ("\n", " ").trim ();
//        assertEquals (expected.replace (" ", ""), out.replace (" ", ""));
//    }
//    @Test (timeout = 1000) public void test4 () throws Exception {
//        Smallest19 mainClass = new Smallest19 ();
//        String expected =
//            "Please enter 4 numbers separated by spaces > 1 is the smallest";
//        mainClass.scanner = new java.util.Scanner ("3 2 4 1");
//        mainClass.exec ();
//        String out = mainClass.output.replace ("\n", " ").trim ();
//        assertEquals (expected.replace (" ", ""), out.replace (" ", ""));
//    }
//    @Test (timeout = 1000) public void test5 () throws Exception {
//        Smallest19 mainClass = new Smallest19 ();
//        String expected =
//            "Please enter 4 numbers separated by spaces > 1 is the smallest";
//        mainClass.scanner = new java.util.Scanner ("1 1 1 1");
//        mainClass.exec ();
//        String out = mainClass.output.replace ("\n", " ").trim ();
//        assertEquals (expected.replace (" ", ""), out.replace (" ", ""));
//    }
//    @Test (timeout = 1000) public void test6 () throws Exception {
//        Smallest19 mainClass = new Smallest19 ();
//        String expected =
//            "Please enter 4 numbers separated by spaces > 2 is the smallest";
//        mainClass.scanner = new java.util.Scanner ("2 2 2 3");
//        mainClass.exec ();
//        String out = mainClass.output.replace ("\n", " ").trim ();
//        assertEquals (expected.replace (" ", ""), out.replace (" ", ""));
//    }
//    @Test (timeout = 1000) public void test7 () throws Exception {
//        Smallest19 mainClass = new Smallest19 ();
//        String expected =
//            "Please enter 4 numbers separated by spaces > -1 is the smallest";
//        mainClass.scanner = new java.util.Scanner ("0 0 0 -1");
//        mainClass.exec ();
//        String out = mainClass.output.replace ("\n", " ").trim ();
//        assertEquals (expected.replace (" ", ""), out.replace (" ", ""));
//    }
//    @Test (timeout = 1000) public void test8 () throws Exception {
//        Smallest19 mainClass = new Smallest19 ();
//        String expected =
//            "Please enter 4 numbers separated by spaces > -1 is the smallest";
//        mainClass.scanner = new java.util.Scanner ("0 -1 0 0");
//        mainClass.exec ();
//        String out = mainClass.output.replace ("\n", " ").trim ();
//        assertEquals (expected.replace (" ", ""), out.replace (" ", ""));
//    }
}
