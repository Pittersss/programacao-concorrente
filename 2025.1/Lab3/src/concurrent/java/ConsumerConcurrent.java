import java.util.concurrent.Semaphore;

class ConsumerConcurrent {
    private final Buffer buffer;
    private final int sleepTime;
    private final int id;
    Semaphore semaphoreBufferControl;
    Semaphore semaphoreCanConsume;

    public ConsumerConcurrent(int id, Buffer buffer, int sleepTime, Semaphore semaphoreBufferControl,
            Semaphore semaphoreCanConsume) {
        this.id = id;
        this.buffer = buffer;
        this.sleepTime = sleepTime;
        this.semaphoreBufferControl = semaphoreBufferControl;
        this.semaphoreCanConsume = semaphoreCanConsume;
    }

    public synchronized void process() throws InterruptedException {
        while (true) {
            int item = buffer.remove();
            if (item == -1) {
                semaphoreCanConsume.acquire();
                item = buffer.remove();
            }

            if (item % 2 == 0 && id % 2 != 0) {
                buffer.put(item);
            } else if (item % 2 != 0 && id % 2 == 0) {
                buffer.put(item);
            } else {
                System.out.println("Consumer " + id + " consumed item " + item);
                try {
                    Thread.sleep(sleepTime);
                    semaphoreBufferControl.release();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}