import java.io.*;

public class Parser {

    public StringBuilder parseFile(String filePath, String srcMlPath) {

        StringBuilder parsedData = null;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("srcml", filePath);

            processBuilder.directory(new File(srcMlPath));
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
