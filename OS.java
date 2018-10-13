//////////////////////////////////////////////////////
//            OPERATING SYSTEM SIMULATOR            //
//      Stephen Erdelyi Jr - CS 446 - Phase III     //
//////////////////////////////////////////////////////
public class OS {
    static boolean allowFatalExecution = false;
    static TaskProcessor taskProcessor = new TaskProcessor();
    static TaskStackQueue taskQueue = new TaskStackQueue("queue");
    static FileHandler fileHandler = new FileHandler();
    static Console console = new Console();
    static StringHelper stringHelper = new StringHelper();
    static ValidKeys validKeys = new ValidKeys();
    static Semaphores semaphores = new Semaphores();
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
        semaphores.test();
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
