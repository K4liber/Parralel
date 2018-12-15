import threads.CountRunnableSignal;
import java.util.Random;
import java.time.Duration;
import java.time.Instant;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Compile: javac -d . Problem4.java
// Run: java Problem5 'arraySize' 'loops'
public class Problem5 {
    
    private static Random r;
    private static Integer threadsNumber;
    private static Integer loops;
    private static Integer arraySize;

    public static void main(String[] args) {
        threadsNumber = Runtime.getRuntime().availableProcessors();
        arraySize = Integer.parseInt(args[0]);
        loops = Integer.parseInt(args[1]);
        r = new Random();
        try{
			new Problem5();
		} catch (InterruptedException err) {
			System.out.println("Error!");
		}
    }

    public Problem5() throws InterruptedException{
        double[] array = new double[arraySize];
        for(int i = 0; i < array.length; i++) {
            array[i] = 1 + r.nextDouble() * 1000000;
        }
        
        //Initialize runnables
        CountRunnableSignal[] countRunnables = new CountRunnableSignal[threadsNumber];
        
        //Split array
        int partSize = arraySize/threadsNumber;
		double[][] arrays = new double[threadsNumber][partSize];
		for (int i=0;i<partSize;i++) {
			for (int part=0;part<threadsNumber;part++) {
				arrays[part][i] = array[i+part*partSize];
			}
		}
		
		//Distribute parts for runnables
		CountDownLatch sync = new CountDownLatch(threadsNumber);
		for (int i=0;i<threadsNumber;i++) 
			countRunnables[i] = new CountRunnableSignal(arrays[i], sync);
		
		final ExecutorService executor = Executors.newFixedThreadPool(threadsNumber); 

		//Count mean time
		double timeMean = 0;
		for( int i=0;i<loops;i++) {
			Instant start = Instant.now();
			//Start threads
			for (int j=0;j<threadsNumber;j++) {
				executor.execute(countRunnables[j]);
			}
			sync.await(); 

			//Sum all
			double sum = 0;
			for (CountRunnableSignal countRunnable : countRunnables) {
				sum += countRunnable.getSum();
				countRunnable.setSum(0.0);
			}
			//System.out.println("Sum: " + sum);
			Instant end = Instant.now();
			timeMean += Duration.between(start, end).toMillis();
		}

		executor.shutdown();
		//Save Time to file 
		try (PrintWriter out = new PrintWriter(new FileOutputStream(
				new File("problem5.csv"), 
				true)
			)) {
			out.println(arraySize + ", " + timeMean);
		} catch (java.io.FileNotFoundException e) {
			System.out.println("File Error!");
		}
    }
}
