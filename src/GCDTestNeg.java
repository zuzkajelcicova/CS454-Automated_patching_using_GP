import junit.framework.TestResult;
import org.junit.Assert;
import org.junit.Rule;

import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.Timeout;

import static java.lang.System.out;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class GCDTestNeg extends TestResult {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);
    @Rule
    public TestName name = new TestName();

    @Test
    public void testGCDNegative1() {
        Class<?> myClass = GCD.class;

        System.out.printf("my class is Class@%x%n", myClass.hashCode());
        System.out.println("reloading");
        URL[] urls = {myClass.getProtectionDomain().getCodeSource().getLocation()};
        ClassLoader delegateParent = myClass.getClassLoader().getParent();
        try (URLClassLoader cl = new URLClassLoader(urls, delegateParent)) {
            Class<?> reloaded = cl.loadClass(myClass.getName());

            Method[] allGCDMethods = reloaded.getDeclaredMethods();
            for (Method m : allGCDMethods) {
                String methodName = m.getName();
                if (methodName.startsWith("gcd")) {
                    out.format("invoking %s()%n", methodName);

                    m.setAccessible(true);
                    Object o = m.invoke(null, 0, 20);
                    //Perform the test itself
                    Assert.assertEquals(20, o);

                    out.format("%s() returned %b%n", methodName, o);
                    // Handle any exceptions thrown by method to be invoked.
                }
            }
            System.out.printf("reloaded my class: Class@%x%n", reloaded.hashCode());
            System.out.println("Different classes: " + (myClass != reloaded));

        } catch (Exception e) {
            System.out.println("FCK");
        }
    }
}
