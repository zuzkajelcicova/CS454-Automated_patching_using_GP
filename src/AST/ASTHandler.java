package AST;

import GP.Individual;
import GP.Patch;
import General.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ASTHandler {

    private static final List<String> ALLOWED_STATEMENTS = new ArrayList<>(Arrays.asList("if", "while", "for", "do",
            "break", "continue", "return", "switch", "assert", "empty_stmt", "expr_stmt"));
    private HashMap<Integer, NodePair> allAstStatements;
    private HashMap<Integer, NodePair> allAstStatementsModified;
    private HashMap<Integer, NodePair> candidateSpace;
    private NodeList ast;
    private Document doc;
    private Utils utils;
    private Parser parser;

    public ASTHandler(Utils utils, Parser parser) {
        this.utils = utils;
        this.parser = parser;

        //Extend original .java faulty program with line numbers
        createFileWithLineNumbers();
        allAstStatements = new HashMap<>();
        candidateSpace = new HashMap<>();
        //Create AST with line numbers
        ast = initializeAST(utils.FAULTY_XML_FILE_WITH_LINES);
        populateStatementList();
        resetAllAstStatementsModified();
        printStatementList();
        printCandidateSpace();
    }

    private void resetAllAstStatementsModified() {
        allAstStatementsModified = new HashMap<>();
        allAstStatementsModified.putAll(allAstStatements);
    }

    public NodeList getAst() {
        return ast;
    }

    public HashMap<Integer, NodePair> getAllAstStatements() {
        return allAstStatements;
    }

    public HashMap<Integer, NodePair> getCandidateSpace() {
        return candidateSpace;
    }

    private NodeList initializeAST(File file) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        NodeList finalAst = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            finalAst = doc.getElementsByTagName("*");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalAst;
    }

    public void printStatementList() {
        for (Map.Entry<Integer, NodePair> entry : allAstStatements.entrySet()) {
            System.out.println(utils.LINE_SEPARATOR + "--------------------------------------------------------------------");
            System.out.println("Statement ID : " + entry.getKey());
            System.out.println("Statement Line : " + entry.getValue().getLineNumber());
            System.out.println("Statement Content: " + utils.LINE_SEPARATOR + entry.getValue().getNode().getTextContent());
            System.out.println("--------------------------------------------------------------------" + utils.LINE_SEPARATOR);
        }
    }

    public void printCandidateSpace() {
        for (Map.Entry<Integer, NodePair> entry : candidateSpace.entrySet()) {
            System.out.println(utils.LINE_SEPARATOR + "--------------------------------------------------------------------");
            System.out.println("Candidate ID : " + entry.getKey());
            System.out.println("Candidate Line : " + entry.getValue().getLineNumber());
            System.out.println("Candidate Content: " + utils.LINE_SEPARATOR + entry.getValue().getNode().getTextContent());
            System.out.println("--------------------------------------------------------------------" + utils.LINE_SEPARATOR);
        }
    }

    private void populateStatementList() {
        for (int i = 0; i < ast.getLength(); i++) {
            Node node = ast.item(i);
            int lineNodeCounter = i;

            int lineNumber = findCorrespondingLine(node.getTextContent());
            while (lineNumber == -1 && ast.getLength() > lineNodeCounter + 1) {
                lineNodeCounter++;
                lineNumber = findCorrespondingLine((ast.item(lineNodeCounter)).getTextContent());
            }
            evaluateNode(i, node, lineNumber);
        }
    }

    private void evaluateNode(int id, Node node, int lineNumber) {
        if (ALLOWED_STATEMENTS.contains(node.getNodeName().toLowerCase())) {
            NodePair newNode = new NodePair(lineNumber, node);
            allAstStatements.put(id, newNode);

            if (!statementAlreadyExists(node.getTextContent()))
                candidateSpace.put(id, newNode);
        }
    }

    private int findCorrespondingLine(String nodeContent) {
        String regex = "//LC:\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(nodeContent);
        if (matcher.find()) {
            String substring = matcher.group();
            regex = "\\d+";
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(substring);
            return matcher.find() ? Integer.parseInt(matcher.group()) : -1;
        }
        return -1;
    }

    private boolean statementAlreadyExists(String contentToCheck) {
        boolean nodeExists = false;
        for (Map.Entry<Integer, NodePair> entry : candidateSpace.entrySet()) {
            if (entry.getValue().getNode().getTextContent().equals(contentToCheck)) {
                nodeExists = true;
                break;
            }
        }
        return nodeExists;
    }

    private void insertNode(int source, int target) {
        Node sourceNode = allAstStatementsModified.get(source).getNode();
        Node targetNode = allAstStatementsModified.get(target).getNode();
        Node parentNodeSource = sourceNode.getParentNode();
        Node parentNodeTarget = targetNode.getParentNode();

        if (parentNodeSource != null && parentNodeTarget != null) {
            Node clonedSourceNode = sourceNode.cloneNode(true);
            parentNodeTarget.insertBefore(clonedSourceNode, targetNode.getNextSibling());
        } else {
            System.out.println("Node(s) does not exist!");
        }
    }

    private void replaceNode(int source, int target) {
        Node sourceNode = allAstStatementsModified.get(source).getNode();
        Node targetNode = allAstStatementsModified.get(target).getNode();
        Node parentNodeSource = sourceNode.getParentNode();
        Node parentNodeTarget = targetNode.getParentNode();

        if (parentNodeSource != null && parentNodeTarget != null) {
            Node clonedSourceNode = sourceNode.cloneNode(true);
            parentNodeTarget.replaceChild(clonedSourceNode, targetNode);
        } else {
            System.out.println("Node(s) does not exist!");
        }
    }

    private void deleteNode(int target) {
        Node targetNode = allAstStatementsModified.get(target).getNode();
        Node parentNodeTarget = targetNode.getParentNode();

        if (parentNodeTarget != null) {
            parentNodeTarget.removeChild(targetNode);
        } else {
            System.out.println("Node does not exist!");
        }
    }

    public void applyPatches(Individual individual) {
        for (Patch currentPatch : individual.getAllPatches()) {
            int operation = currentPatch.getOperation();

            if (operation == utils.DELETE) {
                deleteNode(currentPatch.getTargetNode());
            } else if (operation == utils.REPLACE) {
                replaceNode(currentPatch.getSourceNode(), currentPatch.getTargetNode());
            } else if (operation == utils.INSERT) {
                insertNode(currentPatch.getSourceNode(), currentPatch.getTargetNode());
            } else {
                System.out.println("ERROR: unsupported mutation operation no. " + operation);
            }
        }
        domToXML(utils.FIXED_XML_FILE_PATH);
        resetAllAstStatementsModified();
    }

    public void domToXML(String filepath) {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);

            StringBuilder codeData = parser.parseFile(utils.FIXED_XML_FILE_PATH);
            parser.saveData(utils.SRC_DIRECTORY, utils.TARGET_CODE, codeData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private StringBuilder createCodeWithLineNumbers(String targetCode) {
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        Scanner scanner = null;
        StringBuilder result = new StringBuilder();
        int lineCounter = 0;
        String lineText = "//LC:";

        try {
            fileReader = new FileReader(targetCode);
            bufferedReader = new BufferedReader(fileReader);
            String sCurrentLine;
            scanner = new Scanner(targetCode);

            while (scanner.hasNext()) {
                sCurrentLine = bufferedReader.readLine();
                if (sCurrentLine != null) {
                    lineCounter++;
                    result.append(sCurrentLine).append(lineText + lineCounter).append(utils.LINE_SEPARATOR);
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
                if (fileReader != null)
                    fileReader.close();
                if (scanner != null)
                    scanner.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    private void createFileWithLineNumbers() {
        String targetCode = utils.TARGET_CODE_FILE_PATH;
        StringBuilder modifiedCodeWithLines = createCodeWithLineNumbers(targetCode);
        parser.saveData(utils.RESOURCES_DIRECTORY, utils.TARGET_CODE_WITH_LINES, modifiedCodeWithLines);

        //Original AST extended with line numbers
        StringBuilder xmlDataWithLines = parser.parseFile(utils.TARGET_CODE_FILE_PATH_WITH_LINES);
        parser.saveData(utils.OUTPUT_PARSED_DIRECTORY, utils.FAULTY_XML_WITH_LINES, xmlDataWithLines);
    }

    public StringBuilder removeCodeLines() {
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        Scanner scanner = null;
        StringBuilder result = new StringBuilder();
        String targetCode = utils.TARGET_CODE_FIXED_WITH_LINES_FILE_PATH;
        String regex = "//LC:\\d+";
        Pattern pattern = Pattern.compile(regex);

        try {
            fileReader = new FileReader(targetCode);
            bufferedReader = new BufferedReader(fileReader);
            String sCurrentLine;
            scanner = new Scanner(targetCode);

            while (scanner.hasNext()) {
                sCurrentLine = bufferedReader.readLine();
                if (sCurrentLine != null) {
                    Matcher matcher = pattern.matcher(sCurrentLine);

                    if (matcher.find()) {
                        String substring = matcher.group();
                        String newLine = sCurrentLine.replace(substring, "");
                        result.append(newLine).append(utils.LINE_SEPARATOR);
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
                if (fileReader != null)
                    fileReader.close();
                if (scanner != null)
                    scanner.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}