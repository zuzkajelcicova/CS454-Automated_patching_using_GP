import junit.framework.TestCase;
import junit.framework.TestResult;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.lang.System.out;


public class TestTriNeg extends TestResult {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(200);
    public static int numberOfNegativeTests = 6;

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
            out.format("Invoking %s()%n", testedMethodName, " from test1...");
            Object o = triMethod.invoke(null, 0, 1301,1);
            Assert.assertEquals("INVALID",o.toString());
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test2() {
        try {
            out.format("Invoking %s()%n", testedMethodName, " from test2...");
            Object o = triMethod.invoke(null, 1, 0,-665);
            Assert.assertEquals ("INVALID",o.toString());
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test3() {
        try {
            out.format("Invoking %s()%n", testedMethodName, " from test3...");
            Object o = triMethod.invoke(null, 1, 1,0);
            Assert.assertEquals ("INVALID",o.toString());
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test4() {
        try {
            out.format("Invoking %s()%n", testedMethodName, " from test4...");
            Object o = triMethod.invoke(null, 0, 0,0);
            Assert.assertEquals ("INVALID",o.toString());
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test5() {
        try {
            out.format("Invoking %s()%n", testedMethodName, " from test5...");
            Object o = triMethod.invoke(null, -1, 1,1);
            Assert.assertEquals ("INVALID",o.toString());
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test6() {
        try {
            out.format("Invoking %s()%n", testedMethodName, " from test6...");
            Object o = triMethod.invoke(null, 1, -1,1);
            Assert.assertEquals ("INVALID",o.toString());
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
//
//    @Test
//   public void test6() {
//        assertEquals (Triangle.classify(1,1088,15), Triangle.Type.INVALID);
//   }
//    @Test
//   public void test7() {
//        assertEquals (Triangle.classify(1,2,450), Triangle.Type.INVALID);
//   }
//
//    @Test
//   public void test9() {
//        assertEquals (Triangle.classify(1187,1146,1), Triangle.Type.INVALID);
//   }
//
//    @Test
//   public void test11() {
//        assertEquals (Triangle.classify(784,784,1956), Triangle.Type.INVALID);
//   }
//    @Test
//   public void test12() {
//        assertEquals (Triangle.classify(1,450,1), Triangle.Type.INVALID);
//   }
//    @Test
//   public void test13() {
//        assertEquals (Triangle.classify(1146,1,1146), Triangle.Type.ISOSCELES);
//   }
//    @Test
//   public void test14() {
//        assertEquals (Triangle.classify(1640,1956,1956), Triangle.Type.ISOSCELES);
//   }

//    @Test
//   public void test17() {
//        assertEquals (Triangle.classify(1,2,3), Triangle.Type.INVALID);
//   }
//    @Test
//   public void test18() {
//        assertEquals (Triangle.classify(2,3,1), Triangle.Type.INVALID);
//   }
//    @Test
//   public void test19() {
//        assertEquals (Triangle.classify(3,1,2), Triangle.Type.INVALID);
//   }
//    @Test
//   public void test20() {
//        assertEquals (Triangle.classify(1,1,2), Triangle.Type.INVALID);
//   }
//    @Test
//   public void test21() {
//        assertEquals (Triangle.classify(1,2,1), Triangle.Type.INVALID);
//   }
//    @Test
//   public void test22() {
//        assertEquals (Triangle.classify(2,1,1), Triangle.Type.INVALID);
//   }
//
//    @Test
//   public void test24() {
//        assertEquals (Triangle.classify(0,1,1), Triangle.Type.INVALID);
//   }
//    @Test
//   public void test25() {
//        assertEquals (Triangle.classify(1,0,1), Triangle.Type.INVALID);
//   }
//    @Test
//   public void test26() {
//        assertEquals (Triangle.classify(1,2,-1), Triangle.Type.INVALID);
//   }
//    @Test
//   public void test27() {
//        assertEquals (Triangle.classify(1,1,-1), Triangle.Type.INVALID);
//   }

//    @Test
//   public void test29() {
//        assertEquals (Triangle.classify(3,2,5), Triangle.Type.INVALID);
//   }
//    @Test
//   public void test30() {
//        assertEquals (Triangle.classify(5,9,2), Triangle.Type.INVALID);
//   }
//    @Test
//   public void test31() {
//        assertEquals (Triangle.classify(7,4,3), Triangle.Type.INVALID);
//   }
//    @Test
//   public void test32() {
//        assertEquals (Triangle.classify(3,8,3), Triangle.Type.INVALID);
//   }
//    @Test
//   public void test33() {
//        assertEquals (Triangle.classify(7,3,3), Triangle.Type.INVALID);
//   }
}
