package com.bank.system.model;
import com.bank.system.interfaces.Transactable;



public abstract class Account implements Transactable {
    private final String accountNumber;
    private Customer customer;
    private double balance;
    private String status;
    private static int accountCounter = 0;

     public Account(Customer customer, double initialDeposit) {
        this.customer = customer;
        this.balance = initialDeposit;
        this.status = "Active";
        this.accountNumber = generateAccountNumber();
    }

    private String generateAccountNumber() {
        accountCounter++;
        return String.format("ACC%03d", accountCounter);
    }

    // Abstract methods to be implemented by subclasses
    public abstract void displayAccountDetails();

    public abstract String getAccountType();

    // Deposit method - common for all account types
    public boolean deposit(double amount) {

        if (amount > 0) {
            this.balance += amount;
            return true;
        }
        return false;
    }


    // Withdraw method - to be overridden by subclasses
    public abstract boolean withdraw(double amount);

    // Getters and setters
    public String getAccountNumber() {
        return accountNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static int getAccountCounter() {
        return accountCounter;
    }

    public static void resetAccountCounter() {
        accountCounter = 0;
    }
    @Override
    public boolean processTransaction(double amount, String type) {

        if (type.equalsIgnoreCase("DEPOSIT")) {

            return deposit(amount);

        } else if (type.equalsIgnoreCase("WITHDRAWAL")) {
            return withdraw(amount);
        }
        return false;
    }


}