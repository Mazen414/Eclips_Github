package com.banking;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    private List<Account> accounts;

    public Bank() {
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        accounts.add(account);
        System.out.println("Account created successfully: " + account.getAccountNumber());
    }

    public Account findAccount(String accountNumber) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(accountNumber)) {
                return acc;
            }
        }
        System.out.println("Error: Account " + accountNumber + " not found.");
        return null;
    }
    
    public boolean deleteAccount(String accountNumber) {
        // 1. Reuse your existing search method
        Account accountToDelete = findAccount(accountNumber);

        // 2. If found, remove it from the list
        if (accountToDelete != null) {
            accounts.remove(accountToDelete);
            return true; // Deletion successful
        }
        
        // Account wasn't found (findAccount already printed the error message)
        return false; 
    }
    
    public void printTotalAssets() {
        double totalAssets = 0;
        
        for (Account acc : accounts) {
            totalAssets += acc.getBalance(); // Adds each account's balance to total
        }
        
        System.out.println("---------------------------");
        System.out.println("TOTAL BANK ASSETS: $" + totalAssets);
        System.out.println("Total Accounts: " + accounts.size());
        System.out.println("---------------------------");
    }
}