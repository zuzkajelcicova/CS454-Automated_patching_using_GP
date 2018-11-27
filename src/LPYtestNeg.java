import General.CompiledClassLoader;
import junit.framework.TestResult;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;

import org.junit.Test;
import org.junit.rules.Timeout;

import static java.lang.System.out;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LPYtestNeg extends TestResult {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(2);
    public static int numberOfNegativeTests = 1;

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
    public void testLPYNegative1() {
        try {
            out.format("Invoking %s()%n", testedMethodName, " from testLPYNegative1...");
            Object o = lpyMethod.invoke(null, 2006);
            Assert.assertEquals(2007, o);
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
