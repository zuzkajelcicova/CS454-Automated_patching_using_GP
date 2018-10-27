import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Main {

    public static void main(String[] args) {
        File testFile = new File("resources", "LeapYear.java");
        String filePath = testFile.getAbsolutePath();

        String outputDirectory = "parsed";
        String srcMlPath = "C:\\Program Files\\srcML 0.9.5\\bin";

        String outputXml = "testXml.xml";
        String outputCode = "testJava.java";


        Parser parser = new Parser();

        StringBuilder xmlData = parser.parseFile(filePath, srcMlPath);
        saveData(outputDirectory, outputXml, xmlData);
        System.out.println("Program in AST XML format: \n" + xmlData.toString());

        StringBuilder codeData = parser.parseFile((new File(outputDirectory, outputXml)).getAbsolutePath(), srcMlPath);
        saveData(outputDirectory, outputCode, codeData);
        System.out.println("Program in CODE format: \n" + codeData.toString());


        //parser.runFromCMD(filePath, outputXml);
        //parser.runFromCMD(outputXml, outputCode);
    }

    public static void saveData(String dir, String outputFile, StringBuilder data) {
        try {
            File newFile = new File(dir, outputFile);
            String filepath = newFile.getAbsolutePath();

            newFile.createNewFile();

            FileWriter fileWriter = new FileWriter(filepath);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.append(data.toString());

            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}
