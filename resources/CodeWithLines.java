public class LeapYear {//LC:1
//LC:2
    public static void main(String[] args) {//LC:3
//LC:4
        int year = 1900;//LC:5
        boolean leap = false;//LC:6
//LC:7
        if (year % 4 == 0) {//LC:8
            if (year % 100 == 0) {//LC:9
                // Year is divisible by 400, hence the year is a leap year//LC:10
                if (year % 400 == 0) {//LC:11
                    leap = true;//LC:12
                } else {//LC:13
                    leap = false;//LC:14
                }//LC:15
            } else {//LC:16
                leap = true;//LC:17
            }//LC:18
        } else {//LC:19
            leap = false;//LC:20
        }//LC:21
//LC:22
        if (leap) {//LC:23
            System.out.println(year + " is a leap year.");//LC:24
        } else {//LC:25
            System.out.println(year + " is not a leap year.");//LC:26
        }//LC:27
    }//LC:28
}//LC:29
//LC:30
