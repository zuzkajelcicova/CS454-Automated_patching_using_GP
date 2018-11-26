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
	// Pass
	public void test0() {
        System.out.println("Test String for");
    	App helloWorld = new App();
        assertEquals("a", helloWorld.stringOutput("a"));
	}
	
	// Pass
	public void test1() {
        System.out.println("Test String for");
    	App helloWorld = new App();
    	assertEquals("b", helloWorld.stringOutput("b"));
	}
	
	// Pass
	public void test2() {
        System.out.println("Test String for");
    	App helloWorld = new App();
    	assertEquals("c", helloWorld.stringOutput("c"));
	}
	
	// Pass
	public void test4() {
        System.out.println("Test String for");
    	App helloWorld = new App();
    	assertNotSame("x", helloWorld.stringOutput("a"));
	}
	
	// Pass
	public void test5() {
        System.out.println("Test String for");
    	App helloWorld = new App();
    	assertEquals("x", helloWorld.stringOutput("x"));
	}
}
