package com.bank.system.model;

public abstract class Customer {
    private final String customerId;
    private String name;
    private int age;
    private String contact;
    private String address;
    private static int customerCounter = 0;

    public Customer(String name, int age, String contact, String address) {
        this.name = name;
        this.age = age;
        this.contact = contact;
        this.address = address;
        this.customerId = generateCustomerId();
    }

    private String generateCustomerId() {
        customerCounter++;
        return String.format("CUS%03d", customerCounter);
    }

    // Abstract methods to be implemented by subclasses
    public abstract void displayCustomerDetails();

    public abstract String getCustomerType();

    // Getters and setters
    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        validateAge(age);
        this.age = age;
    }
    private void validateAge(int age) {
        if (age <= 0) {
            throw new IllegalArgumentException("Age must be greater than 0");
        }
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static int getCustomerCounter() {
        return customerCounter;
    }

    public static void resetCustomerCounter() {
        customerCounter = 0;
    }
}