package com.bank.system.models;
import com.bank.system.exceptions.InsufficientFundsException;
import com.bank.system.exceptions.InvalidAmountException;
import com.bank.system.exceptions.OverdraftExceededException;
import com.bank.system.interfaces.Transactable;

import java.util.ArrayList;
import java.util.List;


public abstract class Account implements Transactable {
    private final String accountNumber;
    private final Customer customer;
    private double balance;
    private final String status;
    protected List<Transaction> transactions;
    private static int accountCounter = 0;

     protected Account(Customer customer, double initialDeposit) {
        this.customer = customer;
        this.balance = initialDeposit;
        this.status = "Active";
        this.accountNumber = generateAccountNumber();
        this.transactions = new ArrayList<>();
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

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }
   public void removeTransaction(Transaction transaction) {
       this.transactions.remove(transaction);
   }

   public boolean removeTransactionById(String transactionId) {
       if (transactionId == null) return false;
       return this.transactions.removeIf(t -> transactionId.equals(t.getTransactionId()));
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