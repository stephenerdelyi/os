///////////////////
// MemoryBlock: Class used to store the memory block functions and states
///////////////////
public class MemoryBlock extends OS {
    private int blockLocation = 0; //the block's current cursor location
    private int maxBlockSize = 0; //the maximum size a block can be, set using setMaxBlockSize
    private int numResets = 0; //the number of times the memory block has had to reset
    private TaskStackQueue allocationBuffer = new TaskStackQueue("stack"); //the buffer of blocks stored in a stack for reference

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
        //if the number of bytes is larger than the maximum block size
        if (numBytes > maxBlockSize) {
            console.error("Trying to allocate a block larger than " + maxBlockSize + " bytes.");
        }
        //if the number of bytes is larger than the available number of bytes
        if (numBytes > (maxBlockSize - blockLocation)) {
            blockLocation = 0;
            numResets++;
            allocationBuffer.erase();
        }

        //set the block "task" length
        Task block = new Task();
        block.length = numBytes;

        blockLocation += numBytes;
        allocationBuffer.add(block);
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
