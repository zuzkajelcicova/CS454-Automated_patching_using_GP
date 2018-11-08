public class LeapYear {

    public static void main(String[] args) {

        int year = 1900;
        boolean leap = false;

        if(year % 4 == 0){
            if(year % 100 == 0){
                // Year is divisible by 400, hence the year is a leap year
                if(leap){
            System.out.println(year + " is a leap year.");
        }else{
            System.out.println(year + " is not a leap year.");
        }
            }else{
                
            }
        }else{
            
        }

        if(leap){
            if(leap){
            System.out.println(year + " is a leap year.");
        }else{
            System.out.println(year + " is not a leap year.");
        }System.out.println(year + " is a leap year.");
        }else{
            System.out.println(year + " is not a leap year.");
        }
    }
}