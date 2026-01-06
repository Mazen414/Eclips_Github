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
            System.out.println("9. View Total Bank Assets (Admin)");
            System.out.println("10. Simulate Month End (Interest)");
            System.out.println("11. Export Statement to File");
            System.out.println("12. Exit");
            System.out.println("13. Apply for Loan ");
            System.out.println("14. Pay Loan ");            
            System.out.print("Enter choice: ");

            try {
                int choice = scanner.nextInt();
             // Variables for auth reuse
                String id, pass;
                Account acc;
                
                switch (choice) {
                case 1: // View Balance
                    acc = authenticateUser(scanner, bank); // <--- Uses helper method below
                    if (acc != null) {
                        System.out.println("✅ Current Balance: $" + acc.getBalance());
                    }
                    break;

                case 2: // Deposit
                    acc = authenticateUser(scanner, bank);
                    if (acc != null) {
                        System.out.print("Enter Deposit Amount: ");
                        acc.deposit(scanner.nextDouble());
                    }
                    break;

                case 3: // Withdraw
                    acc = authenticateUser(scanner, bank);
                    if (acc != null) {
                        System.out.print("Enter Withdraw Amount: ");
                        acc.withdraw(scanner.nextDouble());
                    }
                    break;

                case 4: // Transfer
                    Account srcAcc = authenticateUser(scanner, bank);
                    if (srcAcc != null) {
                        System.out.print("Enter Destination Account ID: ");
                        String destId = scanner.next();
                        Account destAcc = bank.findAccount(destId);
                        
                        if (destAcc != null) {
                            System.out.print("Enter Transfer Amount: ");
                            srcAcc.transfer(destAcc, scanner.nextDouble());
                        } else {
                            System.out.println("❌ Error: Destination Account Not Found.");
                        }
                    }
                    break;

                case 5: // Create Account (No password needed to create new)
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
                        bank.addAccount(new SavingsAccount(newNum, newName, newPass, newBal, 0.0, rate));
                    } else if (type == 2) {
                        System.out.print("Enter Overdraft Limit: ");
                        double limit = scanner.nextDouble();
                        bank.addAccount(new CheckingAccount(newNum, newName, newPass, newBal, 0.0, limit));
                    }
                    break;
                    
                case 6: // View History
                    acc = authenticateUser(scanner, bank);
                    if (acc != null) {
                        acc.printStatement();
                    }
                    break;

                case 7: // Calculate Interest
                    acc = authenticateUser(scanner, bank);
                    if (acc != null) {
                        if (acc instanceof SavingsAccount) {
                            ((SavingsAccount) acc).applyInterest();
                        } else {
                            System.out.println("❌ Error: Not a Savings Account.");
                        }
                    }
                    break;

                case 8: // Delete Account
                    System.out.println("⚠ WARNING: You are about to delete an account.");
                    acc = authenticateUser(scanner, bank); // Require login to delete self
                    if (acc != null) {
                        if (bank.deleteAccount(acc.getAccountNumber())) {
                            System.out.println("✅ Account Deleted Successfully.");
                        }
                    }
                    break;

                case 9: // View Assets (Admin - technically public here)
                    bank.printTotalAssets();
                    break;

                case 10: // Month End (System)
                    bank.simulateMonthEnd();
                    break;
                    
                case 11: // Export
                    acc = authenticateUser(scanner, bank);
                    if (acc != null) {
                        acc.exportToTextFile();
                    }
                    break;
                    
                case 12: // Exit
                    System.out.println("Saving data...");
                    bank.saveUsersToFile("users.txt");
                    System.out.println("Exiting System...");
                    System.exit(0);
                    break;
                    
                case 13: // Loan
                    acc = authenticateUser(scanner, bank);
                    if (acc != null) {
                        System.out.print("Enter Loan Amount: ");
                        acc.takeLoan(scanner.nextDouble());
                    }
                    break;

                case 14: // Pay Loan
                    acc = authenticateUser(scanner, bank);
                    if (acc != null) {
                        System.out.println("Current Debt: $" + acc.getLoanDebt());
                        System.out.print("Enter Payment Amount: ");
                        acc.payLoan(scanner.nextDouble());
                    }
                    break;
                        
                    default:
                        System.out.println("Invalid option. Please enter a number between 1-14.");
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

//--- HELPER METHOD TO REDUCE CODE REPETITION ---
// This asks for ID and Password, verifies them, and returns the Account object.
private static Account authenticateUser(Scanner scanner, Bank bank) {
    System.out.print("Enter Account ID: ");
    String id = scanner.next();
    
    System.out.print("Enter Password: ");
    String pass = scanner.next();
    
    Account acc = bank.findAccount(id);
    
    if (acc != null && acc.validatePassword(pass)) {
        return acc; // Success
    } else {
        System.out.println("❌ Access Denied: Invalid ID or Password.");
        return null; // Failure
    }
  }
}