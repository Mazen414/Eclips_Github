package com.banking;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Bank {
    private List<Account> accounts;

    public Bank() {
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        accounts.add(account);
        System.out.println("Account created: " + account.getAccountNumber());
    }

    public Account findAccount(String accountNumber) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(accountNumber)) {
                return acc;
            }
        }
        return null; // Return null if not found (Main handles the error message)
    }

    public boolean deleteAccount(String accountNumber) {
        Account acc = findAccount(accountNumber);
        if (acc != null) {
            accounts.remove(acc);
            return true;
        }
        return false;
    }

    public void printTotalAssets() {
        double total = 0;
        System.out.println("--- All Accounts ---");
        for (Account acc : accounts) {
            System.out.println(acc.getAccountNumber() + ": $" + acc.getBalance());
            total += acc.getBalance();
        }
        System.out.println("TOTAL ASSETS: $" + total);
    }

    public void simulateMonthEnd() {
        for (Account acc : accounts) {
            if (acc instanceof SavingsAccount) {
                ((SavingsAccount) acc).applyInterest();
            }
        }
    }

    // ---------------------------------------------------------
    // 💾 FIXED LOAD METHOD (Matches 6-Column File)
    // ---------------------------------------------------------
    public void loadUsersFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) return;

        accounts.clear(); // Clear existing list to avoid duplicates

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                
                // We check for 6 parts (ID, Name, Pass, Balance, Debt, Rate/Limit)
                if (parts.length >= 6) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String pass = parts[2].trim();
                    double bal = Double.parseDouble(parts[3].trim());
                    double debt = Double.parseDouble(parts[4].trim());
                    double special = Double.parseDouble(parts[5].trim());

                    // LOGIC: If ID starts with 'S', it is Savings. Otherwise Checking.
                    if (id.toUpperCase().startsWith("S")) {
                        addAccount(new SavingsAccount(id, name, pass, bal, debt, special));
                    } else {
                        addAccount(new CheckingAccount(id, name, pass, bal, debt, special));
                    }
                }
            }
            System.out.println("Database loaded: " + accounts.size() + " accounts.");
        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // 💾 FIXED SAVE METHOD (Writes 6-Column File)
    // ---------------------------------------------------------
    public void saveUsersToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Account acc : accounts) {
                // We construct the line WITHOUT the extra "Type" word
                // Format: ID,Name,Password,Balance,Debt,RateOrLimit
                String line = acc.getAccountNumber() + "," +
                              acc.getAccountHolder() + "," +
                              acc.getPassword() + "," +
                              acc.getBalance() + "," +
                              acc.getLoanDebt() + ",";

                if (acc instanceof SavingsAccount) {
                    line += ((SavingsAccount) acc).getInterestRate();
                } else if (acc instanceof CheckingAccount) {
                    line += ((CheckingAccount) acc).getOverdraftLimit();
                }
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}