import General.CompiledClassLoader;
import junit.framework.TestResult;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static java.lang.System.out;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Unit test for simple App.
 */
public class CompareTest extends TestResult
{	
	
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
	
	// Pass, Positive
	public void test0() {
        try {
            out.format("Invoking %s()%n", testedMethodName, " from testGCDPositive1...");
            Object o = gcdMethod.invoke(null, "a");
            Assert.assertEquals("a", o);
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
	}
	
	// Fail, Positive
	public void test1() {
        try {
            out.format("Invoking %s()%n", testedMethodName, " from testGCDPositive1...");
            Object o = gcdMethod.invoke(null, "b");
            Assert.assertEquals("b", o);
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
	}
	
	// Pass, Positive
	public void test2() {
        try {
            out.format("Invoking %s()%n", testedMethodName, " from testGCDPositive1...");
            Object o = gcdMethod.invoke(null, "c");
            Assert.assertEquals("c", o);
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
	}
	
	// Pass, Negative 
	public void test4() {
        try {
            out.format("Invoking %s()%n", testedMethodName, " from testGCDPositive1...");
            Object o = gcdMethod.invoke(null, "a");
            Assert.assertNotSame("x", o);
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
	}
	
	// Pass, Negative
	public void test5() {
        try {
            out.format("Invoking %s()%n", testedMethodName, " from testGCDPositive1...");
            Object o = gcdMethod.invoke(null, "x");
            Assert.assertEquals("x", o);
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
	}
}
