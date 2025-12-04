package com.bank.system.model;

import static com.bank.system.utils.ConsoleUtil.printf;

public class CheckingAccount extends Account {
    private final double overdraftLimit;
    private final double monthlyFee;


    public CheckingAccount(Customer customer, double initialBalance) {
        super(customer, initialBalance);
        this.overdraftLimit = 1000.0; // $1000 overdraft limit
        this.monthlyFee = 10.0; // $10 monthly fee
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
                overdraftLimit,
                monthlyFee);
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
        if (getBalance() + overdraftLimit < amount) {
            return false;
        }

        setBalance(getBalance() - amount);
        return true;
    }

    // Method to apply monthly fee
    public void applyMonthlyFee() {
        // Check if customer is premium - they have waived fees
        if (getCustomer() instanceof PremiumCustomer) {
            // Premium customers have waived fees
            return;
        }

        // Apply monthly fee only if balance is sufficient
        if (getBalance() >= monthlyFee) {
            setBalance(getBalance() - monthlyFee);
        }
    }

    // Getters
    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public double getMonthlyFee() {
        return monthlyFee;
    }

}