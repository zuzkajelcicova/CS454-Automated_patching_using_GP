import General.CompiledClassLoader;
import junit.framework.TestResult;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class TestTriPos extends TestResult {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(2);
    public static int numberOfPositiveTests = 6;

    public static Class triClass = null;
    public static Method[] allMethods = null;
    public static Method triMethod = null;
    public static String testedMethodName = "classify";

    @BeforeClass
    public static void setupEnvironment() {
        //Get access to a recompiled class during the runtime
        triClass = Triangle.class;
        allMethods = CompiledClassLoader.getRecompiledMethods(triClass);

        for (Method m : allMethods) {
            String methodName = m.getName();
            if (methodName.startsWith(testedMethodName)) {
                m.setAccessible(true);
                triMethod = m;
            }
        }
    }

    @Test
    public void test1() {
        try {
            Object o = triMethod.invoke(null, 1108, 1,1);
            Assert.assertEquals ("INVALID",o.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test2() {
        try {
            Object o = triMethod.invoke(null, 582, 582,582);
            Assert.assertEquals ("EQUILATERAL",o.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void test3() {
        try {
            Object o = triMethod.invoke(null, 1663, 1088,823);
            Assert.assertEquals ("SCALENE",o.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test4() {
        try {
            Object o = triMethod.invoke(null, 1640, 1640,1956);
            Assert.assertEquals ("ISOSCELES",o.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test5() {
        try {
            Object o = triMethod.invoke(null, 1, 1,1);
            Assert.assertEquals ("EQUILATERAL",o.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test6() {
        try {
            Object o = triMethod.invoke(null, 1, 450,1);
            Assert.assertEquals ("INVALID",o.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
