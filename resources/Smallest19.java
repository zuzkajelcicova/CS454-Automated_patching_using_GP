

/*
 * Print out smallest integer from database
 */

class IntObj {
    public int value;

    public IntObj() {
    }

    public IntObj(int i) {
        value = i;
    }
}

class FloatObj {
    public float value;

    public FloatObj() {
    }

    public FloatObj(float i) {
        value = i;
    }
}

class LongObj {
    public long value;

    public LongObj() {
    }

    public LongObj(long i) {
        value = i;
    }
}

class DoubleObj {
    public double value;

    public DoubleObj() {
    }

    public DoubleObj(double i) {
        value = i;
    }
}

class CharObj {
    public char value;

    public CharObj() {
    }

    public CharObj(char i) {
        value = i;
    }
}

public class Smallest19 {
    public java.util.Scanner scanner;
    public String output = "";

//    public static void main (String[]args) throws Exception {
//        Smallest19 mainClass = new Smallest19 ();
//        String output;
//        if (args.length > 0) {
//            mainClass.scanner = new java.util.Scanner (args[0]);
//        } else {
//            mainClass.scanner = new java.util.Scanner (System.in);
//        }
//        mainClass.exec ();
//        System.out.println (mainClass.output);
//    }

    public static String exec(int a, int b, int c, int d) {
        String output = "";
//        IntObj a = new IntObj (), b = new IntObj (), c = new IntObj (), d =
//            new IntObj ();
        output +=
                (String.format("Please enter 4 numbers separated by spaces > "));
//        a = scanner.nextInt ();
//        b = scanner.nextInt ();
//        c = scanner.nextInt ();
//        d = scanner.nextInt ();
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
