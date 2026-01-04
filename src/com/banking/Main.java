package com.banking;

import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Scanner scanner = new Scanner(System.in);

       bank.loadUsersFromFile("users.txt");

        System.out.println("==========================================");
        System.out.println("   ____              _    _             ");
        System.out.println("  |  _ \\            | |  (_)            ");
        System.out.println("  | |_) | __ _ _ __ | | ___ _ __   __ _ ");
        System.out.println("  |  _ < / _` | '_ \\| |/ / | '_ \\ / _` |");
        System.out.println("  | |_) | (_| | | | |   <| | | | | (_| |");
        System.out.println("  |____/ \\__,_|_| |_|_|\\_\\_|_| |_|\\__, |");
        System.out.println("                                   __/ |");
        System.out.println("   S  Y  S  T  E  M               |___/ ");
        System.out.println("==========================================");
        System.out.println("        Welcome, " + System.getProperty("user.name") + "!");
        System.out.println("==========================================");
        while (true) {
            System.out.println("\n--- BANKING SYSTEM MENU ---");
            System.out.println("1. View Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Create Account");
            System.out.println("6. View History");
            System.out.println("7. Calculate Interest (Savings Only)");
            System.out.println("8. Delete Account");
            System.out.println("9. View Total Bank Assets");
            System.out.println("10. Simulate Month End (Interest)");
            System.out.println("11. Export Statement to File");
            System.out.println("12. Exit");
            System.out.print("Enter choice: ");

            try {
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
                            // --- SECURITY CHECK START ---
                            System.out.print("Enter Password: ");
                            String pass = scanner.next();
                            
                            if (withdrawAcc.validatePassword(pass)) {
                                // Password Correct: Proceed
                                System.out.print("Enter Withdraw Amount: ");
                                double amount = scanner.nextDouble();
                                withdrawAcc.withdraw(amount);
                            } else {
                                // Password Incorrect: Block
                                System.out.println("Error: Incorrect Password! Access Denied.");
                            }
                            // --- SECURITY CHECK END ---
                        }
                        break;

                    case 4:
                        System.out.print("Enter Source Account: ");
                        String srcNum = scanner.next();
                        Account srcAcc = bank.findAccount(srcNum);
                        
                        if (srcAcc != null) {
                            // --- SECURITY CHECK START ---
                            System.out.print("Enter Password for " + srcNum + ": ");
                            String pass = scanner.next();
                            
                            if (srcAcc.validatePassword(pass)) {
                                // Authorized! Now ask for destination
                                System.out.print("Enter Destination Account: ");
                                String destNum = scanner.next();
                                Account destAcc = bank.findAccount(destNum);
                                
                                if (destAcc != null) {
                                    System.out.print("Enter Transfer Amount: ");
                                    double amount = scanner.nextDouble();
                                    srcAcc.transfer(destAcc, amount);
                                } else {
                                    System.out.println("Error: Destination account not found.");
                                }
                            } else {
                                System.out.println("Error: Incorrect Password. Transfer Cancelled.");
                            }
                            // --- SECURITY CHECK END ---
                        } else {
                            System.out.println("Error: Source account not found.");
                        }
                        break;

                    case 5:
                        System.out.println("Select Account Type: (1) Savings (2) Checking");
                        int type = scanner.nextInt();
                        
                        System.out.print("Enter New Account Number: ");
                        String newNum = scanner.next();
                        
                        System.out.print("Enter Account Holder Name: ");
                        String newName = scanner.next();
                        
                        System.out.print("Set Password: ");
                        String newPass = scanner.next();
                        
                        System.out.print("Enter Initial Balance: ");
                        double newBal = scanner.nextDouble();
                        
                        if (type == 1) {
                            System.out.print("Enter Interest Rate (e.g., 0.03): ");
                            double rate = scanner.nextDouble();
                            // Pass the new name/password to the constructor
                            bank.addAccount(new SavingsAccount(newNum, newName, newPass, newBal, rate));
                        } else if (type == 2) {
                            System.out.print("Enter Overdraft Limit: ");
                            double limit = scanner.nextDouble();
                            bank.addAccount(new CheckingAccount(newNum, newName, newPass, newBal, limit));
                        }
                        break;

                    case 7:
                        System.out.print("Enter Savings Account ID to apply interest: ");
                        String intAccId = scanner.next();

                        // 1. Find the account object
                        Account interestAccount = bank.findAccount(intAccId);

                        if (interestAccount != null) {
                            // 2. Check: Is this actually a SavingsAccount?
                            if (interestAccount instanceof SavingsAccount) {
                                // 3. Cast: Convert generic 'Account' to 'SavingsAccount'
                                SavingsAccount savingsAcc = (SavingsAccount) interestAccount;
                                // 4. Call the method that only exists in SavingsAccount
                                savingsAcc.applyInterest();
                            } else {
                                System.out.println("Error: Account " + intAccId + " is not a Savings Account.");
                            }
                        } else {
                            System.out.println("Error: Account not found.");
                        }
                        break;

                    case 8:
                        System.out.print("Enter Account ID to delete: ");
                        String delId = scanner.next();

                        boolean isDeleted = bank.deleteAccount(delId);

                        if (isDeleted) {
                            System.out.println("Account " + delId + " was successfully deleted.");
                        }
                        break;

                    case 9:
                        // Call the method in Bank.java to sum up all accounts
                        bank.printTotalAssets();
                        break;

                    case 10:
                        bank.simulateMonthEnd();
                        break;
                        
                    case 11:
                        System.out.print("Enter Account ID to export: ");
                        accNum = scanner.next();
                        Account exportAcc = bank.findAccount(accNum);
                        
                        if (exportAcc != null) {
                            exportAcc.exportToTextFile();
                        }
                        break;
                        
                    case 12:
                        System.out.println("Exiting System...");
                        System.exit(0);
                        break;
                        
                    default:
                        System.out.println("Invalid option. Please enter a number between 1-12.");
                }
            } catch (InputMismatchException e) {
                // ---  CATCH BLOCK ---
                System.out.println("Error: Invalid input. Please enter a numeric value.");
                scanner.nextLine(); // Clear the buffer to prevent infinite loop
            }
            // --- END TRY-CATCH ---
            
            System.out.println("-----------------------------------------");
        }
    }
}