package com.bank.system.processes;


import com.bank.system.services.AccountManager;
import com.bank.system.services.TransactionManager;
import com.bank.system.models.*;

import java.util.List;

import static com.bank.system.utils.ConsoleFormatter.*;
import static com.bank.system.utils.ConsoleUtil.*;

public class TransactionProcess {

    private final TransactionManager transactionManager;
    private final AccountManager accountManager;

    public TransactionProcessHandler(TransactionManager transactionManager, AccountManager accountManager) {
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

        Account account = accountManager.getAccount(accountNumber);
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
        Account account = accountManager.getAccount(accountNumber);
        transactionManager.viewTransactionsByAccount(accountNumber, account);
    }
    // Method to view transactions by account
    public void viewTransactionsByAccount(String accountNumber, Account account) {
        print(" ");
        print("TRANSACTION HISTORY FOR ACCOUNT: " + account.getAccountNumber() + " - " + account.getCustomer().getName());
        printSeparator();
        print(" ");
        print("Account :" + account.getAccountNumber() + " - " + account.getCustomer().getName() );
        print("Account Type: " + account.getAccountType());
        printf("Current Balance: $%,.2f%n", account.getBalance());
        print("");
        boolean hasTransactions = false;

        List<Transaction> transactions = getTransactionsForAccount(accountNumber);
        if (transactions == null || transactions.isEmpty()) {
            printSeparator();
            print("No transactions found for this account.");
            printSeparator();
            pressEnterToContinue();
            return;
        }

        // Display transactions in reverse chronological order (newest first)
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            if (t == null) continue;

            if (!hasTransactions) {
                print("TRANSACTION HISTORY");
                printSubSeparator(85);

                printf("%-12s | %-20s | %-10s | %-14s | %-15s%n",
                        "TXN ID", "DATE/TIME", "TYPE", "AMOUNT", "BALANCE AFTER");
                hasTransactions = true;
                printSubSeparator(85);
            }

            double amount = t.getAmount();
            String sign = "WITHDRAWAL".equalsIgnoreCase(t.getType()) ? "-" : "+";
            printf("%-12s | %-20s | %-10s | %s$%,12.2f | $%,15.2f%n",
                    t.getTransactionId(),
                    t.getTimestamp(),
                    t.getType(),
                    sign,
                    amount,
                    t.getBalanceAfter());
        }

        printSubSeparator(85);
        // Display summary
        print(" ");
        print("SUMMARY:");
        print("Total Transactions: " + transactions.size());
        printf("Total Deposits: $%,.2f%n", totalDeposits(accountNumber));
        printf("Total Withdrawals: $%,.2f%n", totalWithdrawals(accountNumber));
        printf("Net Change: +$%,.2f%n",
                totalDeposits(accountNumber) - totalWithdrawals(accountNumber));

        pressEnterToContinue();
    }


}

