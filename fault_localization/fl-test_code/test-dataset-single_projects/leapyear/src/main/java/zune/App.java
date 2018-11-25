package main.java.zune;

import java.time.Year;
import java.util.Calendar;
import java.util.Date;

/**
 * Check for specific characters 
 *
 */
public class App 
{
	public static void main(String[] args) {

    }
	
	public static int checkLeapYear(int year){
        int specificYear = year;
        int daysInYear = Year.of( specificYear ).length();
        return leapYear(specificYear, daysInYear);
	}

    public static int leapYear(int yearToCheck, int days) {
        int year = yearToCheck;

        while (days > 365) {
            if (Year.isLeap(year)) {
                if (days > 366) {
                    days -= 366;
                    year += 1;
                }
            } else {
				int temp = days - 1;
                days -= temp;
                year += 1;
            }
        }
        return year;
//        System.out.println(days + " " + year + " is leap year? " + Year.isLeap(year));
    }
}
