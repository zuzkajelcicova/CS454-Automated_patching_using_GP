package gzoltarCli;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.Runtime;

public class Main {
	
	public static void main(String [ ] args)
	{		
		
		try {
			// set the path to classes and test-classes of the desired application
			Process p = Runtime.getRuntime().exec("java -jar C:/Users/Asdf/Eclipse/workspace/chrysler/com.gzoltar-0.0.11-jar-with-dependencies.jar"
					+ " C:/Users/Asdf/Eclipse/workspace/chrysler daimler target/classes/:target/test-classes");
			
			p.waitFor();

            InputStream is = p.getInputStream();

            byte inputStreamByte[] = new byte[is.available()];
            is.read(inputStreamByte, 0, inputStreamByte.length); 
            
            String inputString = new String(inputStreamByte);

            System.out.println(inputString);
            
            try (PrintWriter out2 = new PrintWriter("outputFile.csv")) {
                out2.println(inputString);
            }
		} catch (IOException e) {
			e.printStackTrace();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
