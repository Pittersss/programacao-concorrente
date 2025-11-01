import java.util.concurrent.Semaphore;

public class Foo implements Runnable {
    Semaphore semaphore;

    public Foo(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        for (int i = 0; i < 200; i++){
            try {
                semaphore.release();
                Counter.count += 1;
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
