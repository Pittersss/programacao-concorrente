import java.util.concurrent.Semaphore;

public class ProducerProcess implements Runnable {
    int numProducers;
    Buffer buffer;
    int maxItemsPerProducer;
    int producingTime;
    Semaphore semaphoreCanConsume;
    Semaphore semaphoreBufferControl;

    public ProducerProcess(Semaphore semaphoreBufferControl, Semaphore semaphoreCanConsume, int numProducers,
            Buffer buffer, int maxItemsPerProducer,
            int producingTime) {
        this.numProducers = numProducers;
        this.buffer = buffer;
        this.maxItemsPerProducer = maxItemsPerProducer;
        this.producingTime = producingTime;
        this.semaphoreBufferControl = semaphoreBufferControl;
        this.semaphoreCanConsume = semaphoreCanConsume;
    }

    @Override
    public void run() {
        for (int i = 1; i <= numProducers; i++) {
            try {
                for (int j = 0; j < maxItemsPerProducer; j++) {
                    semaphoreBufferControl.acquire();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Producer producer = new Producer(i, buffer, maxItemsPerProducer, producingTime);
            producer.produce();
            semaphoreCanConsume.release();
        }
    }

}
