package com.bank.system.services;

import com.bank.system.models.*;

import java.util.ArrayList;
import java.util.List;

public class StatementGenerator {
    private final AccountManager accountManager;
    private final TransactionManager transactionManager;
    
    public StatementGenerator(AccountManager accountManager, TransactionManager transactionManager) {
        this.accountManager = accountManager;
        this.transactionManager = transactionManager;
    }
    
    public String generateStatement(String accountNumber) {
        Account account = accountManager.getAccount(accountNumber);
        if (account == null) {
            return "Error: Account not found. Please check the account number and try again.";
        }
        
        List<Transaction> transactions = transactionManager.getTransactionsForAccount(accountNumber);
        StatementTotals totals = calculateTotals(transactions);

        StringBuilder statement = new StringBuilder();
        statement.append("GENERATE ACCOUNT STATEMENT\n\n");
        statement.append("Account: ").append(account.getCustomer().getName()).append(" (");
        statement.append(account.getClass().getSimpleName()).append(")\n");
        statement.append("Current Balance: $").append(formatAmount(account.getBalance())).append("\n\n");

        if (transactions.isEmpty()) {
            statement.append("No transactions found for this account.\n");
        } else {
            statement.append("Transactions:\n\n");
            
            // Sort transactions by timestamp (newest first)
            List<Transaction> sortedTransactions = sortTransactionsByTimestampDesc(transactions);

            for (Transaction transaction : sortedTransactions) {
                String sign = isCreditTransaction(transaction) ? "+" : "-";
                statement.append(String.format("%s | %s | %s$%.2f | $%.2f\n",
                        transaction.getTransactionId(),
                        transaction.getType(),
                        sign,
                        transaction.getAmount(),
                        transaction.getBalanceAfter()));
             }

            double netChange = totals.totalDeposits - totals.totalWithdrawals;

            statement.append("\nSummary:\n");
            statement.append("Total Deposits: $").append(formatAmount(totals.totalDeposits)).append("\n");
            statement.append("Total Withdrawals: $").append(formatAmount(totals.totalWithdrawals)).append("\n");
            statement.append("Total Received: $").append(formatAmount(totals.totalReceived)).append("\n");
            statement.append("Total Sent: $").append(formatAmount(totals.totalSent)).append("\n");
            statement.append("Net Change: $").append(formatAmount(netChange)).append("\n");
        }
        
        statement.append("\n✓ Statement generated successfully.");
        return statement.toString();
    }

    private List<Transaction> sortTransactionsByTimestampDesc(List<Transaction> transactions) {
        List<Transaction> sortedTransactions = new ArrayList<>(transactions);
        sortedTransactions.sort((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp()));
        return sortedTransactions;
    }

    private boolean isCreditTransaction(Transaction transaction) {
        String type = transaction.getType();
        return "DEPOSIT".equalsIgnoreCase(type) || "RECEIVE".equalsIgnoreCase(type);
    }

    private StatementTotals calculateTotals(List<Transaction> transactions) {
        StatementTotals totals = new StatementTotals();
        for (Transaction transaction : transactions) {
            if (transaction == null) {
                continue;
            }
            String type = transaction.getType();
            double amount = transaction.getAmount();
            if ("DEPOSIT".equalsIgnoreCase(type)) {
                totals.totalDeposits += amount;
            } else if ("WITHDRAWAL".equalsIgnoreCase(type)) {
                totals.totalWithdrawals += amount;
            } else if ("RECEIVE".equalsIgnoreCase(type)) {
                totals.totalReceived += amount;
            } else if ("TRANSFER".equalsIgnoreCase(type)) {
                totals.totalSent += amount;
            }
        }
        return totals;
    }

    private String formatAmount(double amount) {
        return String.format("%.2f", amount);
    }

    private static final class StatementTotals {
        private double totalDeposits;
        private double totalWithdrawals;
        private double totalReceived;
        private double totalSent;
    }
}