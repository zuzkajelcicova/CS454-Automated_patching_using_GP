package General;

import java.io.File;

public class Utils {
    public String SRCML_PATH = "nothing";
    public String LINE_SEPARATOR = System.getProperty("line.separator");
    public String OUTPUT_PARSED_DIRECTORY = "parsed";
    public String RESOURCES_DIRECTORY = "resources";
    public String SRC_DIRECTORY = "src";
    public String GEN_CANDIDATE_DIRECTORY = "src/GeneratedCandidate";
    public String FAULTY_XML = "faulty.xml";
    public String FAULTY_XML_WITH_LINES = "faultyWithLines.xml";
    public String FIXED_XML = "fixed.xml";

    //Buggy program as .java
    public String TARGET_CODE = "LeapYear.java";
    public File TARGET_CODE_FILE = new File(RESOURCES_DIRECTORY, TARGET_CODE);
    public String TARGET_CODE_FILE_PATH = TARGET_CODE_FILE.getAbsolutePath();

    //Buggy program as .java with code lines
    public String TARGET_CODE_WITH_LINES = "CodeWithLines.java";
    public File TARGET_CODE_FILE_WITH_LINES = new File(RESOURCES_DIRECTORY, TARGET_CODE_WITH_LINES);
    public String TARGET_CODE_FILE_PATH_WITH_LINES = TARGET_CODE_FILE_WITH_LINES.getAbsolutePath();

    //Potentially fixed .java  with lines
    public File TARGET_CODE_FIXED_WITH_LINES_FILE = new File(SRC_DIRECTORY, TARGET_CODE);
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
}
