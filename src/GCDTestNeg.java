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

public class GCDTestNeg extends TestResult {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(2);
    public static int numberOfNegativeTests = 1;

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
    public void testGCDNegative1() {
        try {
            out.format("Invoking %s()%n", testedMethodName, " from testGCDPositive1...");
            Object o = gcdMethod.invoke(null, 0, 20);
            Assert.assertEquals(20, o);
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
