package com.bank.system.models;

import static com.bank.system.utils.ConsoleUtil.printf;
public class SavingsAccount extends Account  {
    private static final double INTEREST_RATE = 3.5;
    private static final double MINIMUM_BALANCE = 500.0;
    private static final double WITHDRAWAL_FEE = 2.0;
    public SavingsAccount(Customer customer, double initialBalance) {
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
        printf("%-8s | Interest Rate: %.1f%% | Min Balance: $%,.2f%n",
                "",
                INTEREST_RATE,
                MINIMUM_BALANCE);
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
        if (getBalance() - amount < MINIMUM_BALANCE) {
            return false;
        }

        setBalance(getBalance() - amount);
        return true;
    }


    // Getters
    public double getInterestRate() {
        return INTEREST_RATE;
    }

    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }
    @Override
    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }

        setBalance(getBalance() + amount);
        return true;
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