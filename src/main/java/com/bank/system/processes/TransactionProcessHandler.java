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
        double amount = getValidDoubleInput("Enter amount to deposit: $",
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
        System.out.print("Enter destination account number: ");
        String toAccountNumber = scanner.nextLine().trim();

        if (!accountManager.accountExists(toAccountNumber)) {
            System.out.println("Error: Destination account not found.");
            return;
        }

        System.out.print("Enter amount to transfer: $");
        double amount = getPositiveAmount();

        Account fromAccount = accountManager.getAccount(fromAccountNumber);
        Account toAccount = accountManager.getAccount(toAccountNumber);

        double fromPreviousBalance = fromAccount.getBalance();
        double toPreviousBalance = toAccount.getBalance();

        transactionManager.transfer(fromAccountNumber, toAccountNumber, amount);

        System.out.println("\n✓ Transfer successful!");
        System.out.println("From Account: " + fromAccountNumber + " (Previous: $" + String.format("%.2f", fromPreviousBalance) +
                ", New: $" + String.format("%.2f", fromAccount.getBalance()) + ")");
        System.out.println("To Account: " + toAccountNumber + " (Previous: $" + String.format("%.2f", toPreviousBalance) +
                ", New: $" + String.format("%.2f", toAccount.getBalance()) + ")");
        System.out.println("Transfer Amount: $" + String.format("%.2f", amount));
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
