
public class Digits6000 {

    public static String exec (int num ) throws Exception {
        String output = "";
        int digit;

        output += (String.format("Enter an integer >  "));
        while (num > 0) { //gzoltar: 1,0
            digit = num % 10;
            output += (String.format ("%d\n", digit));
            num = num / 10;
        }
        output += (String.format ("That's all, have a nice day!\n"));
        return output;
    }
}
