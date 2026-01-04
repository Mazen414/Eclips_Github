package com.banking;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JUnit Tests for the Banking System.
 * Covers requirements: Logic testing, Edge cases, Assertion logic.
 */
class AccountTest {

    private SavingsAccount savings;
    private CheckingAccount checking;

    // This runs BEFORE every single test to reset the data
    @BeforeEach
    void setUp() {
        // ID, Name, Pass, Balance, Debt, Rate/Limit
        savings = new SavingsAccount("S1", "TestUser", "123", 1000.0, 0.0, 0.05);
        checking = new CheckingAccount("C1", "TestUser", "123", 1000.0, 0.0, 500.0);
    }

    // 1. Test Normal Deposit
    @Test
    void testDeposit() {
        savings.deposit(500.0);
        assertEquals(1500.0, savings.getBalance(), "Balance should be 1500 after depositing 500");
    }

    // 2. Test Negative Deposit (Edge Case)
    @Test
    void testNegativeDeposit() {
        savings.deposit(-500.0);
        assertEquals(1000.0, savings.getBalance(), "Balance should not change when depositing negative amount");
    }

    // 3. Test Withdraw Logic (Savings vs Checking)
    @Test
    void testWithdrawSufficient() {
        savings.withdraw(200.0);
        assertEquals(800.0, savings.getBalance());
    }

    @Test
    void testOverdraftProtection() {
        // Checking has 1000 balance + 500 overdraft limit. 
        // Withdrawal of 1400 should work.
        checking.withdraw(1400.0);
        assertEquals(-400.0, checking.getBalance(), "Checking account should allow overdraft up to limit");
    }

    @Test
    void testOverdraftLimitExceeded() {
        // Limit is 500. Balance 1000. Total available 1500.
        // Try to withdraw 1600.
        checking.withdraw(1600.0);
        assertEquals(1000.0, checking.getBalance(), "Withdrawal should fail if it exceeds overdraft limit");
    }

    // 4. Test Loan Logic (New Feature)
    @Test
    void testLoanApproval() {
        savings.takeLoan(500.0);
        assertEquals(1500.0, savings.getBalance(), "Balance should increase by loan amount");
        assertEquals(500.0, savings.getLoanDebt(), "Debt should increase by loan amount");
    }

    @Test
    void testLoanBlock() {
        savings.takeLoan(500.0); // First loan
        savings.takeLoan(100.0); // Second loan (Should fail)
        assertEquals(500.0, savings.getLoanDebt(), "User cannot take a second loan before paying the first");
    }

    // 5. Test Transfer Interface
    @Test
    void testTransfer() {
        savings.transfer(checking, 200.0);
        assertEquals(800.0, savings.getBalance());
        assertEquals(1200.0, checking.getBalance());
    }
}