import threads.CountBlockAtomic;
import java.util.Random;
import java.time.Duration;
import java.time.Instant;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.atomic.AtomicIntegerArray;

// Compile: javac -d . Problem7a.java
// Run: java Problem7a 'arraySize' 'loops' 'blockSize'
public class Problem7a {
    
    private static Random r;
    private static Integer threadsNumber;
    private static Integer loops;
    private static Integer arraySize;
    private static Integer blockSize;

    public static void main(String[] args) {
        threadsNumber = Runtime.getRuntime().availableProcessors();
        arraySize = Integer.parseInt(args[0]);
        loops = Integer.parseInt(args[1]);
        blockSize = Integer.parseInt(args[2]);
        r = new Random();
        try{
			new Problem7a();
		} catch (InterruptedException err) {
			System.out.println("Error!");
		}
    }

    public Problem7a() throws InterruptedException {
        double[] array = new double[arraySize];
        AtomicIntegerArray checkArray =  new AtomicIntegerArray(arraySize/blockSize);
        for(int i = 0; i < array.length; i++) {
            array[i] = 1000;//1 + r.nextDouble() * 1000000;
        }
        for(int i = 0; i < arraySize/blockSize; i++) {
			checkArray.set(i, 0);
		}
        
        //Initialize runnables
        CountBlockAtomic[] countRunnables = new CountBlockAtomic[threadsNumber];
		
		//Distribute parts for runnables
		for (int i=0;i<threadsNumber;i++) 
			countRunnables[i] = new CountBlockAtomic(array, checkArray, blockSize);
		
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
			for (CountBlockAtomic countRunnable : countRunnables) {
				sum += countRunnable.getSum();
				countRunnable.setSum(0.0);
			}
			System.out.println("Sum: " + sum);
			Instant end = Instant.now();
			timeMean += Duration.between(start, end).toMillis();
			
			// Clear checks
			for(int j = 0; j < arraySize/blockSize; j++) {
				checkArray.set(j, 0);
			}
		}
		
		//Save Time to file 
		try (PrintWriter out = new PrintWriter(new FileOutputStream(
				new File("problem7a.csv"), 
				true)
			)) {
			out.println(arraySize + ", " + timeMean + ", " + blockSize);
		} catch (java.io.FileNotFoundException e) {
			System.out.println("File Error!");
		}
    }
}
