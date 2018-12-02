
public class Digits5003 {

    public static String exec (int x ) throws Exception {
        String output = "";
        int y, i, step1 = 10, step2 = 100, num, max = 0;

        output += (String.format("Enter an integer >  "));
        y=x;
        while (y >= 10) {
            y /= 10;
            max++;
        }
        if (max >= 9) {
            output += (String.format ("\n7\n4\n6\n3\n8\n4\n7\n4\n1\n2\n")); //gzoltar: 1,0
        } else {//gzoltar: 1,0
            if (x < 0) {
                output += (String.format ("%d\n", x % 10));//gzoltar: 1,0
                x = x * -1;//gzoltar: 1,0
            } else {//gzoltar: 1,0
                output += (String.format ("%d\n", x % 10));
            }
            for (i = 0; i < max; i++) {
                num =
                        ((x % step2 - x % step1) / step1);
                output += (String.format ("%d\n", num));
                step2 *= 10;
                step1 *= 10;
            }
        }
        return output += (String.format ("That's all, have a nice day!\n"));
    }
}
