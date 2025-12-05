package com.bank.system.models;

import static com.bank.system.utils.ConsoleUtil.printf;

public class CheckingAccount extends Account {
    private static final double OVERDRAFT_LIMIT = 500.0;
    private static final double MONTHLY_FEE = 10.0 ;


    public CheckingAccount(Customer customer, double initialBalance) {
        super(customer, initialBalance);

    }

    @Override
    public void displayAccountDetails() {
        printf("%-8s | %-15s | %-9s | $%,-9.2f | %-8s%n",
                getAccountNumber(),
                getCustomer().getName(),
                getAccountType(),
                getBalance(),
                getStatus());
        printf("%-8s | Overdraft Limit: $%.2f | Monthly Fee: $%,.2f%n",
                "",
                OVERDRAFT_LIMIT,
                MONTHLY_FEE);
    }


    @Override
    public String getAccountType() {
        return "Checking";
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            return false;
        }

        // Check if withdrawal is within balance + overdraft limit
        if (getBalance() + OVERDRAFT_LIMIT < amount) {
            return false;
        }

        setBalance(getBalance() - amount);
        return true;
    }
    @Override
    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        setBalance(getBalance() + amount);
        return true;
    }

    // Getters
    public double getOverdraftLimit() {
        return OVERDRAFT_LIMIT;
    }

    public double getMonthlyFee() {
        return MONTHLY_FEE;
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

    public double getMaxWithdrawalAmount() {
        return getBalance() + OVERDRAFT_LIMIT;
    }
}