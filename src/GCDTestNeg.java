import junit.framework.TestResult;
import org.junit.Assert;
import org.junit.Rule;

import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestName;
import org.junit.rules.Timeout;

public class GCDTestNeg extends TestResult {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(20);
    @Rule
    public TestName name = new TestName();
    //@Rule
    //public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testGCDNegative1() {
        Assert.assertEquals("testGCDNegative1", name.getMethodName());
        int gcdResult = GCD.gcd(0, 20);
        Assert.assertEquals(20, gcdResult);
    }
}
