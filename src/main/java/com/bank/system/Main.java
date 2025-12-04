// Java
package com.bank.system;

import com.bank.system.manager.AccountManager;
import com.bank.system.manager.TransactionManager;
import com.bank.system.processes.CreateAccountHandler;
import com.bank.system.processes.TransactionProcessor;

import static com.bank.system.utils.ConsoleFormatter.printHeader;
import static com.bank.system.utils.ConsoleUtil.getValidIntInput;
import static com.bank.system.utils.ConsoleUtil.print;

public class Main {

    private final TransactionManager transactionManager;
    private final AccountManager accountManager;
    private final CreateAccountHandler accountHandler;
    private final TransactionProcessor transactionProcessor;

    private Main() {
        this.transactionManager = new TransactionManager();
        this.accountManager = new AccountManager();
        this.accountHandler = new CreateAccountHandler(accountManager, transactionManager);
        this.transactionProcessor = new TransactionProcessor(accountManager, transactionManager);
    }

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        displayWelcomeMessage();
        accountHandler.initializeSampleData();
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getValidIntInput("Enter your choice: ", 1, 5);
            running = processMenuChoice(choice);
        }
        shutdown();
    }

    private boolean processMenuChoice(int choice) {
        return switch (choice) {
            case 1 -> {
                accountHandler.createAccount();
                yield true;
            }
            case 2 -> {
                accountHandler.viewAccounts();
                yield true;
            }
            case 3 -> {
                transactionProcessor.processTransaction();
                yield true;
            }
            case 4 -> {
                transactionProcessor.viewTransactionHistory();
                yield true;
            }
            case 5 -> false;
            default -> true;
        };
    }

    public static void displayWelcomeMessage() {
        print("\nWelcome to the Bank Account Management System!");
        print("Please select an option from the menu below:");
    }

    private static void displayMainMenu() {
        printHeader("BANK ACCOUNT MANAGEMENT SYSTEM - MAIN MENU");
        print("BANK ACCOUNT MANAGEMENT - MAIN MENU");
        print(" ");
        print("1. Create Account");
        print("2. View Accounts");
        print("3. Process Transaction");
        print("4. View Transaction History");
        print("5. Exit");
        print("");
    }

    private static void shutdown() {
        print("\nThank you for using Bank Account Management System!");
        print("Goodbye!");
    }
}
