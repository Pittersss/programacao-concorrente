import java.util.concurrent.Semaphore;

public class ConsumerProcess implements Runnable {
    int numConsumers;
    Buffer buffer;
    int consumingTime;
    Semaphore semaphore;

    public ConsumerProcess(Semaphore semaphore, int numConsumers, Buffer buffer, int consumingTime) {
        this.numConsumers = numConsumers;
        this.buffer = buffer;
        this.consumingTime = consumingTime;
    }

    @Override
    public void run() {
        for (int i = 1; i <= numConsumers; i++) {
            ConsumerConcurrent consumer = new ConsumerConcurrent(i, buffer, consumingTime);
            consumer.process(semaphore);
        }
    }
}
