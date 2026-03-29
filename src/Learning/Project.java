import java.util.Scanner;

public class Project {
    public static void main(String[] args) {

        // compound interest calculator

        Scanner scanner = new Scanner(System.in);
        double principal;
        double rate;
        int time;
        int year;
        double amount;

        System.out.println("Enter principal amount: ");
        principal = scanner.nextDouble();

        System.out.println("Enter interest rate (in %): ");
        rate = scanner.nextDouble() / 100;

        System.out.println("Enter the number of times compounded per year: ");
        time = scanner.nextInt();

        System.out.println("Enter the numbers of years: ");
        year = scanner.nextInt();

        amount = principal * Math.pow(
                1 + rate / time, time * year
        );

        System.out.println("The amount after " + year + "is: $" + amount);

        scanner.close();
    }
}
