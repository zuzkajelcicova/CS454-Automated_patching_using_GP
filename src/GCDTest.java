import junit.framework.AssertionFailedError;
import junit.framework.TestResult;
import org.junit.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestName;
import org.junit.rules.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GCDTest extends TestResult {

    //todo: split into methods instead
    //todo: just examples, add more to cover the code

    int posTestPassedCounter;
    int negTestPassedCounter;

    @Rule
    public Timeout globalTimeout = Timeout.seconds(1);
    @Rule
    public TestName name = new TestName();
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    public synchronized void addError(Test test, Throwable t){
        super.addError((junit.framework.Test) test, t);

    }
    public synchronized void addFailure(Test test, AssertionFailedError t){
        super.addFailure((junit.framework.Test) test, t);

    }

    @Before
    void setUp() {
        posTestPassedCounter = 0;
        negTestPassedCounter = 0;
    }

    @AfterClass
    void tearDown() {
//        setPos
    }

    //Positive TestCases
    @Test
    public void testgcdPositive1() {
        try {
            Assert.assertEquals(8, GCD.gcd(72, 16));
            Assert.assertEquals("testgcdPositive1", name.getMethodName());
        } finally {
            posTestPassedCounter = posTestPassedCounter + 1;
        }

    }

    @Test
    public void testgcdPositive2() {
        //straight case
        try {
            Assert.assertEquals(18, GCD.gcd(461952,116298));
            Assert.assertEquals("testgcdPositive2", name.getMethodName());
        } finally {
            posTestPassedCounter = posTestPassedCounter + 1;
        }

    }

    @Test
    public void testgcdPositive3() {
        //both have common divisor
        try {
            Assert.assertEquals(14, GCD.gcd(42, 56));
            Assert.assertEquals("testgcdPositive3", name.getMethodName());
        } finally {
            posTestPassedCounter = posTestPassedCounter + 1;
        }

    }

    @Test
    public void testgcdPositive4() {
        //trick case: a = b
        try {
            Assert.assertEquals(13, GCD.gcd(13, 13));
            Assert.assertEquals("testgcdPositive4", name.getMethodName());
        } finally {
            posTestPassedCounter = posTestPassedCounter + 1;
        }

    }

    @Test
    public void testgcdPositive5() {
        //first argument is a prime
        try {
            Assert.assertEquals(1, GCD.gcd(37, 600));
            Assert.assertEquals("testgcdPositive5", name.getMethodName());
        } finally {
            posTestPassedCounter = posTestPassedCounter + 1;
        }

    }

    @Test
    public void testgcdPositive6() {
        //one is multiplum of other
        try {
            Assert.assertEquals(20, GCD.gcd(20, 100));
            Assert.assertEquals("testgcdPositive6", name.getMethodName());
        } finally {
            posTestPassedCounter = posTestPassedCounter + 1;
        }

    }

    @Test
    public void testgcdPositive7() {
        //straight case
        try {
            Assert.assertEquals(18913, GCD.gcd(624129, 2061517));
            Assert.assertEquals("testgcdPositive7", name.getMethodName());
        } finally {
            posTestPassedCounter = posTestPassedCounter + 1;
        }

    }

    //Negative TestCases
    @Test
    public void testgcdNegative1() {
        //Bug 1 - does not handle negative inputs
        //Case 1 - both negative
        try {
            Assert.assertEquals("testgcdNegative1", name.getMethodName());
            Assert.assertEquals(3, GCD.gcd(-6, -3));
        } finally {
            posTestPassedCounter = posTestPassedCounter + 1;
        }


    }
    @Test
    public void testgcdNegative2() {

        //Case 2 - one negative (a will be increased forever while b does not change)
        try {
            Assert.assertEquals("testgcdNegative2", name.getMethodName());
            Assert.assertEquals(4, GCD.gcd(8, -4));
        } finally {
            posTestPassedCounter = posTestPassedCounter + 1;
        }


    }

    @Test
    public void testgcdNegative3() {
        try {
            Assert.assertEquals("testgcdNegative3", name.getMethodName());
            //Case 3 - one negative (b will increase forever)
            Assert.assertEquals(4, GCD.gcd(-8, 4));
        } finally {
            posTestPassedCounter = posTestPassedCounter + 1;
        }

    }

    @Test
    public void testgcdNegative4() {
        try {
            Assert.assertEquals("testgcdNegative4", name.getMethodName());
            // a value is zero
            Assert.assertEquals(20, GCD.gcd(0, 20));
        } finally {
            posTestPassedCounter = posTestPassedCounter + 1;
        }

    }

    @Test
    public void testgcdNegative5() {
        try {
            Assert.assertEquals("testgcdNegative5", name.getMethodName());
            //b value is zero
            //should get divide by zero exception which is ArithmeticException
            exception.expect(ArithmeticException.class);
            int gcdResult = GCD.gcd(0, 0);
        } finally {
            posTestPassedCounter = posTestPassedCounter + 1;
        }


    }

    @Test
    public void testgcdNegative6() {
        try {
            Assert.assertEquals(18913, GCD.gcd(624129, 2061517));
            Assert.assertEquals("testgcdPositive7", name.getMethodName());
        } finally {
            posTestPassedCounter = posTestPassedCounter + 1;
        }
        Assert.assertEquals("testgcdNegative6", name.getMethodName());
        //b is zero, a is negative
        int gcdResult = GCD.gcd(-5, 0);
        Assert.assertEquals(5, gcdResult);
    }

    @Test
    public void testgcdNegative7() {
        try {
            Assert.assertEquals(18913, GCD.gcd(624129, 2061517));
            Assert.assertEquals("testgcdPositive7", name.getMethodName());
        } finally {
            posTestPassedCounter = posTestPassedCounter + 1;
        }
        Assert.assertEquals("testgcdNegative7", name.getMethodName());
        //b is zero, a is negative
        int gcdResult = GCD.gcd(0, -12);
        Assert.assertEquals(12, gcdResult);
    }
}