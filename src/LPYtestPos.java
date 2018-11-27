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


public class LPYtestPos extends TestResult {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(2);
    public static int numberOfPositiveTests = 3;

    public static Class lpyClass = null;
    public static Method[] allMethods = null;
    public static Method lpyMethod = null;
    public static String testedMethodName = "checkLeapYear";


    @BeforeClass
    public static void setupEnvironment() {
        //Get access to a recompiled class during the runtime
        lpyClass = LeapYear.class;
        allMethods = CompiledClassLoader.getRecompiledMethods(lpyClass);

        for (Method m : allMethods) {
            String methodName = m.getName();
            if (methodName.startsWith(testedMethodName)) {
                m.setAccessible(true);
                lpyMethod = m;
            }
        }
    }


    @Test
    public void testLPYPositive1() {
        try {
            out.format("Invoking %s()%n", testedMethodName, " from testLPYPositive1...");
            Object o = lpyMethod.invoke(null, 2004);
            Assert.assertEquals(2004, o);
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLPYPositive2() {
        try {
            out.format("Invoking %s()%n", testedMethodName, " from testLPYPositive2...");
            Object o = lpyMethod.invoke(null, 2008);
            Assert.assertEquals(2008, o);
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLPYPositive3() {
        try {
            out.format("Invoking %s()%n", testedMethodName, " from testLPYPositive3...");
            Object o = lpyMethod.invoke(null, 2012);
            Assert.assertEquals(2012, o);
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}