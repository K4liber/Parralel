package threads;
import java.util.concurrent.CountDownLatch;

public class CountRunnableSignal implements Runnable {

    private double[] array;
    private double sum;
    CountDownLatch sync;

    public CountRunnableSignal(double[] a, CountDownLatch s) {
       array = a;
       this.sum = 0;
       this.sync = s;
    }

    public void run(){
        for (double el : array) {
            sum += Math.log(el);
        }
        sync.countDown();
    }

    public double getSum() {
        return sum;
    }
    
    public void setSum(double s) {
        this.sum = s;
    }
}
