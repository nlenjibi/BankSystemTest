package com.bank.system.model;
import com.bank.system.exceptions.InsufficientFundsException;
import com.bank.system.exceptions.InvalidAmountException;
import com.bank.system.exceptions.OverdraftExceededException;
import com.bank.system.interfaces.Transactable;



public abstract class Account implements Transactable {
    private final String accountNumber;
    private final Customer customer;
    private double balance;
    private final String status;
    private static int accountCounter = 0;

     protected Account(Customer customer, double initialDeposit) {
        this.customer = customer;
        this.balance = initialDeposit;
        this.status = "Active";
        this.accountNumber = generateAccountNumber();
    }

    private static String generateAccountNumber() {
        accountCounter++;
        return String.format("ACC%03d", accountCounter);
    }

    // Abstract methods to be implemented by subclasses
    public abstract void displayAccountDetails();

    public abstract String getAccountType();

    // Deposit method - common for all account types

    // Abstract methods to be implemented by subclasses
    public abstract boolean withdraw(double amount) throws InsufficientFundsException, InvalidAmountException, OverdraftExceededException;
    public abstract boolean deposit(double amount) throws InvalidAmountException;

    // Withdraw method - to be overridden by subclasses

    // Getters and setters
    public String getAccountNumber() {
        return accountNumber;
    }

    public Customer getCustomer() {
        return customer;
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





}