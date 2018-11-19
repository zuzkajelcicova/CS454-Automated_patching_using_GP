package daimler;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import daimler.App;


/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{	
	// Fail
	public void test0() {
        System.out.println("Test String for Str");
    	App helloWorld = new App();
        assertEquals("St", helloWorld.giffString("a"));
	}
	
	// True
	public void test1() {
        System.out.println("Test String for Strd");
    	App helloWorld = new App();
    	assertEquals("a", helloWorld.giffString("a"));
	}
	
	// True
	public void test2() {
        System.out.println("Test String for Strd");
    	App helloWorld = new App();
    	assertEquals("b", helloWorld.giffString("b"));
	}
	
	// True
	public void test3() {
        System.out.println("Test String for Strd");
    	App helloWorld = new App();
    	assertEquals("x", helloWorld.giffString("z"));
	}
	
	// Fail
	public void test4() {
        System.out.println("Test String for Strd");
    	App helloWorld = new App();
    	assertEquals("x", helloWorld.giffString("a"));
	}
	
	// Fail
	public void test5() {
        System.out.println("Test String for Strd");
    	App helloWorld = new App();
    	assertEquals("x", helloWorld.giffString("b"));
	}
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
}
