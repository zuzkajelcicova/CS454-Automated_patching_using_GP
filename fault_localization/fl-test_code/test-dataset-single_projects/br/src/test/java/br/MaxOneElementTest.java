package test.java.br;

import org.junit.Assert;
import org.junit.Test;

import main.java.br.*;

/**
 * Unit test for Max.
 */
public class MaxOneElementTest {

	@Test
	public void test5() {
		int expected = 4;
		int actual = Max.max(new int[] { 4 });
		Assert.assertEquals(expected, actual);
	}
	
}
