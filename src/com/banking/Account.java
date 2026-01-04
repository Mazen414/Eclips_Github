package com.banking;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class representing a generic bank account.
 * Implements the Transferable interface to allow fund transfers.
 */
public abstract class Account implements Transferable {
    
    private String accountNumber;
    private String accountHolder;
    private String password;
    protected double balance;
    private List<Transaction> transactionHistory;

    /**
     * Constructor to initialize an account.
     * @param accountNumber Unique ID for the account.
     * @param balance Initial deposit amount.
     */
    public Account(String accountNumber, String accountHolder, String password, double balance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.password = password;
        this.balance = balance;
        
this.transactionHistory = new ArrayList<>(); 
        
        addTransaction("Initial Creation", balance);
    }

    /**
     * Deposits a positive amount into the account.
     * @param amount The amount to add to the balance.
     */
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            addTransaction("Deposit", amount);
            System.out.println("Deposited: $" + amount);
        } else {
            System.out.println("Error: Deposit amount must be positive.");
        }
    }

    /**
     * Abstract method to withdraw funds. 
     * Specific logic (overdraft vs insufficient funds) is handled by subclasses.
     * @param amount The amount to deduct.
     */
    public abstract void withdraw(double amount);

    /**
     * Transfers funds from this account to another.
     * @param toAccount The destination account object.
     * @param amount The amount to transfer.
     * @return true if successful, false otherwise.
     */
    @Override
    public boolean transfer(Account toAccount, double amount) {
        if (this.balance >= amount) {
            this.withdraw(amount); 
            toAccount.deposit(amount);
            addTransaction("Transfer to " + toAccount.getAccountNumber(), amount);
            return true;
        }
        System.out.println("Transfer Failed: Insufficient funds.");
        return false;
    }
    
    /**
     * Records a transaction internally.
     * @param type The type of transaction (Deposit, Withdraw, etc.).
     * @param amount The amount involved.
     */
    protected void addTransaction(String type, double amount) {
        transactionHistory.add(new Transaction(type, amount, "Balance: " + balance));
    }

    /**
     * Prints the transaction history to the console.
     */
    public void printStatement() {
        System.out.println("\n--- Statement for " + accountNumber + " ---");
        for (Transaction t : transactionHistory) {
            System.out.println(t);
        }
    }

    /**
     * Exports the transaction history to a text file.
     * The file is saved in the project root directory.
     */
    public void exportToTextFile() {
        String fileName = "Statement_" + accountNumber + ".txt";
        
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("--- Bank Statement for Account: " + accountNumber + " ---\n");
            writer.write("Current Balance: $" + balance + "\n");
            writer.write("--------------------------------------------------\n");
            
            for (Transaction t : transactionHistory) {
                writer.write(t.toString() + "\n");
            }
            
            System.out.println("Success! Statement exported to project folder: " + fileName);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    
    
    public boolean validatePassword(String inputPass) { 
        return this.password.equals(inputPass); 
    }
    public String getPassword() { return password; }
    
    public String getAccountHolder() { 
        return accountHolder; 
    }
}