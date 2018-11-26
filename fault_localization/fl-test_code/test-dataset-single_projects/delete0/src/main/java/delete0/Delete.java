package delete0;

/**
 * Print input 
 * delete line 15
 *
 */
public class Delete 
{
	public static void main(String[] args) {
		System.out.print("Print the input");
	}
	
	public String stringOutput(String a){
		a = a + a; // delete this line
        System.out.println(a);
		return a;
	}
}
