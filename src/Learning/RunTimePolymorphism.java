import java.util.Scanner;

public class RunTimePolymorphism {
    public static void main() {

        //Runtime polymorphism = when the method that gets executed is decided
        //                       at runtime based on the actual type of the object

        Scanner scanner = new Scanner(System.in);

        Animalss animal;

        System.out.println("Would you like a dog or a cat? (1 = dog, 2 = cat): ");
        int choice = scanner.nextInt();

        if (choice == 1){
            animal = new Puppy();
            animal.speak();
        } else if (choice == 2) {
            animal = new Kitten();
            animal.speak();
        }else {
            System.out.println("Invalid choice");
        }
    }
}
