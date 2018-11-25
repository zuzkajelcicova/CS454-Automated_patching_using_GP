package replace0;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import replace0.*;

public class TestReplace 
    extends TestCase
{	
	// Pass
	public void test0() {
        System.out.print("Test String for");
    	Replace helloWorld = new Replace();
        assertEquals("a", helloWorld.stringOutput("a"));
	}
	
	// Pass
	public void test1() {
        System.out.print("Test String for");
        Replace helloWorld = new Replace();
    	assertEquals("b", helloWorld.stringOutput("b"));
	}
	
	// Pass
	public void test2() {
        System.out.print("Test String for");
        Replace helloWorld = new Replace();
    	assertEquals("c", helloWorld.stringOutput("c"));
	}
	
	// Pass
	public void test4() {
        System.out.print("Test String for");
        Replace helloWorld = new Replace();
        assertEquals("d", helloWorld.stringOutput("d"));
	}
	
	// Pass
	public void test5() {
        System.out.print("Test String for");
        Replace helloWorld = new Replace();
    	assertEquals("else", helloWorld.stringOutput("x"));
	}
}
