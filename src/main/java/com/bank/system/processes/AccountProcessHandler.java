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
    private static void createAccount() {
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

    private static void viewAccountDetails() {
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

    private static void listAllAccounts() {
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


}
