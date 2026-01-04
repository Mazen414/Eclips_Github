package com.banking;

public class CheckingAccount extends Account {
    private double overdraftLimit; 
   
    public CheckingAccount(String id, String name, String pass, double balance, double limit) {
        super(id, name, pass, balance);
        this.overdraftLimit = limit;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Error: Withdraw amount must be positive.");
            return;
        }

        if (balance >= amount) {
            balance -= amount;
            addTransaction("Withdraw", amount);
            System.out.println("Withdrawn: $" + amount);
        } else {
            System.out.println("Error: Insufficient funds in Savings.");
        }
    }
}