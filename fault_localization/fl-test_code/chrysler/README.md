# README

## How-To gzoltar Eclipse-plugin

Using GZoltar as an Eclipse Plugin, there is an experimental Eclipse plugin.

    1. Get Java 8 JDK/JRE (make sure it's a 32-bit version)
    2. Install Eclipse Mars (also 32-bit version)
    3. Path Variables:
        a. JAVA_HOME: Folder to your Java distribution, e.g. C:\Programms\Java-8-jdk-32-bit
        b. PATH to %JAVA_HOME%\bin
    4. Eclipse: Help -> Install new software -> Work with: http://gzoltar.com/web/eclipse-plugin/ and download this plugin
    5. Open the project *chrysler* or *triangle* as maven project and execute with Goal: compile, also execute the JUnit tests
    6. Gzoltar website says: "The plugin diagnoses all opened projects on Eclipse", but I experienced its working only with one open project, so close the other projects and use [CTRL] + [F5] or click on *Refresh List* in the gzoltar plugin window to executes the gzoltar plugin. 
    7. Open App.java in Eclipse, you should see annotation from gzoltar on the left side
    8. Open File *spectra* with any Texteditor in *gzoltar-data* folder and have a look on the results

Some information on the described plug-in are in http://gzoltar.com/eclipse-plugin.html
