package test.java.zune;

import junit.framework.AssertionFailedError;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestName;
import org.junit.rules.Timeout;
import junit.framework.TestResult;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import main.java.zune.*;


/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestResult
{	
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
	
	// Pass
	@Test
	public void test0() throws Exception {
		int checkYear = 2001;
        System.out.println("Check for " + checkYear);
    	App helloWorld = new App();
        Assert.assertEquals(checkYear, helloWorld.checkLeapYear(checkYear));
	}
	
	// Pass
	@Test
	public void test1() throws Exception{
		int checkYear = 2004;
        System.out.println("Check for " + checkYear);
    	App helloWorld = new App();
    	Assert.assertEquals(checkYear, helloWorld.checkLeapYear(checkYear));
	}
}
