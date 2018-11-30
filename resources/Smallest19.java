//LC:1
//LC:2
/*//LC:3
 * Print out smallest integer from database//LC:4
 *///LC:5

public class Smallest19 {
    public java.util.Scanner scanner;
    public String output = "";

    public static String exec(int a, int b, int c, int d) {
        String output = "";
        output += (String.format("Please enter 4 numbers separated by spaces > "));
        if (a < b && a < c && a < d) {
            return output += (String.format("%d is the smallest\n", a));
        } else if (b < a && b < c && b < d) {
            return output += (String.format("%d is the smallest\n", b));
        } else if (c < a && c < b && c < d) {
            return output += (String.format("%d is the smallest\n", c));
        } else if (d < a && d < c && d < b) {
            return output += (String.format("%d is the smallest\n", d));
        } else {
            return output += (String.format("I don't know what I'm doing. \n"));
        }
    }
}
