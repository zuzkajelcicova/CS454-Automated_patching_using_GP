package fibonacci;

public class Fibonacci {
	
	public static void main(String[] args) {
		int n = Integer.parseInt("45");
		System.out.println(computeFirst(n));
		
        for (int i = 1; i <= n; i++)
        	System.out.println(i + ": " + fibonacci(i));
    
	}
	
    public static int computeFirst(int n) {
    	int result = 0;
    	
        if (n <= 1) { 
        	result = n; 
        } else { 
        	result = computeFirst(n - 1) + computeFirst(n - 2); 
        }
        
        return result;
    }
    public static long fibonacci(int n) {
        if (n <= 1) return n;
        else return fibonacci(n-1) + fibonacci(n-2);
    }
    
}
