import threads.MyThread;
import threads.MyRunnable;
import threads.CountRunnable;
import java.util.Random;
import java.time.Duration;
import java.time.Instant;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileOutputStream;

// Compile: javac -d . Problem1.java
// Run: java Problem1

public class Problem1 {
    
	public static void main(String[] args) {
        Integer threadsNumber = Runtime.getRuntime().availableProcessors();
        System.out.println("Threads number: " + threadsNumber);
    }

}
