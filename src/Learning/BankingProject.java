import java.util.Scanner;

public class BankingProject {
    static Scanner scanner = new Scanner(System.in);
    public static void main (String[] args) {

        double balance = 0;
        boolean isRunning = true;
        int choice;


        while (isRunning) {
            System.out.println("Banking Program");
            System.out.println("1. Show balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Exit");

            System.out.println("Choose a number (1-4): ");
            choice = scanner.nextInt();

            switch (choice){
                case 1 -> showBalance(balance);
                case 2 -> balance = balance + deposit();
                case 3 -> balance = balance - withdraw(balance);
                case 4 -> isRunning=false;
                default -> System.out.println("Invalid Choice");

            }
        }

        System.out.println("Thank you for using our service, have a nice day!");

        scanner.close();
    }

    static void showBalance(double balance) {
        System.out.printf("$%.2f\n", balance);
    }
    static  double deposit() {
        double amount;

        System.out.println("Enter amount to be deposited: ");
        amount = scanner.nextDouble();

        if (amount <= 0) {
            System.out.println("Amount can't be 0 or below");
            return 0;
        }else {
            System.out.printf("You deposited $%.2f, Select 1 to show current balance\n", amount);
            return amount;
        }
    }
    static  double withdraw(double balance) {
        double amount;

         System.out.println("Enter amount to withdraw: ");
         amount = scanner.nextDouble();

         if (amount > balance) {
             System.out.println("Insufficient Funds");
             return 0;
         } else if (amount < 0) {
             System.out.println("Please choose a valid amount");
             return 0;
         }else {
             return amount;
         }
     }
}
