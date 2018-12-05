package GP;

import General.CompiledClassLoader;
import junit.framework.TestResult;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.out;

public class TestFittestSelection extends TestResult {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(2);
    public static int numberOfTests = 2;

    public static Class gpClass = null;
    public static Method[] allMethods = null;
    public static Method gpMethod = null;
    public static String testedMethodName = "getFittest";

    @BeforeClass
    public static void setupEnvironment() {
        //Get access to a recompiled class during the runtime
        gpClass = GeneticOperations.class;
        allMethods = CompiledClassLoader.getRecompiledMethods(gpClass);

        for (Method m : allMethods) {
            String methodName = m.getName();
            if (methodName.startsWith(testedMethodName)) {
                m.setAccessible(true);
                gpMethod = m;
            }
        }
    }

    @Test
    public void testFittestSelection1() {
        try {
            Patch p1 = new Patch(1,22,5);
            Patch p2 = new Patch(0, 23, 5);
            Individual in1 = new Individual(Arrays.asList(p1),10.5);
            Individual in2 = new Individual(Arrays.asList(p2),20.5);

            out.format("Invoking %s()%n", testedMethodName, " from testFittestSelection1...");
            Object o = gpMethod.invoke(null, in1, in2);
            Assert.assertNotNull(in1);
            Assert.assertNotNull(in2);
            Assert.assertEquals(in2, o);
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
