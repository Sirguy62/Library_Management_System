package CRUD.Bank.HTTP.Banking;

import java.io.*;
import java.util.ArrayList;

public class Bank {

    private ArrayList<Account> accounts;

    public Bank() {
        accounts = new ArrayList<>();
    }
    public void createAccount(Account account) {
        for (Account a : accounts) {
            if (a.getAccountNumber() == account.getAccountNumber()) {
                System.out.println("Account already exists");
                return;
            }
        }
        accounts.add(account);
        System.out.println("Account created successfully");
    }
    public void viewAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts found");
            return;
        }
        for (Account account : accounts) {
            System.out.println(account);
        }
    }

    public Account findAccount(int accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber() == accountNumber) {
                return account;
            }
        }
        return null;
    }

    public void searchAccountsByOwner(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Field cannot be empty.");
            return;
        }
        boolean found = false;
        for (Account account : accounts) {
            if (account.getOwnerName().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(account);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No matching accounts found.");
        }
    }

    public void deposit(int accountNumber, double amount) {
        Account account = findAccount(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        account.deposit(amount);
    }

    public void withdraw(int accountNumber, double amount) {
        Account account = findAccount(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        } else {
            account.withdraw(amount);
        }
    }

    public void transfer(int fromAccountNumber, int toAccountNumber, double amount) {
        Account fromAccount = findAccount(fromAccountNumber);
        Account toAccount = findAccount(toAccountNumber);
        if (fromAccount == null || toAccount == null) {
            System.out.println("account not found.");
            return;
        }
        if (fromAccountNumber == toAccountNumber) {
            System.out.println("Cannot transfer to the same account.");
            return;
        }
        if (amount <= 0) {
            System.out.println("Transfer amount must be greater than zero.");
            return;
        }
        if (fromAccount.getBalance() < amount) {
            System.out.println("Insufficient funds.");
            return;
        }
        fromAccount.withdraw(amount);
        toAccount.deposit(amount);
        System.out.println("Transfer successful.");
    }

    public void viewTransactions(int accountNumber) {
        Account account = findAccount(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        ArrayList<String> transactions = account.getTransactions();

        if (transactions.isEmpty()) {
            System.out.println("No transactions available.");
            return;
        }
        for (String t : transactions) {
            System.out.println(t);
        }
    }

    public void viewAccountDetails(int accountNumber) {
        Account account = findAccount(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        System.out.println(account);
        int transactionCount = account.getTransactions().size();
        System.out.println("Total Transactions: " + transactionCount);
    }


    public void saveToFile(String path) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (Account account : accounts) {
                String accountLine =
                        "ACCOUNT," +
                                account.getAccountNumber() + "," +
                                account.getOwnerName() + "," +
                                account.getBalance();

                bw.write(accountLine);
                bw.newLine();
                for (String tx : account.getTransactions()) {
                    bw.write("TX," + tx);
                    bw.newLine();
                }
            }
            System.out.println("Accounts saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving accounts.");
        }
    }

    public void loadFromFile(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            Account currentAccount = null;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",", 2);
                if (fields[0].equals("ACCOUNT")) {
                    String[] accParts = line.split(",");
                    int accNumber = Integer.parseInt(accParts[1]);
                    String ownerName = accParts[2];
                    double balance = Double.parseDouble(accParts[3]);
                    currentAccount = new Account(accNumber, ownerName);
                    currentAccount.deposit(balance);
                    accounts.add(currentAccount);
                } else if (fields[0].equals("TX") && currentAccount != null) {
                    String tx = fields[1];
                    currentAccount.getTransactions().add(tx);
                }
            }
            System.out.println("Accounts loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading accounts.");
        }
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void removeAccount(int accountNumber) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getAccountNumber() == accountNumber) {
                accounts.remove(i);
                return;
            }
        }
    }

    public boolean updateAccount(int accountNumber, String updatedName) {
        for (Account account : accounts) {
            if (account.getAccountNumber() == accountNumber) {
                account.setOwnerName(updatedName);
                return true;
            }
        }return false;
    }




}
