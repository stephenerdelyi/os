import java.util.concurrent.Semaphore;

///////////////////
// OSMutex: Class that abstracts the typical mutex object to function a certain way in order to fulfill OS project requirements and needs
///////////////////
public class OSMutex extends OS {
    private Semaphore semaphore = new Semaphore(1, true); //the actual semaphore we are using to abstract

    //lock - acquires a single permit from the semaphore
    public void lock() {
        try {
            semaphore.acquire();
        } catch(InterruptedException ie) {
            console.error("Error acquiring mutex.");
        }
    }

    //unlock - releases a single permit from the semaphore
    public void unlock() {
        semaphore.release();
    }
}
