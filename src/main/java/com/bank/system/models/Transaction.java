package com.bank.system.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.bank.system.utils.ConsoleFormatter.printSubSeparator;
import static com.bank.system.utils.ConsoleUtil.*;

public class Transaction {
    private final String transactionId;
    private final String accountNumber;
    private final String type; // "DEPOSIT" or "WITHDRAWAL"
    private final double amount;
    private final double balanceAfter;
    private final String timestamp;
    private static int transactionCounter = 0;

    public Transaction(String accountNumber, String type, double amount, double balanceAfter) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.transactionId = generateTransactionId();
        this.timestamp = getCurrentTimestamp();
    }

    private String generateTransactionId() {
        transactionCounter++;
        return String.format("TXN%03d", transactionCounter);
    }

    private String getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");
        return now.format(formatter);
    }

    // Method to display transaction details
    public void displayTransactionDetails(double previousBalance) {

        print("TRANSACTION CONFIRMATION");
        printSubSeparator(60);
        print("Transaction ID: " + transactionId);
        print("Account: " + accountNumber);
        print("Type: " + type);
        System.out.printf("Amount: $%,.2f%n", amount);
        printf("Previous Balance: $%,.2f%n", previousBalance);
        System.out.printf("NewBalance : $%,.2f%n", balanceAfter);
        print("Date/Time: " + timestamp);
        printSubSeparator(60);
    }

    // Getters
    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public static int getTransactionCounter() {
        return transactionCounter;
    }


}