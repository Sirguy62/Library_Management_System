import java.util.Scanner;
public class IF {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int age;
        String name;
        boolean isStudent;

        System.out.println("Enter your age: ");
        age = scanner.nextInt();

        System.out.println("Enter your name: ");
        name = scanner.nextLine();

        System.out.println("Are you a student? (true/false): ");
        isStudent = scanner.nextBoolean();


        if(name.isEmpty()) {
            System.out.println("Please enter your name, it can not be empty");
        }else{
            System.out.println("Hello " + name + "!");
        }

        if(age >= 50) {
            System.out.println("You are a senior");
        } else if (age >= 18) {
            System.out.println("You are of right age");
        }else if (age <= 0) {
            System.out.println("You have not been born yet");
        }
        else {
            System.out.println("You are too young for this");
        }

        if(isStudent) {
            System.out.println("You are a student");
        }else {
            System.out.println("You are not a student");
        }

        scanner.close();
    }
}
