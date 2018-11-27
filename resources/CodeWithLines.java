import java.time.Year;//LC:1
import java.util.Calendar;//LC:2
import java.util.Date;//LC:3
//LC:4
public class LeapYear//LC:5
{//LC:6
    public static void main(String[] args) {//LC:7
//LC:8
    }//LC:9
    public static int checkLeapYear(int year){//LC:10
        int specificYear = year;//LC:11
        int daysInYear = Year.of( specificYear ).length();//LC:12
        return leapYear(specificYear, daysInYear);//LC:13
    }//LC:14
//LC:15
    public static int leapYear(int yearToCheck, int days) {//LC:16
        int year = yearToCheck;//LC:17
//LC:18
        while (days > 365) {//LC:19
            if (Year.isLeap(year)) {//LC:20
                if (days > 366) { // days > 365//LC:21
                    days -= 366;//LC:22
                    year += 1;//LC:23
                }//LC:24
            } else {//LC:25
                int temp = days - 1;//LC:26
                days -= temp;//LC:27
                year += 1;//LC:28
            }//LC:29
        }//LC:30
        return year;//LC:31
    }//LC:32
}//LC:33
