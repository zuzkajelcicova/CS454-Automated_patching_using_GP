
/**
 * Check for specific characters 
 *
 */
public class Compare0 
{

	public String stringOutput(String testString){
		String stringA = new String("a");
		String stringB = new String("b");
		String stringC = new String("c");
		System.out.println(testString);
		if (testString.contains(stringA)){
			return testString;
		}else if (testString == stringB) {
			return testString;
		}else if (testString.contains(stringC)) {
			return testString;
		}else {
			return "x";
		}
	}
	
	public String stringTest (String testString){
		String stringA = new String("a");
		String stringB = new String("b");
		String stringC = new String("c");
		System.out.println(testString);
		if (testString.contains(stringA)){
			return testString;
		}else if (testString.contains(stringB)) {
			return testString;
		}else if (testString.contains(stringC)) {
			return testString;
		}else {
			return "x";
		}
	}
}
