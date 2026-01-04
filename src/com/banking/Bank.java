package com.banking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the collection of accounts in the banking system.
 * Handles adding, finding, deleting, and reporting on accounts.
 */
public class Bank {
    private List<Account> accounts;

    public Bank() {
        this.accounts = new ArrayList<>();
    }

    /**
     * Adds a new account to the bank's database.
     * @param account The Account object (Savings or Checking) to add.
     */
    public void addAccount(Account account) {
        accounts.add(account);
        System.out.println("Account created successfully: " + account.getAccountNumber());
    }

    /**
     * Searches for an account by its ID.
     * @param accountNumber The String ID to search for.
     * @return The Account object if found, or null if not found.
     */
    public Account findAccount(String accountNumber) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(accountNumber)) {
                return acc;
            }
        }
        System.out.println("Error: Account " + accountNumber + " not found.");
        return null;
    }

    /**
     * Deletes an account from the system.
     * @param accountNumber The ID of the account to remove.
     * @return true if deleted, false if not found.
     */
    public boolean deleteAccount(String accountNumber) {
        Account accountToDelete = findAccount(accountNumber);
        
        if (accountToDelete != null) {
            accounts.remove(accountToDelete);
            return true; 
        }
        return false; 
    }

    /**
     * Calculates and prints the total assets held by the bank.
     */
    public void printTotalAssets() {
        double totalAssets = 0;
        
        for (Account acc : accounts) {
            totalAssets += acc.getBalance(); 
        }
        
        System.out.println("---------------------------");
        System.out.println("TOTAL BANK ASSETS: $" + totalAssets);
        System.out.println("Total Accounts: " + accounts.size());
        System.out.println("---------------------------");
    }

    /**
     * Simulates the end of the month processing.
     * Applies interest to all SavingsAccounts.
     */
    public void simulateMonthEnd() {
        System.out.println("\n--- RUNNING MONTH-END SIMULATION ---");
        int savingsAccountsProcessed = 0;
        
        for (Account acc : accounts) {
            if (acc instanceof SavingsAccount) {
                SavingsAccount sAcc = (SavingsAccount) acc;
                sAcc.applyInterest(); 
                savingsAccountsProcessed++;
            }
        }
        
        System.out.println("--------------------------------------");
        System.out.println("Month-End Complete.");
        System.out.println("Interest applied to " + savingsAccountsProcessed + " accounts.");
        System.out.println("--------------------------------------");
    }
    
 //  Reads users.txt
    public void loadUsersFromFile(String filename) {
        try {
            File file = new File(filename);
            Scanner fileScanner = new Scanner(file);
            
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(","); // Split by comma
                
                // Expected format: ID, Name, Pass, Type, Balance, Rate/Limit
                if (parts.length == 6) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String pass = parts[2].trim();
                    String type = parts[3].trim();
                    double balance = Double.parseDouble(parts[4].trim());
                    double extra = Double.parseDouble(parts[5].trim()); // Rate or Limit
                    
                    if (type.equalsIgnoreCase("Savings")) {
                        addAccount(new SavingsAccount(id, name, pass, balance, extra));
                    } else if (type.equalsIgnoreCase("Checking")) {
                        addAccount(new CheckingAccount(id, name, pass, balance, extra));
                    }
                }
            }
            fileScanner.close();
            System.out.println("Data loaded from " + filename);
        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not find " + filename);
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
    
 // NEW METHOD: Saves data back to users.txt
    public void saveUsersToFile(String filename) {
        try {
            FileWriter writer = new FileWriter(filename); // Overwrites the file
            
            for (Account acc : accounts) {
                String line = "";
                // 1. Get common data
                String common = acc.getAccountNumber() + "," + acc.getAccountHolder() + "," + acc.getPassword();
                
                // 2. Add specific data based on type
                if (acc instanceof SavingsAccount) {
                    SavingsAccount s = (SavingsAccount) acc;
                    line = common + ",Savings," + s.getBalance() + "," + s.getInterestRate();
                } else if (acc instanceof CheckingAccount) {
                    CheckingAccount c = (CheckingAccount) acc;
                    line = common + ",Checking," + c.getBalance() + "," + c.getOverdraftLimit();
                }
                
                // 3. Write to file
                writer.write(line + "\n");
            }
            writer.close();
            System.out.println("Data saved successfully to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
}