package com.bank.system.model;

import static com.bank.system.utils.ConsoleUtil.printf;
public class SavingsAccount extends Account  {
    private final double interestRate;
    private final double minimumBalance;
    public SavingsAccount(Customer customer, double initialBalance) {
        super(customer, initialBalance);
        this.interestRate = 3.5; // 3.5% annually
        this.minimumBalance = 500.0; // $50 minimum balance
    }

    @Override
    public void displayAccountDetails() {
        printf("%-8s | %-15s | %-9s | $%,-9.2f | %-8s%n",
                getAccountNumber(),
                getCustomer().getName(),
                getAccountType(),
                getBalance(),
                getStatus());
        printf("%-8s | Interest Rate: %.1f%% | Min Balance: $%,.2f%n",
                "",
                interestRate,
                minimumBalance);
    }

    @Override
    public String getAccountType() {
        return "Savings";
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            return false;
        }

        // Check if withdrawal would bring balance below minimum
        if (getBalance() - amount < minimumBalance) {
            return false;
        }

        setBalance(getBalance() - amount);
        return true;
    }

    // Method to calculate interest
    public double calculateInterest() {
        return getBalance() * (interestRate / 100);
    }

    // Getters
    public double getInterestRate() {
        return interestRate;
    }

    public double getMinimumBalance() {
        return minimumBalance;
    }

}