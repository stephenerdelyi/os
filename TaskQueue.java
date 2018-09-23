
///////////////////
// TaskQueue: A queue consisting of objects of type Task; contains all typical queue functions
///////////////////
public class TaskQueue extends OS {
    int numAllowed = 150;
    int numCreated = 0;
    Task[] dataArray = new Task[numAllowed];

    //enqueue - takes the input task and adds it to the queue
    public boolean enqueue(Task newTask) {
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
    public Task dequeue() {
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
    public Task peek() {
        if (!isEmpty()) {
            return dataArray[0];
        } else {
            Task nullTask = new Task();
            return nullTask;
        }
    }

    //print - outputs the contents of the task queue and the data inside it
    public void print() {
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
}
