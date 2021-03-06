package threads;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Arrays;

public class CountEnergy implements Runnable {
	
	private boolean s[][] ;
	private int i;
	private int j;
	private int N;
	private CountDownLatch latch;
	private AtomicBoolean locks[][];
	private double jota;
	private double beta;
	private double h;
	private AtomicInteger stepsDid;
	private Random random;
	
	public CountEnergy(double jota, double beta, double h, boolean s[][], int N, CountDownLatch latch, AtomicBoolean locks[][], AtomicInteger stepsDid) {
		this.s = s;
		this.N = N;
		this.latch = latch;
		this.locks = locks;
		this.jota = jota;
		this.beta = beta;
		this.h = h;
		this.stepsDid = stepsDid;
		this.random = new Random();
	}
	
	public void run() {
		boolean didJob = false;
		while (!didJob) {
			this.i = this.random.nextInt(this.N);
			this.j = this.random.nextInt(this.N);
					
			boolean neighbours[] = new boolean[4];
			Arrays.fill(neighbours, false);
			boolean node = this.locks[i][j].compareAndSet(false, true);
			neighbours[0] = this.locks[(i+1)%N][j].compareAndSet(false, true);
			neighbours[1] = this.locks[i][(j+1)%N].compareAndSet(false, true);
			neighbours[2] = this.locks[(i-1+N)%N][j].compareAndSet(false, true);
			neighbours[3] = this.locks[i][(j-1+N)%N].compareAndSet(false, true);

			if (neighbours[0] && neighbours[1] && neighbours[2] && neighbours[3] && node) {
				double deltaE = 0.0;

				if (this.s[i][j] == this.s[(i+1)%N][j])   deltaE++;
				else                                  deltaE--;
				if (this.s[i][j] == this.s[i][(j+1)%N])   deltaE++;
				else                                  deltaE--;
				if (this.s[i][j] == this.s[(i-1+N)%N][j]) deltaE++;
				else                                  deltaE--;
				if (this.s[i][j] == this.s[i][(j-1+N)%N]) deltaE++;
				else                                  deltaE--;

				int spin = this.s[i][j]? 1 : -1;
				deltaE = 0.5*this.jota * deltaE + spin * h;

				if ((deltaE <= 0) || (Math.random() <= Math.exp(-deltaE*beta)))
					this.s[i][j] = !this.s[i][j];

				this.locks[i][j].compareAndSet(true, false);
				this.locks[(i+1)%N][j].compareAndSet(true, false);
				this.locks[i][(j+1)%N].compareAndSet(true, false);
				this.locks[(i-1+N)%N][j].compareAndSet(true, false);
				this.locks[i][(j-1+N)%N].compareAndSet(true, false);
				this.stepsDid.incrementAndGet();
				this.latch.countDown();
				didJob = true;
			} else {
				if (node) this.locks[i][j].compareAndSet(true, false);
				if (neighbours[0]) this.locks[(i+1)%N][j].compareAndSet(true, false);
				if (neighbours[1]) this.locks[i][(j+1)%N].compareAndSet(true, false);
				if (neighbours[2]) this.locks[(i-1+N)%N][j].compareAndSet(true, false);
				if (neighbours[3]) this.locks[i][(j-1+N)%N].compareAndSet(true, false);
			}
		}
	}
	
}
