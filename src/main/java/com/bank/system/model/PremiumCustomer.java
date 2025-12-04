package com.bank.system.model;

import static com.bank.system.utils.ConsoleUtil.*;
public class PremiumCustomer extends Customer {
    private double minimumBalance;

    public PremiumCustomer(String name, int age, String contact, String address) {
        super(name, age, contact, address);
        this.minimumBalance = 10000.0; // $10,000 minimum balance for premium status
    }

    @Override
    public void displayCustomerDetails() {
        print("Customer: " + getName() + " (Premium)");
        print("Age: " + getAge());
        print("Contact: " + getContact());
        print("Address: " + getAddress());
        print("Benefits: No monthly fees, Priority service");
    }

    @Override
    public String getCustomerType() {
        return "Premium";
    }

    // Method to check if fees are waived
    public boolean hasWaivedFees() {
        return true;
    }

    // Getters and setters
    public double getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(double minimumBalance) {
        this.minimumBalance = minimumBalance;
    }
}