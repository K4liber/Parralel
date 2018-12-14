import threads.MyThread;
import threads.MyRunnable;
import threads.CountRunnable;
import java.util.Random;
import java.time.Duration;
import java.time.Instant;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileOutputStream;

// Compile: javac -d . HelloWorld.java
// Run: java HelloWorld
public class HelloWorld {
    
    private static Random r;
    private static Integer arraySize;
    private static Integer loops;

    public static void main(String[] args) {
        Integer threadsNumber = Runtime.getRuntime().availableProcessors();
        System.out.println("Threads number: " + threadsNumber);
        arraySize = Integer.parseInt(args[0]);
        loops = Integer.parseInt(args[1]);
        r = new Random();
        try{
			new HelloWorld();
		} catch (InterruptedException err) {
			System.out.println("Error!");
		}
    }

    public HelloWorld() throws InterruptedException{
        double[] array = new double[arraySize];
        for(int i = 0; i < array.length; i++) {
            array[i] = 1 + r.nextDouble() * 1000000;
        }
        //MyRunnable firstRunnable = new MyRunnable();
        /*
        CountRunnable countRunnable = new CountRunnable(array);
        Thread firstThread = new Thread(countRunnable, "T1");
        try{
			firstThread.start();
			firstThread.join();
		} catch (InterruptedException err) {
			System.out.println("Error!");
		}
        System.out.println("Sum: " + countRunnable.getSum());
		*/
		countLogSum(array);
    }

    
    public void countLogSum(double[] array) {
		double timeMean = 0;
		double sum = 0;
		for(int i=0;i<loops;i++) {
			Instant start = Instant.now();
			for (double el : array) {
				sum += Math.log(el);
			}
			Instant end = Instant.now();
			timeMean += Duration.between(start, end).toMillis();
		}
		try (PrintWriter out = new PrintWriter(new FileOutputStream(
				new File("data.csv"), 
				true)
			)) {
			out.println(arraySize + ", " + timeMean);
		} catch (java.io.FileNotFoundException e) {
			System.out.println("File Error!");
		}
	}
}
