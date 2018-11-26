public class GCD {//LC:1
//LC:2
    public static void main(String[] args) {//LC:3
        gcd(6, 3);//LC:4
    }//LC:5
//LC:6
    public static int gcd(int a, int b) {//LC:7
        int gcd = b;//LC:8
//LC:9
        if (a == 0) {//LC:10
            System.out.println("GCD: " + gcd + "\n");//LC:11
        }//LC:12
        while (b != 0) {//LC:13
            if (a > b) {//LC:14
                a = a - b;//LC:15
            } else {//LC:16
                gcd = b;//LC:17
                b = b - a;//LC:18
            }//LC:19
        }//LC:20
        System.out.printf("GCD: " + gcd + "\n");//LC:21
        return gcd;//LC:22
    }//LC:23
}//LC:24
