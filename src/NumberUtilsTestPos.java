/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import General.CompiledClassLoader;
import junit.framework.TestResult;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;

import org.junit.Test;
import org.junit.rules.Timeout;

import static java.lang.System.out;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;

import org.junit.Test;

/**
 * Unit tests.
 *
 * @version $Id$
 */
public class NumberUtilsTestPos extends TestResult {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(2);
    public static int numberOfPositiveTests = 1;

    public static Class testClass = null;
    public static Method[] allMethods = null;
    public static Method testMethod = null;
    public static String testedMethodName = "createNumber";

    @BeforeClass
    public static void setupEnvironment() {
        //Get access to a recompiled class during the runtime
        testClass = NumberUtils.class;
        allMethods = CompiledClassLoader.getRecompiledMethods(testClass);

        for (Method m : allMethods) {
            String methodName = m.getName();
            if (methodName.startsWith(testedMethodName)) {
                m.setAccessible(true);
                testMethod = m;
            }
        }
    }

    @Test
    // Check that the code fails to create a valid number when preceeded by -- rather than -
    public void testCreateNumberFailure_11() {
        try {
//            NumberUtils nu = new NumberUtils();
            out.format("Invoking %s()%n", testedMethodName, " from NumberUtilsTestPos...");
            Object o = testMethod.invoke(null, "3.4028236e+38");
            Assert.assertEquals(Double.valueOf(3.4028236e+38), o);
//            Assert.assertEquals(Double.valueOf(21), o);
            out.format("%s() returned %b%n", testedMethodName, o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.getCause().printStackTrace();
        }
    }




    //@Test(expected=NumberFormatException.class)
//    @Test
    // Check that the code fails to create a valid number when preceeded by -- rather than -
//    public void testCreateNumberFailure_1() {
//        try {
////            NumberUtils nu = new NumberUtils();
//            out.format("Invoking %s()%n", testedMethodName, " from NumberUtilsTestPos...");
//            Object o = testMethod.invoke(null, "3.4028236E38");
////            Assert.assertEquals(Double.valueOf(3.4028236e+38), o);
//            Assert.assertEquals(Double.valueOf(3.4028236e+38), o);
//            out.format("%s() returned %b%n", testedMethodName, o);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.getCause().printStackTrace();
//        }
//    }

}
