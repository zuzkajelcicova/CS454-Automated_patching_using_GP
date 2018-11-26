package test.java.gcd;

import junit.framework.AssertionFailedError;
import junit.framework.TestResult;
import main.java.gcd.GCD;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestName;
import org.junit.rules.Timeout;


public class GCDTestNeg extends TestResult {

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

    //Negative TestCases
    @Test
    public void testgcdNegative1() {
        //Bug 1 - does not handle negative inputs
        //Case 1 - both negative
        Assert.assertEquals("testgcdNegative1", name.getMethodName());
        int gcdResult = GCD.gcd(-6, -3);
        Assert.assertEquals(3, gcdResult);

    }
    @Test
    public void testgcdNegative2() {
        //Case 2 - one negative (a will be increased forever while b does not change)
        Assert.assertEquals("testgcdNegative2", name.getMethodName());
        int gcdResult = GCD.gcd(8, -4);
        Assert.assertEquals(4, gcdResult);

    }

    @Test
    public void testgcdNegative3() {
        Assert.assertEquals("testgcdNegative3", name.getMethodName());
        //Case 3 - one negative (b will increase forever)
        int gcdResult = GCD.gcd(-8, 4);
        Assert.assertEquals(4, gcdResult);
    }

    @Test
    public void testgcdNegative4() {
        Assert.assertEquals("testgcdNegative4", name.getMethodName());
        // a value is zero
        int gcdResult = GCD.gcd(0, 20);
        Assert.assertEquals(20, gcdResult);
    }

//    @Test
//    public void testgcdNegative5() {
//        Assert.assertEquals("testgcdNegative5", name.getMethodName());
//        //b value is zero
//        //should get divide by zero exception which is ArithmeticException
//        exception.expect(ArithmeticException.class);
//        int gcdResult = GCD.gcd(0, 0);
//
//    }

    @Test
    public void testgcdNegative5() {
        Assert.assertEquals("testgcdNegative5", name.getMethodName());
        //b is zero, a is negative
        int gcdResult = GCD.gcd(-5, 0);
        Assert.assertEquals(5, gcdResult);
    }

    @Test
    public void testgcdNegative6() {
        Assert.assertEquals("testgcdNegative6", name.getMethodName());
        //b is zero, a is negative
        int gcdResult = GCD.gcd(0, -12);
        Assert.assertEquals(12, gcdResult);
    }
}
