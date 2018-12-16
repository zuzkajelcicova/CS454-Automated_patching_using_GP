package test.java.gcd;

import junit.framework.TestResult;
import main.java.gcd.GCD;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.lang.System.out;


public class GCDTestPos extends TestResult {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(2);
    public static int numberOfPositiveTests = 6;

    public static Class gcdClass = null;
    public static Method[] allMethods = null;
    public static Method gcdMethod = null;
    public static String testedMethodName = "gcd";


//    @BeforeClass
//    public static void setupEnvironment() {
//        //Get access to a recompiled class during the runtime
//        testClass = GCD.class;
//        allMethods = CompiledClassLoader.getRecompiledMethods(testClass);
//
//        for (Method m : allMethods) {
//            String methodName = m.getName();
//            if (methodName.startsWith(testedMethodName)) {
//                m.setAccessible(true);
//                testMethod = m;
//            }
//        }
//    }


    @Test
    public void testGCDPositive1() {
        out.format("Invoking %s()%n", testedMethodName, " from testGCDPositive1...");
//            Object o = testMethod.invoke(null, 72, 16);
		Assert.assertEquals(4, GCD.gcd(76, 16));
		out.format("%s() returned %b%n", testedMethodName, GCD.gcd(76, 16));
    }
//
    @Test
    public void testGCDPositive2() {
        out.format("Invoking %s()%n", testedMethodName, " from testGCDPositive2...");
//        Object o = testMethod.invoke(null, 461952, 116298);
        Assert.assertEquals(18, GCD.gcd(461952, 116298));
        out.format("%s() returned %b%n", testedMethodName, GCD.gcd(461952, 116298));

    }
//
//    @Test
//    public void testGCDPositive3() {
//        try {
//            out.format("Invoking %s()%n", testedMethodName, " from testGCDPositive3...");
//            Object o = testMethod.invoke(null, 42, 56);
//            Assert.assertEquals(14, o);
//            out.format("%s() returned %b%n", testedMethodName, o);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testGCDPositive4() {
//        try {
//            out.format("Invoking %s()%n", testedMethodName, " from testGCDPositive4...");
//            Object o = testMethod.invoke(null, 13, 13);
//            Assert.assertEquals(13, o);
//            out.format("%s() returned %b%n", testedMethodName, o);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testGCDPositive5() {
//        try {
//            out.format("Invoking %s()%n", testedMethodName, " from testGCDPositive5...");
//            Object o = testMethod.invoke(null, 37, 600);
//            Assert.assertEquals(1, o);
//            out.format("%s() returned %b%n", testedMethodName, o);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testGCDPositive6() {
//        try {
//            out.format("Invoking %s()%n", testedMethodName, " from testGCDPositive6...");
//            Object o = testMethod.invoke(null, 20, 100);
//            Assert.assertEquals(20, o);
//            out.format("%s() returned %b%n", testedMethodName, o);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//    }
}