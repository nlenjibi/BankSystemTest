package com.bank.system.processes;


import com.bank.system.services.*;
import com.bank.system.models.*;
import com.bank.system.models.Transaction;

import static com.bank.system.utils.ConsoleUtil.*;
public class AccountProcessHandler {
    private final TransactionManager transactionManager;
    private final AccountManager accountManager;

    public AccountProcessHandler(AccountManager accountManager, TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        this.accountManager = accountManager;
    }
    public static void createAccount() {
        System.out.println("\nCREATE ACCOUNT");
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter customer ID: ");
        String customerId = scanner.nextLine().trim();

        System.out.println("Select account type:");
        System.out.println("1. Savings Account");
        System.out.println("2. Checking Account");
        System.out.print("Choose an option: ");

        int accountType = getChoice(1, 2);

        System.out.println("Select customer type:");
        System.out.println("1. Regular Customer");
        System.out.println("2. Premium Customer");
        System.out.print("Choose an option: ");

        int customerType = getChoice(1, 2);

        System.out.print("Enter initial deposit amount: $");
        double initialAmount = getPositiveAmount();

        Customer customer;
        if (customerType == 1) {
            customer = new RegularCustomer(name, customerId);
        } else {
            customer = new PremiumCustomer(name, customerId);
        }

        String accountNumber = accountManager.generateAccountNumber();
        Account account;

        if (accountType == 1) {
            account = new SavingsAccount(accountNumber, initialAmount, customer);
        } else {
            account = new CheckingAccount(accountNumber, initialAmount, customer);
        }

        if (accountManager.addAccount(account)) {
            System.out.println("\n✓ Account created successfully!");
            System.out.println("Account Number: " + accountNumber);
            System.out.println("Account Type: " + account.getClass().getSimpleName());
            System.out.println("Customer: " + customer.getName());
            System.out.println("Initial Balance: $" + String.format("%.2f", initialAmount));
        } else {
            System.out.println("\n✗ Failed to create account. Account number may already exist.");
        }
    }

    public static void viewAccountDetails() {
        System.out.println("\nVIEW ACCOUNT DETAILS");
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine().trim();

        Account account = accountManager.getAccount(accountNumber);
        if (account != null) {
            System.out.println("\nAccount Details:");
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Account Type: " + account.getClass().getSimpleName());
            System.out.println("Customer: " + account.getCustomer().getName());
            System.out.println("Customer Type: " + account.getCustomer().getClass().getSimpleName());
            System.out.println("Current Balance: $" + String.format("%.2f", account.getBalance()));

            if (account instanceof SavingsAccount) {
                SavingsAccount savings = (SavingsAccount) account;
                System.out.println("Minimum Balance: $" + String.format("%.2f", savings.getMinimumBalance()));
            } else if (account instanceof CheckingAccount) {
                CheckingAccount checking = (CheckingAccount) account;
                System.out.println("Overdraft Limit: $" + String.format("%.2f", checking.getOverdraftLimit()));
                System.out.println("Max Withdrawal Amount: $" + String.format("%.2f", checking.getMaxWithdrawalAmount()));
            }
        } else {
            System.out.println("\nError: Account not found. Please check the account number and try again.");
        }
    }

    public static void listAllAccounts() {
        System.out.println("\nLIST ALL ACCOUNTS");
        if (accountManager.getTotalAccounts() == 0) {
            System.out.println("No accounts found.");
            return;
        }

        System.out.println("Total Accounts: " + accountManager.getTotalAccounts());
        System.out.println("\nAccount List:");
        for (Account account : accountManager.getAllAccounts().values()) {
            System.out.printf("  %s - %s - Balance: $%.2f%n",
                    account.getAccountNumber(),
                    account.getCustomer().getName(),
                    account.getBalance());
        }
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
