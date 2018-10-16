///////////////////
// TaskStackQueue: A queue OR stack (depending on constructor value) consisting of objects of type Task; contains all typical queue/stack functions
///////////////////
public class TaskStackQueue extends OS {
    String classType;
    int numAllowed = 150;
    int numCreated = 0;
    Task[] dataArray = new Task[numAllowed];

    //TaskStackQueue (constructor) - requires that the data structure type is named to proceed
    TaskStackQueue(String inputClassType) {
        if (inputClassType.equals("queue") || inputClassType.equals("stack")) {
            classType = inputClassType;
        } else {
            console.error("Invalid TaskStackQueue constructor value.");
        }
    }

    //add - enqueue or push, depending on the structure type
    public boolean add(Task newTask) {
        if (classType.equals("queue")) {
            if (enqueue(newTask)) {
                return true;
            }
        } else if (classType.equals("queue")) {
            if (push(newTask)) {
                return true;
            }
        }
        return false;
    }

    //enqueue - takes the input task and adds it to the queue
    public boolean enqueue(Task newTask) {
        verifyDatatype("queue");
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

    //push - takes the input task and adds it to the stack
    public boolean push(Task newTask) {
        verifyDatatype("stack");
        if (!isFull()) {
            dataArray[numCreated] = new Task();
            dataArray[numCreated].code = newTask.code;
            dataArray[numCreated].description = newTask.description;
            dataArray[numCreated].numCycles = newTask.numCycles;
            numCreated++;
            return true;
        } else {
            console.error("Ran out of stack space - increase numAllowed");
        }
        //should never reach this return
        return false;
    }

    //remove - dequeue or pop, depending on the structure type
    public Task remove() {
        if (classType.equals("queue")) {
            return dequeue();
        } else if (classType.equals("queue")) {
            return pop();
        } else {
            Task nullTask = new Task();
            return nullTask;
        }
    }

    //dequeue - takes the task at the front of the queue and returns it
    public Task dequeue() {
        verifyDatatype("queue");
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

    //pop - takes the task at the top of the stack and returns it
    public Task pop() {
        verifyDatatype("stack");
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

    //peek - returns the task at the front of the queue but doesn't remove it
    public Task peek() {
        if (!isEmpty()) {
            return dataArray[0];
        } else {
            Task nullTask = new Task();
            return nullTask;
        }
    }

    //contains - returns true if the queue contains the task
    public boolean contains(Task taskCriteria) {
        for (int i = 0; i < numCreated; i++) {
            if (taskCriteria.code == dataArray[i].code && taskCriteria.numCycles == dataArray[i].numCycles && taskCriteria.description.equals(dataArray[i].description)) {
                return true;
            }
        }
        return false;
    }

    //print - outputs the contents of the task queue and the data inside it
    public void print() {
        console.log(classType + " printout:");
        for (int i = 0; i < numCreated; i++) {
            int j = i + 1;
            char code = dataArray[i].code;
            String description = dataArray[i].description;
            int numCycles = dataArray[i].numCycles;
            console.log("Task " + j + ": " + code + " - " + description + " - " + numCycles);
        }
    }

    //isFull - returns whether or not the queue is full
    public boolean isFull() {
        if (numCreated >= numAllowed) {
            return true;
        }
        return false;
    }

    //isEmpty - returns whether or not the queue is empty
    public boolean isEmpty() {
        if (numCreated == 0) {
            return true;
        }
        return false;
    }

    //verifyDatatype - used in this class' functions that require stack/queue-specific actions
    private void verifyDatatype(String requiredDataType) {
        if (!requiredDataType.equals(classType)) {
            if (requiredDataType.equals("stack")) {
                console.error("Trying to execute queue function in stack");
            } else if (requiredDataType.equals("queue")) {
                console.error("Trying to execute stack function in queue");
            }
        }
    }
}
