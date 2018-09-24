import java.util.Map;
import java.util.HashMap;

///////////////////
// ConfigFile: Holds all data for the system's configuration file
///////////////////
public class ConfigFile extends OS {
    String fileName;
    double version;
    int systemMemory;
    String inputFileName;
    String logOption;
    String logFileName;
    Map<String, Integer> times = new HashMap<>();

    //ConfigFile (constructor) - requires that the config file is named to proceed
    ConfigFile(String configFileName) {
        super();
        if (configFileName != "" && configFileName != null) {
            fileName = configFileName;
        } else {
            console.error("Config file is not named");
        }
    }

    //outputSettings - output the configuration settings
    public void outputSettings() {
        console.log("Monitor = " + times.get("monitor") + " ms/cycle");
        console.log("Processor = " + times.get("run") + " ms/cycle");
        console.log("Scanner = " + times.get("scanner") + " ms/cycle");
        console.log("Hard Drive = " + times.get("hard drive") + " ms/cycle");
        console.log("Keyboard = " + times.get("keyboard") + " ms/cycle");
        console.log("Memory = " + times.get("allocate") + " ms/cycle");
        console.log("Projector = " + times.get("projector") + " ms/cycle");
        if (logOption.equals("Log to Both")) {
            console.log("Logged to monitor and " + logFileName);
        } else if (logOption.equals("Log to Monitor")) {
            console.log("Logged to monitor");
        } else if (logOption.equals("Log to File")) {
            console.log("Logged to " + logFileName);
        }
    }
}
