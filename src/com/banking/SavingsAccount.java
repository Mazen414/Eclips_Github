package com.banking;

public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String id, String name, String pass, double balance, double debt, double rate) {
    	super(id, name, pass, balance, debt);
        this.interestRate = rate;
    }

    @Override
    public void withdraw(double amount) {
        // Validation Logic 
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

    public void applyInterest() {
        double interest = balance * interestRate;
        if (interest > 0) {
        	deposit(interest);
            System.out.println("Interest applied: $" + interest);
          }
        }
    public double getInterestRate() { return interestRate; }
}
