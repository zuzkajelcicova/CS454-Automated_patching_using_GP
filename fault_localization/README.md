# README

The branch contains the fault localization done by gzoltar framework.

- /fl-main
    - gzoltarCli: not finished yet
    - gzoltarEclipse: Main.java extarcts lines and suspicioness from spectra file
- /fl-test_code
    - chrysler: maven project to test gzoltar 
    - triangle: maven project to test gzoltar
    - Both projects serve testing purposes and can be used in Eclipse 
    

## How-To gzoltar

    1. Get Java 8 JDK/JRE (make sure it's a 32-bit version)
    2. Install Eclipse Mars (also 32-bit version)
    3. Path Variables:
        a. JAVA_HOME: Folder to your Java distribution, e.g. C:\Programms\Java-8-jdk-32-bit
        b. PATH to %JAVA_HOME%\bin
    4. Eclipse: Help -> Install new software -> Work with: http://gzoltar.com/web/eclipse-plugin/ and download this plugin
    5. Open the project *chrysler* or *triangle* as maven project and execute them, also execute the JUnit tests
    6. Gzoltar website says: "The plugin diagnoses all opened projects on Eclipse", but I experienced its working only with one open project, so close the other projects and use [CTRL] + [F5] to executes the gzoltar plugin. 
    7. Open App.java in Eclipse, you should see annotation from gzoltar on the left side
    8. Open File *spectra* with any Texteditor in *gzoltar-data* folder and have a look on the results
    9. Use /fl-main/fault_localization/fl-main/start-gzoltar/src/gzoltarEclipse/Main.java to extract the results into a List

Some information on the described plug-in are in http://gzoltar.com/eclipse-plugin.html
