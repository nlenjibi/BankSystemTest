package com.bank.system.manager;

import com.bank.system.model.Account;
import com.bank.system.model.Transaction;

import static com.bank.system.utils.ConsoleFormatter.printSeparator;
import static com.bank.system.utils.ConsoleFormatter.printSubSeparator;
import static com.bank.system.utils.ConsoleUtil.*;

public class TransactionManager {
    private final Transaction[] transactions;
    private int transactionCount;

    public TransactionManager() {
        transactions = new Transaction[200]; // Array to hold up to 200 transactions
        transactionCount = 0;
    }

    // Method to add a transaction
    public boolean addTransaction(Transaction transaction) {
        if (transactionCount < transactions.length) {
            transactions[transactionCount] = transaction;
            transactionCount++;
            return true;
        }
        return false;
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

        // Display transactions in reverse chronological order (newest first)
        for (int i = transactionCount - 1; i >= 0; i--) {
            if (transactions[i] != null && transactions[i].getAccountNumber().equals(accountNumber)) {
                if (!hasTransactions) {
                    print("TRANSUCTION HISTROY");
                    printSubSeparator(85);

                    printf("%-12s | %-20s | %-10s | %-14s | %-15s%n",
                            "TXN ID", "DATE/TIME", "TYPE", "AMOUNT", "BALANCE AFTER");
                    hasTransactions = true;
                    printSubSeparator(85);
                }
                double amount = transactions[i].getAmount();

                // Determine sign
                String sign = transactions[i].getType().equalsIgnoreCase("WITHDRAWAL") ? "-" : "+";
                printf("%-12s | %-20s | %-10s | %s$%,12.2f | $%,15.2f%n",
                        transactions[i].getTransactionId(),
                        transactions[i].getTimestamp(),
                        transactions[i].getType(),
                        sign,
                        amount,
                        transactions[i].getBalanceAfter());

            }

        }
       //
        if (!hasTransactions) {
            printSeparator();
            print("No transactions found for this account.");
            printSeparator();
        } else {
            printSubSeparator(85);
            // Display summary
            print(" ");
            print("SUMMARY:");
            print("Total Transactions: "+ getTransactionCount() );
            printf("Total Deposits: $%,.2f%n", calculateTotalDeposits(accountNumber));
            printf("Total Withdrawals: $%,.2f%n", calculateTotalWithdrawals(accountNumber));
            printf("Net Change: +$%,.2f%n",
                    calculateTotalDeposits(accountNumber) - calculateTotalWithdrawals(accountNumber));
        }

        pressEnterToContinue();
    }

    // Method to calculate total deposits for an account
    public double calculateTotalDeposits(String accountNumber) {
        double total = 0;
        for (int i = 0; i < transactionCount; i++) {
            if (transactions[i] != null &&
                    transactions[i].getAccountNumber().equals(accountNumber) &&
                    transactions[i].getType().equals("DEPOSIT")) {
                total += transactions[i].getAmount();
            }
        }
        return total;
    }

    // Method to calculate total withdrawals for an account
    public double calculateTotalWithdrawals(String accountNumber) {
        double total = 0;
        for (int i = 0; i < transactionCount; i++) {
            if (transactions[i] != null &&
                    transactions[i].getAccountNumber().equals(accountNumber) &&
                    transactions[i].getType().equals("WITHDRAWAL")) {
                total += transactions[i].getAmount();
            }
        }
        return total;
    }

    // Method to get the number of transactions
    public int getTransactionCount() {
        return transactionCount;
    }

    // Getter for transactions array
    public Transaction[] getTransactions() {
        return transactions;
    }

  //Method to remove a transaction
    public void removeTransaction(Transaction transaction) {
        if (transaction == null) return;
        String id = transaction.getTransactionId();
        for (int i = 0; i < transactionCount; i++) {
            if (transactions[i] != null && transactions[i].getTransactionId().equals(id)) {
                for (int j = i; j < transactionCount - 1; j++) {
                    transactions[j] = transactions[j + 1];
                }
                transactions[transactionCount - 1] = null;
                transactionCount--;
                break;
            }
        }
    }



}