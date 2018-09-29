import java.text.DecimalFormat;

///////////////////
// Clock: Class that contains all functionality related to timer and clock
///////////////////
public class Clock extends OS {
    long startTime;
    boolean isStarted = false;

    //start - starts the clock by setting our start time
    public void start() {
        startTime = getTime();
        isStarted = true;
    }

    public void stop() {
        startTime = 0;
        isStarted = false;
    }

    //getTime - returns the current time for start and timer
    private long getTime() {
        return System.nanoTime();
    }

    //getDurationTime - output the difference in time (in sec) from clock start time
    public String getDurationTime() {
        double val = (System.nanoTime() - startTime) * .000000001;
        DecimalFormat df = new DecimalFormat("#.######");
        String formattedString = df.format(val);
        String[] splitString = stringHelper.splitOnDelimeter(formattedString, "\\.");

        if (!isStarted) {
            console.error("Attempting to get clock duration time while clock is stopped.");
        }

        if (splitString[1].length() < 6) {
            int runFor = 6 - splitString[1].length();
            for (int i = 0; i < runFor; i++) {
                splitString[1] = splitString[1] + "0"; //append a 0
            }
            formattedString = splitString[0] + "." + splitString[1];
        }
        return formattedString;
    }

    //timer - function that will wait for waitTimeInMs
    public void timer(long waitTimeInMs) {
        long waitTimeInNs = waitTimeInMs * 3225310; //fix for inacurate times
        //long waitTimeInNs = waitTimeInMs * 1000000;
        long runUntil = getTime() + waitTimeInNs;

        while (getTime() < runUntil) {
            //do nothing
        }
    }
}
