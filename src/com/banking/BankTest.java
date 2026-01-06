package com.banking;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BankTest {

    Bank bank;
    SavingsAccount alice;
    CheckingAccount bob;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        // Setup: Alice (Savings) and Bob (Checking)
        // Alice has 1000.0, 5% interest
        alice = new SavingsAccount("S1", "Alice", "pass1", 1000.0, 0.0, 0.05); 
        // Bob has 500.0, 200 Overdraft
        bob = new CheckingAccount("C1", "Bob", "pass2", 500.0, 0.0, 200.0);    
        
        bank.addAccount(alice);
        bank.addAccount(bob);
    }

    // 1. TEST ADMIN FEATURES
    @Test
    void testAdminOperations() {
        Account found = bank.findAccount("S1");
        assertNotNull(found, "Should find Alice");
        assertEquals("Alice", found.getAccountHolder());

        boolean deleted = bank.deleteAccount("S1");
        assertTrue(deleted, "Delete should return true");
        assertNull(bank.findAccount("S1"), "Alice should be gone");
    }

    // 2. TEST TRANSFER
    @Test
    void testTransfer() {
        // Alice sends 200 to Bob
        boolean success = alice.transfer(bob, 200.0);
        
        assertTrue(success, "Transfer should succeed");
        assertEquals(800.0, alice.getBalance(), "Alice should have 800 left");
        assertEquals(700.0, bob.getBalance(), "Bob should have 700 now");
    }

    // 3. TEST OVERDRAFT
    @Test
    void testCheckingOverdraft() {
        // Bob has 500. Spends 600. (Overdraft used)
        bob.withdraw(600.0);
        assertEquals(-100.0, bob.getBalance(), "Bob should be in debt (-100)");

        // Spends 200 more (Total debt -300, Limit 200). Should FAIL.
        bob.withdraw(200.0);
        assertEquals(-100.0, bob.getBalance(), "Should block withdraw beyond limit");
    }

    // 4. TEST LOANS
    @Test
    void testLoanSystem() {
        alice.takeLoan(500.0);
        assertEquals(1500.0, alice.getBalance(), "Balance should increase by loan");
        assertEquals(500.0, alice.getLoanDebt(), "Debt should record 500");

        alice.payLoan(250.0);
        assertEquals(250.0, alice.getLoanDebt(), "Debt should decrease after payment");
    }

    // 5. TEST INTEREST
    @Test
    void testInterestCalculation() {
        // Alice has 1000. Interest is 5% (0.05).
        // 1000 * 0.05 = 50. New Balance should be 1050.
        
        bank.simulateMonthEnd();
        
        // We use a small 'delta' (0.01) to ignore tiny micro-cent errors
        assertEquals(1050.0, alice.getBalance(), 0.01, "Alice should earn exactly $50 interest");
        assertEquals(500.0, bob.getBalance(), "Bob (Checking) should get NO interest");
    }
}