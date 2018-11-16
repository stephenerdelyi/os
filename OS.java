//////////////////////////////////////////////////////
//            OPERATING SYSTEM SIMULATOR            //
//      Stephen Erdelyi Jr - CS 446 - Phase IV      //
//////////////////////////////////////////////////////
public class OS {
    static boolean allowFatalExecution = false;
    static Console console = new Console();
    static ValidKeys validKeys = new ValidKeys();
    static Scheduler scheduler = new Scheduler();
    static StringHelper stringHelper = new StringHelper();
    static TaskProcessor taskProcessor = new TaskProcessor();
    static TaskStackQueue taskQueue = new TaskStackQueue("queue");
    static FileHandler fileHandler = new FileHandler();
    static MemoryBlock memoryBlock = new MemoryBlock();
    static Locks locks = new Locks();
    static Clock clock = new Clock();
    static PCB PCB = new PCB();
    static ConfigFile config;

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
                    console.log("✓ Input data has been verified with no semantic errors");
                    if (taskProcessor.scheduleTasks()) {
                        console.log("✓ Input data has been scheduled using " + config.schedulingCode + " [" + scheduler.numSwapsOccurred + " swap(s)]");
                    }
                }
            }
        }
        console.printDiv();

        /////////////////////////////////////////////////////
        //                   SYSTEM READY                  //
        /////////////////////////////////////////////////////
        execute();
        console.printNewline();
    }

    //execute - processes all tasks in the queue according to the current OS specifications
    public static void execute() {
        //while there are tasks to complete
        while(!taskQueue.isEmpty()) {
            //remove the next task from the front of the queue
            Task currentTask = taskQueue.dequeue();

            //send the task to the appropriate handler based on the task code
            if(currentTask.code == 'S') {
                taskProcessor.simulator(currentTask);
            } else if(currentTask.code == 'A') {
                taskProcessor.application(currentTask);
            } else if(currentTask.code == 'P') {
                taskProcessor.program(currentTask);
            } else if(currentTask.code == 'M') {
                taskProcessor.memory(currentTask);
            } else if(currentTask.code == 'O') {
                taskProcessor.output(currentTask);
            } else if(currentTask.code == 'I') {
                taskProcessor.input(currentTask);
            }
        }
    }
}
