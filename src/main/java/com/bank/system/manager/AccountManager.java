package com.bank.system.manager;

import com.bank.system.model.Account;

import static com.bank.system.utils.ConsoleFormatter.*;
import static com.bank.system.utils.ConsoleUtil.*;
public class AccountManager {
    private final Account[] accounts;
    private int accountCount;


    public AccountManager() {
        accounts = new Account[50]; // Array to hold up to 50 accounts
        accountCount = 0;

    }

    // Method to add an account
    public boolean addAccount(Account account) {
        if (accountCount < accounts.length) {
            accounts[accountCount] = account;
            accountCount++;
            return true;
        }
        return false;
    }

    // Method to find an account by account number
    public Account findAccount(String accountNumber) {
        for (int i = 0; i < accountCount; i++) {
            if (accounts[i] != null && accounts[i].getAccountNumber().equals(accountNumber)) {
                return accounts[i];
            }
        }
        return null; // Account not found
    }

    // Method to view all accounts
    public void viewAllAccounts() {
        print(" ");
        printHeader("ACCOUNT LISTING");
        printSeparator();
        System.out.printf("%-8s | %-15s | %-9s | %-10s | %-8s%n",
                "ACC NO", "CUSTOMER NAME", "TYPE", "BALANCE", "STATUS");
        printSeparator();
        for (int i = 0; i < accountCount; i++) {
            if (accounts[i] != null) {
                accounts[i].displayAccountDetails();
              printSeparator();
            }
        }

        printf("Total Accounts: %d%n", getAccountCount());
        printf("Total Bank Balance: $%,.2f%n", getTotalBalance());
        pressEnterToContinue(); // Wait for user to press Enter
    }

    // Method to get total balance of all accounts
    public double getTotalBalance() {
        double total = 0;
        for (int i = 0; i < accountCount; i++) {
            if (accounts[i] != null) {
                total += accounts[i].getBalance();
            }
        }
        return total;
    }

    // Method to get the number of accounts
    public int getAccountCount() {
        return accountCount;
    }

    // Getter for accounts array
    public Account[] getAccounts() {
        return accounts;
    }

    // Getter for account count
    public int getAccountCountActual() {
        return accountCount;
    }
    public  void displayAccountDetails(Account account) {
        if (account == null) {
            print("Account not found!");
            pressEnterToContinue();
            return;
        }

        print(" ");
        print("Account Details:");
        print("Customer: " + account.getCustomer().getName());
        print("Account Type: " + account.getAccountType());
        printf("Current Balance: $%,.2f%n", account.getBalance());
    }


}