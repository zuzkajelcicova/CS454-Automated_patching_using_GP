public class GCD {

    public static void main(String[] args) {

        int a = 32;
        int b = 72;

        while (a != 0 && b != 0) {
            int c = b;
            b = a % b;
            a = c;
        }
        return a + b;
    }
}