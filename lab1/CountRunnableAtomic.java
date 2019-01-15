package threads;

import java.util.concurrent.atomic.AtomicIntegerArray;

// Compile: javac -d . Problem6.java
public class CountRunnableAtomic implements Runnable {

    private double[] array;
    private AtomicIntegerArray checkArray;
    private double sum;

    public CountRunnableAtomic(double[] a, AtomicIntegerArray cA) {
       array = a;
       this.sum = 0;
       this.checkArray = cA;
    }

    public void run(){
        for (int i = 0; i < this.array.length; i++) {
			if (this.checkArray.compareAndSet(i, 0, 1)) {
				sum += Math.log10(array[i]);
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
