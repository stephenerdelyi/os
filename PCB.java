///////////////////
// PCB: Process control block class used to determine process state
///////////////////
public class PCB extends OS {
    int processState = 0; //review setProcessState for integer values

    //setProcessState - sets the process state by inputting either valid string or integer value
    public <Generic> void setProcessState(Generic inputProcessState) {
        if (inputProcessState instanceof String) {
            int processStateIndex = stringHelper.findTokenIndexInArray(validKeys.processStateDeclarations, inputProcessState.toString());
            if (processStateIndex != -1) {
                processState = processStateIndex;
            }
        } else if (inputProcessState instanceof Integer) {
            int processStateIndex = (Integer) inputProcessState;
            if (processStateIndex >= 0 && processStateIndex < validKeys.processStateDeclarations.length) {
                processState = processStateIndex;
            }
        }
    }

    //getProcessStateInt - returns the current process state in int format
    public int getProcessStateInt() {
        return processState;
    }

    //getProcessStateString - returns the current process state in string format
    public String getProcessStateString() {
        return validKeys.processStateDeclarations[processState];
    }
}
