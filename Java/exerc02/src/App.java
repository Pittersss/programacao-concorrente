import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {
        Semaphore semaphore = new Semaphore(1);

        Counter.count = 0;
        Thread thread1 = new Thread(new Foo(semaphore));
        Thread thread2 = new Thread(new Foo(semaphore));
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        System.out.println(Counter.count);
    }
}
