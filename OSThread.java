///////////////////
// OSThread: Class that holds all functions for Thread actions and processes
///////////////////
class OSThread implements Runnable {
    private Thread t; //the thread object itself
    private Console console = new Console(); //make sure we include console for logging
    private Clock clock = new Clock(); //make sure we include clock for simulating wait times
    private long countTime; //used to hold the number of ms we should wait

    //start - the publically accessible start function that will call run()
    public void start(long inputCountTime) {
        countTime = inputCountTime;
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }

    //run - the threaded function that will run in parallel with the rest of the program
    public void run() {
        try {
            clock.timer(countTime);
            Thread.sleep(0);
        } catch (InterruptedException e) {
            console.error("Thread interrupted: " + e);
        }
    }

    //isRunning - returns whether or not the thread is currently running
    public boolean isRunning() {
        return t.isAlive();
    }
}
