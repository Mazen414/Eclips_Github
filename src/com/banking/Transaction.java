package com.banking;

import java.util.Date;

public class Transaction {
    private String type;
    private double amount;
    private Date timestamp;
    private String description;

    public Transaction(String type, double amount, String description) {
        this.type = type;
        this.amount = amount;
        this.timestamp = new Date();
        this.description = description;
    }

    @Override
    public String toString() {
        return timestamp + " | " + type + ": $" + amount + " (" + description + ")";
    }
}