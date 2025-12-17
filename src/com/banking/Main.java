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
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            String accNum;

            switch (choice) {
                case 1:
                    System.out.print("Enter Account Number: ");
                    accNum = scanner.next();
                    Account acc = bank.findAccount(accNum);
                    if (acc != null) {
                        acc.printStatement();
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
            }
            if (choice == 5) {
                System.out.println("Exiting System...");
                break;
            }
            
        }
        scanner.close();
    }
}