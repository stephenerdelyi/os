///////////////////
// Locks: Class that holds all necessary semaphores and mutexes for the OS
///////////////////
public class Locks extends OS {
    public OSSemaphore projector = new OSSemaphore(); //projector semaphore (should have n permits where n is specified in the config file)
    public OSSemaphore hardDrive = new OSSemaphore(); //hardDrive semaphore (should have n permits where n is specified in the config file)
    public OSMutex keyboard = new OSMutex(); //keyboard mutex (should have 1 permit since there is only 1 keybaord)
}
