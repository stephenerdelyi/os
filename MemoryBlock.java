///////////////////
// MemoryBlock: Class used to store the memory block functions and states
///////////////////
public class MemoryBlock extends OS {
    private int blockLocation = 0; //the block's current cursor location
    private int maxBlockSize = 0; //the maximum size a block can be, set using setMaxBlockSize

    //setMaxBlockSize - sets the max block size; can be increased but not decreased
    public void setMaxBlockSize(int inputMaxBlockSize) {
        if (inputMaxBlockSize > maxBlockSize) {
            maxBlockSize = inputMaxBlockSize;
        } else {
            console.error("Can not lower the maxBlockSize value after it has been initialized.");
        }
    }

    //allocate - allocates the given number of bytes to the memory block, if possible
    public void allocate(int numBytes) {
        if (numBytes <= (maxBlockSize - blockLocation)) {
            blockLocation += numBytes;
        } else {
            //reset blockLocation and start over
            blockLocation = 0;
            blockLocation += numBytes;
        }
    }

    //getNumBytesAvailable - returns the number of bytes still available
    public int getNumBytesAvailable() {
        return maxBlockSize - blockLocation;
    }

    //getHex - returns the hex representation of the blockLocation
    public String getHex() {
        String returnHex = Integer.toHexString(blockLocation);

        //ensure the string is always 8 characters in length
        for (int i = returnHex.length(); i < 8; i++) {
            returnHex = "0" + returnHex;
        }
        returnHex = "0x" + returnHex;

        return returnHex;
    }
}
