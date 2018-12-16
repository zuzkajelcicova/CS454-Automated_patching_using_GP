
public class Digits10000 {

    public static String exec (int num ) throws Exception {
        String output = "";

        output += (String.format("Enter an integer >  "));
        if (num < 0) {
            num = (Math.abs (num));
            while (num > 10) {
                output += (String.format ("%d\n", num % 10));
                num /= 10;
            }
            num = num - 2 * num;
            output += (String.format ("%d\n", num));
        } else {
            while (num != 0) { //gzoltar: 1,0
                output += (String.format ("%d\n", num % 10));
                num /= 10;
            }
        }
        return output += (String.format ("That's all, have a nice day!\n"));
    }
}
