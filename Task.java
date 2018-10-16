///////////////////
// Task: object data structure that represents a system task
///////////////////
public class Task extends OS {
    char code;
    String description;
    int numCycles;
    int length;

    //computedTaskTime - returns the computed task time in ms
    public long computedTaskTime() {
        return numCycles * config.times.get(description);
    }
}
