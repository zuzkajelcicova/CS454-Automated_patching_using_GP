package General;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public String SRCML_PATH = "ProvideYourOwnPath";
    public String LINE_SEPARATOR = System.getProperty("line.separator");
    public String OUTPUT_PARSED_DIRECTORY = "parsed";
    public String RESOURCES_DIRECTORY = "resources";
    public String SRC_DIRECTORY = "src";
    public String GEN_CANDIDATE_DIRECTORY = "GAOutput";
    public String FL_DIRECTORY = "FL";
    public String FAULTY_XML = "faulty.xml";
    public String FAULTY_XML_WITH_LINES = "faultyWithLines.xml";
    public String FIXED_XML = "fixed.xml";

    //GZoltar FL output
    public String FL_TARGET_FILE = "gzoltar.csv";

    //Buggy program as .java
    public String TARGET_CODE = "LeapYear.java";
    public File TARGET_CODE_FILE = new File(RESOURCES_DIRECTORY, TARGET_CODE);
    public String TARGET_CODE_FILE_PATH = TARGET_CODE_FILE.getAbsolutePath();

    //Buggy program as .java with code lines
    public String TARGET_CODE_WITH_LINES = "CodeWithLines.java";
    public File TARGET_CODE_FILE_WITH_LINES = new File(RESOURCES_DIRECTORY, TARGET_CODE_WITH_LINES);
    public String TARGET_CODE_FILE_PATH_WITH_LINES = TARGET_CODE_FILE_WITH_LINES.getAbsolutePath();

    //Potentially fixed .java  with lines
    public File TARGET_CODE_FIXED_WITH_LINES_FILE = new File(GEN_CANDIDATE_DIRECTORY, TARGET_CODE);
    public String TARGET_CODE_FIXED_WITH_LINES_FILE_PATH = TARGET_CODE_FIXED_WITH_LINES_FILE.getAbsolutePath();

    //Buggy program as XML
    public File FAULTY_XML_FILE = new File(OUTPUT_PARSED_DIRECTORY, FAULTY_XML);
    public String FAULTY_XML_FILE_PATH = FAULTY_XML_FILE.getAbsolutePath();

    //Buggy program as XML extended with line numbers
    public File FAULTY_XML_FILE_WITH_LINES = new File(OUTPUT_PARSED_DIRECTORY, FAULTY_XML_WITH_LINES);
    public String FAULTY_XML_FILE_PATH_WITH_LINES = FAULTY_XML_FILE_WITH_LINES.getAbsolutePath();

    //Potentially fixed program as XML
    public File FIXED_XML_FILE = new File(OUTPUT_PARSED_DIRECTORY, FIXED_XML);
    public String FIXED_XML_FILE_PATH = FIXED_XML_FILE.getAbsolutePath();

    //Mutation operations
    public int DELETE = 0;
    public int REPLACE = 1;
    public int INSERT = 2;


    public void saveData(String dir, String outputFile, StringBuilder data) {
        try {
            File newFile = new File(dir, outputFile);
            String filepath = newFile.getAbsolutePath();
            Files.deleteIfExists(Paths.get(filepath));

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

    public StringBuilder removeCodeLines() {
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        Scanner scanner = null;
        StringBuilder result = new StringBuilder();
        String targetCode = TARGET_CODE_FIXED_WITH_LINES_FILE_PATH;
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
                        result.append(newLine).append(LINE_SEPARATOR);
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
