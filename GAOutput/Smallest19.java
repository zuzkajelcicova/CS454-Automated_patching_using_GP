//LC:1//LC:1
//LC:2//LC:2
/*//LC:3//LC:3
 * Print out smallest integer from database//LC:4//LC:4
 *///LC:5//LC:5
//LC:6
public class Smallest19 {//LC:7
    public java.util.Scanner scanner;//LC:8
    public String output = "";//LC:9
//LC:10
    public static String exec(int a, int b, int c, int d) {//LC:11
        String output = "";//LC:12
        output += (String.format("Please enter 4 numbers separated by spaces > "));//LC:13
        if (a < b && a < c && a < d) {//LC:14
            return output += (String.format("%d is the smallest\n", a));//LC:15
        } else if (b < a && b < c && b < d) {//LC:16
            return output += (String.format("%d is the smallest\n", b));//LC:17
        } else if (c < a && c < b && c < d) {//LC:18
            return output += (String.format("%d is the smallest\n", c));//LC:19
        } else if (d < a && d < c && d < b) {//LC:20
            return output += (String.format("%d is the smallest\n", d));//LC:21
        } else {//LC:22
            //LC:23
        }//LC:24
    }//LC:25
}//LC:26
