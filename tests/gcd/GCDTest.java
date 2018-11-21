
import org.junit.Rule;

import org.junit.rules.Timeout;

import static java.time.Duration.ofSeconds;


class GCDTest {

    //todo: split into methods instead
    //todo: just examples, add more to cover the code

    int posTestPassedCounter;
    int negTestPassedCounter;

    //@Rule
    //public Timeout globalTimeout = Timeout.seconds(5);

//    @BeforeEach
//    void setUp() {
//        posTestPassedCounter = 0;
//        negTestPassedCounter = 0;
//    }
//
//    @AfterEach
//    void tearDown() {
//
//    }
//
//    //Positive TestCases
//    @Test
//    void gcdPositive1() {
//        assertEquals(8, GCD.gcd(72, 16));
//    }
//
//    @Test
//    void gcdPositive2() {
//        //Bug 1 - loops forever
//        assertEquals(20, GCD.gcd(0, 20));
//    }
//
//    @Test
//    void gcdPositive3() {
//        //Bug 2 - does not handle negative inputs
//        //Case 1 - both negative
//        assertEquals(3, GCD.gcd(-6, -3));
//    }
//
//    @Test
//    void gcdPositive4() {
//        //Case 2 - one negative (a will be increased forever while b does not change)
//        assertEquals(4, GCD.gcd(8, -4));
//    }
//
//    @Test
//    void gcdPositive5() {
//        //Case 3 - one negative (b will increase forever)
//        assertEquals(4, GCD.gcd(-8, 4));
//
//    }
//
//    //Negative TestCases
//    @Test
//    void gcdNegative() {
//        //Bug 1 - does not handle negative inputs
//        //Case 1 - both negative
//        int gcdResult = GCD.gcd(-6, -3);
//        //assertEquals(3, gcdResult);
//
//        //Case 2 - one negative (a will be increased forever while b does not change)
//        gcdResult = GCD.gcd(8, -4);
//        //assertEquals(4, gcdResult);
//
//        //Case 3 - one negative (b will increase forever)
//        gcdResult = GCD.gcd(-8, 4);
//        //assertEquals(4, gcdResult);
//    }
}