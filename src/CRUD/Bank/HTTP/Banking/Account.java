package CRUD.Bank.HTTP.Banking;

import java.util.ArrayList;

public class Account {
    private int accountNumber;
    private String ownerName;
    private double balance;
    private ArrayList<String> transactions;


    public Account(int accountNumber, String ownerName) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = 0;
        this.transactions = new ArrayList<>();
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Amount must be greater than 0.");
            return;
        }
         balance = balance + amount;
        addTransaction("Deposited: " + amount + " | Balance: " + balance);
    }

    public void withdraw(double amount) {
       if (amount <= 0) {
           System.out.println("Amount must be greater than 0.");
           return;
       }
       if (amount > balance) {
           System.out.println("Insufficient funds.");
           addTransaction("Failed Withdrawal: " + amount);
           return;
       }
       balance = balance - amount;
       addTransaction("Withdrawn: " + amount + " | Balance: " + balance);
    }

    private void addTransaction(String description) {
        transactions.add(description);
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public double getBalance() {
        return balance;
    }

    public ArrayList<String> getTransactions() {
        return transactions;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @Override
    public String toString() {
        return "Account Number: " +
                accountNumber + ", | Owner: " +
                ownerName + ", | Balance: " +
                balance;
    }











}
