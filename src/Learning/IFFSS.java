import java.util.Scanner;

public class IFFSS {
    public static void main(String[] args){
        // instead of using "if" we can use switch, more modern

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the day of the week");
        String day = scanner.nextLine();

        switch (day) {
            case "Monday","Tuesday","Wednesday","Thursday","Friday"  -> System.out.println("It is a weekday 😣");
            case "Saturday","Sunday" -> System.out.println("It is the weekend 😁");
            default ->  System.out.println(day + "is not a day");
        }

        scanner.close();
    }
}
