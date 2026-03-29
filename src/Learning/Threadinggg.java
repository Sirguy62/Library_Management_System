import java.util.Scanner;

public class Threadinggg {
    public static void main(String[] args) {

        //Threading = Allow a program to run multiple tasks simultaneously
        //            Helps improve performance with time-consuming operation
        //            (File I/O, network communication or any background tasks)

        // how to create a thread
        // Option 1. Extend the thread class (simpler)
        // option 2. implement the Runnable interface (better)

        Scanner scanner = new Scanner(System.in);
        MyRunnable myRunnable = new MyRunnable();
        Thread thread = new Thread(myRunnable);
        thread.setDaemon(true);
        thread.start();

        System.out.println("You have just 10 seconds to enter your name");

        System.out.println("Please enter your name: ");
        String name = scanner.nextLine();
        System.out.println("Hello " + name);

        scanner.close();
    }
}
