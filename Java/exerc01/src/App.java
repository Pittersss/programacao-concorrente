import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner leitor = new Scanner(System.in);
        int threadsQuantity = leitor.nextInt();
        int[] values = new int[threadsQuantity];
        Thread mainThread = new Thread(new Xpto(threadsQuantity, values));
        mainThread.start();
    }
}
