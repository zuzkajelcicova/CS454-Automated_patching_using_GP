package AST;

import GP.Bug;
import GP.Individual;
import GP.Patch;
import General.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
    // HashMap<Integer, Integer> => NodeID, LineNumber, Keeping NodePair for debugging purposes
    private HashMap<Integer, NodePair> allAstStatements;
    private HashMap<Integer, NodePair> candidateSpace;
    private HashMap<Integer, NodePair> faultSpace;
    private NodeList ast;
    private NodeList astOriginal;
    private Document doc;
    private Document docOriginal;
    private Utils utils;
    private Parser parser;

    public ASTHandler(Utils utils, Parser parser, List<Bug> bugs) {
        this.utils = utils;
        this.parser = parser;

        //Extract AST
        extractAst();

        //Extend original .java faulty program with line numbers
        createFileWithLineNumbers();
        allAstStatements = new HashMap<>();
        candidateSpace = new HashMap<>();
        faultSpace = new HashMap<>();

        //Create AST with line numbers
        doc = initializeDoc(utils.FAULTY_XML_FILE_WITH_LINES, doc);
        ast = initializeAST(doc);

        //Create DOC and AST copy for resetting
        docOriginal = initializeDoc(utils.FAULTY_XML_FILE_WITH_LINES, docOriginal);
        astOriginal = initializeAST(docOriginal);

        populateStatementList();
        populateFaultSpace(bugs);
        removeBugsFromCandidateSpace();

        printStatementList();
        printCandidateSpace();
        printFaultSpace();
    }

    private void extractAst() {
        //Original AST
        StringBuilder xmlData = parser.parseFile(utils.TARGET_CODE_FILE_PATH);
        utils.saveData(utils.OUTPUT_PARSED_DIRECTORY, utils.FAULTY_XML, xmlData);
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

    private Document initializeDoc(File file, Document doc) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    private NodeList initializeAST(Document doc) {
        return doc.getElementsByTagName("*");
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

    public void printFaultSpace() {
        for (Map.Entry<Integer, NodePair> entry : faultSpace.entrySet()) {
            System.out.println(utils.LINE_SEPARATOR + "--------------------------------------------------------------------");
            System.out.println("Fault ID : " + entry.getKey());
            System.out.println("Fault Line : " + entry.getValue().getLineNumber());
            System.out.println("Fault Content: " + utils.LINE_SEPARATOR + entry.getValue().getNode().getTextContent());
            System.out.println("--------------------------------------------------------------------" + utils.LINE_SEPARATOR);
        }
    }

    private void removeBugsFromCandidateSpace() {
        for (Map.Entry<Integer, NodePair> currentStatement : faultSpace.entrySet()) {
            candidateSpace.remove(currentStatement.getKey());
        }
    }

    private void populateStatementList() {
        for (int i = 0; i < astOriginal.getLength(); i++) {
            Node node = astOriginal.item(i);
            int lineNodeCounter = i;

            int lineNumber = findCorrespondingLine(node.getTextContent());
            while (lineNumber == -1 && astOriginal.getLength() > lineNodeCounter + 1) {
                lineNodeCounter++;
                lineNumber = findCorrespondingLine((astOriginal.item(lineNodeCounter)).getTextContent());
            }
            evaluateNode(i, node, lineNumber);
        }
    }

    private void evaluateNode(int id, Node node, int lineNumber) {
        if (ALLOWED_STATEMENTS.contains(node.getNodeName().toLowerCase())) {
            NodePair newNode = new NodePair(lineNumber, node.cloneNode(true));
            allAstStatements.put(id, newNode);
            candidateSpace.put(id, newNode);

            //todo: we might be excluding statements that are the same (but that is already looking at the context)
            /*if (!statementAlreadyExists(node.getTextContent()))
                candidateSpace.put(id, newNode);*/
        }
    }

    private void populateFaultSpace(List<Bug> bugs) {
        for (Bug currentBug : bugs) {
            for (Map.Entry<Integer, NodePair> currentStatement : allAstStatements.entrySet()) {
                if (currentBug.getCodeLine().equalsIgnoreCase(String.valueOf(currentStatement.getValue().getLineNumber()))) {
                    faultSpace.put(currentStatement.getKey(), currentStatement.getValue());
                }
            }
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

    private void insertNode(int source, Node targetNode) {
        Node sourceNode = doc.importNode(astOriginal.item(source), true);
        Node parentNodeTarget = targetNode.getParentNode();

        if (parentNodeTarget != null) {
            Node clonedSourceNode = sourceNode.cloneNode(true);
            parentNodeTarget.insertBefore(clonedSourceNode, targetNode.getNextSibling());
        } else {
            System.out.println("Node: " + utils.LINE_SEPARATOR + targetNode.getTextContent() + utils.LINE_SEPARATOR + " does not exist!");
        }
    }

    private void replaceNode(int source, Node targetNode) {
        Node sourceNode = doc.importNode(astOriginal.item(source), true);
        Node parentNodeTarget = targetNode.getParentNode();

        if (parentNodeTarget != null) {
            Node clonedSourceNode = sourceNode.cloneNode(true);
            parentNodeTarget.replaceChild(clonedSourceNode, targetNode);
        } else {
            System.out.println("Node: " + utils.LINE_SEPARATOR + targetNode.getTextContent() + utils.LINE_SEPARATOR + " does not exist!");
        }
    }

    private void deleteNode(Node targetNode) {
        Node parentNodeTarget = targetNode.getParentNode();

        if (parentNodeTarget != null) {
            parentNodeTarget.removeChild(targetNode);
        } else {
            System.out.println("Node: " + utils.LINE_SEPARATOR + targetNode.getTextContent() + utils.LINE_SEPARATOR + " does not exist!");
        }
    }

    public void applyPatches(Individual individual) {
        List<Node> originalNodeReferences = storeReferencesToNodes(individual);

        for (int i = 0; i < individual.getAllPatches().size(); i++) {
            Patch currentPatch = individual.getAllPatches().get(i);
            Node currentTargetNode = originalNodeReferences.get(i);
            int operation = currentPatch.getOperation();

            if (operation == utils.DELETE) {
                deleteNode(currentTargetNode);
            } else if (operation == utils.REPLACE) {
                replaceNode(currentPatch.getSourceNode(), currentTargetNode);
            } else if (operation == utils.INSERT) {
                insertNode(currentPatch.getSourceNode(), currentTargetNode);
            } else {
                System.out.println("ERROR: unsupported mutation operation no. " + operation);
            }
        }
        domToXML(utils.FIXED_XML_FILE_PATH);
        resetAST();
    }

    //In case we had multiple targets - otherwise we can store a reference to 1 target Node (1 bug)
    private List<Node> storeReferencesToNodes(Individual individual) {
        List<Node> originalNodeReferences = new ArrayList<>();

        for (Patch currentPatch : individual.getAllPatches()) {
            originalNodeReferences.add(ast.item(currentPatch.getTargetNode()));
        }
        return originalNodeReferences;
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
            utils.saveData(utils.GEN_CANDIDATE_DIRECTORY, utils.TARGET_CODE, codeData);

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
        utils.saveData(utils.RESOURCES_DIRECTORY, utils.TARGET_CODE_WITH_LINES, modifiedCodeWithLines);

        //Original AST extended with line numbers
        StringBuilder xmlDataWithLines = parser.parseFile(utils.TARGET_CODE_FILE_PATH_WITH_LINES);
        utils.saveData(utils.OUTPUT_PARSED_DIRECTORY, utils.FAULTY_XML_WITH_LINES, xmlDataWithLines);
    }

    public void resetAST() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Node originalRoot = docOriginal.getDocumentElement();

        doc = db.newDocument();
        Node copiedRoot = doc.importNode(originalRoot, true);
        doc.appendChild(copiedRoot);
        ast = initializeAST(doc);
    }
}