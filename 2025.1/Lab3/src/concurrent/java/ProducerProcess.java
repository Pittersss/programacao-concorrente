import java.util.concurrent.Semaphore;

public class ProducerProcess implements Runnable {
    int numProducers;
    Buffer buffer;
    int maxItemsPerProducer;
    int producingTime;
    Semaphore semaphore;

    public ProducerProcess(Semaphore semaphore, int numProducers, Buffer buffer, int maxItemsPerProducer,
            int producingTime) {
        this.numProducers = numProducers;
        this.buffer = buffer;
        this.maxItemsPerProducer = maxItemsPerProducer;
        this.producingTime = producingTime;
    }

    @Override
    public void run() {
        for (int i = 1; i <= numProducers; i++) {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Producer producer = new Producer(i, buffer, maxItemsPerProducer, producingTime);
            producer.produce();
        }
    }

}
