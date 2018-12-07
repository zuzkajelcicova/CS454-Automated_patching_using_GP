package AST;

import GP.Bug;
import GP.Edit;
import GP.Patch;
import General.Utils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ASTHandlerTest {
    private static String srcMl;
    private static String targetCode;
    private static String dotClassFolderPath;
    private static Utils utils;
    private static Parser parser;
    private static ASTHandler astHandler;
    private static final int gcdReturnStatementID = 156;
    private static final String gcdReturnStatementContent = "return gcd;";
    private static final String node111Content = "a = a - b;";
    private static final int node111ID = 111;
    private static final String TARGET_TEST_CODE = "GCDTest.java";
    private static final String TARGET_TEST_CODE_LINES = "GCDTestLines.txt";
    private static final String DIR = "tests/AST/resources/";

    /**
     * Test dependency on gzoltar.csv and GCD.java
     */

    @Before
    public void setupEnvironment() {
        srcMl = "C:\\Program Files\\srcML 0.9.5\\bin";
        targetCode = "GCD";
        dotClassFolderPath = "C:\\Users\\admin\\git\\CS454-Automated_patching_using_GP\\out\\production\\CS454_AutomatedPatching";

        List<Bug> allBugs = new ArrayList<>();
        allBugs.add(new Bug("13", 0.57));
        allBugs.add(new Bug("11", 1.00));
        allBugs.add(new Bug("18", 0.82));

        utils = new Utils(srcMl, targetCode + ".java", dotClassFolderPath, targetCode + ".class");
        parser = new Parser(utils);
        astHandler = new ASTHandler(utils, parser, allBugs);
    }

    @Test
    public void resetAST() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method methodDocument = ASTHandler.class.getDeclaredMethod("initializeDoc", File.class, Document.class);
        methodDocument.setAccessible(true);
        Document doc = null;
        doc = (Document) (methodDocument.invoke(astHandler, utils.FAULTY_XML_FILE_WITH_LINES, doc));

        Method methodInitialize = ASTHandler.class.getDeclaredMethod("initializeAST", Document.class);
        methodInitialize.setAccessible(true);
        NodeList originalAST = (NodeList) (methodInitialize.invoke(astHandler, doc));

        //Introduce change
        Node targetNode = astHandler.getAst().item(gcdReturnStatementID); //return GCD;

        Method methodInsert = ASTHandler.class.getDeclaredMethod("insertNode", int.class, Node.class);
        methodInsert.setAccessible(true);
        methodInsert.invoke(astHandler, gcdReturnStatementID, targetNode);

        int result = 0;
        //Comparison - not equal
        if (astHandler.getAst().getLength() == originalAST.getLength()) {
            result = 1;
        }

        Assert.assertEquals(0, result);

        Method methodReset = ASTHandler.class.getDeclaredMethod("resetAST");
        methodReset.setAccessible(true);
        methodReset.invoke(astHandler);

        //Comparison - equal
        result = 1;
        for (int i = 0; i < originalAST.getLength(); i++) {
            if (!originalAST.item(i).getTextContent().equalsIgnoreCase(astHandler.getAst().item(i).getTextContent())) {
                result = 0;
                break;
            }
        }
        Assert.assertEquals(1, result);
    }

    @Test
    public void removeBugsFromCandidateSpace() throws NoSuchMethodException {
        Method method = ASTHandler.class.getDeclaredMethod("removeBugsFromCandidateSpace");
        method.setAccessible(true);

        //Before removal
        Assert.assertFalse(astHandler.getCandidateSpace().containsKey(13));
        Assert.assertFalse(astHandler.getCandidateSpace().containsKey(11));
        Assert.assertFalse(astHandler.getCandidateSpace().containsKey(18));
    }

    @Test
    public void findCorrespondingLine() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = ASTHandler.class.getDeclaredMethod("findCorrespondingLine", String.class);
        method.setAccessible(true);
        int lineNumber = (int) (method.invoke(astHandler, "return gcd;//LC:11"));
        int lineNumberNotFound = (int) (method.invoke(astHandler, gcdReturnStatementContent));

        Assert.assertEquals(11, lineNumber);
        Assert.assertEquals(-1, lineNumberNotFound);
    }

    @Test
    public void insertNode() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int originalLength = astHandler.getAst().getLength();
        Node targetNode = astHandler.getAst().item(gcdReturnStatementID); //return GCD;
        Method methodInsert = ASTHandler.class.getDeclaredMethod("insertNode", int.class, Node.class);
        methodInsert.setAccessible(true);
        methodInsert.invoke(astHandler, gcdReturnStatementID, targetNode);

        Assert.assertNotEquals(originalLength, astHandler.getAst().getLength());
        Assert.assertTrue(astHandler.getAst().item(159).getTextContent().equalsIgnoreCase(astHandler.getAst().item(gcdReturnStatementID).getTextContent())); //node 159
    }

    @Test
    public void replaceNode() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Assert.assertEquals(gcdReturnStatementContent, astHandler.getAst().item(gcdReturnStatementID).getTextContent());

        Node targetNode = astHandler.getAst().item(gcdReturnStatementID); //return GCD;
        Method methodReplace = ASTHandler.class.getDeclaredMethod("replaceNode", int.class, Node.class);
        methodReplace.setAccessible(true);
        methodReplace.invoke(astHandler, node111ID, targetNode); // node 111 => a = a - b;

        Assert.assertNotEquals(gcdReturnStatementContent, astHandler.getAst().item(gcdReturnStatementID).getTextContent());
        Assert.assertTrue(astHandler.getAst().item(gcdReturnStatementID).getTextContent().equalsIgnoreCase(node111Content));
    }

    @Test
    public void deleteNode() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int originalLength = astHandler.getAst().getLength();
        Assert.assertEquals(gcdReturnStatementContent, astHandler.getAst().item(gcdReturnStatementID).getTextContent());

        Node targetNode = astHandler.getAst().item(gcdReturnStatementID); //return GCD;
        Method methodDelete = ASTHandler.class.getDeclaredMethod("deleteNode", Node.class);
        methodDelete.setAccessible(true);
        methodDelete.invoke(astHandler, targetNode);

        Assert.assertNotEquals(originalLength, astHandler.getAst().getLength());
        Assert.assertNotEquals(gcdReturnStatementContent, astHandler.getAst().item(gcdReturnStatementID).getTextContent());
    }

    @Test
    public void storeReferencesToNodes() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Patch patch = new Patch();
        patch.getAllEdits().add(new Edit(1, -1, gcdReturnStatementID)); //line 22
        patch.getAllEdits().add(new Edit(2, gcdReturnStatementID, node111ID)); //line 22, line 15

        Method method = ASTHandler.class.getDeclaredMethod("storeReferencesToNodes", Patch.class);
        method.setAccessible(true);
        List<Node> nodes = (List<Node>) (method.invoke(astHandler, patch));
        Assert.assertEquals(gcdReturnStatementContent, nodes.get(0).getTextContent());
        Assert.assertEquals(node111Content, nodes.get(1).getTextContent());
    }

    @Test
    public void createCodeWithLineNumbers() {
        File targetCodeFile = new File(DIR, TARGET_TEST_CODE);
        File targetCodeFileLines = new File(DIR, TARGET_TEST_CODE_LINES);
        String targetCodeFilePath = targetCodeFile.getAbsolutePath();
        String targetCodeLinesFilePath = targetCodeFileLines.getAbsolutePath();

        try {
            StringBuilder expected = new StringBuilder();
            FileReader fileReader = new FileReader(targetCodeLinesFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String sCurrentLine;
            Scanner scanner = new Scanner(targetCodeLinesFilePath);

            while (scanner.hasNext()) {
                sCurrentLine = bufferedReader.readLine();
                if (sCurrentLine != null) {
                    expected.append(sCurrentLine).append(utils.LINE_SEPARATOR);
                } else {
                    break;
                }
            }

            Method methodCodeWithLines = ASTHandler.class.getDeclaredMethod("createCodeWithLineNumbers", String.class);
            methodCodeWithLines.setAccessible(true);
            StringBuilder result = (StringBuilder) methodCodeWithLines.invoke(astHandler, targetCodeFilePath);
            Assert.assertTrue(expected.toString().equalsIgnoreCase(result.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}