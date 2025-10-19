public class Xpto implements Runnable {
    int threadsQuantity;
    int[] values;

    public Xpto(int threadsQuantity, int[] values) {
        this.threadsQuantity = threadsQuantity;
        this.values = values;
    }

    @Override
    public void run() {
        try {
            SleepRandom(threadsQuantity, values);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(SumArray(values));
    }

    public void SleepRandom(int threadsQuantity, int[] values) throws InterruptedException {
        for (int i = 0; i < threadsQuantity; i++) {
            Thread thread = new Thread(new Foo(values, i));
            thread.start();
            thread.join();
        }
    }

    public int SumArray(int[] array) {
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }
        return sum;
    }
}
