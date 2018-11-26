package replace0;

/**
 * Check for specific characters 
 * replace line 22
 *
 */
public class Replace 
{
	public static void main(String[] args) {
		System.out.print("Check if some characters are inside specific string");
	}
	
	public String stringOutput(String testString){
		String result = "";
		switch (testString) {
		  case "a":
			  result = testString(testString); break;
		  case "b":
			  result = testString(testString); break;
		  case "c":
			  result = testString("2"); break;
		  case "d":
			  result = testString(testString); break;
		  default:
			  result = "else"; break;
		}
		return result;
	}
	
	public static String testString(String testString){
		System.out.print(" " + testString + "\n");
		return testString;
		
	}
	
		
}
