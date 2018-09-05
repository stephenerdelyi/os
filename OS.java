//RandomStringHelper
import java.util.Random;
import java.security.SecureRandom;
//FileHandler http://www.avajava.com/tutorials/lessons/how-do-i-read-a-string-from-a-file-line-by-line.html
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
//StringHelper https://stackoverflow.com/questions/7021074/string-delimiter-in-string-split-method
import java.util.Arrays;

public class OS {
    static TaskQueue taskList = new TaskQueue();
    static TaskHelpers taskHelpers = new TaskHelpers();
    static RandomStringHelper stringGenerator = new RandomStringHelper();
    static FileHandler fileHandler = new FileHandler();
    static ReadFile fileReader = new ReadFile();
    static ConfigFile config = new ConfigFile("config.txt");
    static Console console = new Console();
    static StringHelper stringHelper = new StringHelper();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Task randomTask = taskHelpers.randomTask();
            taskList.enqueue(randomTask);
        }

        //taskList.print();

        console.printDiv();
        if (FileHandler.verifyConfigFile()) {
            console.log("--Config file has been verified with no syntax errors");
            FileHandler.loadConfigFile();
            console.log("--Config file has been loaded");
            console.log("--Version: " + config.version);
            console.printNewline();

            if (FileHandler.verifyInputFile()) {
                console.log("--Input file has been verified with no syntax errors");
            }
        }
        console.printDiv();
    }

    public static class FileHandler {
        public static boolean verifyConfigFile() {
            String[] splitByLineBreak = stringHelper.splitOnDelimeter(fileReader.read(config.fileName), "\\\n");
            String[] validInputDeclarations = {"Version/Phase","File Path","Monitor display time {msec}","Processor cycle time {msec}","Scanner cycle time {msec}","Hard drive cycle time {msec}","Keyboard cycle time {msec}","Memory cycle time {msec}","Projector cycle time {msec}","Log","Log File Path",""};
            String[] validTypeDeclarations = {"double","fileName","int","int","int","int","int","int","int","logOption","fileName"};

            for (int i = 0; i < splitByLineBreak.length - 1; i++) {
                if (stringHelper.substringIsInString(splitByLineBreak[i], ":")) {
                    String[] splitByColon = stringHelper.splitOnDelimeter(splitByLineBreak[i], "\\:\\ ");
                    int returnedIndex = stringHelper.findTokenIndexInArray(validInputDeclarations, splitByColon[0]);

                    //check to see if the token is valid
                    if (returnedIndex != -1) {
                        //if it is of type double, try to parse it
                        if (validTypeDeclarations[returnedIndex].equals("double")) {
                            try {
                                Double.parseDouble(splitByColon[1]);
                            } catch (Exception e) {
                                console.log("There was an error parsing the double provided in " + config.fileName + ": \n \"" + splitByColon[1] + "\" next to declaration \"" + splitByColon[0] + "\"", true);
                            }
                        } else if (validTypeDeclarations[returnedIndex].equals("int")) {
                            try {
                                Integer.parseInt(splitByColon[1]);
                            } catch (Exception e) {
                                console.log("There was an error parsing the int provided in " + config.fileName + ": \n \"" + splitByColon[1] + "\" next to declaration \"" + splitByColon[0] + "\"", true);
                            }
                        } else if (validTypeDeclarations[returnedIndex].equals("fileName")) {
                            if (!stringHelper.substringIsInString(splitByColon[1], ".")) {
                                console.log("There was an error parsing the filename provided in " + config.fileName + ": \n \"" + splitByColon[1] + "\" next to declaration \"" + splitByColon[0] + "\"", true);
                            }
                        } else if (validTypeDeclarations[returnedIndex].equals("logOption")) {
                            if (!splitByColon[1].equals("Log to Both") && !splitByColon[1].equals("Log to Console") && !splitByColon[1].equals("Log to File")) {
                                console.log("There was an error parsing the log option provided in " + config.fileName + ": \n \"" + splitByColon[1] + "\" next to declaration \"" + splitByColon[0] + "\"", true);
                            }
                        }
                    } else {
                        console.log("Invalid parameter declaration in " + config.fileName + ": \n \"" + splitByColon[0] + "\"", true);
                    }
                }
            }

            return true;
        }

        public static void loadConfigFile() {
            String[] splitByLineBreak = stringHelper.splitOnDelimeter(fileReader.read(config.fileName), "\\\n");

            for (int i = 0; i < splitByLineBreak.length - 1; i++) {
                if (stringHelper.substringIsInString(splitByLineBreak[i], ":")) {
                    String[] splitByColon = stringHelper.splitOnDelimeter(splitByLineBreak[i], "\\:\\ ");

                    if (splitByColon[0].equals("Version/Phase")) {
                        config.version = Double.parseDouble(splitByColon[1]);
                    } else if (splitByColon[0].equals("File Path")) {
                        config.inputFileName = splitByColon[1];
                    } else if (splitByColon[0].equals("Monitor display time {msec}")) {
                        config.monitorTime = Integer.parseInt(splitByColon[1]);
                    } else if (splitByColon[0].equals("Processor cycle time {msec}")) {
                        config.processorTime = Integer.parseInt(splitByColon[1]);
                    } else if (splitByColon[0].equals("Scanner cycle time {msec}")) {
                        config.scannerTime = Integer.parseInt(splitByColon[1]);
                    } else if (splitByColon[0].equals("Hard drive cycle time {msec}")) {
                        config.hardDriveTime = Integer.parseInt(splitByColon[1]);
                    } else if (splitByColon[0].equals("Keyboard cycle time {msec}")) {
                        config.keyboardTime = Integer.parseInt(splitByColon[1]);
                    } else if (splitByColon[0].equals("Memory cycle time {msec}")) {
                        config.memoryTime = Integer.parseInt(splitByColon[1]);
                    } else if (splitByColon[0].equals("Projector cycle time {msec}")) {
                        config.projectorTime = Integer.parseInt(splitByColon[1]);
                    } else if (splitByColon[0].equals("Log")) {
                        config.logOption = splitByColon[1];
                    } else if (splitByColon[0].equals("Log File Path")) {
                        config.logFileName = splitByColon[1];
                    } else {
                        //should never reach this after verifying the file
                        console.log("Invalid parameter declaration in " + config.fileName + ": \n \"" + splitByColon[0] + "\"", true);
                    }
                }
            }
        }

        public static boolean verifyInputFile() {
            String[] splitByLineBreak = stringHelper.splitOnDelimeter(fileReader.read(config.inputFileName), "\\\n");

            return true;
        }

        public static void loadInputFile() {

        }

        public static void writeOutputFile() {

        }
    }

    public static class TaskQueue {
        static int numAllowed = 150;
        static int numCreated = 0;
        static Task[] dataArray = new Task[numAllowed];

        public static boolean enqueue(Task newTask) {
            if (!isFull()) {
                dataArray[numCreated] = new Task();
                dataArray[numCreated].code = newTask.code;
                dataArray[numCreated].description = newTask.description;
                dataArray[numCreated].numCycles = newTask.numCycles;
                numCreated++;
                return true;
            }
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
            }
            return taskHelpers.nullTask();
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
            if (!isFatal) {
                System.out.println((char)27 + "[37m" + echoStatement);
            } else {
                System.out.println((char)27 + "[37m");
                System.out.println((char)27 + "[31m" + "FATAL ERROR: " + echoStatement);
                System.out.println((char)27 + "[37m");
                crash();
            }
        }

        public static void log(String echoStatement) {
            log(echoStatement, false);
        }

        public static void printDiv() {
            log("=======================================================", false);
        }

        public static void printNewline() {
            log("", false);
        }

        public static void crash() {
            System.exit(0);
        }
    }

    public static class ConfigFile {
        static String fileName;
        static double version;
        static String inputFileName;
        static int monitorTime;
        static int processorTime;
        static int scannerTime;
        static int hardDriveTime;
        static int keyboardTime;
        static int memoryTime;
        static int projectorTime;
        static String logOption;
        static String logFileName;

        ConfigFile(String configFileName) {
            super();
            if (configFileName != "" && configFileName != null) {
                fileName = configFileName;
            } else {
                console.log("Config file is not named", true);
            }
        }
    }

    public static class Task {
        char code;
        String description;
        int numCycles;
    }

    public static class TaskHelpers {
        public static Task randomTask() {
            Random random = new Random();
            int randomNum = random.nextInt(99);

            String testString = stringGenerator.generate(10, "alphanumeric");
            char testChar = stringGenerator.generate(10, "metacode").charAt(0);

            Task randomTask = new Task();
            randomTask.code = testChar;
            randomTask.description = testString;
            randomTask.numCycles = randomNum;
            return randomTask;
        }

        private static Task nullTask() {
            Task nullTask = new Task();
            return nullTask;
        }
    }

    public static class RandomStringHelper {
        private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private static final String NUMS = "0123456789";
        private static final String ALPHANUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private static final String METACODES = "SAPIOM";
        private static final SecureRandom RANDOM = new SecureRandom();

        public static String generate(int count, String type) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < count; ++i) {
                if (type == "alpha") {
                    sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
                } else if (type == "num") {
                    sb.append(NUMS.charAt(RANDOM.nextInt(NUMS.length())));
                } else if (type == "metacode") {
                    sb.append(METACODES.charAt(RANDOM.nextInt(METACODES.length())));
                } else {
                    sb.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
                }
            }
            return sb.toString();
        }
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
            return "error";
    	}
    }
}
