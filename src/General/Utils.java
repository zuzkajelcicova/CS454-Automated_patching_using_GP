package General;

import GP.Bug;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Utils {
    public String SRCML_PATH = "C:\\Program Files\\srcML 0.9.5\\bin";
    //Tested .class file name and location (default)
    public String TARGET_CLASS = "GCD.class";
    public String DOT_CLASS_FOLDER_PATH = "C:\\Users\\admin\\git\\CS454-Automated_patching_using_GP\\out\\production\\CS454_AutomatedPatching";

    public String LINE_SEPARATOR = System.getProperty("line.separator");
    public String OUTPUT_PARSED_DIRECTORY = "parsed";
    public String RESOURCES_DIRECTORY = "resources";
    public String SRC_DIRECTORY = "src";
    public String GEN_CANDIDATE_DIRECTORY = "GAOutput";
    public String SOLUTION_DIRECTORY = "correctPatch";
    public String FL_DIRECTORY = "fault_localization";
    public String FAULTY_XML = "faulty.xml";
    public String FAULTY_XML_WITH_LINES = "faultyWithLines.xml";
    public String FIXED_XML = "fixed.xml";
    public String FIXED_JAVA = "solution";
    public String FIXED_JAVA_STATISTICS = "stats";

    //GZoltar FL output
    public String FL_TARGET = "gzoltar.csv";
    public File FL_TARGET_FILE = new File(FL_DIRECTORY, FL_TARGET);
    public String FL_TARGET_FILE_PATH = FL_TARGET_FILE.getAbsolutePath();

    //Extracted lines and suspiciousness from GZoltar output file
    public String FL_EXTRACTED = "suspicious.csv";
    public File FL_EXTRACTED_FILE = new File(FL_DIRECTORY, FL_EXTRACTED);
    public String FL_EXTRACTED_FILE_PATH = FL_EXTRACTED_FILE.getAbsolutePath();

    //Buggy program as .java
    public String TARGET_CODE = "GCD.java";
    public File TARGET_CODE_FILE = new File(RESOURCES_DIRECTORY, TARGET_CODE);
    public String TARGET_CODE_FILE_PATH = TARGET_CODE_FILE.getAbsolutePath();

    //Target code in src/
    public File TARGET_CODE_SRC_FILE = new File(SRC_DIRECTORY, TARGET_CODE);
    public String TARGET_CODE_SRC_FILE_PATH = TARGET_CODE_SRC_FILE.getAbsolutePath();

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

    //Compile pass or fail
    public final int PASS = 0;
    public final int FAIL = 1;

    public final String positive = "Positive";
    public final String negative = "Negative";

    //Weight for Positive and Negative test cases
    public final double WEIGHT_POS = 0.1;
    public final double WEIGHT_NEG = 2 * WEIGHT_POS;

    public void obtainSuspiciousLines() {
        String line;
        String csvSplitComma = ",";
        String csvSplitHash = "#";
        String header = "line,probability" + LINE_SEPARATOR;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(header);

        try (BufferedReader br = new BufferedReader(new FileReader(FL_TARGET_FILE_PATH))) {
            while ((line = br.readLine()) != null) {
                if (line.contains(csvSplitHash)) {
                    //# separator to get data
                    String data = (line.split(csvSplitHash))[1];

                    // Obtain line and the probability
                    String[] codeLineAndProbability = data.split(csvSplitComma);
                    String codeLine = codeLineAndProbability[0];
                    double probability = Double.parseDouble(codeLineAndProbability[1] + "." + codeLineAndProbability[2]);
                    double rounded = Math.round(probability * 100.0) / 100.0;
                    stringBuilder.append(codeLine).append(csvSplitComma).append(rounded).append(LINE_SEPARATOR);
                }
            }
            saveData(FL_DIRECTORY, FL_EXTRACTED, stringBuilder);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<Bug> amountOfBugsToFix(int numberOfBugs) {
        String line;
        String csvSplitComma = ",";
        List<Bug> allBugs = new ArrayList<>();
        int lineCounter = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(FL_EXTRACTED_FILE_PATH))) {
            while ((line = br.readLine()) != null) {
                if (lineCounter != 0) {
                    String[] data = line.split(csvSplitComma);

                    // Obtain line and the probability
                    String codeLine = data[0];
                    double probability = Double.parseDouble(data[1]);
                    allBugs.add(new Bug(codeLine, probability));
                }
                lineCounter++;
            }
            return chooseLikelyBugs(numberOfBugs, allBugs);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Bug> chooseLikelyBugs(int numberOfBugs, List<Bug> allBugs) {
        allBugs = sortBugs(allBugs);
        List<Bug> chosenBugs = new ArrayList<>();
        int counter = 0;

        while (counter < numberOfBugs && allBugs != null && allBugs.size() > 0) {
            chosenBugs.add(allBugs.get(counter));
            counter++;
        }
        return chosenBugs;
    }

    public List<Bug> sortBugs(List<Bug> bugs) {
        List<Bug> sortedBugs = bugs.stream()
                .sorted(Comparator.comparing(Bug::getProbability).reversed())
                .collect(Collectors.toList());
        return sortedBugs;
    }

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

    public StringBuilder getCodeWithLines() {
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        Scanner scanner = null;
        StringBuilder result = new StringBuilder();
        String targetCode = TARGET_CODE_FIXED_WITH_LINES_FILE_PATH;

        try {
            fileReader = new FileReader(targetCode);
            bufferedReader = new BufferedReader(fileReader);
            String sCurrentLine;
            scanner = new Scanner(targetCode);

            while (scanner.hasNext()) {
                sCurrentLine = bufferedReader.readLine();
                if (sCurrentLine != null) {
                    result.append(sCurrentLine).append(LINE_SEPARATOR);
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
