import junit.framework.TestResult;
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


    @BeforeClass
    public static void setupEnvironment() {
        //Get access to a recompiled class during the runtime
        gcdClass = GCD.class;
        allMethods = CompiledClassLoader.getRecompiledMethods(gcdClass);

        for (Method m : allMethods) {
            String methodName = m.getName();
            if (methodName.startsWith(testedMethodName)) {
                m.setAccessible(true);
                gcdMethod = m;
            }
        }
    }


    @Test
    public void testGCDPositive1() {
        try {
            out.format("Invoking %s()%n", testedMethodName, " from testGCDPositive1...");
            Object o = gcdMethod.invoke(null, 72, 16);
            Assert.assertEquals(8, o);
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGCDPositive2() {
        try {
            out.format("Invoking %s()%n", testedMethodName, " from testGCDPositive2...");
            Object o = gcdMethod.invoke(null, 461952, 116298);
            Assert.assertEquals(18, o);
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGCDPositive3() {
        try {
            out.format("Invoking %s()%n", testedMethodName, " from testGCDPositive3...");
            Object o = gcdMethod.invoke(null, 42, 56);
            Assert.assertEquals(14, o);
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGCDPositive4() {
        try {
            out.format("Invoking %s()%n", testedMethodName, " from testGCDPositive4...");
            Object o = gcdMethod.invoke(null, 13, 13);
            Assert.assertEquals(13, o);
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGCDPositive5() {
        try {
            out.format("Invoking %s()%n", testedMethodName, " from testGCDPositive5...");
            Object o = gcdMethod.invoke(null, 37, 600);
            Assert.assertEquals(1, o);
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGCDPositive6() {
        try {
            out.format("Invoking %s()%n", testedMethodName, " from testGCDPositive6...");
            Object o = gcdMethod.invoke(null, 20, 100);
            Assert.assertEquals(20, o);
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}