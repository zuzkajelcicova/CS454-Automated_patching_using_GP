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
import java.io.File;
import java.util.*;

public class ASTHandler {

    private static final List<String> ALLOWED_STATEMENTS = new ArrayList<>(Arrays.asList("if", "while", "for", "do",
            "break", "continue", "return", "switch", "assert", "empty_stmt", "expr_stmt"));
    private HashMap<Integer, Node> allAstStatements;
    private HashMap<Integer, Node> allAstStatementsModified;
    private HashMap<Integer, Node> candidateSpace;
    private NodeList ast;
    private Document doc;
    private String TARGET_CODE;

    public ASTHandler(File file, String tCode) {
        setTargetCodeName(tCode);
        allAstStatements = new HashMap<>();
        candidateSpace = new HashMap<>();
        ast = initializeAST(file);
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

    public HashMap<Integer, Node> getAllAstStatements() {
        return allAstStatements;
    }

    public HashMap<Integer, Node> getCandidateSpace() {
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
        for (Map.Entry<Integer, Node> entry : allAstStatements.entrySet()) {
            System.out.println("Statement ID : " + entry.getKey());
            System.out.println("Statement content: " + Utils.LINE_SEPARATOR + entry.getValue().getTextContent());
            System.out.println("----------------------------------------------" + Utils.LINE_SEPARATOR);
        }
    }

    public void printCandidateSpace() {
        for (Map.Entry<Integer, Node> entry : candidateSpace.entrySet()) {
            System.out.println("Candidate ID : " + entry.getKey());
            System.out.println("Candidate: " + Utils.LINE_SEPARATOR + entry.getValue().getTextContent());
            System.out.println("----------------------------------------------" + Utils.LINE_SEPARATOR);
        }
    }

    private void populateStatementList() {
        for (int i = 0; i < ast.getLength(); i++) {
            Node node = ast.item(i);
            evaluateNode(i, node);
        }
    }

    private void evaluateNode(int id, Node node) {
        if (ALLOWED_STATEMENTS.contains(node.getNodeName().toLowerCase())) {
            allAstStatements.put(id, node);
            if (!statementAlreadyExists(node.getTextContent()))
                candidateSpace.put(id, node);
        }
    }

    private boolean statementAlreadyExists(String contentToCheck) {
        boolean nodeExists = false;
        for (Map.Entry<Integer, Node> entry : candidateSpace.entrySet()) {
            if (entry.getValue().getTextContent().equals(contentToCheck)) {
                nodeExists = true;
                break;
            }
        }
        return nodeExists;
    }

    private void insertNode(int source, int target) {
        Node sourceNode = allAstStatementsModified.get(source);
        Node targetNode = allAstStatementsModified.get(target);
        Node parentNodeSource = sourceNode.getParentNode();
        Node parentNodeTarget = targetNode.getParentNode();

        if (parentNodeSource != null && parentNodeTarget != null) {
            Node clonedSourceNode = sourceNode.cloneNode(true);
            parentNodeTarget.insertBefore(clonedSourceNode, targetNode);
        } else {
            System.out.println("Node(s) does not exist!");
        }
    }

    private void replaceNode(int source, int target) {
        Node sourceNode = allAstStatementsModified.get(source);
        Node targetNode = allAstStatementsModified.get(target);
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
        Node targetNode = allAstStatementsModified.get(target);
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

            if (operation == Utils.DELETE) {
                deleteNode(currentPatch.getTargetNode());
            } else if (operation == Utils.REPLACE) {
                replaceNode(currentPatch.getSourceNode(), currentPatch.getTargetNode());
            } else if (operation == Utils.INSERT) {
                insertNode(currentPatch.getSourceNode(), currentPatch.getTargetNode());
            } else {
                System.out.println("ERROR: unsupported mutation operation no. " + operation);
            }
        }
        domToXML(Utils.FIXED_XML_FILE_PATH);
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

            StringBuilder codeData = Parser.parseFile(Utils.FIXED_XML_FILE_PATH);
            Parser.saveData(Utils.SRC_DIRECTORY, TARGET_CODE, codeData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTargetCodeName(String targetCodeName) {
        this.TARGET_CODE = targetCodeName;
    }
}