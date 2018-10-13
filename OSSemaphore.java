import java.util.concurrent.Semaphore;

///////////////////
// OSSemaphore:
///////////////////
public class OSSemaphore extends OS {
    private boolean isSet = false;
    private int numPermitsAllowed = 0;
    private Semaphore semaphore = new Semaphore(0, true);

    public void init(int numPermits) {
        if (!isSet) {
            numPermitsAllowed = numPermits;
            semaphore.release(numPermitsAllowed);
            isSet = true;
        }
    }

    public boolean isSet() {
        return isSet;
    }

    public void verifySet() {
        if(!isSet) {
            console.error("Attempting to access a semaphore that is not initialized.");
        }
    }

    public void acquire() {
        verifySet();
        try {
            semaphore.acquire();
        } catch(InterruptedException ie) {
            console.error("Error acquiring semaphore.");
        }
    }

    public void release() {
        verifySet();
        semaphore.release();
    }

    public int numAvailablePermits() {
        verifySet();
        return semaphore.availablePermits();
    }

    public int numPermitsInUse() {
        verifySet();
        return (numPermitsAllowed - numAvailablePermits() - 1); //subtract 1 to get a 0th index
    }
}
