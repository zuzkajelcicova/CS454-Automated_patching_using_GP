# CS454-Automated_patching_using_GP

The project contains the following directories:
- **documentation**
    - project execution plan 
    - diagrams
    - report summarizing the project outcome
- **parsed**
    - stores AST tree as an .xml file. Three types of .xml files are always generated:
       - file representing the original faulty program
       - file representing the original faulty program extended with line numbers
       - file that is a potential fix for the faulty program
- **resources**
    - stores 2 types of .java files
        - faulty program(s) submitted for repairing
        - faulty program submitted for repairing that is extended with line numbers
- **src**
    - **AST**
        - contains classes responsible for AST manipulation (replace/insert/delete), generation and conversion from .java to .xml and vice versa, populating statement/candidate/faulty lists (temporary), extending AST and .java code with line numbers, translation of the output of GZoltar to AST nodes (in progress), ...
    - **General**
        - contains a Utils.java class serving as a reference point for file names, paths, allowed operations, ...
    - **GP**
        - contains classes necessary for genetic algorithm such as definition of Individual in the population, and Patch (a signle edit) 
    - Main.java and potentially fixed .java program
- **test**
    - **Development** 
        - JUnit tests for the system
    - **Fitness**
         - JUnit tests for evaluating fitness
   
## Running the system

In order to run the current version of the system, instructions below must be followed. Currently, Main.java hardcodes mutation operations that should be performed on "faulty" resources/LeapYear.java for testing purposes. Change these if you want to test another file/modifications. 

    1. Install SRCML https://www.srcml.org/#download for parsing the .java and .xml code
    2. Run Main.java with the following papameters: 
       "faultyProgram.java pathToSrcmlTool -p initialPopulationSize -f allowedNumberOfFitnessEvaluations -t maxRunTimeInMins".
       
       A specific example can be seen below:
       LeapYear.java "C:\\Program Files\\srcML 0.9.5\\bin" -p 50 -f 1000 -t 90
       
       -p, -f, -t are currently not used (in progress).
    3. The potentially fixed program can be found in the src/ directory. Its name matches the faulty program name.

## Usage of GZoltar

### Usage as Eclipse Plugin
    1.  Download Java 8 32 Bit and Eclipse Mars 32 bit
    2. Follow steps mentioned here: http://www.gzoltar.com/eclipse-plugin.html
    3. Open new project
    4. Press CTRL + F5 to run GZoltar
    5. Output files of Gzoltar are created in folder "gzoltar" in the project directory 
    
### Usage as CLI
    1. Use: "com.gzoltar-0.0.11-jar-with-dependencies.java" in fault_localization\fl-main\start-gzoltar
    2. Run: "java -jar ./com.gzoltar-0.0.11-jar-with-dependencies.jar projectPath ProjectName classDirectory"
    3. Run (Example): "java -jar ./com.gzoltar-0.0.11-jar-with-dependencies.jar ../src/triangle triangle target/classes/:target/test-classes"
    4. Output files of Gzoltar are created in folder "gzoltar" in the project directory 
