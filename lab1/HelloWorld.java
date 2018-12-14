import threads.MyThread;
import threads.MyRunnable;
import threads.CountRunnable;
import java.util.Random;

// Compile: javac -d . HelloWorld.java
// Run: java HelloWorld
public class HelloWorld {
    
    private static Random r;
    private static Integer arraySize;

    public static void main(String[] args) {
        Integer threadsNumber = Runtime.getRuntime().availableProcessors();
        System.out.println("Threads number: " + threadsNumber);
        arraySize = Integer.parseInt(args[0]);
        r = new Random();
        new HelloWorld();
    }

    public HelloWorld() {
        double[] array = new double[arraySize];
        for (double el : array){
            el = 1 + r.nextDouble() * 1000000;
        }
        //MyRunnable firstRunnable = new MyRunnable();
        CountRunnable countRunnable = new CountRunnable(array);
        Thread firstThread = new Thread(countRunnable, "T1");
        firstThread.start();
        firstThread.join();
        System.out.println("Sum: " + countRunnable.getSum());
    }
}