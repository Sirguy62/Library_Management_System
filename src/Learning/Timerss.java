import java.util.Timer;
import java.util.TimerTask;

public class Timerss {
    public static void main(String[] args){

        Timer time = new Timer();
        TimerTask task = new TimerTask() {

            int count = 3;

            @Override
            public void run() {
                System.out.println("This is a timer");
                count--;
                if (count <= 0) {
                    System.out.println("TASK COMPLETED");
                    time.cancel();
                }
            }
        };
        time.schedule(task, 0, 1000);
    }
}
