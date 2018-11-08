package gzoltarEclipse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {

	public static void main(String[] args) {
		
		// Define path to "(...)/gzoltar-data/spectra"
		String filePath = "";
		Main main = new Main();
		main.getLineSusp(filePath);
		
		// Print result
		// System.out.println(mainy.firstTry(filePath).toString());
	}
	
	public List<List<String>> getLineSusp(String filePath){

		File inputFile = new File(filePath);
		
		List<List<String>> lineSuspeciousness = new ArrayList<>();
		
		try(BufferedReader in = new BufferedReader(new FileReader(filePath))) {
		    String str = in.readLine();
		    
		    // Extract lines and suspeciousness value from file
		    while ((str = in.readLine()) != null) {
		        String[] tokens = str.split("#");
		        String[] values = tokens[1].split(",");
		        List<String> listy = Arrays.asList(new String[] {values[0], ("0." + values[2]).substring(0, 6)});
		        lineSuspeciousness.add(listy);
		    }
		}
		catch (IOException e) {
		    System.out.println("File Read Error");
		}
		return lineSuspeciousness;
	}
	

}
