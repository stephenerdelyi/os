///////////////////
// TaskProcessor: Class that holds all functions called by execute in main. Also contains semantic task checker
///////////////////
public class TaskProcessor extends OS {
    int processCount = 0; //number of processes executed since simulator start
    TaskStackQueue taskStack = new TaskStackQueue("stack"); //the stack used to simulate stack-based process management

    //simulator - if a task code is simulator [S], pass the task to this function
    public void simulator(Task currentTask) {
        if (currentTask.description.equals("start")) {
            PCB.setProcessState("Start");
            clock.start(); //set the simulator start time
            taskStack.push(currentTask);
            outputMessage("program starting", "simulator");
        } else if (currentTask.description.equals("end")) {
            PCB.setProcessState("Exit");
            taskStack.pop();
            outputMessage("program ending", "simulator");
        }
    }

    //application - if a task code is application [A], pass the task to this function
    public void application(Task currentTask) {
        if (currentTask.description.equals("start")) {
            processCount++;
            taskStack.push(currentTask);
            outputMessage("preparing process", "os");
            //should prepare process here in future OS iteration
            outputMessage("starting process", "os");
            PCB.setProcessState("Ready");
        } else if (currentTask.description.equals("end")) {
            taskStack.pop();
            //should remove process here in future OS iteration
            outputMessage("removing process", "os");
        }
    }

    //program - if a task code is program [P], pass the task to this function
    public void program(Task currentTask) {
        PCB.setProcessState("Running");
        if (currentTask.description.equals("run")) {
            outputMessage("start processing action", "process");
            //should process action here in future OS iteration
            clock.timer(currentTask.computedTaskTime()); //simulate wait time using the timer
            outputMessage("end processing action", "process");
        }
        PCB.setProcessState("Waiting");
    }

    //memory - if a task code is memory [M], pass the task to this function
    public void memory(Task currentTask) {
        PCB.setProcessState("Running");
        if (currentTask.description.equals("allocate")) {
            outputMessage("allocating memory", "process");
            //sholuld allocate memory here in future OS iteration
            clock.timer(currentTask.computedTaskTime()); //simulate wait time using the timer
            outputMessage("memory allocated at " + stringHelper.makeFakeHex(), "process");
        } else if (currentTask.description.equals("block")) {
            outputMessage("start memory blocking", "process");
            //should do memory blocking here in future OS iteration
            clock.timer(currentTask.computedTaskTime()); //simulate wait time using the timer
            outputMessage("end memory blocking", "process");
        }
        PCB.setProcessState("Waiting");
    }

    //output - if a task code is output [O], pass the task to this function
    public void output(Task currentTask) {
        PCB.setProcessState("Running");
        if (currentTask.description.equals("monitor")) {
            outputMessage("start monitor output", "process");
            //monitor output thread creation
            OSThread monitorOutputThread = new OSThread();
            monitorOutputThread.start(currentTask.computedTaskTime());
            while(monitorOutputThread.isRunning()) {
                //do nothing, since we are simulating time waiting
            }
            outputMessage("end monitor output", "process");
        } else if (currentTask.description.equals("projector")) {
            outputMessage("start projector output", "process");
            //projector output thread creation
            OSThread projectorOutputThread = new OSThread();
            projectorOutputThread.start(currentTask.computedTaskTime());
            while(projectorOutputThread.isRunning()) {
                //do nothing, since we are simulating time waiting
            }
            outputMessage("end projector output", "process");
        } else if (currentTask.description.equals("hard drive")) {
            outputMessage("start hard drive output", "process");
            //hard drive output thread creation
            OSThread hardDriveOutputThread = new OSThread();
            hardDriveOutputThread.start(currentTask.computedTaskTime());
            while(hardDriveOutputThread.isRunning()) {
                //do nothing, since we are simulating time waiting
            }
            outputMessage("end hard drive output", "process");
        }
        PCB.setProcessState("Waiting");
    }

    //input - if a task code is input [I], pass the task to this function
    public void input(Task currentTask) {
        PCB.setProcessState("Running");
        if (currentTask.description.equals("keyboard")) {
            outputMessage("start keyboard input", "process");
            //keyboard input thread creation
            OSThread keyboardInputThread = new OSThread();
            keyboardInputThread.start(currentTask.computedTaskTime());
            while(keyboardInputThread.isRunning()) {
                //do nothing, since we are simulating time waiting
            }
            outputMessage("end keyboard input", "process");
        } else if (currentTask.description.equals("hard drive")) {
            outputMessage("start hard drive input", "process");
            //hard drive input thread creation
            OSThread hardDriveInputThread = new OSThread();
            hardDriveInputThread.start(currentTask.computedTaskTime());
            while(hardDriveInputThread.isRunning()) {
                //do nothing, since we are simulating time waiting
            }
            outputMessage("end hard drive input", "process");
        }
        PCB.setProcessState("Waiting");
    }

    //outputMessage - used throughout task processing to output log statements in a cleaner way
    public void outputMessage(String message, String messageType) {
        String finalMessage = ""; //final message that will output behind the timestamp

        if (messageType.equals("simulator")) {
            finalMessage = "Simulator " + message;
        } else if (messageType.equals("os")) {
            finalMessage = "OS: " + message + " " + processCount;
        } else if (messageType.equals("process")) {
            finalMessage = "Process " + processCount + ": " + message;
        }

        console.log(clock.getDurationTime() + " - " + finalMessage);
    }

    //verifyTaskData - verifies the semantics of the input data
    public boolean verifyTaskData() {
        TaskStackQueue workingTaskQueue = taskQueue; //copy of the loaded taskQueue for working processing
        TaskStackQueue SAStack = new TaskStackQueue("stack"); //stack that will be used to load/read combinations of S/A codes

        //check combinations of keywords and numbers
        for (int i = 0; i < workingTaskQueue.numCreated; i++) {
            Task currentTask = workingTaskQueue.dataArray[i];

            if (currentTask.code == 'S' || currentTask.code == 'A') {
                SAStack.push(currentTask);

                if (currentTask.numCycles > 0) {
                    console.error("Can not have code '" + currentTask.code + "' with numCycles > 0");
                }
                if (!currentTask.description.equals("start") && !currentTask.description.equals("end")) {
                    console.error("Invalid description \"" + currentTask.description + "\" for code '" + currentTask.code + "'");
                } else if (currentTask.description.equals("start")) {
                    //no actions
                } else if (currentTask.description.equals("end")) {
                    //no actions
                }
            } else if (currentTask.code == 'P') {
                if (!currentTask.description.equals("run")) {
                    console.error("Invalid description \"" + currentTask.description + "\" for code '" + currentTask.code + "'");
                }
            } else if (currentTask.code == 'M') {
                if (!currentTask.description.equals("block") && !currentTask.description.equals("allocate")) {
                    console.error("Invalid description \"" + currentTask.description + "\" for code '" + currentTask.code + "'");
                }
            } else if (currentTask.code == 'O') {
                if (!currentTask.description.equals("hard drive") && !currentTask.description.equals("monitor") && !currentTask.description.equals("projector")) {
                    console.error("Invalid description \"" + currentTask.description + "\" for code '" + currentTask.code + "'");
                }
            } else if (currentTask.code == 'I') {
                if (!currentTask.description.equals("hard drive") && !currentTask.description.equals("keyboard") && !currentTask.description.equals("scanner")) {
                    console.error("Invalid description \"" + currentTask.description + "\" for code '" + currentTask.code + "'");
                }
            }
        }

        int numStartAsEncountered = 0; //number of tasks with code 'A' and description "Start" encountered as the loop occurred
        int numEndAsEncountered = 0; //number of tasks with code 'A' and description "End" encountered as the loop occurred

        //check S/A placement
        for (int i = 0; i < SAStack.numCreated; i++) {
            Task currentTask = SAStack.dataArray[i];
            if (i == 0) {
                if (currentTask.code != 'S' || !currentTask.description.equals("start")) {
                    console.error("Program does not start with S{start}0");
                }
            } else if (i == SAStack.numCreated - 1) {
                if (currentTask.code != 'S' || !currentTask.description.equals("end")) {
                    console.error("Program does not end with S{end}0");
                }
            } else {
                Task nextTask = SAStack.dataArray[i + 1];
                if (currentTask.code == 'A') {
                    if (i % 2 != 0) {
                        if (currentTask.description.equals("end")) {
                            numEndAsEncountered++;
                            console.error(stringHelper.returnOrdinal(numEndAsEncountered) + " A{end}0 is missing preceding A{start}0");
                        } else {
                            numStartAsEncountered++;
                            if (nextTask.description.equals("start") || nextTask.code == 'S') {
                                console.error(stringHelper.returnOrdinal(numStartAsEncountered) + " A{start}0 is missing following A{end}0");
                            }
                        }
                    } else {
                        if (currentTask.description.equals("end")) {
                            numEndAsEncountered++;
                        } else {
                            numStartAsEncountered++;
                        }
                    }
                } else if (currentTask.code == 'S') {
                    console.error("Tasks can not have nested 'S' codes of any type");
                }
            }
        }

        return true;
    }
}
