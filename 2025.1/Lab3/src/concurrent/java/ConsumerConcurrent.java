import java.util.concurrent.Semaphore;

class ConsumerConcurrent {
    private final Buffer buffer;
    private final int sleepTime;
    private final int id;

    public ConsumerConcurrent(int id, Buffer buffer, int sleepTime) {
        this.id = id;
        this.buffer = buffer;
        this.sleepTime = sleepTime;
    }

    public synchronized void process(Semaphore semaphore) {
        while (true) {
            int item = buffer.remove();
            if (item % 2 == 0 && id % 2 != 0) {
                buffer.put(item);
            } else if (item % 2 != 0 && id % 2 == 0) {
                buffer.put(item);
            } else {
                if (item == -1)
                    break;
                System.out.println("Consumer " + id + " consumed item " + item);
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

        }
    }
}