package com.banking;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Scanner scanner = new Scanner(System.in);

        SavingsAccount user1 = new SavingsAccount("User1", 1000.0, 0.02);
        bank.addAccount(user1);

        while (true) {
            System.out.println("\n--- BANKING SYSTEM MENU ---");
            System.out.println("1. View Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Create Account");
            System.out.println("6. View History");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            String accNum;

            switch (choice) {
            case 1:
                System.out.print("Enter Account Number: ");
                accNum = scanner.next();
                Account acc = bank.findAccount(accNum);
                if (acc != null) {
                    System.out.println("Current Balance: $" + acc.getBalance());
                }
                    break;
                case 2:
                    System.out.print("Enter Account Number: ");
                    accNum = scanner.next();
                    Account depositAcc = bank.findAccount(accNum);
                    if (depositAcc != null) {
                        System.out.print("Enter Deposit Amount: ");
                        double amount = scanner.nextDouble();
                        depositAcc.deposit(amount);
                    }
                    break;
                case 3:
                    System.out.print("Enter Account Number: ");
                    accNum = scanner.next();
                    Account withdrawAcc = bank.findAccount(accNum);
                    if (withdrawAcc != null) {
                        System.out.print("Enter Withdraw Amount: ");
                        double amount = scanner.nextDouble();
                        // Polymorphism: This runs different code for Savings vs Checking!
                        withdrawAcc.withdraw(amount);
                    }
                    break;
                case 4:
                    System.out.print("Enter Source Account: ");
                    String srcNum = scanner.next();
                    Account srcAcc = bank.findAccount(srcNum);
                    
                    System.out.print("Enter Destination Account: ");
                    String destNum = scanner.next();
                    Account destAcc = bank.findAccount(destNum);
                    
                    if (srcAcc != null && destAcc != null) {
                        System.out.print("Enter Transfer Amount: ");
                        double amount = scanner.nextDouble();
                        // This uses the 'Transferable' interface method
                        srcAcc.transfer(destAcc, amount); 
                    } else {
                        System.out.println("Error: One or both accounts not found.");
                    }
                    break;
                case 5:
                    System.out.println("Select Account Type: (1) Savings (2) Checking");
                    int type = scanner.nextInt();
                    
                    System.out.print("Enter New Account Number: ");
                    String newNum = scanner.next();
                    System.out.print("Enter Initial Balance: ");
                    double newBal = scanner.nextDouble();
                    
                    if (type == 1) {
                        System.out.print("Enter Interest Rate (e.g., 0.03): ");
                        double rate = scanner.nextDouble();
                        bank.addAccount(new SavingsAccount(newNum, newBal, rate));
                    } else if (type == 2) {
                        System.out.print("Enter Overdraft Limit: ");
                        double limit = scanner.nextDouble();
                        bank.addAccount(new CheckingAccount(newNum, newBal, limit));
                    }
                    break;
                case 6:
                    System.out.print("Enter Account Number: ");
                    accNum = scanner.next();
                    Account historyAcc = bank.findAccount(accNum);
                    if (historyAcc != null) {
                        historyAcc.printStatement();
                    }
                    break;
            }
            if (choice == 7) {
                System.out.println("Exiting System...");
                break;
            }
            
        }
        scanner.close();
    }
}