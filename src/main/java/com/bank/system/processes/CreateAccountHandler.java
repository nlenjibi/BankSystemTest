package com.bank.system.processes;

import com.bank.system.services.*;
import com.bank.system.models.*;
import com.bank.system.models.Transaction;

import static com.bank.system.utils.ConsoleUtil.*;

public class CreateAccountHandler {

    private final TransactionManager transactionManager;
    private final AccountManager accountManager;

    public CreateAccountHandler(AccountManager accountManager, TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        this.accountManager = accountManager;
    }

    private record CustomerData(String name, int age, String contact, String address) {}

    private void execute() {
        print(" ");
        print("ACCOUNT CREATION");
        print(" ");

        CustomerData data = readCustomerDetails();
        Customer customer = createCustomerFromData(data);
        print(" ");
        print("Account type:");
        boolean isRegular = "Regular".equals(customer.getCustomerType());
        double savingsMin = isRegular ? 500 : 10000;
        print("1. Savings Account (Interest: 3.5% Min Balance: $" + String.format("%,.0f", savingsMin) + ")");
        print("2. Checking Account (Overdraft: $1,000, Monthly Fee: $10)");
        int accountType = getValidIntInput("Select type (1-2): ", 1, 2);
        double initialDeposit = getValidDoubleInput(
                "Enter initial deposit amount: $",
                v -> v >= savingsMin,
                "Value must be greater than or equal to $" + String.format("%,.0f", savingsMin) + "."
        );

        Account account = (accountType == 2)
                ? new CheckingAccount(customer, initialDeposit)
                : new SavingsAccount(customer, initialDeposit);

        if (accountManager.addAccount(account)) {
            Transaction transaction = new Transaction(
                    account.getAccountNumber(),
                    account.getAccountType(),
                    initialDeposit,
                    account.getBalance()
            );

            transactionManager.addTransaction(transaction);
            displayAccountCreatedInfo(account, customer);
        } else {
            print("Failed to create account. Maximum account limit reached.");
        }
        print(" ");
        pressEnterToContinue();
    }

    public void createAccount() {
        execute();
    }

    private CustomerData readCustomerDetails() {
        String name = readString("Enter Customer Name: ",
                s -> s != null && !s.trim().isEmpty() && !s.matches(".*\\d.*"),
                "Name cannot be empty and cannot contain digits.");

        int age = getValidIntInput("Enter customer age: ", 1, 150);

        String contact = readString("Enter customer contact: ",
                s -> s != null && !s.trim().isEmpty() && !s.matches(".*[A-Za-z].*"),
                "Contact cannot be empty and cannot contain letters.");

        String address = readString("Enter customer address: ",
                s -> s != null && !s.trim().isEmpty(),
                "Address cannot be empty.");

        return new CustomerData(name, age, contact, address);
    }

    private void displayAccountCreatedInfo(Account account, Customer customer) {
        print(" ");
        print("✓ Account created successfully!");
        print("Account Number: " + account.getAccountNumber());
        print("Customer: " + customer.getName() + " (" + customer.getCustomerType() + ")");
        print("Account Type: " + account.getAccountType());
        printf("Initial Balance: $%.2f%n", account.getBalance());

        if (account instanceof SavingsAccount savings) {
            printf("Interest Rate: %.1f%%%n", savings.getInterestRate());
            printf("Minimum Balance: $%,.2f%n", savings.getMinimumBalance());
        } else if (account instanceof CheckingAccount checking) {
            printf("Overdraft Limit: $%,.2f%n", checking.getOverdraftLimit());
            if (customer instanceof PremiumCustomer) {
                System.out.println("Monthly Fee: Waived (Premium Customer)");
            } else {
                printf("Monthly Fee: $%,.2f%n", checking.getMonthlyFee());
            }
        }
        print("Status: " + account.getStatus());
    }

    private Customer createCustomerFromData(CustomerData data) {

        print(" ");
        print("Customer type:");
        print("1. Regular Customer (Standard banking services)");
        print("2. Premium Customer (Enhanced benefits, min balance $10,000)");

        int customerType = getValidIntInput("Select type (1-2): ", 1, 2);

        if (customerType == 2) {
            return new PremiumCustomer(data.name, data.age, data.contact, data.address);
        } else {
            return new RegularCustomer(data.name, data.age, data.contact, data.address);
        }
    }

    public void viewAccounts() {
        accountManager.viewAllAccounts();
    }

    public void initializeSampleData() {
        Customer customer1 = new RegularCustomer("John Smith", 35, "+1-555-0101", "456 Elm Street, Metropolis");
        Customer customer2 = new RegularCustomer("Sarah Johnson", 28, "+1-555-0102", "789 Oak Avenue, Metropolis");
        Customer customer3 = new PremiumCustomer("Michael Chen", 42, "+1-555-0103", "321 Pine Road, Metropolis");
        Customer customer4 = new RegularCustomer("Emily Brown", 31, "+1-555-0104", "654 Maple Drive, Metropolis");
        Customer customer5 = new PremiumCustomer("David Wilson", 48, "+1-555-0105", "987 Cedar Lane, Metropolis");

        Account account1 = new SavingsAccount(customer1, 5250.00);
        Account account2 = new CheckingAccount(customer2, 3450.00);
        Account account3 = new SavingsAccount(customer3, 15750.00);
        Account account4 = new CheckingAccount(customer4, 890.00);
        Account account5 = new SavingsAccount(customer5, 25300.00);

        accountManager.addAccount(account1);
        accountManager.addAccount(account2);
        accountManager.addAccount(account3);
        accountManager.addAccount(account4);
        accountManager.addAccount(account5);
    }

}