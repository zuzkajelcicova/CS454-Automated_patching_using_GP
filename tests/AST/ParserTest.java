package AST;

import General.Utils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class ParserTest {
    private static String srcMl;
    private static String targetCode;
    private static String dotClassFolderPath;
    private static Utils utils;
    private static Parser parser;
    private static final String TARGET_TEST_CODE = "GCDTest.java";
    private static final String TARGET_TEST_CODE_XML = "GCDTestXML.xml";
    private static final String TARGET_TEST_CODE_XML_RESULT = "GCDTestXMLResult.xml";
    private static final String DIR = "tests/AST/resources/";

    @Before
    public void setupEnvironment() {
        srcMl = "C:\\Program Files\\srcML 0.9.5\\bin";
        targetCode = "GCD";
        dotClassFolderPath = "C:\\Users\\admin\\git\\CS454-Automated_patching_using_GP\\out\\production\\CS454_AutomatedPatching";

        utils = new Utils(srcMl, targetCode + ".java", dotClassFolderPath, targetCode + ".class");
        parser = new Parser(utils);
    }

    @Test
    public void parseFile() throws IOException {
        File targetCodeFile = new File(DIR, TARGET_TEST_CODE);
        File targetCodeFileXML = new File(DIR, TARGET_TEST_CODE_XML);
        String targetCodeFilePath = targetCodeFile.getAbsolutePath();
        String targetCodeXMLFilePath = targetCodeFileXML.getAbsolutePath();

        StringBuilder result = parser.parseFile(targetCodeFilePath);
        utils.saveData(DIR, TARGET_TEST_CODE_XML_RESULT, result);

        StringBuilder expected = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(targetCodeXMLFilePath))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                expected.append(sCurrentLine);
            }
        }

        StringBuilder resultRead = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(targetCodeXMLFilePath))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                resultRead.append(sCurrentLine);
            }
        }

        Assert.assertTrue(expected.toString().equalsIgnoreCase(resultRead.toString()));
    }
}