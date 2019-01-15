import threads.CountRunnableAtomic;
import java.util.Random;
import java.time.Duration;
import java.time.Instant;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.atomic.AtomicIntegerArray;

// Compile: javac -d . Problem4.java
// Run: java Problem4 'arraySize' 'loops'
public class Problem6 {
    
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
			new Problem6();
		} catch (InterruptedException err) {
			System.out.println("Error!");
		}
    }

    public Problem6() throws InterruptedException{
        double[] array = new double[arraySize];
        AtomicIntegerArray checkArray =  new AtomicIntegerArray(arraySize);
        for(int i = 0; i < array.length; i++) {
            array[i] = 100;//1 + r.nextDouble() * 1000000;
            checkArray.set(i, 0);
        }
        
        //Initialize runnables
        CountRunnableAtomic[] countRunnables = new CountRunnableAtomic[threadsNumber];
		
		//Distribute parts for runnables
		for (int i=0;i<threadsNumber;i++) 
			countRunnables[i] = new CountRunnableAtomic(array, checkArray);
		
		//Count mean time
		double timeMean = 0;
		for( int i=0;i<loops;i++) {
			Instant start = Instant.now();
			//Start threads
			Thread[] threads = new Thread[threadsNumber];
			for (int j=0;j<threadsNumber;j++) {
				threads[j] = new Thread(countRunnables[j]);
				threads[j].start();
			}
			
			//Join all
			for (int j=0;j<threadsNumber;j++) {
				try{
					threads[j].join();
				} catch (InterruptedException err) {
					System.out.println("Error!");
				}
			}
			//Sum all
			double sum = 0;
			for (CountRunnableAtomic countRunnable : countRunnables) {
				sum += countRunnable.getSum();
				countRunnable.setSum(0.0);
			}
			System.out.println("Sum: " + sum);
			Instant end = Instant.now();
			timeMean += Duration.between(start, end).toMillis();
			
			// Clear checks
			for(int j = 0; j < array.length; j++) {
				checkArray.set(j, 0);
			}
		}
		
		//Save Time to file 
		try (PrintWriter out = new PrintWriter(new FileOutputStream(
				new File("problem6.csv"), 
				true)
			)) {
			out.println(arraySize + ", " + timeMean);
		} catch (java.io.FileNotFoundException e) {
			System.out.println("File Error!");
		}
    }
}
