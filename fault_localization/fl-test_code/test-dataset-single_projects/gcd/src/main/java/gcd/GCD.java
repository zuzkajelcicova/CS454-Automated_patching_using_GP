package main.java.gcd;

public class GCD {

    public static void main(String[] args) {
        gcd(6, 3);
    }

    public static int gcd(int a, int b) {
        int gcd = b;

        if (a == 0) {
            System.out.println("GCD: " + gcd + "\n");
        }
        while (b != 0) {
            if (a > b) {
                a = a - b;
            } else {
                gcd = b;
                b = b - a;
            }
        }
        System.out.printf("GCD: " + gcd + "\n");
        return gcd;
    }
}