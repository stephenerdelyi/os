import java.util.concurrent.Semaphore;

///////////////////
// OSSemaphore: Class that abstracts the typical semaphore object to function a certain way in order to fulfill OS project requirements and needs
///////////////////
public class OSSemaphore extends OS {
    private boolean isSet = false; //used to determine if init has been called already
    private int numPermitsAllowed = 0; //the number of permits allowed in the semaphore- can only be set once through init function
    private Semaphore semaphore = new Semaphore(0, true); //the actual semaphore we are using to abstract

    //init - used to initialize the semaphore by setting the number of permits allowed; function can only be run once
    public void init(int numPermits) {
        if (!isSet) {
            if (numPermits > 0) {
                numPermitsAllowed = numPermits;
                semaphore.release(numPermitsAllowed);
                isSet = true;
            } else {
                console.error("Semaphore can not be initialized with 0 permits.");
            }
        } else {
            console.error("Can not initialize semaphore more than once.");
        }
    }

    //verifySet - used internally by the class to verify a semaphore is initialized before completing actions on it
    private void verifySet() {
        if(!isSet) {
            console.error("Attempting to access a semaphore that is not initialized.");
        }
    }

    //acquire - acquires a single permit from the semaphore
    public void acquire() {
        verifySet();
        try {
            semaphore.acquire();
        } catch(InterruptedException ie) {
            console.error("Error acquiring semaphore.");
        }
    }

    //release - releases a single permit from the semaphore
    public void release() {
        verifySet();
        semaphore.release();
    }

    //numAvailablePermits - returns the number of available permits
    public int numAvailablePermits() {
        verifySet();
        return semaphore.availablePermits();
    }

    //numPermitsInUse - returns the number of permits currently in use
    public int numPermitsInUse() {
        verifySet();
        return (numPermitsAllowed - numAvailablePermits() - 1); //subtract 1 to get a 0th index
    }
}
