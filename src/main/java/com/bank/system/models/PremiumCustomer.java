package com.bank.system.models;

public class PremiumCustomer extends Customer {
    private  final double minimumBalance;
    private static final double INTEREST_RATE = 0.035; // 3.5% annual interest

    public PremiumCustomer(String name, int age, String contact, String address) {
        super(name, age, contact, address);
        this.minimumBalance = 10000.0; // $10,000 minimum balance for premium status
    }


    @Override
    public String getCustomerType() {
        return "Premium";
    }

    @Override
    public double getInterestRate() {
        return INTEREST_RATE;
    }


}