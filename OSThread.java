import java.lang.Thread;

class OSThread implements Runnable {
    private Thread t;
    private long countTime;
    private Console console = new Console();
    private Clock clock = new Clock();
    private boolean isRunning;

    OSThread(long inputCountTime) {
        countTime = inputCountTime;
        //console.log("Creating thread");
    }

    public void run() {
        //console.log("Running thread");
        try {
            clock.timer(countTime);
            Thread.sleep(0);
        } catch (InterruptedException e) {
            console.error("Thread interrupted");
        }
        //console.log("Exiting thread");
    }

    public void start() {
        //console.log("Starting thread");
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }

    public boolean isRunning() {
        return t.isAlive();
    }
}
