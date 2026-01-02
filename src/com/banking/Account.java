package com.banking;

import java.util.ArrayList;
import java.util.List;

public abstract class Account implements Transferable {
    
    private String accountNumber;
    protected double balance;
    private List<Transaction> transactionHistory;

    public Account(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
        addTransaction("Initial Creation", balance);
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            addTransaction("Deposit", amount);
            System.out.println("Deposited: $" + amount);
        } else {
            System.out.println("Error: Deposit amount must be positive.");
        }
    }

    public abstract void withdraw(double amount);

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
    
    protected void addTransaction(String type, double amount) {
        transactionHistory.add(new Transaction(type, amount, "Balance: " + balance));
    }

    public void printStatement() {
        System.out.println("\n--- Statement for " + accountNumber + " ---");
        for (Transaction t : transactionHistory) {
            System.out.println(t);
        }
    }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
}