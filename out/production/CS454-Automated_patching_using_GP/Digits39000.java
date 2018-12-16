
public class Digits39000 {

    public static String exec (int number) {
        String output = "";
        int rem;

        output += (String.format("\nEnter an integer > "));
        output += (String.format("\n"));
        if (number < 0) {
            number = Math.abs(number);
            while (number > 10) {
                rem = number % 10;
                output += (String.format("%d\n", rem));
                number = number / 10;
            }
            number = number - 2 * number;
            output +=
                    (String.format("%d\nThat's all, have a nice day!\n", number));
        } else {
            while (number != 0) { //gzoltar:1,0
                rem = number % 10;
                output += (String.format("%d\n", rem));
                number = number / 10;
            }
            return output += (String.format("That's all, have a nice day!\n"));
        }
        return output += (String.format("That's all, have a nice day!\n"));
    }
}
