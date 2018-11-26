package delete0;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import delete0.Delete;


/**
 * Unit test for simple app.
 */
public class DeleteTest 
    extends TestCase
{	
	// True
	public void test0() {
        System.out.println("Test String for s");
        Delete app = new Delete();
        assertEquals("", app.stringOutput(""));
	}
	
	// Fail
	public void test1() {
        System.out.println("Test String for St");
        Delete app = new Delete();
        assertEquals("St", app.stringOutput("St"));
	}
}

