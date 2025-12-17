package com.banking;

public interface Transferable {
boolean transfer(Account toAccount, double amount);
}