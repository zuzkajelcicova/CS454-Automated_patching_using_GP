import General.CompiledClassLoader;
import org.junit.BeforeClass;
import org.junit.rules.Timeout;
import junit.framework.TestResult;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ZuneTestPos extends TestResult {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(1);
    public static int numberOfPositiveTests = 3;

    public static Class zuneClass = null;
    public static Method[] allMethods = null;
    public static Method zuneMethod = null;
    public static String testedMethodName = "zune";


    @BeforeClass
    public static void setupEnvironment() {
        //Get access to a recompiled class during the runtime
        zuneClass = Zune.class;
        allMethods = CompiledClassLoader.getRecompiledMethods(zuneClass);

        for (Method m : allMethods) {
            String methodName = m.getName();
            if (methodName.startsWith(testedMethodName)) {
                m.setAccessible(true);
                zuneMethod = m;
            }
        }
    }

    @Test
    public void testZunePositive1() {
        try {
            Object o = zuneMethod.invoke(null, 1979, 365);
            Assert.assertEquals(1979, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testZunePositive2() {
        try {
            Object o = zuneMethod.invoke(null, 1980, 365);
            Assert.assertEquals(1980, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testZunePositive3() {
        try {
            Object o = zuneMethod.invoke(null, 1980, 366);
            Assert.assertEquals(1981, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
