import java.text.DecimalFormat; //Clock

///////////////////
// Clock:
///////////////////
public class Clock extends OS {
    long startTime;

    public void start() {
        startTime = getTime();
    }

    public long getTime() {
        return System.nanoTime();
    }

    public String getDurationTime() {
        double val = (System.nanoTime() - startTime) * .000000001;
        DecimalFormat df = new DecimalFormat("#.######");
        String formattedString = df.format(val);
        String[] splitString = stringHelper.splitOnDelimeter(formattedString, "\\.");

        if (splitString[1].length() < 6) {
            int runFor = 6 - splitString[1].length();
            for (int i = 0; i < runFor; i++) {
                splitString[1] = splitString[1] + "0"; //append a 0
            }
            formattedString = splitString[0] + "." + splitString[1];
        }
        return formattedString;
    }

    public void timer(long waitTimeInMs) {
        long waitTimeInNs = waitTimeInMs * 3225310;
        //long waitTimeInNs = waitTimeInMs * 1000000;
        long runUntil = getTime() + waitTimeInNs;

        while (getTime() < runUntil) {
            //do nothing
        }
    }
}
