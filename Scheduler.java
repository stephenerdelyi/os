///////////////////
// Scheduler:
///////////////////
public class Scheduler extends OS {
    int partitionPosition = -1;
    int numSwapsOccurred = 0;
    int numPartitionsAllowed = 50;
    TaskStackQueue[] queueArray = new TaskStackQueue[numPartitionsAllowed];

    //SJF - the Shortest Job First algorithm scheduling function
    public void SJF() {
        partitionTaskQueue();

        //bubble sort the queue based off the number of processes
        for(int i = 1; i < partitionPosition; i++) {
            for(int j = 1; j < partitionPosition-i; j++) {
                if (queueArray[j].numCreated > queueArray[j+1].numCreated) {
                    swap(j);
                }
            }
        }

        reloadTaskQueue();
    }

    //PS - the Priority Sort algorithm scheduling function
    public void PS() {
        partitionTaskQueue();

        //bubble sort the queue based off the number of I/O operations
        for(int i = 1; i < partitionPosition; i++) {
            for(int j = 1; j < partitionPosition-i; j++) {
                int numCurrentIO = 0;
                int numNextIO = 0;

                //Determine the number of I/O operations in the current queue
                for(int k = 0; k < queueArray[j].numCreated; k++) {
                    if(queueArray[j].dataArray[k].code == 'I' || queueArray[j].dataArray[k].code == 'O') {
                        numCurrentIO++;
                    }
                }

                //Determine the number of I/O operations in the next queue
                for(int k = 0; k < queueArray[j + 1].numCreated; k++) {
                    if(queueArray[j + 1].dataArray[k].code == 'I' || queueArray[j + 1].dataArray[k].code == 'O') {
                        numNextIO++;
                    }
                }

                //Swap the queue positions if the current number is less than the next
                if (numCurrentIO < numNextIO) {
                    swap(j);
                }
            }
        }

        reloadTaskQueue();
    }

    //swap - swaps the current index value with the next index value (used for bubble sort)
    private void swap(int indexValue) {
        TaskStackQueue tempQueue = new TaskStackQueue("queue");
        tempQueue = queueArray[indexValue];
        queueArray[indexValue] = queueArray[indexValue+1];
        queueArray[indexValue+1] = tempQueue;
        numSwapsOccurred++;
    }

    //reloadTaskQueue - load back on to the OS's taskQueue
    private void reloadTaskQueue() {
        for(int i = 0; i <= partitionPosition; i++) {
            while(!queueArray[i].isEmpty()) {
                taskQueue.enqueue(queueArray[i].dequeue());
            }
        }
    }

    //partitionTaskQueue - partition the taskQueue to sort processeses according to the scheduling algorithm
    private void partitionTaskQueue() {
        //partition the queue
        while(!taskQueue.isEmpty()) {
            Task currentTask = taskQueue.dequeue();

            if((currentTask.code == 'S' && currentTask.description.equals("begin")) || (currentTask.code == 'S' && currentTask.description.equals("finish")) || (currentTask.code == 'A' && currentTask.description.equals("begin"))) {
                partitionPosition++;

                //check that we will not exceed bounds
                if(numPartitionsAllowed <= partitionPosition) {
                    console.error("Ran out of partition space. Please increase numPartitionsAllowed in scheduler.");
                }

                TaskStackQueue currentStack = new TaskStackQueue("queue");
                queueArray[partitionPosition] = currentStack;
            }
            queueArray[partitionPosition].enqueue(currentTask);
        }
    }
}
