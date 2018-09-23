///////////////////
// TaskProcessor:
///////////////////
public class TaskProcessor extends OS {
    int processCount = 0;

    public void simulator(Task currentTask) {
        if (currentTask.description.equals("start")) {
            PCB.setProcessState("Start");
            clock.start();
            taskStack.push(currentTask);
            outputMessage("program starting", "simulator");
        } else if (currentTask.description.equals("end")) {
            PCB.setProcessState("Exit");
            taskStack.pop();
            outputMessage("program ending", "simulator");
        }
    }

    public void application(Task currentTask) {
        if (currentTask.description.equals("start")) {
            processCount++;
            taskStack.push(currentTask);
            outputMessage("preparing process", "os");
            //should prepare process here
            outputMessage("starting process", "os");
            PCB.setProcessState("Ready");
        } else if (currentTask.description.equals("end")) {
            taskStack.pop();
            //should remove process here
            outputMessage("removing process", "os");
        }
    }

    public void program(Task currentTask) {
        PCB.setProcessState("Running");
        if (currentTask.description.equals("run")) {
            outputMessage("start processing action", "process");
            //should process action here
            clock.timer(currentTask.numCycles * config.times.get(currentTask.description));
            outputMessage("end processing action", "process");
        }
        PCB.setProcessState("Waiting");
    }

    public void memory(Task currentTask) {
        PCB.setProcessState("Running");
        if (currentTask.description.equals("allocate")) {
            outputMessage("allocating memory", "process");
            //sholuld allocate memory here
            clock.timer(currentTask.numCycles * config.times.get(currentTask.description));
            outputMessage("memory allocated at " + stringHelper.makeFakeHex(), "process");
        } else if (currentTask.description.equals("block")) {
            outputMessage("start memory blocking", "process");
            //should do memory blocking here
            clock.timer(currentTask.numCycles * config.times.get(currentTask.description));
            outputMessage("end memory blocking", "process");
        }
        PCB.setProcessState("Waiting");
    }

    public void output(Task currentTask) {
        PCB.setProcessState("Running");
        if (currentTask.description.equals("monitor")) {
            outputMessage("start monitor output", "process");
            //should do monitor output thread here
            clock.timer(currentTask.numCycles * config.times.get(currentTask.description));
            outputMessage("end monitor output", "process");
        } else if (currentTask.description.equals("projector")) {
            outputMessage("start projector output", "process");
            //should do projector output thread here
            clock.timer(currentTask.numCycles * config.times.get(currentTask.description));
            outputMessage("end projector output", "process");
        } else if (currentTask.description.equals("hard drive")) {
            outputMessage("start hard drive output", "process");
            //should do hard drive output thread here
            clock.timer(currentTask.numCycles * config.times.get(currentTask.description));
            outputMessage("end hard drive output", "process");
        }
        PCB.setProcessState("Waiting");
    }

    public void input(Task currentTask) {
        PCB.setProcessState("Running");
        if (currentTask.description.equals("keyboard")) {
            outputMessage("start keyboard input", "process");
            //should do keyboard input thread here
            clock.timer(currentTask.numCycles * config.times.get(currentTask.description));
            outputMessage("end keyboard input", "process");
        } else if (currentTask.description.equals("hard drive")) {
            outputMessage("start hard drive input", "process");
            //should do hard drive input thread here
            clock.timer(currentTask.numCycles * config.times.get(currentTask.description));
            outputMessage("end hard drive input", "process");
        }
        PCB.setProcessState("Waiting");
    }

    public void outputMessage(String message, String messageType) {
        String finalMessage = "";

        if (messageType.equals("simulator")) {
            finalMessage = "Simulator " + message;
        } else if (messageType.equals("os")) {
            finalMessage = "OS: " + message + " " + processCount;
        } else if (messageType.equals("process")) {
            finalMessage = "Process " + processCount + ": " + message;
        }

        console.log(clock.getDurationTime() + " - " + finalMessage);
    }

    public boolean verifyTaskData() {
        TaskQueue workingTaskList = taskList;
        TaskStack simOSStack = new TaskStack();

        for (int i = 0; i < workingTaskList.numCreated; i++) {
            Task currentTask = workingTaskList.dataArray[i];

            if (currentTask.code == 'S' || currentTask.code == 'A') {
                simOSStack.push(currentTask);

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

        int numStartAsEncountered = 0;
        int numEndAsEncountered = 0;
        int numStartS = 0;
        int numEndS = 0;

        simOSStack.print();

        //check number of S entries
        for (int i = 0; i < simOSStack.numCreated; i++) {
            Task currentTask = simOSStack.dataArray[i];

            if (currentTask.code == 'S' && currentTask.description.equals("start")) {
                numStartS++;
            } else if (currentTask.code == 'S' && currentTask.description.equals("end")) {
                numEndS++;
            }

            if (numStartS > 1 || numEndS > 1) {
                console.error("Tasks can not have nested 'S' codes of any type");
            }
        }

        TaskStack dupStack = new TaskStack();
        console.log("print:");
        dupStack.print();

        //check S/A placement
        for (int i = 0; i < simOSStack.numCreated; i++) {
            Task currentTask = simOSStack.dataArray[i];
            if (i == 0) {
                if (currentTask.code != 'S' || !currentTask.description.equals("start")) {
                    console.error("Program does not start with S{start}0");
                }
            } else if (i == simOSStack.numCreated - 1) {
                if (currentTask.code != 'S' || !currentTask.description.equals("end")) {
                    console.error("Program does not end with S{end}0");
                }
            } else {
                Task nextTask = simOSStack.dataArray[i + 1];
                console.log("i = " + i);
                if (currentTask.code == 'A') {
                    if (currentTask.description.equals("start")) {
                        numStartAsEncountered++;
                        if (nextTask.code != 'A' || !nextTask.description.equals("end")) {
                            /*if (nextTask.code == 'A') {
                                numStartAsEncountered++;
                            }*/
                            console.error(stringHelper.returnOrdinal(numStartAsEncountered) + " instance of A{start}0 is missing following A{end}0 statment");
                        }
                    } else if (currentTask.description.equals("end")) {
                        numEndAsEncountered++;
                        if ((nextTask.code != 'A' || !nextTask.description.equals("start")) && (nextTask.code != 'S' || !nextTask.description.equals("end"))) {
                            /*if (nextTask.code == 'A' && nextTask.description.equals("end")) {
                                numEndAsEncountered++;
                            }*/
                            console.error(stringHelper.returnOrdinal(numEndAsEncountered) + " instance of A{end}0 is missing preceding A{start}0 statment");
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
