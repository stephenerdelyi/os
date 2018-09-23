///////////////////
// PCB:
///////////////////
public class PCB extends OS {
    int processState = 0; //review setProcessState for integer values

    //setProcessState -
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

    //getProcessStateInt -
    public int getProcessStateInt() {
        return processState;
    }

    //getProcessStateString -
    public String getProcessStateString() {
        return validKeys.processStateDeclarations[processState];
    }
}
