import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // if (args.length != 5) {
        // System.out.println(
        // "Use: java Main <num_producers> <max_items_per_producer> <producing_time>
        // <num_consumers> <consuming_time>");
        // return;
        // }

        int numProducers = 10;// Integer.parseInt(args[0]);
        int maxItemsPerProducer = 10;// Integer.parseInt(args[1]);
        int producingTime = 2;// Integer.parseInt(args[2]);
        int numConsumers = 100;// Integer.parseInt(args[3]);
        int consumingTime = 1;// Integer.parseInt(args[4]);
        Semaphore semaphoreBufferLimit = new Semaphore(100);
        Semaphore semaphoreCanConsume = new Semaphore(0);

        Buffer buffer = new Buffer();

        Thread producerProcess = new Thread(
                new ProducerProcess(semaphoreBufferLimit, semaphoreCanConsume, numProducers, buffer,
                        maxItemsPerProducer, producingTime));

        Thread consumerProcess = new Thread(
                new ConsumerProcess(semaphoreBufferLimit, semaphoreCanConsume, numConsumers, buffer, consumingTime));

        producerProcess.start();
        consumerProcess.start();
        producerProcess.join();
        consumerProcess.join();
    }
}