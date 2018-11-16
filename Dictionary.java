///////////////////
// Dictionary:
///////////////////
public class Dictionary extends OS {
    private int numAllowed = 150;
    public int numCreated = 0;
    public Definition[] definitions = new Definition[numAllowed];

    //add -
    public boolean add(Definition newDefintion) {
        if (!isFull()) {
            if (!definitionExists(newDefintion.definition)) {
                definitions[numCreated] = newDefintion;
                numCreated++;
                return true;
            } else {
                console.error("Tried to re-define the definition: " + newDefintion.definition);
            }
        } else {
            console.error("Ran out of dictionary space - increase numAllowed");
        }
        //should never reach this return
        return false;
    }

    //isFull - returns whether or not the dictionary is full
    public boolean isFull() {
        if (numCreated >= numAllowed) {
            return true;
        }
        return false;
    }

    public boolean definitionExists(String definition) {
        for(int i = 0; i < numCreated; i++) {
            if (definitions[i].definition.equals(definition)) {
                return true;
            }
        }
        return false;
    }

    public void markDefinitionRead(String definition) {
        for(int i = 0; i < numCreated; i++) {
            if (definitions[i].definition.equals(definition)) {
                definitions[i].usedDefinition = true;
            }
        }
    }
}
