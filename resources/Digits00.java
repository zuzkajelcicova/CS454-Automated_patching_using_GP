
public class Digits00 {

    public static void exec (int input){
        String output = "";
        int digit;

        output += (String.format ("\nEnter an integer > "));
        output += (String.format ("\n"));
        while (true) {
            digit = input % 10;
            if (input == 0) {
                output += (String.format ("0\n"));
                break;
            } else if (Math.abs (digit) < 10) {
                output += (String.format ("%d\n", digit));
            }
            input = input / 10;
            if (Math.abs (input) < 10 && input != 0) {
                output += (String.format ("%d\n", input));
                break;
            }
        }
        output += (String.format ("That's all, have a nice day!\n"));
        if (true)
            return;;
    }
}
