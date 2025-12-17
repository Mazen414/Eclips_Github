package com.banking;

public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String accountNumber, double balance, double interestRate) {
        super(accountNumber, balance);
        this.interestRate = interestRate;
    }

    @Override
    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            addTransaction("Withdraw", amount);
            System.out.println("Withdrawn: $" + amount);
        } else {
            System.out.println("Error: Insufficient funds in Savings.");
        }
    }

    public void applyInterest() {
        double interest = balance * interestRate;
        deposit(interest); 
        System.out.println("Interest applied: $" + interest);
    }
}