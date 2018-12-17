/*//LC:1
 * Licensed to the Apache Software Foundation (ASF) under one or more//LC:2
 * contributor license agreements.  See the NOTICE file distributed with//LC:3
 * this work for additional information regarding copyright ownership.//LC:4
 * The ASF licenses this file to You under the Apache License, Version 2.0//LC:5
 * (the "License"); you may not use this file except in compliance with//LC:6
 * the License.  You may obtain a copy of the License at//LC:7
 *//LC:8
 *      http://www.apache.org/licenses/LICENSE-2.0//LC:9
 *//LC:10
 * Unless required by applicable law or agreed to in writing, software//LC:11
 * distributed under the License is distributed on an "AS IS" BASIS,//LC:12
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.//LC:13
 * See the License for the specific language governing permissions and//LC:14
 * limitations under the License.//LC:15
 *///LC:16
//LC:17
import java.io.*;//LC:18
import java.lang.reflect.*;//LC:19
import java.math.BigDecimal;//LC:20
import java.math.BigInteger;//LC:21
import java.text.Normalizer;//LC:22
import java.util.*;//LC:23
import java.util.regex.Pattern;//LC:24
import java.lang.reflect.Array;//LC:25
import java.util.Arrays;//LC:26
//LC:27
//LC:28
//import org.apache.commons.lang3.StringUtils;//LC:29
//LC:30
/**//LC:31
 * <p>Provides extra functionality for Java Number classes.</p>//LC:32
 *//LC:33
 * @since 2.0//LC:34
 * @version $Id$//LC:35
 *///LC:36
public class NumberUtils2 {//LC:37
//LC:38
    public static Number createNumber2(final String str) throws NumberFormatException {//LC:39
        if (str == null) {//LC:40
            return null;//LC:41
        }//LC:42
        // Current tests fail because StringUtils() cant be resolved//LC:43
//        if (StringUtils.isBlank(str)) {//LC:44
//            throw new NumberFormatException("A blank string is not a valid number");//LC:45
//        }//LC:46
//LC:47
        int strLen;//LC:48
        if (str == null || (strLen = str.length()) == 0) {//LC:49
            throw new NumberFormatException("A blank string is not a valid number");//LC:50
        }//LC:51
//LC:52
        // Need to deal with all possible hex prefixes here//LC:53
        final String[] hex_prefixes = {"0x", "0X", "-0x", "-0X", "#", "-#"};//LC:54
        int pfxLen = 0;//LC:55
        for (final String pfx : hex_prefixes) {//LC:56
            if (str.startsWith(pfx)) {//LC:57
                pfxLen += pfx.length();//LC:58
                break;//LC:59
            }//LC:60
        }//LC:61
//LC:62
        /* bug fix//LC:63
        if (pfxLen > 0) { // we have a hex number//LC:64
            char firstSigDigit = 0; // strip leading zeroes//LC:65
            for(int i = pfxLen; i < str.length(); i++) {//LC:66
                firstSigDigit = str.charAt(i);//LC:67
                if (firstSigDigit == '0') { // count leading zeroes//LC:68
                    pfxLen++;//LC:69
                } else {//LC:70
                    break;//LC:71
                }//LC:72
            }//LC:73
            final int hexDigits = str.length() - pfxLen;//LC:74
            if (hexDigits > 16 || (hexDigits == 16 && firstSigDigit > '7')) { // too many for Long//LC:75
                return createBigInteger(str);//LC:76
            }//LC:77
            if (hexDigits > 8 || (hexDigits == 8 && firstSigDigit > '7')) { // too many for an int//LC:78
                return createLong(str);//LC:79
            }//LC:80
            return createInteger(str);//LC:81
        }//LC:82
         *///LC:83
        //bug, gzoltar likelihood faulty//LC:84
        if (pfxLen > 0) { // we have a hex number//LC:85
            final int hexDigits = str.length() - pfxLen;//LC:86
            if (hexDigits > 16) { // too many for Long//LC:87
                return createBigInteger(str);//LC:88
            }//LC:89
            if (hexDigits > 8) { // too many for an int//LC:90
                return createLong(str);//LC:91
            }//LC:92
//LC:93
            if (str == null) {//LC:94
                return null;//LC:95
            }//LC:96
            // decode() handles 0xAABD and 0777 (hex and octal) as well.//LC:97
            return Integer.decode(str);//LC:98
//LC:99
//LC:100
//            return createInteger(str);//LC:101
        }return null;//LC:102
        //bug//LC:103
//LC:104
//LC:105
        final char lastChar = str.charAt(str.length() - 1);//LC:106
        String mant;//LC:107
        String dec;//LC:108
        String exp;//LC:109
        final int decPos = str.indexOf('.');//LC:110
        final int expPos = str.indexOf('e') + str.indexOf('E') + 1; // assumes both not present//LC:111
        // if both e and E are present, this is caught by the checks on expPos (which prevent IOOBE)//LC:112
        // and the parsing which will detect if e or E appear in a number due to using the wrong offset//LC:113
//LC:114
        int numDecimals = 0; // Check required precision (LANG-693)//LC:115
        if (decPos > -1) { // there is a decimal point//LC:116
//LC:117
            if (expPos > -1) { // there is an exponent//LC:118
                if (expPos < decPos || expPos > str.length()) { // prevents double exponent causing IOOBE//LC:119
                    throw new NumberFormatException(str + " is not a valid number.");//LC:120
                }//LC:121
                dec = str.substring(decPos + 1, expPos);//LC:122
            } else {//LC:123
                dec = str.substring(decPos + 1);//LC:124
            }//LC:125
            mant = str.substring(0, decPos);//LC:126
            numDecimals = dec.length(); // gets number of digits past the decimal to ensure no loss of precision for floating point numbers.//LC:127
        } else {//LC:128
            if (expPos > -1) {//LC:129
                if (expPos > str.length()) { // prevents double exponent causing IOOBE//LC:130
                    throw new NumberFormatException(str + " is not a valid number.");//LC:131
                }//LC:132
                mant = str.substring(0, expPos);//LC:133
            } else {//LC:134
                mant = str;//LC:135
            }//LC:136
            dec = null;//LC:137
        }//LC:138
        if (!Character.isDigit(lastChar) && lastChar != '.') {//LC:139
            if (expPos > -1 && expPos < str.length() - 1) {//LC:140
                exp = str.substring(expPos + 1, str.length() - 1);//LC:141
            } else {//LC:142
                exp = null;//LC:143
            }//LC:144
            //Requesting a specific type..//LC:145
            final String numeric = str.substring(0, str.length() - 1);//LC:146
            final boolean allZeros = isAllZeros(mant) && isAllZeros(exp);//LC:147
            switch (lastChar) {//LC:148
                case 'l'://LC:149
                case 'L'://LC:150
                    if (dec == null//LC:151
                            && exp == null//LC:152
                            && (numeric.charAt(0) == '-' && isDigits(numeric.substring(1)) || isDigits(numeric))) {//LC:153
                        try {//LC:154
                            return createLong(numeric);//LC:155
                        } catch (final NumberFormatException nfe) { // NOPMD//LC:156
                            // Too big for a long//LC:157
                        }//LC:158
                        return createBigInteger(numeric);//LC:159
//LC:160
                    }//LC:161
                    throw new NumberFormatException(str + " is not a valid number.");//LC:162
                case 'f'://LC:163
                case 'F'://LC:164
                    try {//LC:165
                        final Float f = NumberUtils.createFloat(numeric);//LC:166
                        if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {//LC:167
                            //If it's too big for a float or the float value = 0 and the string//LC:168
                            //has non-zeros in it, then float does not have the precision we want//LC:169
                            return f;//LC:170
                        }//LC:171
//LC:172
                    } catch (final NumberFormatException nfe) { // NOPMD//LC:173
                        // ignore the bad number//LC:174
                    }//LC:175
                    //$FALL-THROUGH$//LC:176
                case 'd'://LC:177
                case 'D'://LC:178
                    try {//LC:179
                        final Double d = NumberUtils.createDouble(numeric);//LC:180
                        if (!(d.isInfinite() || (d.floatValue() == 0.0D && !allZeros))) {//LC:181
                            return d;//LC:182
                        }//LC:183
                    } catch (final NumberFormatException nfe) { // NOPMD//LC:184
                        // ignore the bad number//LC:185
                    }//LC:186
                    try {//LC:187
                        return createBigDecimal(numeric);//LC:188
                    } catch (final NumberFormatException e) { // NOPMD//LC:189
                        // ignore the bad number//LC:190
                    }//LC:191
                    //$FALL-THROUGH$//LC:192
                default://LC:193
                    throw new NumberFormatException(str + " is not a valid number.");//LC:194
//LC:195
            }//LC:196
        }//LC:197
        //User doesn't have a preference on the return type, so let's start//LC:198
        //small and go from there...//LC:199
        if (expPos > -1 && expPos < str.length() - 1) {//LC:200
            exp = str.substring(expPos + 1, str.length());//LC:201
        } else {//LC:202
            exp = null;//LC:203
        }//LC:204
        if (dec == null && exp == null) { // no decimal point and no exponent//LC:205
            //Must be an Integer, Long, Biginteger//LC:206
            try {//LC:207
                return createInteger(str);//LC:208
            } catch (final NumberFormatException nfe) { // NOPMD//LC:209
                // ignore the bad number//LC:210
            }//LC:211
            try {//LC:212
                return createLong(str);//LC:213
            } catch (final NumberFormatException nfe) { // NOPMD//LC:214
                // ignore the bad number//LC:215
            }//LC:216
            return createBigInteger(str);//LC:217
        }//LC:218
//LC:219
        //Must be a Float, Double, BigDecimal//LC:220
        final boolean allZeros = isAllZeros(mant) && isAllZeros(exp);//LC:221
        try {//LC:222
            if (numDecimals <= 7) {// If number has 7 or fewer digits past the decimal point then make it a float//LC:223
                final Float f = createFloat(str);//LC:224
                if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {//LC:225
                    return f;//LC:226
                }//LC:227
            }//LC:228
        } catch (final NumberFormatException nfe) { // NOPMD//LC:229
            // ignore the bad number//LC:230
        }//LC:231
        try {//LC:232
            if (numDecimals <= 16) {// If number has between 8 and 16 digits past the decimal point then make it a double//LC:233
                final Double d = createDouble(str);//LC:234
                if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {//LC:235
                    return d;//LC:236
                }//LC:237
            }//LC:238
        } catch (final NumberFormatException nfe) { // NOPMD//LC:239
            // ignore the bad number//LC:240
        }//LC:241
//LC:242
        return createBigDecimal(str);//LC:243
    }//LC:244
//LC:245
    public static BigInteger createBigInteger(final String str) {//LC:246
        if (str == null) {//LC:247
            return null;//LC:248
        }//LC:249
        int pos = 0; // offset within string//LC:250
        int radix = 10;//LC:251
        boolean negate = false; // need to negate later?//LC:252
        if (str.startsWith("-")) {//LC:253
            negate = true;//LC:254
            pos = 1;//LC:255
        }//LC:256
        if (str.startsWith("0x", pos) || str.startsWith("0x", pos)) { // hex//LC:257
            radix = 16;//LC:258
            pos += 2;//LC:259
        } else if (str.startsWith("#", pos)) { // alternative hex (allowed by Long/Integer)//LC:260
            radix = 16;//LC:261
            pos ++;//LC:262
        } else if (str.startsWith("0", pos) && str.length() > pos + 1) { // octal; so long as there are additional digits//LC:263
            radix = 8;//LC:264
            pos ++;//LC:265
        } // default is to treat as decimal//LC:266
//LC:267
        final BigInteger value = new BigInteger(str.substring(pos), radix);//LC:268
        return negate ? value.negate() : value;//LC:269
    }//LC:270
//LC:271
    public static Long createLong(final String str) {//LC:272
        if (str == null) {//LC:273
            return null;//LC:274
        }//LC:275
        return Long.decode(str);//LC:276
    }//LC:277
//LC:278
    private static boolean isAllZeros(final String str) {//LC:279
        if (str == null) {//LC:280
            return true;//LC:281
        }//LC:282
        for (int i = str.length() - 1; i >= 0; i--) {//LC:283
            if (str.charAt(i) != '0') {//LC:284
                return false;//LC:285
            }//LC:286
        }//LC:287
        return str.length() > 0;//LC:288
    }//LC:289
//LC:290
    public static boolean isDigits(final String str) {//LC:291
        if (StringUtils.isEmpty(str)) {//LC:292
            return false;//LC:293
        }//LC:294
        for (int i = 0; i < str.length(); i++) {//LC:295
            if (!Character.isDigit(str.charAt(i))) {//LC:296
                return false;//LC:297
            }//LC:298
        }//LC:299
        return true;//LC:300
    }//LC:301
//LC:302
    public static BigDecimal createBigDecimal(final String str) {//LC:303
        if (str == null) {//LC:304
            return null;//LC:305
        }//LC:306
        // handle JDK1.3.1 bug where "" throws IndexOutOfBoundsException//LC:307
        if (StringUtils.isBlank(str)) {//LC:308
            throw new NumberFormatException("A blank string is not a valid number");//LC:309
        }//LC:310
        if (str.trim().startsWith("--")) {//LC:311
            // this is protection for poorness in java.lang.BigDecimal.//LC:312
            // it accepts this as a legal value, but it does not appear//LC:313
            // to be in specification of class. OS X Java parses it to//LC:314
            // a wrong value.//LC:315
            throw new NumberFormatException(str + " is not a valid number.");//LC:316
        }//LC:317
        return new BigDecimal(str);//LC:318
    }//LC:319
//LC:320
    public static Integer createInteger(final String str) {//LC:321
        if (str == null) {//LC:322
            return null;//LC:323
        }//LC:324
        // decode() handles 0xAABD and 0777 (hex and octal) as well.//LC:325
        return Integer.decode(str);//LC:326
    }//LC:327
//LC:328
    public static Float createFloat(final String str) {//LC:329
        if (str == null) {//LC:330
            return null;//LC:331
        }//LC:332
        return Float.valueOf(str);//LC:333
    }//LC:334
//LC:335
    public static Double createDouble(final String str) {//LC:336
        if (str == null) {//LC:337
            return null;//LC:338
        }//LC:339
        return Double.valueOf(str);//LC:340
    }//LC:341
}//LC:342
