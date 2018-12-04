package General;

import GP.Bug;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Test dependency on gzoltar.csv, suspicious.csv, GCD.java
 */

public class UtilsTest {

    private static List<String> allLines;
    private static String srcMl;
    private static String targetCode;
    private static String dotClassFolderPath;
    private static Utils utils;

    @BeforeClass
    public static void setupEnvironment() {
        allLines = new ArrayList<>();
        allLines.add("line,probability");
        allLines.add("3,0.0");
        allLines.add("6,0.0");
        allLines.add("7,0.0");
        allLines.add("10,0.0");
        allLines.add("11,1.0");
        allLines.add("13,0.58");
        allLines.add("15,0.0");
        allLines.add("16,0.91");
        allLines.add("17,0.71");
        allLines.add("18,0.71");
        allLines.add("19,0.82");
        allLines.add("20,0.82");
        allLines.add("23,0.41");
        allLines.add("24,0.41");

        srcMl = "C:\\Program Files\\srcML 0.9.5\\bin";
        targetCode = "GCD";
        dotClassFolderPath = "C:\\Users\\admin\\git\\CS454-Automated_patching_using_GP\\out\\production\\CS454_AutomatedPatching";
        utils = new Utils(srcMl, targetCode + ".java", dotClassFolderPath, targetCode + ".class");
    }

    @Test
    public void obtainSuspiciousLines() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(utils.FL_EXTRACTED_FILE_PATH))) {
            String sCurrentLine;
            int loopCounter = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                Assert.assertTrue(allLines.get(loopCounter).equalsIgnoreCase(sCurrentLine));
                loopCounter++;
            }
        }
    }

    @Test
    public void amountOfBugsToFix() {
        int deltaPrecision = 0;
        List<Bug> lessBugsThanExisting = utils.amountOfBugsToFix(3);
        //11,1.0;
        Assert.assertEquals("11", lessBugsThanExisting.get(0).getCodeLine());
        Assert.assertEquals(1.0, lessBugsThanExisting.get(0).getProbability(), deltaPrecision);
        //16,0.91
        Assert.assertEquals("16", lessBugsThanExisting.get(1).getCodeLine());
        Assert.assertEquals(0.91, lessBugsThanExisting.get(1).getProbability(), deltaPrecision);
        //19,0.82
        Assert.assertEquals("19", lessBugsThanExisting.get(2).getCodeLine());
        Assert.assertEquals(0.82, lessBugsThanExisting.get(2).getProbability(), deltaPrecision);

        List<Bug> moreBugsThanExisting = utils.amountOfBugsToFix(100);
        Assert.assertEquals(allLines.size() - 1, moreBugsThanExisting.size());
    }
}