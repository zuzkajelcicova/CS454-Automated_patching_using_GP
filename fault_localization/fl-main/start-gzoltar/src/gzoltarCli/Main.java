package gzoltarCli;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.Runtime;

public class Main {
	
	public static void main(String [ ] args)
	{		
		
		try {
			System.out.println(System.getProperty("user.dir"));
			// TODO: project path and name as inout for jar file
			// set the path to classes and test-classes of the desired application
//			String gzoltarFile = "./com.gzoltar-0.0.11-jar-with-dependencies.jar";
//			String testProjectPath = "C:/Eigene_Programme/Git-Data/Own_Repositories/AISE/fault_localization/fl-test_code/chrysler";
//			String testProjectName = "daimler";
//			String outputFilePath = "gzoltar.csv";
			
			System.out.println(args[0]);
			System.out.println(args[1]);
			System.out.println(args[2]);
			System.out.println(args[3]);
			
			String gzoltarFile = args[0];
			String testProjectPath = args[1];
			String testProjectName = args[2];
			String outputFilePath = args[3];
			String classesPath = "target/classes/";
			String classesTestPath = "target/test-classes/";

			
			// start java process
			Process startGzoltar = Runtime.getRuntime().exec("java -jar"
					+ " " + gzoltarFile
					+ " " + testProjectPath 
					+ " " + testProjectName 
					+ " " + classesPath
					+ ":" + classesTestPath
					);
			
			// catch jar file output
			startGzoltar.waitFor();
            InputStream is = startGzoltar.getInputStream();
            byte inputStreamByte[] = new byte[is.available()];
            is.read(inputStreamByte, 0, inputStreamByte.length); 
            String inputString = new String(inputStreamByte);

            System.out.println(inputString);
            
            // print jar file output to csv file
            try (PrintWriter out = new PrintWriter(outputFilePath)) {
                out.println(inputString);
            }
		} catch (IOException e) {
			e.printStackTrace();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
