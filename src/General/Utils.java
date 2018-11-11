package General;

import java.io.File;

public class Utils {
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String OUTPUT_PARSED_DIRECTORY = "parsed";
    public static final String RESOURCES_DIRECTORY = "resources";
    public static final String SRC_DIRECTORY = "src";
    public static final String FAULTY_XML = "faulty.xml";
    public static final String FIXED_XML = "fixed.xml";

    //Buggy program as XML
    public static final File FAULTY_XML_FILE = new File(OUTPUT_PARSED_DIRECTORY, FAULTY_XML);
    public static final String FAULTY_XML_FILE_PATH = FAULTY_XML_FILE.getAbsolutePath();

    //Potentially fixed program as XML
    public static final File FIXED_XML_FILE = new File(OUTPUT_PARSED_DIRECTORY, FIXED_XML);
    public static final String FIXED_XML_FILE_PATH = FIXED_XML_FILE.getAbsolutePath();

    //Mutation operations
    public static final int DELETE = 0;
    public static final int REPLACE = 1;
    public static final int INSERT = 2;

    //Compile pass or fail
    public static final int PASS = 0;
    public static final int FAIL = 1;
}
