package com.bank.system.model;
import static com.bank.system.utils.ConsoleUtil.*;
public class RegularCustomer extends Customer {
    public RegularCustomer(String name, int age, String contact, String address) {
        super(name, age, contact, address);
    }

    @Override
    public void displayCustomerDetails() {
        print("Customer: " + getName() + " (Regular)");
        print("Age: " + getAge());
        print("Contact: " + getContact());
        print("Address: " + getAddress());
    }

    @Override
    public String getCustomerType() {
        return "Regular";
    }
}