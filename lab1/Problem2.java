import threads.HelloWorld;

// Compile: javac -d . Problem2.java
// Run: java Problem2 arraySize loops
public class Problem2 {

    public static void main(String[] args) {
        HelloWorld helloWorld = new HelloWorld();
		helloWorld.start();
    }
}
