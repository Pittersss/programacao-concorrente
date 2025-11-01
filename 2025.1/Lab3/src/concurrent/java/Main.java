import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        if (args.length != 5) {
            System.out.println(
                    "Use: java Main <num_producers> <max_items_per_producer> <producing_time> <num_consumers> <consuming_time>");
            return;
        }

        int numProducers = Integer.parseInt(args[0]);
        int maxItemsPerProducer = Integer.parseInt(args[1]);
        int producingTime = Integer.parseInt(args[2]);
        int numConsumers = Integer.parseInt(args[3]);
        int consumingTime = Integer.parseInt(args[4]);
        Semaphore semaphore = new Semaphore(100);

        Buffer buffer = new Buffer();

        Thread producerProcess = new Thread(
                new ProducerProcess(semaphore, numProducers, buffer, maxItemsPerProducer, producingTime));

        Thread consumerProcess = new Thread(new ConsumerProcess(semaphore, numConsumers, buffer, consumingTime));

        producerProcess.start();
        consumerProcess.start();

        producerProcess.join();
        consumerProcess.join();

    }
}
