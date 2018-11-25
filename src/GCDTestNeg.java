import General.Utils;
import junit.framework.TestResult;
import org.junit.Assert;
import org.junit.Rule;

import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.Timeout;

import static java.lang.System.out;
import static java.lang.System.err;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class GCDTestNeg extends TestResult {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);
    @Rule
    public TestName name = new TestName();
    //@Rule
    //public final ExpectedException exception = ExpectedException.none();

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
            //Assert.assertEquals(20, myClass.gcd(0, 20));
            // Assert.assertEquals("testGCDNegative1", name.getMethodName());

        } catch (Exception e) {
            System.out.println("FCK");
        }







        /*MethodHandles.Lookup lookup = MethodHandles.lookup();
        // If we have already compiled our class, simply load it
        Class className = null;
        try {
            className = lookup.lookupClass().getClassLoader().loadClass(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Method[] allGCDMethods = className.getDeclaredMethods();
        for (Method m : allGCDMethods) {
            String methodName = m.getName();
            if (methodName.startsWith("gcd")) {
                out.format("invoking %s()%n", methodName);

                try {
                    m.setAccessible(true);
                    Object o = null;
                    try {
                        o = m.invoke(null, 0, 20);
                        //Perform the test itself
                        Assert.assertEquals(20, o);

                        out.format("%s() returned %b%n", methodName, o);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    // Handle any exceptions thrown by method to be invoked.
                } catch (InvocationTargetException x) {
                    Throwable cause = x.getCause();
                    err.format("invocation of %s failed: %s%n",
                            methodName, cause.getMessage());
                }
            }*/


        //SOLUTION 3
        /*try {
            String source = new String(Files.readAllBytes(Paths.get("C:\\Users\\admin\\git\\CS454-Automated_patching_using_GP\\GAOutput\\GCD.java")));
            //String source = new String(Files.readAllBytes(Paths.get("C:\\Users\\admin\\git\\CS454-Automated_patching_using_GP\\resources\\GCD.java")));
            File sourceFile = new File("C:\\Users\\admin\\git\\CS454-Automated_patching_using_GP\\src\\GCD.java");
            sourceFile.getParentFile().mkdirs();
            Files.write(sourceFile.toPath(), source.getBytes(StandardCharsets.UTF_8));

            // Compile source file.
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
            fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(new File("C:\\Users\\admin\\git\\CS454-Automated_patching_using_GP\\out\\production\\CS454_AutomatedPatching\\")));
            compiler.run(null, null, null, sourceFile.getPath());

            //todo: Naive, will this work?
            Assert.assertEquals(20, GCD.gcd(0, 20));
            Assert.assertEquals("testGCDNegative1", name.getMethodName());*/
            /*Method gcdMethod = GCD.class.getMethod("gcd", int.class, int.class);
            int result = ((Integer) gcdMethod.invoke(null, 0, 20)).intValue();

            Assert.assertEquals(20, result);**/

        // Load and instantiate compiled class.
           /* URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{root.toURI().toURL()});
            Class<?> cls = Class.forName("test.Test", true, classLoader); // Should print "hello".
            Object instance = cls.newInstance(); // Should print "world".
            System.out.println(instance); // Should print "test.Test@hashcode".*/
        /*} catch (Exception e) {
            e.printStackTrace();
        }*/
        //SOLUTION 2
        /*try {
            Method  gcdMethod = GCD.class.getMethod("gcd", int.class, int.class);
            int result = ((Integer) gcdMethod.invoke(null, 0, 20)).intValue();

            Assert.assertEquals(20, result);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        //SOLUTION 1
        /*
        Class gcdClass = GCD.class;
        Method[] allGCDMethods = gcdClass.getDeclaredMethods();
        for (Method m : allGCDMethods) {
            String methodName = m.getName();
            if (methodName.startsWith("gcd")) {
                out.format("invoking %s()%n", methodName);

                try {
                    m.setAccessible(true);
                    Object o = null;
                    try {
                        o = m.invoke(null, 0, 20);
                        //Perform the test itself
                        Assert.assertEquals(20, o);

                        out.format("%s() returned %b%n", methodName, o);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    // Handle any exceptions thrown by method to be invoked.
                } catch (InvocationTargetException x) {
                    Throwable cause = x.getCause();
                    err.format("invocation of %s failed: %s%n",
                            methodName, cause.getMessage());
                }
            }
        }*/


        //Assert.assertEquals("testGCDNegative1", name.getMethodName());
        //int gcdResult = GCD.gcd(0, 20);
        //int gcdResult = GCD.class.getMethod("gcd", int.class, int.class);
        //Assert.assertEquals(20, gcdResult);
    }
}
