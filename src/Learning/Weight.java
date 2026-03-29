import java.util.Scanner;

public class Weight {
    public  static void main (String[] args) {
        //Weight conversion program

        Scanner scanner = new Scanner(System.in);

        double weight;
        double newWeight;
        int choice;
        int score = 75;
        int number = 5;
        int hour = 13;
        int income = 60000;

        System.out.println("Weight conversion program");
        System.out.println("1: Convert lbs to kgs");
        System.out.println("2: convert kgs to lbs");

        System.out.println("Choose an option: ");
        choice = scanner.nextInt();

        if(choice == 1){
            System.out.println("Enter the weight in lbs: ");
            weight = scanner.nextDouble();
            newWeight =weight * 0.453592;
            System.out.printf("The new weight in kgs is %.2f", newWeight);
        } else  if(choice == 2){
            System.out.println("Enter the weight in kgs: ");
            weight = scanner.nextDouble();
            newWeight =weight * 2.20462;
            System.out.printf("The new weight in lbs is %.2f", newWeight);
        } else {
            System.out.println("Invalid input");
        }

        //Another way of writing if statement using ternaries
        String passOrFail = (score >= 60) ? "Pass" : "Fail";
        String evenOrOdd = (number % 2 == 0) ? "EVEN" : "ODD";
        String time = (hour > 12) ? "P.M." : "A.M.";
        double tax = (income >= 45000) ? 0.25 : 0.15;
        System.out.println(passOrFail);
        System.out.println(evenOrOdd);
        System.out.println(time);
        System.out.println(tax);

        scanner.close();
    }
}
