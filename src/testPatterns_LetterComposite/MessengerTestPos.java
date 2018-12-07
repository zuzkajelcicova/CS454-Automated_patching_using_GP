package testPatterns_LetterComposite; /**
 * The MIT License
 * Copyright (c) 2014-2016 Ilkka Seppälä
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


import General.CompiledClassLoader;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.After;
import junit.framework.TestResult;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.lang.System.out;
import static org.junit.Assert.assertEquals;

/**
 * Date: 12/11/15 - 8:12 PM
 *
 * @author Jeroen Meulemeester
 */
public class MessengerTestPos extends TestResult{

  @Rule
  public Timeout globalTimeout = Timeout.seconds(2);
  public static int numberOfPositiveTests = 1;

  public static Class testClass = null;
  public static Method[] allMethods = null;
  public static Method testMethod = null;
  public static String testedMethodName = "messageFromElves";

  @BeforeClass
  public static void setupEnvironment() {
    //Get access to a recompiled class during the runtime
    testClass = Messenger.class;
    allMethods = CompiledClassLoader.getRecompiledMethods(testClass);

    for (Method m : allMethods) {
      String methodName = m.getName();
      if (methodName.startsWith(testedMethodName)) {
        m.setAccessible(true);
        testMethod = m;
      }
    }
  }



  /**
   * The buffer used to capture every write to {@link System#out}
   */
  private ByteArrayOutputStream stdOutBuffer = new ByteArrayOutputStream();

  /**
   * Keep the original std-out so it can be restored after the test
   */
  private final PrintStream realStdOut = System.out;

  /**
   * Inject the mocked std-out {@link PrintStream} into the {@link System} class before each test
   */
  @Before
  public void setUp() {
    this.stdOutBuffer = new ByteArrayOutputStream();
    System.setOut(new PrintStream(stdOutBuffer));
  }

  /**
   * Removed the mocked std-out {@link PrintStream} again from the {@link System} class
   */
  @After
  public void tearDown() {
    System.setOut(realStdOut);
  }

  /**
   * Test the message from the orcs
   */


  /**
   * Test the message from the elves
   */
  @Test
  public void testMessageFromElves() {
      try {
          out.format("Invoking %s()%n", testedMethodName, " from test1...");
          String expected = "Much wind pours from your mouth.";
          Object o = testMethod.invoke(null);
          Assert.assertEquals("Much wind pours from your mouth.", o.toString());
          out.format("%s() returned %b%n", testedMethodName, o);
      } catch (IllegalAccessException e) {
          e.printStackTrace();
      } catch (InvocationTargetException e) {
          e.printStackTrace();
      } catch (Exception e) {
          e.printStackTrace();
      }
  }

  /**
   * Test if the given composed message matches the expected message
   *
   * @param composedMessage The composed message, received from the messenger
   * @param message         The expected message
   */
  private void testMessage(final LetterComposite composedMessage, final String message) {
    // Test is the composed message has the correct number of words
    final String[] words = message.split(" ");
    assertNotNull(composedMessage);
    assertEquals(words.length, composedMessage.count());

    // Print the message to the mocked stdOut ...
    composedMessage.print();

    // ... and verify if the message matches with the expected one
    assertEquals(message, new String(this.stdOutBuffer.toByteArray()).trim());
  }

}
