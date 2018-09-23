

///////////////////
// TaskStack: A stack consisting of objects of type Task; contains all typical stack functions
///////////////////
public class TaskStack extends OS {
    int numAllowed = 150;
    int numCreated = 0;
    Task[] dataArray = new Task[numAllowed];

    //push - takes the input task and adds it to the stack
    public boolean push(Task newTask) {
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
    public Task pop() {
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
    public Task peek() {
        if (!isEmpty()) {
            return dataArray[numCreated - 1];
        } else {
            Task nullTask = new Task();
            return nullTask;
        }
    }

    public boolean contains(Task taskCriteria) {
        for (int i = 0; i < numCreated; i++) {
            if (taskCriteria.code == dataArray[i].code && taskCriteria.numCycles == dataArray[i].numCycles && taskCriteria.description.equals(dataArray[i].description)) {
                return true;
            }
        }
        return false;
    }

    //print - outputs the contents of the task stack and the data inside it
    public void print() {
        for (int i = 0; i < numCreated; i++) {
            int j = i + 1;
            char code = dataArray[i].code;
            String description = dataArray[i].description;
            int numCycles = dataArray[i].numCycles;
            console.log("Task " + j + ": " + code + " - " + description + " - " + numCycles);
        }
    }

    //isFull - returns whether or not the stack is full
    private boolean isFull() {
        if (numCreated >= numAllowed) {
            return true;
        }
        return false;
    }

    //isEmpty - returns whether or not the stack is empty
    private boolean isEmpty() {
        if (numCreated == 0) {
            return true;
        }
        return false;
    }
}
