import java.io.IOException;
import java.util.Scanner;

public class Emumss {
    public static void main (String[] args) {

        // Enums = (Enumerations) A special kind of class that represents a fixed set of constants.
        //          they improve code readability and are easy to maintain,
        //          More efficiently with switches when comparing strings.

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a day of the week: ");
        String response = scanner.nextLine().toUpperCase();

        try {
            Day day = Day.valueOf(response);

            System.out.println(day);
            System.out.println(day.getDayNumber());
            switch (day){
                case MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY -> System.out.println("it is a weekday");
                case SATURDAY, SUNDAY -> System.out.println("It is the weekend");

            }
        }catch (IllegalArgumentException e){
            System.out.println("Invalid input");
        }
        finally {
            System.out.println("This will always run");
        }


        scanner.close();
    }
}
