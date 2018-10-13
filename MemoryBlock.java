///////////////////
// MemoryBlock:
///////////////////
public class MemoryBlock extends OS {
    private int blockLocation = 0;
    private int maxBlockSize = 0;

    public void setMaxBlockSize(int inputMaxBlockSize) {
        if (inputMaxBlockSize > maxBlockSize) {
            maxBlockSize = inputMaxBlockSize;
        } else {
            console.error("Can not lower the maxBlockSize value after it has been initialized.");
        }
    }

    public void allocate(int numBytes) {
        if (numBytes <= (maxBlockSize - blockLocation)) {
            blockLocation += numBytes;
        } else {
            console.error("Ran out of system memory. Please increase system memory in " + config.fileName + ".");
        }
    }

    public int getNumBytesAvailable() {
        return maxBlockSize - blockLocation;
    }

    public String getHex() {
        String returnHex = Integer.toHexString(blockLocation);

        for (int i = returnHex.length(); i < 8; i++) {
            returnHex = "0" + returnHex;
        }
        returnHex = "0x" + returnHex;

        return returnHex;
    }
}
