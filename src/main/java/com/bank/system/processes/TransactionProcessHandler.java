package com.bank.system.processes;

import com.bank.system.exceptions.*;
import com.bank.system.models.Account;
import com.bank.system.models.SavingsAccount;
import com.bank.system.models.Transaction;
import com.bank.system.services.AccountManager;
import com.bank.system.services.TransactionManager;

import static com.bank.system.utils.ConsoleUtil.*;

public class TransactionProcessHandler {
    private final TransactionManager transactionManager;
    private final AccountManager accountManager;

    public TransactionProcessHandler(AccountManager accountManager, TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        this.accountManager = accountManager;
    }
    public void performDeposit(String accountNumber) throws InvalidAmountException, InsufficientFundsException, OverdraftExceededException {

        double amount = getValidDoubleInput("Enter amount to deposit: $",
                v -> v > 0,
                "Amount must be greater than zero.");

        Account account = accountManager.getAccount(accountNumber);
        double previousBalance = account.getBalance();
        boolean success = transactionManager.deposit(accountNumber, amount);

        if (!success) {
            print("Deposit failed. Invalid amount.");
            pressEnterToContinue();
            return;
        }

        Transaction transaction = transactionManager.getLastTransaction(accountNumber);

        if (transaction != null) {
            transaction.displayTransactionDetails(previousBalance);
        } else {
            print("Transaction details unavailable.");
        }
        print(" ");
        boolean confirmed = readConfirmation("Confirm transaction?");
        handleTransactionConfirmation(confirmed, account, previousBalance, transaction != null ? transaction.getTransactionId() : null);
        pressEnterToContinue();


    }


    public void performWithdrawal(String accountNumber) throws InsufficientFundsException, InvalidAmountException, OverdraftExceededException {
        double amount = getValidDoubleInput("Enter amount to withdraw: $",
                v -> v > 0,
                "Amount must be greater than zero.");

        Account account = accountManager.getAccount(accountNumber);
        double previousBalance = account.getBalance();

        boolean success = transactionManager.withdraw(accountNumber, amount);

        if (!success) {
            if (account instanceof SavingsAccount) {
                print("Withdrawal failed. Insufficient funds.");
            } else {
                print("Withdrawal failed. Insufficient funds or exceeds overdraft limit.");
            }
            pressEnterToContinue();
            return;
        }
        Transaction transaction = transactionManager.getLastTransaction(accountNumber);

        if (transaction != null) {
            transaction.displayTransactionDetails(previousBalance);
        } else {
            print("Transaction details unavailable.");
        }
        print(" ");
        boolean confirmed = readConfirmation("Confirm transaction?");
        handleTransactionConfirmation(confirmed, account, previousBalance, transaction != null ? transaction.getTransactionId() : null);
        pressEnterToContinue();

    }

    public void performTransfer(String fromAccountNumber) throws InsufficientFundsException, InvalidAmountException, OverdraftExceededException {
        String toAccountNumber = readString("Enter destination account number: ",
                s -> !s.isEmpty(),
                "Account Number cannot be empty."
        );

        if (!accountManager.accountExists(toAccountNumber)) {
            print("Error: Destination account not found. Please check the account number and try again.");
            pressEnterToContinue();
            return;
        }

        double amount = getValidDoubleInput("Enter amount to transfer: $",
                v -> v > 0,
                "Amount must be greater than zero.");


        Account fromAccount = accountManager.getAccount(fromAccountNumber);
        Account toAccount = accountManager.getAccount(toAccountNumber);

        double fromPreviousBalance = fromAccount.getBalance();
        double toPreviousBalance = toAccount.getBalance();

        transactionManager.transfer(fromAccountNumber, toAccountNumber, amount);
        Transaction fromTransaction = transactionManager.getLastTransaction(fromAccountNumber);
        Transaction toTransaction = transactionManager.getLastTransaction(toAccountNumber);

        if (fromTransaction != null) {
            fromTransaction.displayTransactionDetails(fromPreviousBalance);
        } else {
            print("Transaction details unavailable.");
        }
        print(" ");
        boolean confirmed = readConfirmation("Confirm transaction?");
        if (confirmed) {
            System.out.println("\n✓ Transfer successful!");
            System.out.println("From Account: " + fromAccountNumber + " (Previous: $" + String.format("%.2f", fromPreviousBalance) +
                    ", New: $" + String.format("%.2f", fromAccount.getBalance()) + ")");
            System.out.println("To Account: " + toAccountNumber + " (Previous: $" + String.format("%.2f", toPreviousBalance) +
                    ", New: $" + String.format("%.2f", toAccount.getBalance()) + ")");
            System.out.println("Transfer Amount: $" + String.format("%.2f", amount));
        }
        else {
            transactionManager.removeTransaction(fromTransaction != null ? fromTransaction.getTransactionId() : null);
            transactionManager.removeTransaction(toTransaction != null ? toTransaction.getTransactionId() : null);
            fromAccount.removeTransactionById(fromTransaction != null ? fromTransaction.getTransactionId() : null);
            toAccount.removeTransactionById(toTransaction != null ? toTransaction.getTransactionId() : null);
            fromAccount.setBalance(fromPreviousBalance);
            toAccount.setBalance(toPreviousBalance);
            print(" ");
            print("Transaction cancelled.");

        }
    }
    private void handleTransactionConfirmation(boolean confirmed, Account account, double previousBalance, String transaction) {
        if (confirmed) {
            print(" ");
            print("✓ Transaction completed successfully!");
        } else {
            transactionManager.removeTransaction(transaction);
            account.removeTransactionById(transaction);
            account.setBalance(previousBalance);
            print(" ");
            print("Transaction cancelled.");
        }
    }

}
