import junit.framework.AssertionFailedError;
import junit.framework.TestResult;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestName;
import org.junit.rules.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GCDTestPos extends TestResult {

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

    @BeforeEach
    void setUp() {
        posTestPassedCounter = 0;
        negTestPassedCounter = 0;
    }

    @AfterEach
    void tearDown() {

    }

    //Positive TestCases
    @Test
    public void testgcdPositive1() {
        Assert.assertEquals(8, GCD.gcd(72, 16));
        Assert.assertEquals("testgcdPositive1", name.getMethodName());
    }

    @Test
    public void testgcdPositive2() {
        //straight case
        Assert.assertEquals(18, GCD.gcd(461952,116298));
        Assert.assertEquals("testgcdPositive2", name.getMethodName());
    }

    @Test
    public void testgcdPositive3() {
        //both have common divisor
        Assert.assertEquals(14, GCD.gcd(42, 56));
        Assert.assertEquals("testgcdPositive3", name.getMethodName());
    }

    @Test
    public void testgcdPositive4() {
        //trick case: a = b
        Assert.assertEquals(13, GCD.gcd(13, 13));
        Assert.assertEquals("testgcdPositive4", name.getMethodName());
    }

    @Test
    public void testgcdPositive5() {
        //first argument is a prime
        Assert.assertEquals(1, GCD.gcd(37, 600));
        Assert.assertEquals("testgcdPositive5", name.getMethodName());
    }

    @Test
    public void testgcdPositive6() {
        //one is multiplum of other
        Assert.assertEquals(20, GCD.gcd(20, 100));
        Assert.assertEquals("testgcdPositive6", name.getMethodName());
    }

    @Test
    public void testgcdPositive7() {
        //straight case
        Assert.assertEquals(18913, GCD.gcd(624129, 2061517));
        Assert.assertEquals("testgcdPositive7", name.getMethodName());
    }

}