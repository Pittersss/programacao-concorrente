import java.util.Random;

public class Foo implements Runnable {
    int[] values;
    int index;

    public Foo(int[] values, int index) {
        this.values = values;
        this.index = index;
    }

    @Override
    public void run() {
        Random random = new Random();
        int randomNumber = random.nextInt(10);
        System.out.println("VALOR ALEATORIO " + randomNumber);
        try {
            Thread.sleep(randomNumber);
            values[index] = randomNumber;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
