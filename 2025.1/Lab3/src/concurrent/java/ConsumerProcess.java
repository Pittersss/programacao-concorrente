import java.util.concurrent.Semaphore;

public class ConsumerProcess implements Runnable {
    int numConsumers;
    Buffer buffer;
    int consumingTime;
    Semaphore semaphoreBufferControl;
    Semaphore semaphoreCanConsume;

    public ConsumerProcess(Semaphore semaphoreBufferControl, Semaphore semaphoreCanConsume, int numConsumers,
            Buffer buffer, int consumingTime) {
        this.numConsumers = numConsumers;
        this.buffer = buffer;
        this.consumingTime = consumingTime;
        this.semaphoreBufferControl = semaphoreBufferControl;
        this.semaphoreCanConsume = semaphoreCanConsume;
    }

    @Override
    public void run() {
        for (int i = 1; i <= numConsumers; i++) {
            ConsumerConcurrent consumer = new ConsumerConcurrent(i, buffer, consumingTime, semaphoreBufferControl,
                    semaphoreCanConsume);
            try {
                consumer.process();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
