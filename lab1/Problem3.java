import java.util.Random;
import java.time.Duration;
import java.time.Instant;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileOutputStream;

// Compile: javac -d . Problem3.java
// Run: java Problem3 'arraySize' 'loops'
public class Problem3 {
    
    private static Random r;
    private static Integer arraySize;
    private static Integer loops;

    public static void main(String[] args) {
        arraySize = Integer.parseInt(args[0]);
        loops = Integer.parseInt(args[1]);
        r = new Random();
        try{
			new Problem3();
		} catch (InterruptedException err) {
			System.out.println("Error!");
		}
    }

    public Problem3() throws InterruptedException{
        double[] array = new double[arraySize];
        for(int i = 0; i < array.length; i++) {
            array[i] = 100;
        }
		countLogSum(array);
    }

    
    public void countLogSum(double[] array) {
		double timeMean = 0;
		double sum = 0;
		for(int i=0;i<loops;i++) {
			Instant start = Instant.now();
			for (double el : array) {
				sum += Math.log10(el);
			}
			Instant end = Instant.now();
			timeMean += Duration.between(start, end).toMillis();
		}
		try (PrintWriter out = new PrintWriter(new FileOutputStream(
				new File("problem3.csv"), 
				true)
			)) {
			out.println(arraySize + ", " + timeMean);
		} catch (java.io.FileNotFoundException e) {
			System.out.println("File Error!");
		}
	}
}
