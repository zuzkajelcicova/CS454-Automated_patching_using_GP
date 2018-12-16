
public class Digits12000 {

    public static String exec (int value ) throws Exception {
        String output = "";
        int digit;
        output += (String.format("Enter an integer >  \n"));
        if (value >= 0) {
            while (value != 0) {
                digit = value % 10;
                value = value / 10;
                output += (String.format ("%d\n", digit));
            }
        }
        if (value < 0) {
            while (value < -10) { //gzoltar: 1,0
                digit = Math.abs (value % 10);
                value = value / 10;
                output += (String.format ("%d\n", digit));
            }
            digit = value % 10;
            output += (String.format ("%d\n", digit));
        }
        return output += (String.format ("That's all, have a nice day!\n"));
    }
}
