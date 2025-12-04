package com.bank.system.model;
import static com.bank.system.utils.ConsoleUtil.*;
public class RegularCustomer extends Customer {
    private static final double INTEREST_RATE = 0.035; // 3.5% annual interest
    public RegularCustomer(String name, int age, String contact, String address) {
        super(name, age, contact, address);
    }

    @Override
    public String getCustomerType() {
        return "Regular";
    }

    @Override
    public double getInterestRate() {
        return INTEREST_RATE;
    }

}