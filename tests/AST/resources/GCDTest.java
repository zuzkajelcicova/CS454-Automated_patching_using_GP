package AST.resources;

public class GCDTest {

    public static int gcd(int a, int b) {
        int gcd = b;

        if (a == 0) {
            System.out.println("GCD: " + gcd + "\n");
        }
        System.out.printf("GCD: " + gcd + "\n");
        return gcd;
    }
}