package gzoltarCli;

import java.io.IOException;
import java.io.InputStream;
import java.lang.Runtime;

public class Main {
	
	public static void main(String [ ] args)
	{		
		
		
		System.out.println(System.getProperty("user.dir"));
		
		// Set the projectName
		String projectName = "chrysler";
		
		// set the package name
		String packageName = "daimler";
		
		// set the path to classes and test-classes of the desired application, 
		// no need of changing that, since it shouldn't change
		
		try {
			
			Process p = Runtime.getRuntime().exec("java -jar ../com.gzoltar-0.0.11-jar-with-dependencies.jar"
					+ " ../fl-test_code/chrysler daimler target/classes/:target/test-classes");
			
			
			p.waitFor();

            InputStream is = p.getInputStream();

            byte b[] = new byte[is.available()];
            is.read(b, 0, b.length); // probably try b.length-1 or -2 to remove "new-line(s)"

//            String s = new String(b);
            System.out.println(new String(b));
		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
