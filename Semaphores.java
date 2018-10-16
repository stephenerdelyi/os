///////////////////
// Semaphores:
///////////////////
public class Semaphores extends OS {
    public OSSemaphore projector = new OSSemaphore();
    public OSSemaphore hardDrive = new OSSemaphore();
    public OSSemaphore keyboard = new OSSemaphore();

    Semaphores() {
        super();
        keyboard.init(1);
    }
}
