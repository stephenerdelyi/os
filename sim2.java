import java.io.BufferedReader; //WriteFile, ReadFile, FileHandler http://www.avajava.com/tutorials/lessons/how-do-i-read-a-string-from-a-file-line-by-line.html
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays; //StringHelper https://stackoverflow.com/questions/7021074/string-delimiter-in-string-split-method
import java.util.Map; //FileHandler.verifyInputFile & FileHandler.verifyConfigFile
import java.util.HashMap;
import java.text.DecimalFormat; //Clock
import java.math.RoundingMode;

public class sim2 {
    static boolean allowFatalExecution = false;
    static TaskQueue taskList = new TaskQueue();
    static TaskStack taskStack = new TaskStack();
    static FileHandler fileHandler = new FileHandler();
    static Console console = new Console();
    static StringHelper stringHelper = new StringHelper();
    static ValidKeys validKeys = new ValidKeys();
    static PCB PCB = new PCB();
    static ConfigFile config;
    static Clock clock = new Clock();

    public static void main(String[] args) {
        /////////////////////////////////////////////////////
        //                 STARTUP ACTIONS                 //
        /////////////////////////////////////////////////////
        config = new ConfigFile(args[0]); //init the config file with the name provided from terminal parameter
        console.printDiv();
        if (FileHandler.verifyConfigFile()) {
            console.log("✓ Config file has been verified with no syntax errors");
            FileHandler.loadConfigFile();
            console.log("✓ Config file (v" + config.version + ") has been loaded [" + config.fileName + "]");
            if (FileHandler.verifyInputFile()) {
                console.log("✓ Input file has been verified with no syntax errors");
                FileHandler.loadInputFile();
                console.log("✓ Input file has been loaded [" + config.inputFileName + "]");
            }
        }
        console.printDiv();

        /////////////////////////////////////////////////////
        //                   SYSTEM READY                  //
        /////////////////////////////////////////////////////
        /*console.log("Configuration File Data");
        config.outputSettings();
        console.printNewline();

        console.log("Meta-Data Metrics");*/
        execute();

        console.printNewline();
    }

    //execute - processes all tasks in the queue according to the current OS specifications
    public static void execute() {
        int processCount = 0;
        //clock.start();
        //console.log(clock.getDurationTime() + " - Simulator program starting");
        //while there are tasks to complete
        while (!taskList.isEmpty()) {
            //remove the next task from the front of the queue
            Task currentTask = taskList.dequeue();

            if (currentTask.code == 'S') {
                if (currentTask.description.equals("start")) {
                    PCB.setProcessState("Start");
                    clock.start();
                    taskStack.push(currentTask);
                    console.log(clock.getDurationTime() + " - Simulator program starting");
                } else if (currentTask.description.equals("end")) {
                    PCB.setProcessState("Exit");
                    taskStack.pop();
                    console.log(clock.getDurationTime() + " - Simulator program ending");
                } else {
                    console.error("Invalid description \"" + currentTask.description + "\" for code '" + currentTask.code + "'");
                }
            } else if (currentTask.code == 'A') {
                if (currentTask.description.equals("start")) {
                    processCount++;
                    taskStack.push(currentTask);
                    console.log(clock.getDurationTime() + " - OS: preparing process " + processCount);
                    //should prepare process here
                    console.log(clock.getDurationTime() + " - OS: starting process " + processCount);
                    PCB.setProcessState("Ready");
                } else if (currentTask.description.equals("end")) {
                    taskStack.pop();
                    //should remove process here
                    console.log(clock.getDurationTime() + " - OS: removing process " + processCount);
                } else {
                    console.error("Invalid description \"" + currentTask.description + "\" for code '" + currentTask.code + "'");
                }
            } else if (currentTask.code == 'P') {
                PCB.setProcessState("Running");
                if (currentTask.description.equals("run")) {
                    console.log(clock.getDurationTime() + " - Process " + processCount + ": start processing action");
                    //should process action here
                    console.log(clock.getDurationTime() + " - Process " + processCount + ": end processing action");
                } else {
                    console.error("Invalid description \"" + currentTask.description + "\" for code '" + currentTask.code + "'");
                }
                PCB.setProcessState("Waiting");
            } else if (currentTask.code == 'M') {
                PCB.setProcessState("Running");
                if (currentTask.description.equals("allocate")) {
                    console.log(clock.getDurationTime() + " - Process " + processCount + ": allocating memory");
                    //sholuld allocate memory here
                    //should create hex value here
                    console.log(clock.getDurationTime() + " - Process " + processCount + ": memory allocated at 0x00000000");
                } else if (currentTask.description.equals("block")) {
                    console.log(clock.getDurationTime() + " - Process " + processCount + ": start memory blocking");
                    //should do memory blocking here
                    console.log(clock.getDurationTime() + " - Process " + processCount + ": end memory blocking");
                } else {
                    console.error("Invalid description \"" + currentTask.description + "\" for code '" + currentTask.code + "'");
                }
                PCB.setProcessState("Waiting");
            } else if (currentTask.code == 'O') {
                PCB.setProcessState("Running");
                if (currentTask.description.equals("monitor")) {
                    console.log(clock.getDurationTime() + " - Process " + processCount + ": start monitor output");
                    //should do monitor output thread here
                    console.log(clock.getDurationTime() + " - Process " + processCount + ": end monitor output");
                } else if (currentTask.description.equals("projector")) {
                    console.log(clock.getDurationTime() + " - Process " + processCount + ": start projector output");
                    //should do projector output thread here
                    console.log(clock.getDurationTime() + " - Process " + processCount + ": end projector output");
                } else if (currentTask.description.equals("hard drive")) {
                    console.log(clock.getDurationTime() + " - Process " + processCount + ": start hard drive output");
                    //should do hard drive output thread here
                    console.log(clock.getDurationTime() + " - Process " + processCount + ": end hard drive output");
                } else {
                    console.error("Invalid description \"" + currentTask.description + "\" for code '" + currentTask.code + "'");
                }
                PCB.setProcessState("Waiting");
            } else if (currentTask.code == 'I') {
                PCB.setProcessState("Running");
                if (currentTask.description.equals("keyboard")) {
                    console.log(clock.getDurationTime() + " - Process " + processCount + ": start keyboard input");
                    //should do keyboard input thread here
                    console.log(clock.getDurationTime() + " - Process " + processCount + ": end keyboard input");
                } else if (currentTask.description.equals("hard drive")) {
                    console.log(clock.getDurationTime() + " - Process " + processCount + ": start hard drive input");
                    //should do hard drive input thread here
                    console.log(clock.getDurationTime() + " - Process " + processCount + ": end hard drive input");
                } else {
                    console.error("Invalid description \"" + currentTask.description + "\" for code '" + currentTask.code + "'");
                }
                PCB.setProcessState("Waiting");
            }
        }

        if (!taskStack.isEmpty()) {
            console.printDiv();
            console.log("Stack printout:");
            taskStack.print();
            console.error("Invalid combination of S/A in runtime stack");
        }
    }

    /////////////////////////////////////////////////////
    //                 HELPER CLASSES                  //
    /////////////////////////////////////////////////////
    ///////////////////
    // Clock:
    ///////////////////
    public static class Clock {
        static long startTime;

        public static void start() {
            startTime = System.nanoTime();
        }

        public static String getDurationTime() {
            double val = (System.nanoTime() - startTime) * .000000001;
            DecimalFormat df = new DecimalFormat("#.######");
            String formattedString = df.format(val);
            String[] splitString = stringHelper.splitOnDelimeter(formattedString, "\\.");

            if (splitString[1].length() < 6) {
                int runFor = 6 - splitString[1].length();
                for (int i = 0; i < runFor; i++) {
                    splitString[1] = splitString[1] + "0"; //append a 0
                }
                formattedString = splitString[0] + "." + splitString[1];
            }
            return formattedString;
        }
    }
    ///////////////////
    // PCB:
    ///////////////////
    public static class PCB {
        static int processState = 0; //review setProcessState for integer values

        //setProcessState -
        public static <Generic> void setProcessState(Generic inputProcessState) {
            if (inputProcessState instanceof String) {
                int processStateIndex = stringHelper.findTokenIndexInArray(validKeys.processStateDeclarations, inputProcessState.toString());
                if (processStateIndex != -1) {
                    processState = processStateIndex;
                }
            } else if (inputProcessState instanceof Integer) {
                int processStateIndex = (Integer) inputProcessState;
                if (processStateIndex >= 0 && processStateIndex < validKeys.processStateDeclarations.length) {
                    processState = processStateIndex;
                }
            }
        }

        //getProcessStateInt -
        public static int getProcessStateInt() {
            return processState;
        }

        //getProcessStateString -
        public static String getProcessStateString() {
            return validKeys.processStateDeclarations[processState];
        }
    }
    ///////////////////
    // FileHandler: handles all file processing (excluding reading/writing)
    ///////////////////
    public static class FileHandler {
        static ReadFile fileReader = new ReadFile();
        static WriteFile fileWriter = new WriteFile();

        //verifyConfigFile - verify the configuration file
        public static boolean verifyConfigFile() {
            fileReader.read(config.fileName); //actually read the config file and store it in the class's string var
            String[] splitByLineBreak = stringHelper.splitOnDelimeter(fileReader.lastReadFile, "\\\n"); //array of strings containing each line read in from the config file
            String[] configTokenHistory = new String[validKeys.configKeyDeclarations.length]; //string array that is used to determine if there are too many/few assignments of a given config declaration
            System.arraycopy(validKeys.configKeyDeclarations, 0, configTokenHistory, 0, validKeys.configKeyDeclarations.length); //make a second copy of configKeyDeclarations tocd configTokenHistory since we need two working arrays to accomplish error checking
            int lineNum = 0; //used for error logging

            //verify the config file extension is valid
            String fileExtension = stringHelper.splitOnDelimeter(config.fileName, "\\.")[1];
            if (!fileExtension.equals("conf")) {
                console.error("Config file does not end in .conf");
            }

            //loop through each line in configuration file
            for (int i = 0; i < splitByLineBreak.length; i++) {
                lineNum++;
                //if the current line is not a comment
                if (stringHelper.substringIsInString(splitByLineBreak[i], ":")) {
                    String[] splitByColon = stringHelper.splitOnDelimeter(splitByLineBreak[i], ":[\\s]*"); //array of 2 cells - 1st is the left hand side of a config line (key), 2nd is the right hand side of a config line (value)
                    int returnedIndex = stringHelper.findTokenIndexInArray(validKeys.configKeyDeclarations, splitByColon[0]); //the index of the token found in configKeyDeclarations (= -1 if not found in array)

                    //if the token is not invalid
                    if (returnedIndex != -1) {
                        //if the token has already been used before
                        if (configTokenHistory[returnedIndex].equals("0")) {
                            console.error("Duplicate parameter declaration in " + config.fileName + " on line " + lineNum + ": \n  \"" + splitByColon[0] + "\"");
                        } else {
                            //remove the token from the history array so that it can't be used twice
                            configTokenHistory[returnedIndex] = "0";
                        }
                        //if it is of type ______, try to parse it & verify the validity of the value
                        if (validKeys.configTypeDeclarations[returnedIndex].equals("double")) {
                            try {
                                Double.parseDouble(splitByColon[1]);
                            } catch (Exception e) {
                                console.error("There was an error parsing the double provided in " + config.fileName + " on line " + lineNum + ": \n  \"" + splitByColon[1] + "\" next to declaration \"" + splitByColon[0] + "\"");
                            }
                        } else if (validKeys.configTypeDeclarations[returnedIndex].equals("int")) {
                            try {
                                Integer.parseInt(splitByColon[1]);
                            } catch (Exception e) {
                                console.error("There was an error parsing the int provided in " + config.fileName + " on line " + lineNum + ": \n  \"" + splitByColon[1] + "\" next to declaration \"" + splitByColon[0] + "\"");
                            }
                        } else if (validKeys.configTypeDeclarations[returnedIndex].equals("fileName")) {
                            if (!stringHelper.substringIsInString(splitByColon[1], ".")) {
                                console.error("There was an error parsing the filename provided in " + config.fileName + " on line " + lineNum + ": \n  \"" + splitByColon[1] + "\" next to declaration \"" + splitByColon[0] + "\"");
                            } else {
                                if (splitByColon[0].equals("Log File Path")) {
                                    //verify the log file extension is valid
                                    String logFileExtension = stringHelper.splitOnDelimeter(splitByColon[1], "\\.")[1];
                                    if (!logFileExtension.equals("lgf")) {
                                        console.error("Log file name does not end in .lgf");
                                    }
                                } else if (splitByColon[0].equals("File Path")) {
                                    //verify the input file extension is valid
                                    String inputFileExtension = stringHelper.splitOnDelimeter(splitByColon[1], "\\.")[1];
                                    if (!inputFileExtension.equals("mdf")) {
                                        console.error("Input file does not end in .mdf");
                                    }
                                }
                            }
                        } else if (validKeys.configTypeDeclarations[returnedIndex].equals("logOption")) {
                            if (!splitByColon[1].equals("Log to Both") && !splitByColon[1].equals("Log to Monitor") && !splitByColon[1].equals("Log to File")) {
                                console.error("There was an error parsing the log option provided in " + config.fileName + " on line " + lineNum + " (invalid option specified): \n  \"" + splitByColon[1] + "\" next to declaration \"" + splitByColon[0] + "\"");
                            }
                        }
                    } else {
                        console.error("Invalid parameter declaration in " + config.fileName + " on line " + lineNum + ": \n  \"" + splitByColon[0] + "\"");
                    }
                }
            }

            //make sure all the tokens were used
            for (int i = 0; i < configTokenHistory.length - 1; i++) {
                if (!configTokenHistory[i].equals("0")) {
                    console.error("Missing parameter declaration in " + config.fileName + ": \n  \"" + configTokenHistory[i] + "\"");
                }
            }

            return true;
        }
        //loadConfigFile - load the verified configuration file into the system
        public static void loadConfigFile() {
            String[] splitByLineBreak = stringHelper.splitOnDelimeter(fileReader.lastReadFile, "\\\n"); //array of strings containing each line read in from the config file

            //loop through each line in the file
            for (int i = 0; i < splitByLineBreak.length - 1; i++) {
                //if line is not a comment
                if (stringHelper.substringIsInString(splitByLineBreak[i], ":")) {
                    String[] splitByColon = stringHelper.splitOnDelimeter(splitByLineBreak[i], ":[\\s]*"); //array of 2 cells - 1st is the left hand side of a config line (key), 2nd is the right hand side of a config line (value)

                    //make begin and end count for 0 time
                    config.times.put("begin", 0);
                    config.times.put("finish", 0);

                    //check to find which config value should be filled
                    if (splitByColon[0].equals("Version/Phase")) {
                        config.version = Double.parseDouble(splitByColon[1]);
                    } else if (splitByColon[0].equals("File Path")) {
                        config.inputFileName = splitByColon[1];
                    } else if (splitByColon[0].equals("Monitor display time {msec}")) {
                        config.times.put("monitor", Integer.parseInt(splitByColon[1]));
                    } else if (splitByColon[0].equals("Processor cycle time {msec}")) {
                        config.times.put("run", Integer.parseInt(splitByColon[1]));
                    } else if (splitByColon[0].equals("Scanner cycle time {msec}")) {
                        config.times.put("scanner", Integer.parseInt(splitByColon[1]));
                    } else if (splitByColon[0].equals("Hard drive cycle time {msec}")) {
                        config.times.put("hard drive", Integer.parseInt(splitByColon[1]));
                    } else if (splitByColon[0].equals("Keyboard cycle time {msec}")) {
                        config.times.put("keyboard", Integer.parseInt(splitByColon[1]));
                    } else if (splitByColon[0].equals("Memory cycle time {msec}")) {
                        config.times.put("allocate", Integer.parseInt(splitByColon[1]));
                        config.times.put("block", Integer.parseInt(splitByColon[1]));
                    } else if (splitByColon[0].equals("Projector cycle time {msec}")) {
                        config.times.put("projector", Integer.parseInt(splitByColon[1]));
                    } else if (splitByColon[0].equals("Log")) {
                        config.logOption = splitByColon[1];
                    } else if (splitByColon[0].equals("Log File Path")) {
                        config.logFileName = splitByColon[1];
                    } else if (splitByColon[0].equals("System memory {kbytes}")) {
                        config.systemMemory = Integer.parseInt(splitByColon[1]);
                    } else {
                        //should never reach this after verifying the file
                        console.error("Invalid parameter declaration in " + config.fileName + ": \n  \"" + splitByColon[0] + "\"");
                    }
                }
            }

            //make the log file
            fileWriter.createFile(config.logFileName);
            //tell the user if there will be no further monitor/file output
            if (config.logOption.equals("Log to File")) {
                console.writeConsoleLog("✓ Output from this point on will show only in the log file", false);
            } else if (config.logOption.equals("Log to Monitor")) {
                console.writeFileLog("✓ Output from this point on will show only in the monitor window (log to monitor statement is in config file)", false);
            }
        }

        //verifyInputFile - verify the input file
        public static boolean verifyInputFile() {
            fileReader.read(config.inputFileName); //actually read the input file and store it in the class's string var
            String[] splitByLineBreak = stringHelper.splitOnDelimeter(fileReader.lastReadFile, "\\\n");
            int lineNum = 0;

            //make sure the file is not empty
            if (splitByLineBreak.length <= 1) {
                console.error("Input file is empty");
            }

            for (int i = 0; i < splitByLineBreak.length; i++) {
                lineNum++;
                if (stringHelper.substringIsInString(splitByLineBreak[i], "{")) {
                    //pre-process the line data to fix white space issues
                    splitByLineBreak[i] = stringHelper.removeBadWhitespace(splitByLineBreak[i], validKeys.inputDescriptorDeclarations);
                    String[] splitBySemiColon = stringHelper.splitOnDelimeter(splitByLineBreak[i], "\\;|\\.");

                    for (int j = 0; j < splitBySemiColon.length; j++) {
                        //check to make sure the string matches the general regex pattern
                        if (!splitBySemiColon[j].matches("^[A-Z][\\s]*\\{[a-z]+(\\ *[a-z]*)\\}[\\s]*\\d+")) {
                            console.error("Syntax error in \"" + config.inputFileName + "\" on line " + lineNum + " for process:\n  \"" + splitBySemiColon[j] + "\"");
                        }

                        //define the items based off of the resulting string
                        String metaCode = splitBySemiColon[j].substring(0,1);
                        String numCycles = splitBySemiColon[j].replaceAll("\\D+","");
                        String descriptor = splitBySemiColon[j];
                        descriptor = descriptor.substring(descriptor.indexOf("{") + 1);
                        descriptor = descriptor.substring(0, descriptor.indexOf("}"));

                        //check the values individually
                        if (stringHelper.findTokenIndexInArray(validKeys.inputMetaCodeDeclarations, metaCode) == -1) {
                            console.error("Invalid meta code in \"" + config.inputFileName + "\" on line " + lineNum + " for process:\n  \""+ metaCode + "\" in \"" + splitBySemiColon[j] + "\"");
                        }
                        if (stringHelper.findTokenIndexInArray(validKeys.inputDescriptorDeclarations, descriptor) == -1) {
                            console.error("Invalid descriptor value in \"" + config.inputFileName + "\" on line " + lineNum + " for process:\n  \"" + descriptor + "\" in \"" + splitBySemiColon[j] + "\"");
                        }
                        try {
                            Integer.parseInt(numCycles);
                        } catch (Exception e) {
                            console.error("There was an error parsing the numCycles int in \"" + config.inputFileName + "\" on line " + lineNum + " for process:\n\"" + splitBySemiColon[j] + "\"");
                        }
                    }
                }
            }

            return true;
        }

        //loadInputFile - load the verified input file into the system
        public static void loadInputFile() {
            String[] splitByLineBreak = stringHelper.splitOnDelimeter(fileReader.lastReadFile, "\\\n");

            for (int i = 0; i < splitByLineBreak.length; i++) {
                if (stringHelper.substringIsInString(splitByLineBreak[i], "{")) {
                    //pre-process the line data to fix white space issues
                    splitByLineBreak[i] = stringHelper.removeBadWhitespace(splitByLineBreak[i], validKeys.inputDescriptorDeclarations);
                    String[] splitBySemiColon = stringHelper.splitOnDelimeter(splitByLineBreak[i], "\\;|\\.");

                    for (int j = 0; j < splitBySemiColon.length; j++) {
                        //define the items based off of the resulting string
                        char metaCode = splitBySemiColon[j].substring(0,1).charAt(0);
                        int numCycles = Integer.parseInt(splitBySemiColon[j].replaceAll("\\D+",""));
                        String descriptor = splitBySemiColon[j];
                        descriptor = descriptor.substring(descriptor.indexOf("{") + 1);
                        descriptor = descriptor.substring(0, descriptor.indexOf("}"));
                        //make a new (temp) task and add the data to it
                        Task newTask = new Task();
                        newTask.code = metaCode;
                        newTask.numCycles = numCycles;
                        newTask.description = descriptor;
                        //add the temp task to the task list
                        taskList.enqueue(newTask);
                    }
                }
            }
        }
    }

    ///////////////////
    // TaskQueue: A queue consisting of objects of type Task; contains all typical queue functions
    ///////////////////
    public static class TaskQueue {
        static int numAllowed = 150;
        static int numCreated = 0;
        static Task[] dataArray = new Task[numAllowed];

        //enqueue - takes the input task and adds it to the queue
        public static boolean enqueue(Task newTask) {
            if (!isFull()) {
                dataArray[numCreated] = new Task();
                dataArray[numCreated].code = newTask.code;
                dataArray[numCreated].description = newTask.description;
                dataArray[numCreated].numCycles = newTask.numCycles;
                numCreated++;
                return true;
            } else {
                console.error("Ran out of queue space - increase numAllowed");
            }
            //should never reach this return
            return false;
        }

        //dequeue - takes the task at the front of the queue and returns it
        public static Task dequeue() {
            if (!isEmpty()) {
                Task returnTask = dataArray[0];
                for (int i = 1; i < numCreated; i++) {
                    dataArray[i - 1] = dataArray[i];
                }
                dataArray[numCreated] = null;
                numCreated--;
                return returnTask;
            } else {
                Task nullTask = new Task();
                return nullTask;
            }
        }

        //peek - returns the task at the front of the queue but doesn't remove it
        public static Task peek() {
            if (!isEmpty()) {
                return dataArray[0];
            } else {
                Task nullTask = new Task();
                return nullTask;
            }
        }

        //print - outputs the contents of the task queue and the data inside it
        public static void print() {
            for (int i = 0; i < numCreated; i++) {
                int j = i + 1;
                char code = dataArray[i].code;
                String description = dataArray[i].description;
                int numCycles = dataArray[i].numCycles;
                console.log("Task " + j + ": " + code + " - " + description + " - " + numCycles);
            }
        }

        //isFull - returns whether or not the queue is full
        private static boolean isFull() {
            if (numCreated >= numAllowed) {
                return true;
            }
            return false;
        }

        //isEmpty - returns whether or not the queue is empty
        private static boolean isEmpty() {
            if (numCreated == 0) {
                return true;
            }
            return false;
        }
    }

    ///////////////////
    // TaskStack: A stack consisting of objects of type Task; contains all typical stack functions
    ///////////////////
    public static class TaskStack {
        static int numAllowed = 150;
        static int numCreated = 0;
        static Task[] dataArray = new Task[numAllowed];

        //push - takes the input task and adds it to the stack
        public static boolean push(Task newTask) {
            if (!isFull()) {
                dataArray[numCreated] = new Task();
                dataArray[numCreated].code = newTask.code;
                dataArray[numCreated].description = newTask.description;
                dataArray[numCreated].numCycles = newTask.numCycles;
                numCreated++;
                return true;
            } else {
                console.error("Ran out of queue space - increase numAllowed");
            }
            //should never reach this return
            return false;
        }

        //pop - takes the task at the top of the stack and returns it
        public static Task pop() {
            if (!isEmpty()) {
                Task returnTask = dataArray[numCreated - 1];
                dataArray[numCreated - 1] = null;
                numCreated--;
                return returnTask;
            } else {
                Task nullTask = new Task();
                return nullTask;
            }
        }

        //peek - returns the task at the top of the stack but doesn't remove it
        public static Task peek() {
            if (!isEmpty()) {
                return dataArray[numCreated - 1];
            } else {
                Task nullTask = new Task();
                return nullTask;
            }
        }

        //print - outputs the contents of the task stack and the data inside it
        public static void print() {
            for (int i = 0; i < numCreated; i++) {
                int j = i + 1;
                char code = dataArray[i].code;
                String description = dataArray[i].description;
                int numCycles = dataArray[i].numCycles;
                console.log("Task " + j + ": " + code + " - " + description + " - " + numCycles);
            }
        }

        //isFull - returns whether or not the stack is full
        private static boolean isFull() {
            if (numCreated >= numAllowed) {
                return true;
            }
            return false;
        }

        //isEmpty - returns whether or not the stack is empty
        private static boolean isEmpty() {
            if (numCreated == 0) {
                return true;
            }
            return false;
        }
    }

    ///////////////////
    // Console: contains important output functions to the monitor or log file; also contains the error and crash functions
    ///////////////////
    public static class Console {
        static WriteFile fileWriter = new WriteFile();

        //log - contains all important logic for logging to the monitor, log file, or both
        public static void log(String echoStatement, boolean isFatal) {
            if (config.logOption != null) {
                if (config.logOption.equals("Log to File")) {
                    writeFileLog(echoStatement, isFatal);
                } else if (config.logOption.equals("Log to Monitor")) {
                    writeConsoleLog(echoStatement, isFatal);
                } else if (config.logOption.equals("Log to Both")) {
                    writeConsoleLog(echoStatement, isFatal);
                    writeFileLog(echoStatement, isFatal);
                }
            } else {
                //since config file hasn't loaded yet, just output to console
                writeConsoleLog(echoStatement, isFatal);
            }

            //after we write everything, if the log was fatal we should ask to crash
            if (isFatal) {
                //if we allow fatal executions
                if (allowFatalExecution) {
                    //prompt the user to continue
                    console.writeConsoleLog("Continue execution? (Y/N)", false);
                    String name = System.console().readLine();
                    //if the user does not want to continue, then crash
                    if (!name.equals("y") && !name.equals("Y")) {
                        crash();
                    }
                } else {
                    crash();
                }
            }
        }

        //writeConsoleLog - writes a log message to the monitor only
        public static void writeConsoleLog(String echoStatement, boolean isFatal) {
            if (!isFatal) {
                System.out.println((char)27 + "[39m" + echoStatement);
            } else {
                System.out.println((char)27 + "[39m");
                System.out.println((char)27 + "[31m" + "✖ FATAL ERROR: " + echoStatement);
                System.out.println((char)27 + "[39m");
            }
        }

        //writeFileLog - writes a log message to the log file only
        public static void writeFileLog(String echoStatement, boolean isFatal) {
            if (!isFatal) {
                fileWriter.write(config.logFileName, true, echoStatement);
            } else {
                fileWriter.write(config.logFileName, true, "✖ FATAL ERROR: " + echoStatement);
            }
        }

        //log - parameter override for cleaner log statement outside class
        public static void log(String echoStatement) {
            log(echoStatement, false);
        }

        //error - writes a log message with isFatal set to true - will crash program
        public static void error(String echoStatement) {
            log(echoStatement, true);
        }

        //printDiv - prints a divider to the monitor or log file
        public static void printDiv() {
            log("=======================================================", false);
        }

        //printNewline - prints a new line to the monitor or log file
        public static void printNewline() {
            log(" ", false);
        }

        //crash - crashes the program prematurely so that the files can not be processed after a syntax error is found
        private static void crash() {
            System.exit(0);
        }
    }

    ///////////////////
    // ConfigFile: Holds all data for the system's configuration file
    ///////////////////
    public static class ConfigFile {
        static String fileName;
        static double version;
        static int systemMemory;
        static String inputFileName;
        static String logOption;
        static String logFileName;
        static Map<String, Integer> times = new HashMap<>();

        //ConfigFile (constructor) - requires that the config file is named to proceed
        ConfigFile(String configFileName) {
            super();
            if (configFileName != "" && configFileName != null) {
                fileName = configFileName;
            } else {
                console.log("Config file is not named", true);
            }
        }

        //outputSettings - output the configuration settings
        public static void outputSettings() {
            console.log("Monitor = " + times.get("monitor") + " ms/cycle");
            console.log("Processor = " + times.get("run") + " ms/cycle");
            console.log("Scanner = " + times.get("scanner") + " ms/cycle");
            console.log("Hard Drive = " + times.get("hard drive") + " ms/cycle");
            console.log("Keyboard = " + times.get("keyboard") + " ms/cycle");
            console.log("Memory = " + times.get("allocate") + " ms/cycle");
            console.log("Projector = " + times.get("projector") + " ms/cycle");
            if (logOption.equals("Log to Both")) {
                console.log("Logged to monitor and " + logFileName);
            } else if (logOption.equals("Log to Monitor")) {
                console.log("Logged to monitor");
            } else if (logOption.equals("Log to File")) {
                console.log("Logged to " + logFileName);
            }
        }
    }

    ///////////////////
    // Task: object data structure that represents a system task
    ///////////////////
    public static class Task {
        char code;
        String description;
        int numCycles;
    }

    ///////////////////
    // ValidKeys: holds all the valid keys or tokens for the configuration file and data input file (makes managing possible inputs easier)
    ///////////////////
    public static class ValidKeys {
        String[] configKeyDeclarations = {"Version/Phase","File Path","Monitor display time {msec}","Processor cycle time {msec}","Scanner cycle time {msec}","Hard drive cycle time {msec}","Keyboard cycle time {msec}","Memory cycle time {msec}","Projector cycle time {msec}","System memory {kbytes}|System memory {Mbytes}|System memory {Gbytes}","Log","Log File Path"}; //a string array of all valid config declarations for error checking
        String[] configTypeDeclarations = {"double","fileName","int","int","int","int","int","int","int","int","logOption","fileName"}; //a string array of datatypes that correspond to the entires in configKeyDeclarations used for error checking
        String[] inputMetaCodeDeclarations = {"S","A","P","M","O","I"}; //string (easier than char) array that contains all valid input file meta code values
        String[] inputDescriptorDeclarations = {"run","start","allocate","monitor","hard drive","scanner","projector","block","keyboard","end"}; //string array that contains all valid input file description values
        String[] processStateDeclarations = {"Start", "Ready", "Running", "Waiting", "Exit"};
    }

    ///////////////////
    // StringHelper: contains many helper fuctions for strings used throughout portions of the program
    ///////////////////
    public static class StringHelper {
        //splitOnDelimeter - split the input string into an array based on the delimiter
        public static String[] splitOnDelimeter(String inputString, String delimiter) {
            String[] data = inputString.split(delimiter);
            return data;
        }

        //substringIsInString - check to see if the given sub string exists within the input string
        public static boolean substringIsInString(String inputString, String subString) {
            if (inputString.toLowerCase().contains(subString.toLowerCase())) {
                return true;
            }
            return false;
        }

        //findTokenIndexInArray - loops through the input array to see if the token to find exists within it
        public static int findTokenIndexInArray(String[] inputArray, String tokenToFind) {
            for (int i = 0; i < inputArray.length; i++) {
                if (inputArray[i].equals(tokenToFind)) {
                    return i;
                }
            }
            return -1;
        }

        //removeBadWhitespace - removes all white space except for that which descriptor tokens are found
        public static String removeBadWhitespace(String inputString, String[] validKeys) {
            //first remove all white space from the line
            inputString = inputString.replaceAll("[\\s]*","");

            //loop through all valid keys
            for (int i = 0; i < validKeys.length; i++) {
                //if the valid key even contains a space
                if (substringIsInString(validKeys[i], " ")) {
                    String keyWithSpace = validKeys[i];
                    String keyWithoutSpace = validKeys[i].replaceAll(" ", "");
                    //if the key is present in the string without spaces
                    if (substringIsInString(inputString, keyWithoutSpace)) {
                        //replace with spaces
                        inputString = inputString.replaceAll(keyWithoutSpace, keyWithSpace);
                    }
                }
            }

            return inputString;
        }
    }

    ///////////////////
    // ReadFile: file reader used to read input and config file content
    ///////////////////
    public static class ReadFile {
        public static String lastReadFile;

        //read - read the file and store it to the lastReadFile variable
        public static void read(String fileName) {
    	    try {
                File file = new File(fileName);
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                	stringBuffer.append(line);
                	stringBuffer.append("\n");
                }
                fileReader.close();
                lastReadFile = stringBuffer.toString();
            } catch (IOException e) {
                console.error("The filename \"" + fileName + "\" does not exist or is corrupted");
            }
    	}
    }

    ///////////////////
    // WriteFile: file writer used to write output log file content
    ///////////////////
    public static class WriteFile {
        //createFile - creates a file named by the input string
        public static void createFile(String fileName) {
            write(fileName, false, "");
        }

        //write - writes output to the given file name
    	public static void write(String fileName, boolean appendMode, String inputString) {
    		BufferedWriter bw = null;
    		FileWriter fw = null;

    		try {
    			fw = new FileWriter(fileName, appendMode);
    			bw = new BufferedWriter(fw);
                if (inputString != "") {
                    bw.write(inputString + "\n");
                }
    		} catch (IOException e) {
                console.error("The file \"" + fileName + "\" can not be written to because it could not be opened or it is corrupted");
    		} finally {
    			try {
    				if (bw != null)
    					bw.close();
    				if (fw != null)
    					fw.close();
    			} catch (IOException ex) {
                    console.error("The file \"" + fileName + "\" can not be written to because it could not be opened or it is corrupted");
    			}
    		}
    	}
    }
}
