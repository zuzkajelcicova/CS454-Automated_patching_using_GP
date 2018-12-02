
public class Digits5000 {

    public static String exec (int x ) throws Exception {
        String output = "";
        int y, i, step1 = 10, step2 = 100, num, max = 0;

        output += (String.format("Enter an integer >  "));
        if (x < 0) {
            output += (String.format("%d\n", x % 10));
            x = x * -1;
        } else {
            output += (String.format("%d\n", x % 10));
        }
        y = x;
        while (y >= 10) {   //gzoltar: 1,0
            y /= 10;        //gzoltar: 1,0
            max++;          //gzoltar: 1,0
        }
        for (i = 0; i < max; i++) {
            num =
                    ((x % step2 - x % step1) / step1);
            output += (String.format("%d\n", num));
            step2 *= 10;
            step1 *= 10;
        }
        output += (String.format("That's all, have a nice day!\n"));
        return output;
    }
}
