package GP;

public class TestCase {
    private int[] input;
    private int expectedResult;


    public TestCase(int[] input, int expectedResult) {
        this.input = input;
        this.expectedResult = expectedResult;

    }

    public int[] getInput() {
        return input;
    }

    public int getExpectedResult() {
        return expectedResult;
    }

//    gcd(42, 56) = 14
//    gcd(461952, 116298) = 18
//    gcd(7966496, 314080416) = 32
//    gcd(24826148, 45296490) = 526
//    gcd(12, 0) = 12
//    gcd(0, 0) = 0
//    gcd(0, 9) = 9

//    testcases = [ (13, 13, 13),              // trick case: a = b
//            (37, 600, 1),              // first argument is a prime
//            (20, 100, 20),             // one is multiplum of other
//            (624129, 2061517, 18913) ] // straight case
}
