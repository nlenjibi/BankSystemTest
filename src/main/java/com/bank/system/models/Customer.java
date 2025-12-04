package com.bank.system.models;

import com.bank.system.interfaces.CustomerService;

public abstract class Customer implements CustomerService {
    private final String customerId;
    private final String name;
    private final int age;
    private final String contact;
    private final String address;
    private static int customerCounter = 0;

    protected Customer(String name, int age, String contact, String address) {
        this.name = name;
        this.age = age;
        this.contact = contact;
        this.address = address;
        this.customerId = generateCustomerId();
    }

    private static String generateCustomerId() {
        customerCounter++;
        return String.format("CUS%03d", customerCounter);
    }

    // Abstract methods to be implemented by subclasses





    // Getters and setters

    public String getName() {
        return name;
    }




}