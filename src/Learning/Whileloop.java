import java.util.Scanner;

public class Whileloop {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String name = "";
        String response = "";
        int age = 1;
        int number = 1;

        while (name.isEmpty()) {
            System.out.println("Please enter your name: ");
            name = scanner.nextLine();
        }
        System.out.println("Hello " + name);

        while (!response.equals("Q")) {
            System.out.println("You are playing a gem");
            System.out.println("Press Q to quit: ");
            response = scanner.nextLine();
        }
        System.out.println("You have quit the game");


        System.out.println("Please enter your age");
        age = scanner.nextInt();

        while (age < 1) {
            System.out.println("Your age can't be less than 1");
            System.out.println("Please enter your age");
            age = scanner.nextInt();
        }
        System.out.println("You are" + age + "years old");


        //do while loop

        do {
            System.out.println("Please enter a number between 1 to 10");
            number = scanner.nextInt();
        } while (number < 1 || number > 10);

        System.out.println("You picked " + number);

        scanner.close();
    }
}
