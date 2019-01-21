import java.util.Random;
import java.awt.Color;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.Executors;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileOutputStream;

import draw.StdDraw;
import threads.CountEnergy;

// Check magnetisation in function of B
// Check magnetisation after x steps and do it in loops and count mean
// https://introcs.cs.princeton.edu/java/98simulation/Ising.java.html

public class Ising {
	
	private int N = 100;
	private double J;
	private double h;
	private double B;
	private boolean[][] s;
	private int steps = 100000;
	private CountEnergy[][] counters;
	private ExecutorService executor; 
	private CountDownLatch latch;
	private Integer availableProcessors;
	private AtomicBoolean[][] locks;
	private Random random;

	public Ising(int N, double J, double h, double B) {
		this.N = N;
		this.J = J;
		this.h = h;
		this.B = B;
		this.random = new Random();
		this.counters = new CountEnergy[N][N];
		this.s = new boolean[N][N];
		this.availableProcessors = Runtime.getRuntime().availableProcessors();
		this.executor = Executors.newFixedThreadPool(this.availableProcessors);
		this.randomSpins();
		this.createLocks();
		this.createCounters();
	}
	
	public void vizualize() {
        StdDraw.setXscale(0, this.N);
        StdDraw.setYscale(0, this.N);
        
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                if (this.s[i][j]) StdDraw.setPenColor(StdDraw.WHITE);
                else            StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.filledSquare(i + 0.5, j + 0.5, .5);
            }
        }

		StdDraw.setPenColor(StdDraw.BLACK);
		
        for (int i = 0; i < this.N; i++) {
            StdDraw.line(i, 0, i, this.N);
            StdDraw.line(0, i, this.N, i);
        }
        
	}
	
	private void randomSpins() {

		for (int i=0;i<this.N;i++) {
			for (int j=0;j<this.N;j++) {
				this.s[i][j] = this.random.nextInt(2) == 1;
			}
		}
	}
	
	private void createCounters() {
		this.latch = new CountDownLatch(this.steps);

		for (int i=0;i<this.N;i++) {
			for (int j=0;j<this.N;j++) {
				this.counters[i][j] = new CountEnergy(J, B, h, s, i, j, N, this.latch, this.locks);
			}
		}
	}

	private void createLocks() {
		this.locks = new AtomicBoolean[N][N];
		for (int i = 0; i < this.N; i++) {
			for (int j = 0; j < this.N; j++) {
				this.locks[i][j] = new AtomicBoolean(false);
			}
		}
	}

	private void saveResults() {
		double magnetisation = 0;

		for (int i=0;i<N;i++) {
			for (int j=0;j<N;j++) {
				if (s[i][j]) magnetisation++; else magnetisation--;
			}
		}

		magnetisation = magnetisation/(N*N);

		try (PrintWriter out = new PrintWriter(new FileOutputStream(
				new File("dataN" + N + "J" + J + "h" + h + ".csv"), 
				true)
			)) {
			out.println(N + ", " + J + ", " + h + ", " + B + ", " + magnetisation);
		} catch (java.io.FileNotFoundException e) {
			System.out.println("File Error!");
		}
	}

	public void go() {
		try {
			while (this.latch.getCount() > 0) {
				int i = this.random.nextInt(this.N);
				int j = this.random.nextInt(this.N);
				this.executor.execute(this.counters[i][j]);
			}

			this.latch.await();
			this.executor.shutdown();
			this.saveResults();
		} catch (InterruptedException interruptedException) {
			System.out.println(interruptedException.getMessage());
		}
	}
	
	public static void main(String[] args) {
		int N = Integer.parseInt(args[0]);
		double J = Double.parseDouble(args[1]);
		double h = Double.parseDouble(args[2]);
		double B = Double.parseDouble(args[3]);
		Ising model;
		for (int i=0; i<100; i++) {
			model = new Ising(N, J, h, B);
			// model.vizualize();
			model.go();
		}
	}
	
}
