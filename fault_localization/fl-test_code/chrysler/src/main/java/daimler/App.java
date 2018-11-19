package daimler;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.print("Hello World");
//		System.out.print(b);
	}
	
	public String giffString(String a){
		if (a.contains("a")){
			return a;
		}else if (a.contains("b")) {
			return a;
		}else if (a.contains("c")) {
			return a;
		}else {
			return "x";
		}
	}
	
	public String giffElse(){
		String a = "asdad";
		
		return "a";
	}
}
