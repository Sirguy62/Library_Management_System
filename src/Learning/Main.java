import java.lang.String;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){

        // comments, first java code

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your name: ");
        String name = scanner.nextLine();


        System.out.println("Enter your age: ");
        int age = scanner.nextInt();

        System.out.println("Enter your gpa: ");
        double gpa = scanner.nextDouble();

        System.out.println("Hello " + name);
        System.out.println("You are " + age + "years old");
        System.out.println("Your gpa is: " + gpa );

        System.out.println("i like pizza");
        System.out.println("i like pizzasss");

        scanner.close();


    }
}
