import java.io.BufferedReader; //WriteFile, ReadFile, FileHandler http://www.avajava.com/tutorials/lessons/how-do-i-read-a-string-from-a-file-line-by-line.html
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays; //StringHelper https://stackoverflow.com/questions/7021074/string-delimiter-in-string-split-method
import java.util.Map; //FileHandler.verifyInputFile & FileHandler.verifyConfigFile
import java.util.HashMap;

public class sim1 {
    static boolean allowFatalExecution = false;
    static TaskQueue taskList = new TaskQueue();
    static FileHandler fileHandler = new FileHandler();
    static ReadFile fileReader = new ReadFile();
    static WriteFile fileWriter = new WriteFile();
    static Console console = new Console();
    static StringHelper stringHelper = new StringHelper();
    static ConfigFile config;

    public static void main(String[] args) {
        /////////////////////////////////////////////////////
        //                 STARTUP ACTIONS                 //
        /////////////////////////////////////////////////////
        config = new ConfigFile(args[0]);
        console.printDiv();
        if (FileHandler.verifyConfigFile()) {
            console.log("✓ Config file has been verified with no syntax errors");
            FileHandler.loadConfigFile();
            console.log("✓ Config file (v" + config.version + ") has been loaded");
            if (FileHandler.verifyInputFile()) {
                console.log("✓ Input file has been verified with no syntax errors");
                FileHandler.loadInputFile();
                console.log("✓ Input file has been loaded");
            }
        }
        console.printDiv();

        /////////////////////////////////////////////////////
        //                   SYSTEM READY                  //
        /////////////////////////////////////////////////////
        console.log("Configuration File Data");
        config.outputSettings();
        console.printNewline();

        console.log("Meta-Data Metrics");
        taskList.execute();
        console.printNewline();
    }

    public static class FileHandler {
        //verifyConfigFile - verify the configuration file
        public static boolean verifyConfigFile() {
            String[] splitByLineBreak = stringHelper.splitOnDelimeter(fileReader.read(config.fileName), "\\\n"); //array of strings containing each line read in from the config file
            String[] validInputDeclarations = {"Version/Phase","File Path","Monitor display time {msec}","Processor cycle time {msec}","Scanner cycle time {msec}","Hard drive cycle time {msec}","Keyboard cycle time {msec}","Memory cycle time {msec}","Projector cycle time {msec}","Log","Log File Path",""}; //a string array of all valid config declarations for error checking
            String[] validTypeDeclarations = {"double","fileName","int","int","int","int","int","int","int","logOption","fileName"}; //a string array of datatypes that correspond to the entires in validInputDeclarations used for error checking
            String[] validTokenHistory = new String[validInputDeclarations.length]; //string array that is used to determine if there are too many/few assignments of a given config declaration
            System.arraycopy(validInputDeclarations, 0, validTokenHistory, 0, validInputDeclarations.length); //make a second copy of validInputDeclarations in validTokenHistory since we need two working arrays to accomplish error checking
            int lineNum = 0; //used for error logging

            //loop through each line in configuration file
            for (int i = 0; i < splitByLineBreak.length; i++) {
                lineNum++;
                //if the current line is not a comment
                if (stringHelper.substringIsInString(splitByLineBreak[i], ":")) {
                    String[] splitByColon = stringHelper.splitOnDelimeter(splitByLineBreak[i], "\\:\\ "); //array of 2 cells - 1st is the left hand side of a config line (key), 2nd is the right hand side of a config line (value)
                    int returnedIndex = stringHelper.findTokenIndexInArray(validInputDeclarations, splitByColon[0]); //the index of the token found in validInputDeclarations (= -1 if not found in array)

                    //if the token is not invalid
                    if (returnedIndex != -1) {
                        //if the token has already been used before
                        if (validTokenHistory[returnedIndex].equals("0")) {
                            console.log("Duplicate parameter declaration in " + config.fileName + " on line " + lineNum + ": \n  \"" + splitByColon[0] + "\"", true);
                        } else {
                            //remove the token from the history array so that it can't be used twice
                            validTokenHistory[returnedIndex] = "0";
                        }
                        //if it is of type ______, try to parse it & verify the validity of the value
                        if (validTypeDeclarations[returnedIndex].equals("double")) {
                            try {
                                Double.parseDouble(splitByColon[1]);
                            } catch (Exception e) {
                                console.log("There was an error parsing the double provided in " + config.fileName + " on line " + lineNum + ": \n  \"" + splitByColon[1] + "\" next to declaration \"" + splitByColon[0] + "\"", true);
                            }
                        } else if (validTypeDeclarations[returnedIndex].equals("int")) {
                            try {
                                Integer.parseInt(splitByColon[1]);
                            } catch (Exception e) {
                                console.log("There was an error parsing the int provided in " + config.fileName + " on line " + lineNum + ": \n  \"" + splitByColon[1] + "\" next to declaration \"" + splitByColon[0] + "\"", true);
                            }
                        } else if (validTypeDeclarations[returnedIndex].equals("fileName")) {
                            if (!stringHelper.substringIsInString(splitByColon[1], ".")) {
                                console.log("There was an error parsing the filename provided in " + config.fileName + " on line " + lineNum + ": \n  \"" + splitByColon[1] + "\" next to declaration \"" + splitByColon[0] + "\"", true);
                            }
                        } else if (validTypeDeclarations[returnedIndex].equals("logOption")) {
                            if (!splitByColon[1].equals("Log to Both") && !splitByColon[1].equals("Log to Console") && !splitByColon[1].equals("Log to File")) {
                                console.log("There was an error parsing the log option provided in " + config.fileName + " on line " + lineNum + ": \n  \"" + splitByColon[1] + "\" next to declaration \"" + splitByColon[0] + "\"", true);
                            }
                        }
                    } else {
                        console.log("Invalid parameter declaration in " + config.fileName + " on line " + lineNum + ": \n  \"" + splitByColon[0] + "\"", true);
                    }
                }
            }

            //make sure all the tokens were used
            for (int i = 0; i < validTokenHistory.length - 1; i++) {
                if (!validTokenHistory[i].equals("0")) {
                    console.log("Missing parameter declaration in " + config.fileName + " on line " + lineNum + ": \n  \"" + validTokenHistory[i] + "\"", true);
                }
            }

            return true;
        }
        //loadConfigFile - load the configuration file into the system
        public static void loadConfigFile() {
            String[] splitByLineBreak = stringHelper.splitOnDelimeter(fileReader.read(config.fileName), "\\\n"); //array of strings containing each line read in from the config file

            //loop through each line in the file
            for (int i = 0; i < splitByLineBreak.length - 1; i++) {
                //if line is not a comment
                if (stringHelper.substringIsInString(splitByLineBreak[i], ":")) {
                    String[] splitByColon = stringHelper.splitOnDelimeter(splitByLineBreak[i], "\\:\\ "); //array of 2 cells - 1st is the left hand side of a config line (key), 2nd is the right hand side of a config line (value)

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
                        //tell the user if there will be no further console output
                        if (config.logOption.equals("Log to File")) {
                            console.writeConsoleLog("✓ Output from this point on will show only in the log file", false);
                        }
                    } else if (splitByColon[0].equals("Log File Path")) {
                        config.logFileName = splitByColon[1];
                    } else {
                        //should never reach this after verifying the file
                        console.log("Invalid parameter declaration in " + config.fileName + ": \n  \"" + splitByColon[0] + "\"", true);
                    }
                }
            }

            //make the log file
            fileWriter.createFile(config.logFileName);
        }

        //verifyInputFile - verify the input file
        public static boolean verifyInputFile() {
            String[] splitByLineBreak = stringHelper.splitOnDelimeter(fileReader.read(config.inputFileName), "\\\n");
            String[] validMetaCodeInputDeclarations = {"S","A","P","M","O","I"};
            String[] validDescriptorDeclarations = {"run","begin","allocate","monitor","hard drive","scanner","projector","block","keyboard","finish"};
            int lineNum = 0;

            for (int i = 0; i < splitByLineBreak.length; i++) {
                lineNum++;
                if (stringHelper.substringIsInString(splitByLineBreak[i], ";")) {
                    String[] splitBySemiColon = stringHelper.splitOnDelimeter(splitByLineBreak[i], "\\;\\ |\\;");

                    for (int j = 0; j < splitBySemiColon.length; j++) {
                        //check to make sure the string matches the general regex pattern
                        if (!splitBySemiColon[j].matches("^[A-Z]\\{[a-z]+(\\ *[a-z]*)\\}\\d+")) {
                            console.log("Syntax error in \"" + config.inputFileName + "\" on line " + lineNum + " for process:\n  \"" + splitBySemiColon[j] + "\"", true);
                        }

                        //define the items based off of the resulting string
                        String metaCode = splitBySemiColon[j].substring(0,1);
                        String numCycles = splitBySemiColon[j].replaceAll("\\D+","");
                        String descriptor = splitBySemiColon[j];
                        descriptor = descriptor.substring(descriptor.indexOf("{") + 1);
                        descriptor = descriptor.substring(0, descriptor.indexOf("}"));

                        //check the values individually
                        if (stringHelper.findTokenIndexInArray(validMetaCodeInputDeclarations, metaCode) == -1) {
                            console.log("Invalid meta code in \"" + config.inputFileName + "\" on line " + lineNum + " for process:\n  \""+ metaCode + "\" in \"" + splitBySemiColon[j] + "\"", true);
                        }
                        if (stringHelper.findTokenIndexInArray(validDescriptorDeclarations, descriptor) == -1) {
                            console.log("Invalid descriptor value in \"" + config.inputFileName + "\" on line " + lineNum + " for process:\n  \"" + descriptor + "\" in \"" + splitBySemiColon[j] + "\"", true);
                        }
                        try {
                            Integer.parseInt(numCycles);
                        } catch (Exception e) {
                            console.log("There was an error parsing the numCycles int in \"" + config.inputFileName + "\" on line " + lineNum + " for process:\n\"" + splitBySemiColon[j] + "\"", true);
                        }
                    }
                }
            }

            return true;
        }

        public static void loadInputFile() {
            String[] splitByLineBreak = stringHelper.splitOnDelimeter(fileReader.read(config.inputFileName), "\\\n");

            for (int i = 0; i < splitByLineBreak.length; i++) {
                if (stringHelper.substringIsInString(splitByLineBreak[i], ";")) {
                    String[] splitBySemiColon = stringHelper.splitOnDelimeter(splitByLineBreak[i], "\\;\\ |\\;");

                    for (int j = 0; j < splitBySemiColon.length; j++) {
                        //define the items based off of the resulting string
                        char metaCode = splitBySemiColon[j].substring(0,1).charAt(0);
                        int numCycles = Integer.parseInt(splitBySemiColon[j].replaceAll("\\D+",""));
                        String descriptor = splitBySemiColon[j];
                        descriptor = descriptor.substring(descriptor.indexOf("{") + 1);
                        descriptor = descriptor.substring(0, descriptor.indexOf("}"));
                        //make a temp task and add the data to it
                        Task temporaryTask = new Task();
                        temporaryTask.code = metaCode;
                        temporaryTask.numCycles = numCycles;
                        temporaryTask.description = descriptor;
                        //add the temp task to the task list
                        taskList.enqueue(temporaryTask);
                    }
                }
            }
        }

        public static void writeOutputFile() {

        }
    }

    public static class TaskQueue {
        static int numAllowed = 150;
        static int numCreated = 0;
        static Task[] dataArray = new Task[numAllowed];

        public static void execute() {
            for (int i = 0; i < numCreated; i++) {
                if (dataArray[i].numCycles > 0) {
                    int computedTime = dataArray[i].numCycles * config.times.get(dataArray[i].description);
                    console.log(dataArray[i].code + "{" + dataArray[i].description + "}" + dataArray[i].numCycles + " - " + computedTime + " ms");
                }
            }
        }

        public static boolean enqueue(Task newTask) {
            if (!isFull()) {
                dataArray[numCreated] = new Task();
                dataArray[numCreated].code = newTask.code;
                dataArray[numCreated].description = newTask.description;
                dataArray[numCreated].numCycles = newTask.numCycles;
                numCreated++;
                return true;
            } else {
                console.log("Ran out of queue space - increase numAllowed", true);
            }
            //should never reach this return
            return false;
        }

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

        public static void print() {
            for (int i = 0; i < numCreated; i++) {
                int j = i + 1;
                char code = dataArray[i].code;
                String description = dataArray[i].description;
                int numCycles = dataArray[i].numCycles;
                console.log("Task " + j + ": " + code + " - " + description + " - " + numCycles);
            }
        }

        private static boolean isFull() {
            if (numCreated >= numAllowed) {
                return true;
            }
            return false;
        }

        private static boolean isEmpty() {
            if (numCreated == 0) {
                return true;
            }
            return false;
        }
    }

    /////////////////////////////////////////////////////
    //                 HELPER CLASSES                  //
    /////////////////////////////////////////////////////
    public static class Console {
        public static void log(String echoStatement, boolean isFatal) {
            if (config.logOption != null) {
                if (config.logOption.equals("Log to File")) {
                    writeFileLog(echoStatement, isFatal);
                } else if (config.logOption.equals("Log to Console")) {
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

        public static void writeConsoleLog(String echoStatement, boolean isFatal) {
            if (!isFatal) {
                System.out.println((char)27 + "[37m" + echoStatement);
            } else {
                System.out.println((char)27 + "[37m");
                System.out.println((char)27 + "[31m" + "✖ FATAL ERROR: " + echoStatement);
                System.out.println((char)27 + "[37m");
            }
        }

        public static void writeFileLog(String echoStatement, boolean isFatal) {
            if (!isFatal) {
                fileWriter.write(config.logFileName, true, echoStatement);
            } else {
                fileWriter.write(config.logFileName, true, "✖ FATAL ERROR: " + echoStatement);
            }
        }

        public static void log(String echoStatement) {
            log(echoStatement, false);
        }

        public static void printDiv() {
            log("=======================================================", false);
        }

        public static void printNewline() {
            log(" ", false);
        }

        private static void crash() {
            System.exit(0);
        }
    }

    public static class ConfigFile {
        static String fileName;
        static double version;
        static String inputFileName;
        static String logOption;
        static String logFileName;
        static Map<String, Integer> times = new HashMap<>();

        ConfigFile(String configFileName) {
            super();
            if (configFileName != "" && configFileName != null) {
                fileName = configFileName;
            } else {
                console.log("Config file is not named", true);
            }
        }

        public static void outputSettings() {
            console.log("Monitor = " + times.get("monitor") + " ms/cycle");
            console.log("Processor = " + times.get("processor") + " ms/cycle");
            console.log("Scanner = " + times.get("scanner") + " ms/cycle");
            console.log("Hard Drive = " + times.get("hard drive") + " ms/cycle");
            console.log("Keyboard = " + times.get("keyboard") + " ms/cycle");
            console.log("Memory = " + times.get("memory") + " ms/cycle");
            console.log("Projector = " + times.get("projector") + " ms/cycle");
            if (logOption.equals("Log to Both")) {
                console.log("Logged to monitor and " + logFileName);
            } else if (logOption.equals("Log to Console")) {
                console.log("Logged to monitor");
            } else if (logOption.equals("Log to File")) {
                console.log("Logged to " + logFileName);
            }
        }
    }

    public static class Task {
        char code;
        String description;
        int numCycles;
    }

    public static class StringHelper {
        public static String[] splitOnDelimeter(String inputString, String delimiter) {
            String[] data = inputString.split(delimiter);
            return data;
        }

        public static boolean substringIsInString(String inputString, String subString) {
            if (inputString.toLowerCase().contains(subString.toLowerCase())) {
                return true;
            }
            return false;
        }

        public static int findTokenIndexInArray(String[] inputArray, String tokenToFind) {
            for (int i = 0; i < inputArray.length; i++) {
                if (inputArray[i].equals(tokenToFind)) {
                    return i;
                }
            }
            return -1;
        }
    }

    public static class ReadFile {
        public static String read(String fileName) {
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
                return stringBuffer.toString();
            } catch (IOException e) {
                console.log("The filename \"" + fileName + "\" does not exist or is corrupted", true);
            }
            //should never reach this return
            return "error";
    	}
    }

    public static class WriteFile {
        public static void createFile(String fileName) {
            write(fileName, false, "");
        }

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
                console.log("The file \"" + fileName + "\" can not be written to because it could not be opened or it is corrupted", true);
    		} finally {
    			try {
    				if (bw != null)
    					bw.close();
    				if (fw != null)
    					fw.close();
    			} catch (IOException ex) {
                    console.log("The file \"" + fileName + "\" can not be written to because it could not be opened or it is corrupted", true);
    			}
    		}
    	}
    }
}
