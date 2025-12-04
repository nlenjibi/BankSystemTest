package com.bank.system.services;

import com.bank.system.exceptions.*;
import com.bank.system.models.Account;
import com.bank.system.models.Transaction;

import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;
import static com.bank.system.utils.ConsoleFormatter.printSeparator;
import static com.bank.system.utils.ConsoleFormatter.printSubSeparator;
import static com.bank.system.utils.ConsoleUtil.*;

public class TransactionManager {
    private List<Transaction> allTransactions;

    private AccountManager accountManager;
    private int transactionCount;

    public TransactionManager(AccountManager accountManager) {
        this.accountManager = accountManager;
        this.allTransactions = new ArrayList<>();

    }

    // Method to add a transaction
    public boolean addTransaction(Transaction transaction) {
        if (transaction == null) {
            return false;
        }
        allTransactions.add(transaction);
        return true;
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


    // Method to calculate total withdrawals for an account

    public double totalWithdrawals(String accountNumber) {
        if (allTransactions == null || allTransactions.isEmpty()) return 0.0;
        return allTransactions.stream()
                .filter(t -> t != null
                        && accountNumber.equals(t.getAccountNumber())
                        && "WITHDRAWAL".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
    public double calculateTotalTransfers(String accountNumber) {
        if (allTransactions == null || allTransactions.isEmpty()) return 0.0;
        return allTransactions.stream()
                .filter(t -> t != null
                        && accountNumber.equals(t.getAccountNumber())
                        && "TRANSFER".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
    public double totalDeposits(String accountNumber) {
        if (allTransactions == null || allTransactions.isEmpty()) return 0.0;
        return allTransactions.stream()
                .filter(t -> t != null
                        && accountNumber.equals(t.getAccountNumber())
                        && "DEPOSIT".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
    public double totalReceives(String accountNumber) {
        if (allTransactions == null || allTransactions.isEmpty()) return 0.0;
        return allTransactions.stream()
                .filter(t -> t != null
                        && accountNumber.equals(t.getAccountNumber())
                        && "RECEIVE".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }


    // Method to get the number of transactions
    public int getTransactionCount() {
        return transactionCount;
    }





    public boolean deposit(String accountNumber, double amount) throws InvalidAmountException, InsufficientFundsException, OverdraftExceededException {
        Account account = accountManager.getAccount(accountNumber);
        if (account == null) {
            throw new InvalidAmountException("Account not found: " + accountNumber);
        }

        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than 0");
        }

        double previousBalance = account.getBalance();
        account.deposit(amount);
        double newBalance = account.getBalance();

        // Create and record the transaction

        Transaction transaction = new Transaction( accountNumber, "DEPOSIT", amount, newBalance);
        allTransactions.add(transaction);
        account.addTransaction(transaction);

        return true;
    }

    public boolean withdraw(String accountNumber, double amount) throws InsufficientFundsException, InvalidAmountException, OverdraftExceededException {
        Account account = accountManager.getAccount(accountNumber);
        if (account == null) {
            throw new InvalidAmountException("Account not found: " + accountNumber);
        }

        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than 0");
        }

        double previousBalance = account.getBalance();
        boolean success = account.withdraw(amount);
        double newBalance = account.getBalance();

        if (success) {
            // Create and record the transaction

            Transaction transaction = new Transaction( accountNumber, "WITHDRAWAL", amount, newBalance);
            allTransactions.add(transaction);
            account.addTransaction(transaction);
        }

        return success;
    }

    public boolean transfer(String fromAccountNumber, String toAccountNumber, double amount)
            throws InsufficientFundsException, InvalidAmountException, OverdraftExceededException {
        Account fromAccount = accountManager.getAccount(fromAccountNumber);
        Account toAccount = accountManager.getAccount(toAccountNumber);

        if (fromAccount == null) {
            throw new InvalidAmountException("Source account not found: " + fromAccountNumber);
        }

        if (toAccount == null) {
            throw new InvalidAmountException("Destination account not found: " + toAccountNumber);
        }

        if (amount <= 0) {
            throw new InvalidAmountException("Transfer amount must be greater than 0");
        }

        // Perform withdrawal from source account
        double previousFromBalance = fromAccount.getBalance();
        fromAccount.withdraw(amount);
        double newFromBalance = fromAccount.getBalance();

        // Perform deposit to destination account
        double previousToBalance = toAccount.getBalance();
        toAccount.deposit(amount);
        double newToBalance = toAccount.getBalance();

        // Record withdrawal transaction

        Transaction withdrawalTransaction = new Transaction( fromAccountNumber, "TRANSFER_OUT", amount, newFromBalance);
        allTransactions.add(withdrawalTransaction);
        fromAccount.addTransaction(withdrawalTransaction);

        // Record deposit transaction

        Transaction depositTransaction = new Transaction( toAccountNumber, "TRANSFER_IN", amount, newToBalance);
        allTransactions.add(depositTransaction);
        toAccount.addTransaction(depositTransaction);

        return true;
    }
    public List<Transaction> getTransactionsForAccount(String accountNumber) {
        return allTransactions.stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .collect(Collectors.toList());
    }

    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(allTransactions);
    }

    public int getTotalTransactions() {
        return allTransactions.size();
    }
    public void removeTransaction(String transactionId) {
        allTransactions.removeIf(transaction -> transaction.getTransactionId().equals(transactionId));
    }

}