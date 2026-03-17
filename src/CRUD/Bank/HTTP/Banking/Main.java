package CRUD.Bank.HTTP.Banking;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Bank bank = new Bank();
        bank.loadFromFile("accounts.txt");

        boolean running = true;
        System.out.println("\n====== BANK MENU ======");
        while (running) {

            System.out.println("1. Create Account");
            System.out.println("2. View Accounts");
            System.out.println("3. Search account with account number");
            System.out.println("4. Search account by name");
            System.out.println("5. Deposit");
            System.out.println("6. Withdraw");
            System.out.println("7. Transfer");
            System.out.println("8. View transactions");
            System.out.println("9. View account details");
            System.out.println("0. Exit");

            try {
                int choice = scanner.nextInt();
                switch (choice) {

                    case 1:
                        System.out.print("Enter account number: ");
                        int number = scanner.nextInt();
                        scanner.nextLine();

                        System.out.print("Enter account name: ");
                        String name = scanner.nextLine();

                        Account account = new Account(number, name);
                        bank.createAccount(account);
                        bank.saveToFile("accounts.txt");
                        break;

                    case 2:
                        bank.viewAccounts();
                        break;

                    case 3:
                        System.out.print("Enter account number: ");
                        int accNum = scanner.nextInt();
                        scanner.nextLine();

                        Account acc = bank.findAccount(accNum);

                        if (acc == null) {
                            System.out.println("Account not found.");
                        } else {
                            System.out.println(acc);
                        }
                        break;

                    case 4:
                        System.out.print("Enter owner name keyword: ");
                        String keyword = scanner.nextLine();

                        bank.searchAccountsByOwner(keyword);
                        break;

                    case 5:
                        System.out.print("Enter account number: ");
                        int depAcc = scanner.nextInt();

                        System.out.print("Enter amount to deposit: ");
                        double depAmount = scanner.nextDouble();
                        scanner.nextLine();

                        bank.deposit(depAcc, depAmount);
                        bank.saveToFile("accounts.txt");
                        break;

                    case 6:
                        System.out.print("Enter account number: ");
                        int witAcc = scanner.nextInt();

                        System.out.print("Enter amount to withdraw: ");
                        double witAmount = scanner.nextDouble();
                        scanner.nextLine();

                        bank.withdraw(witAcc, witAmount);
                        bank.saveToFile("accounts.txt");
                        break;

                    case 7:
                        System.out.print("Enter sender account number: ");
                        int fromAcc = scanner.nextInt();

                        System.out.print("Enter receiver account number: ");
                        int toAcc = scanner.nextInt();

                        System.out.print("Enter amount to transfer: ");
                        double tfAmount = scanner.nextDouble();
                        scanner.nextLine();

                        bank.transfer(fromAcc, toAcc, tfAmount);
                        bank.saveToFile("accounts.txt");
                        break;

                    case 8:
                        System.out.print("Enter account number to view transaction history: ");
                        int accountNum = scanner.nextInt();
                        scanner.nextLine();

                        bank.viewTransactions(accountNum);
                        break;

                    case 9:
                        System.out.print("Enter account number to view transaction details: ");
                        int accountNum2 = scanner.nextInt();
                        scanner.nextLine();

                        bank.viewAccountDetails(accountNum2);
                        break;

                    case 0:
                        running = false;
                        System.out.println("Exiting...");
                        break;

                    default:
                        System.out.println("Invalid choice");
                }
            } catch (Exception e) {
                System.out.println("Invalid input type.");
                scanner.nextLine();
                continue;
            }
        }


        bank.saveToFile("accounts.txt");
        scanner.close();
    }
}
