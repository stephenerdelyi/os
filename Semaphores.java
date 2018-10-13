///////////////////
// Semaphores:
///////////////////
public class Semaphores extends OS {
    public OSSemaphore projector = new OSSemaphore();
    public OSSemaphore hardDrive = new OSSemaphore();

    public void test() {
        projector.init(5);
        console.log(projector.numAvailablePermits());
        projector.acquire();
        projector.acquire();
        console.log(projector.numAvailablePermits());
        projector.release();
        console.log(projector.numAvailablePermits());
        projector.release();
        console.log(projector.numAvailablePermits());
    }
}
