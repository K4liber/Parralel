package threads;

import java.util.concurrent.atomic.AtomicIntegerArray;

// Compile: javac -d . Problem6.java
public class CountBlockAtomic implements Runnable {

    private double[] array;
    private AtomicIntegerArray checkArray;
    private double sum;
    private int blockSize;

    public CountBlockAtomic(double[] a, AtomicIntegerArray cA, int bS) {
       array = a;
       this.sum = 0;
       this.checkArray = cA;
       this.blockSize = bS;
    }

    public void run(){
        for (int i = 0; i < this.array.length/this.blockSize; i++) {
			if (this.checkArray.compareAndSet(i, 0, 1)) {
				for(int j = i*this.blockSize; j<(i+1)*blockSize; j++) {
					sum += Math.log10(array[j]);
				}
			}
		}
    }

    public double getSum() {
        return sum;
    }
    
    public void setSum(double s) {
        this.sum = s;
    }
}
