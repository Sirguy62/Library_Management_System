import java.awt.*;
import java.time.LocalTime;

public class Clock implements Runnable{

    private final LocalTime alarmTime;

    public Clock(LocalTime alarmTime) {
        this.alarmTime = alarmTime;
    }

    @Override
    public void run() {
//        LocalTime now = LocalTime.now();
//        System.out.println(now);

        while (LocalTime.now().isBefore(alarmTime)){
            try {
                Thread.sleep(1000);

                int hours = LocalTime.now().getHour();
                int minutes = LocalTime.now().getMinute();
                int seconds = LocalTime.now().getSecond();

                System.out.printf("\r%02d:%02d:%02d", hours, minutes, seconds);
            } catch (InterruptedException e) {
                System.out.println("Thread was interrupted");
            }
        }

        System.out.println("**\nALARM NOISES");
        System.out.println();
        Toolkit.getDefaultToolkit().beep();
    }
}
