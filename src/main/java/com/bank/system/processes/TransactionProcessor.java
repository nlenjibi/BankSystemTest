package com.bank.system.processes;


import com.bank.system.manager.AccountManager;
import com.bank.system.manager.TransactionManager;
import com.bank.system.model.*;

import static com.bank.system.utils.ConsoleFormatter.*;
import static com.bank.system.utils.ConsoleUtil.*;

public class TransactionProcessor {

    private final TransactionManager transactionManager;
    private final AccountManager accountManager;

    public TransactionProcessor(AccountManager accountManager, TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        this.accountManager = accountManager;
    }

    private void execute() {
        print(" ");
        print("PROCESS TRANSACTION");
        printSubSeparator(60);
        print(" ");

        String accountNumber = readString("Enter Account Number: ",
                s -> !s.isEmpty(),
                "Account Number cannot be empty."
        );

        Account account = accountManager.findAccount(accountNumber);
        if (account == null) {
            print("Account not found.");
            pressEnterToContinue();
            return;
        }
        accountManager.displayAccountDetails(account);
        print(" ");
        print("Transaction type:");
        print("1. Deposit");
        print("2. Withdrawal");
        print(" ");

        int transactionType = getValidIntInput("Select type (1-2): ", 1, 2);
        print(" ");

        String typeStr = (transactionType == 1) ? "DEPOSIT" : "WITHDRAWAL";

        double amount = getValidDoubleInput("Enter amount: $",
                v -> v > 0,
                "Amount must be greater than zero.");

        double previousBalance = account.getBalance();

        boolean success = account.processTransaction(amount, typeStr);

        if (!success) {
            handleFailedTransaction(transactionType, account);
            pressEnterToContinue();
            return;
        }

        Transaction transaction = new Transaction(
                accountNumber,
                typeStr,
                amount,
                account.getBalance()
        );

        transactionManager.addTransaction(transaction);

        transaction.displayTransactionDetails(previousBalance);

        print(" ");

        boolean confirmed = readConfirmation("Confirm transaction?");
        handleTransactionConfirmation(confirmed, account, previousBalance, transaction);
        pressEnterToContinue();
    }

    private void handleTransactionConfirmation(boolean confirmed, Account account, double previousBalance, Transaction transaction) {
        if (confirmed) {
            print(" ");
            print("✓ Transaction completed successfully!");
        } else {
            transactionManager.removeTransaction(transaction);
            account.setBalance(previousBalance);
            print(" ");
            print("Transaction cancelled.");
        }
    }


    private void handleFailedTransaction(int transactionType, Account account) {
        if (transactionType == 1) {
            print("Deposit failed. Invalid amount.");
        } else {
            if (account instanceof SavingsAccount) {
                print("Withdrawal failed. Insufficient funds.");
            } else {
                print("Withdrawal failed. Insufficient funds or exceeds overdraft limit.");
            }
        }
    }

    public void processTransaction() {
        execute();
    }

    public void viewTransactionHistory() {
        print(" ");
        print(underline("VIEW TRANSACTION HISTORY", '-'));
        print(" ");

        String accountNumber = readString("Enter Account Number: ", s -> !s.isEmpty(), "Account Number cannot be empty.");
        Account account = accountManager.findAccount(accountNumber);
        transactionManager.viewTransactionsByAccount(accountNumber, account);
    }

}

