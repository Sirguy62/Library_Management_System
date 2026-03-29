import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class AlermClock {
    public static void main(String[] args) {

        // JAVA ALARM CLOCK PROJECT

        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime alarmTime = null;


        while (alarmTime == null){
            try {
                System.out.println("Enter an alarm time (HH:mm:ss): ");
                String inputTime = scanner.nextLine();

                alarmTime = LocalTime.parse(inputTime, formatter);
                System.out.println("Alarm set for " + alarmTime);
            }catch (DateTimeParseException e){
                System.out.println("Invalid time format, Please use HH:MM:SS");
            }
        }

        Clock alermClock = new Clock(alarmTime);
        Thread alermThread = new Thread(alermClock);
        alermThread.start();

        scanner.close();
    }
}
