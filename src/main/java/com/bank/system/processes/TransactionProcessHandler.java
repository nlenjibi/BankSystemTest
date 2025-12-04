package com.bank.system.processes;

import com.bank.system.exceptions.*;
import com.bank.system.services.AccountManager;
import com.bank.system.services.TransactionManager;

public class TransactionProcessHandler {
    private final TransactionManager transactionManager;
    private final AccountManager accountManager;

    public TransactionProcessHandler(AccountManager accountManager, TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        this.accountManager = accountManager;
    }
    private  void performDeposit(String accountNumber) throws InvalidAmountException, InsufficientFundsException, OverdraftExceededException {
        System.out.print("Enter amount to deposit: $");
        double amount = getPositiveAmount();

        Account account = accountManager.getAccount(accountNumber);
        double previousBalance = account.getBalance();

        transactionManager.deposit(accountNumber, amount);

        System.out.println("\n✓ Deposit successful!");
        System.out.println("Previous Balance: $" + String.format("%.2f", previousBalance));
        System.out.println("Deposit Amount: $" + String.format("%.2f", amount));
        System.out.println("New Balance: $" + String.format("%.2f", account.getBalance()));
    }

    private  void performWithdrawal(String accountNumber) throws InsufficientFundsException, InvalidAmountException, OverdraftExceededException {
        System.out.print("Enter amount to withdraw: $");
        double amount = getPositiveAmount();

        Account account = accountManager.getAccount(accountNumber);
        double previousBalance = account.getBalance();

        boolean success = transactionManager.withdraw(accountNumber, amount);

        if (success) {
            System.out.println("\n✓ Withdrawal successful!");
            System.out.println("Previous Balance: $" + String.format("%.2f", previousBalance));
            System.out.println("Withdrawal Amount: $" + String.format("%.2f", amount));
            System.out.println("New Balance: $" + String.format("%.2f", account.getBalance()));
        } else {
            System.out.println("\n✗ Withdrawal failed.");
        }
    }

    private  void performTransfer(String fromAccountNumber) throws InsufficientFundsException, InvalidAmountException, OverdraftExceededException {
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

}
