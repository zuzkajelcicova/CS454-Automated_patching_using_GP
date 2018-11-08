package AST;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Parser {
    private static final String SRCML_PATH = "C:\\Program Files\\srcML 0.9.5\\bin";

    public static StringBuilder parseFile(String filePath) {

        StringBuilder parsedData = null;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("srcml", filePath);

            processBuilder.directory(new File(SRCML_PATH));
            Process process = processBuilder.start();

            InputStream inputStream = process.getInputStream();
            int i;
            parsedData = new StringBuilder();
            while ((i = inputStream.read()) != -1) {
                parsedData.append((char) i);
            }
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        return parsedData;
    }

    public static void saveData(String dir, String outputFile, StringBuilder data) {
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

    public void runFromCMD(String inputFile, String outputFile) {

        String command = "srcml " + inputFile + " -o " + outputFile;
        ProcessBuilder xmlBuilder = new ProcessBuilder(
                "cmd.exe", "/c", command);
        xmlBuilder.redirectErrorStream(true);
        Process p = null;

        try {
            p = xmlBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
