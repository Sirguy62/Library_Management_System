import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateandTime {
    public static void main() {

        //How to work with date and time using java
        //(LocalDate, LocalTime, LocalDateTime, UTC timestamp)

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        LocalDateTime timeDate = LocalDateTime.now();
        Instant instant = Instant.now(); // UTC time
        System.out.println(date);
        System.out.println(time);
        System.out.println(timeDate);
        System.out.println(instant);

        //Custom format
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String newDateTime = dateTime.format(dateTimeFormatter);

        System.out.println(newDateTime);

        //Custom date object

        LocalDateTime date1 = LocalDateTime.of(2026, 12, 23, 12, 0 ,0);
        LocalDateTime date2 = LocalDateTime.of(2027, 1, 3, 11, 3,5);

        if (date1.isBefore(date2)) {
            System.out.println(date1 + "is earlier than " + date2);
        }
        else {
            System.out.println(date1 + " is later than " + date2);
        }
    }
}
