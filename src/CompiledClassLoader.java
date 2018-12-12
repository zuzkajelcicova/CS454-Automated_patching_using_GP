package General;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class CompiledClassLoader {

    public static Method[] getRecompiledMethods(Class<?> classReference) {
        Class<?> recompiledClass = classReference;

        System.out.printf("Class@%x%n", recompiledClass.hashCode(), " reloading...");
        URL[] urls = {recompiledClass.getProtectionDomain().getCodeSource().getLocation()};
        ClassLoader delegateParent = recompiledClass.getClassLoader().getParent();
        try (URLClassLoader cl = new URLClassLoader(urls, delegateParent)) {
            Class<?> reloaded = cl.loadClass(recompiledClass.getName());

            Method[] allMethods = reloaded.getDeclaredMethods();
            return allMethods;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
