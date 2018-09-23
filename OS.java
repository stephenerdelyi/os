///////////////////
// OS:
///////////////////
public class OS {
    static boolean allowFatalExecution = false;
    static TaskQueue taskList = new TaskQueue();
    static TaskStack taskStack = new TaskStack();
    static TaskProcessor taskProcessor = new TaskProcessor();
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
        if (fileHandler.verifyConfigFile()) {
            console.log("✓ Config file has been verified with no syntax errors");
            fileHandler.loadConfigFile();
            console.log("✓ Config file (v" + config.version + ") has been loaded [" + config.fileName + "]");
            if (fileHandler.verifyInputFile()) {
                console.log("✓ Input file has been verified with no syntax errors");
                fileHandler.loadInputFile();
                console.log("✓ Input file has been loaded [" + config.inputFileName + "]");
                if (taskProcessor.verifyTaskData()) {
                    console.log("✓ Input file has been verified with no semantic errors");
                }
            }
        }
        console.printDiv();

        /////////////////////////////////////////////////////
        //                   SYSTEM READY                  //
        /////////////////////////////////////////////////////
        //execute();

        console.printNewline();
    }

    //execute - processes all tasks in the queue according to the current OS specifications
    public static void execute() {
        //while there are tasks to complete
        while (!taskList.isEmpty()) {
            //remove the next task from the front of the queue
            Task currentTask = taskList.dequeue();

            if (currentTask.code == 'S') {
                taskProcessor.simulator(currentTask);
            } else if (currentTask.code == 'A') {
                taskProcessor.application(currentTask);
            } else if (currentTask.code == 'P') {
                taskProcessor.program(currentTask);
            } else if (currentTask.code == 'M') {
                taskProcessor.memory(currentTask);
            } else if (currentTask.code == 'O') {
                taskProcessor.output(currentTask);
            } else if (currentTask.code == 'I') {
                taskProcessor.input(currentTask);
            }
        }
    }
}
