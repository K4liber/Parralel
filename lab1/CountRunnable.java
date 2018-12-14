package threads;

public class CountRunnable implements Runnable {

    private double[] array;
    private double sum;

    public CountRunnable(double[] a) {
       array = a;
       sum = 0;
    }

    public void run(){
        for (double el : array) {
            sum += Math.log(el);
        }
    }

    public double getSum() {
        return sum;
    }
}