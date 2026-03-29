import java.util.Scanner;

public class Logical {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        double temp = -20;
        boolean isSunny = false;
        String username;

        if (temp <= 30 && temp >= 0 && isSunny) {
            System.out.println("The weather is GOOD 😊");
            System.out.println("The weather is Sunny 🌞");
        } else if (temp <= 30 && temp >= 0 && !isSunny) {
            System.out.println("The weather is GOOD 😊");
            System.out.println("The weather is CLOUDY 🌞");
        } else if (temp > 30 || temp < 0) {
            System.out.println("The weather is Bad 😊");
        }


        //username must be between 4-12 characters
        // username must not contain space or underscores

        System.out.println("Enter your username: ");
        username = scanner.nextLine();

        if (username.length() < 4 || username.length() > 12){
            System.out.println("Username must be between 4-12 characters");
        } else if (username.contains(" ") || username.contains("_")) {
            System.out.println("Username must not contain spaces or underscores");
        } else {
            System.out.println("Welcome " + username);
        }

        scanner.close();
    }
}