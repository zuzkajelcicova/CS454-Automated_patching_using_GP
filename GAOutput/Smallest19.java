//LC:1
//LC:2
/*//LC:3
 * Print out smallest integer from database//LC:4
 *///LC:5
//LC:6
class IntObj {//LC:7
    public int value;//LC:8

    public IntObj() {//LC:9
    }

    public IntObj(int i) {//LC:10
        value = i;//LC:11
    }//LC:12
}//LC:13

//LC:14
class FloatObj {//LC:15
    public float value;//LC:16

    public FloatObj() {//LC:17
    }

    public FloatObj(float i) {//LC:18
        value = i;//LC:19
    }//LC:20
}//LC:21

//LC:22
class LongObj {//LC:23
    public long value;//LC:24

    public LongObj() {//LC:25
    }

    public LongObj(long i) {//LC:26
        value = i;//LC:27
    }//LC:28
}//LC:29

//LC:30
class DoubleObj {//LC:31
    public double value;//LC:32

    public DoubleObj() {//LC:33
    }

    public DoubleObj(double i) {//LC:34
        value = i;//LC:35
    }//LC:36
}//LC:37

//LC:38
class CharObj {//LC:39
    public char value;//LC:40

    public CharObj() {//LC:41
    }

    public CharObj(char i) {//LC:42
        value = i;//LC:43
    }//LC:44
}//LC:45

//LC:46
public class Smallest19 {//LC:47
    public java.util.Scanner scanner;//LC:48
    public String output = "";//LC:49

    //LC:50
//    public static void main (String[]args) throws Exception {//LC:51
//        Smallest19 mainClass = new Smallest19 ();//LC:52
//        String output;//LC:53
//        if (args.length > 0) {//LC:54
//            mainClass.scanner = new java.util.Scanner (args[0]);//LC:55
//        } else {//LC:56
//            mainClass.scanner = new java.util.Scanner (System.in);//LC:57
//        }//LC:58
//        mainClass.exec ();//LC:59
//        System.out.println (mainClass.output);//LC:60
//    }//LC:61
//LC:62
    public static String exec(int a, int b, int c, int d) {//LC:63
        String output = "";//LC:64
//        IntObj a = new IntObj (), b = new IntObj (), c = new IntObj (), d =//LC:65
//            new IntObj ();//LC:66
        output +=//LC:67
                (String.format("Please enter 4 numbers separated by spaces > "));//LC:68
//        a = scanner.nextInt ();//LC:69
//        b = scanner.nextInt ();//LC:70
//        c = scanner.nextInt ();//LC:71
//        d = scanner.nextInt ();//LC:72
        if (a < b && a < c && a < d) {//LC:73
            return output += (String.format("%d is the smallest\n", a));//LC:74
        } else if (b < a && b < c && b < d) {//LC:75
            return output += (String.format("%d is the smallest\n", b));//LC:76
        } else if (c < a && c < b && c < d) {//LC:77
            return output += (String.format("%d is the smallest\n", c));//LC:78
        } else if (d < a && d < c && d < b) {//LC:79
            return output += (String.format("%d is the smallest\n", d));//LC:80
        } else {//LC:81
            return output += (String.format("I don't know what I'm doing. \n"));//LC:82
        }//LC:83
    }//LC:84
}//LC:85
