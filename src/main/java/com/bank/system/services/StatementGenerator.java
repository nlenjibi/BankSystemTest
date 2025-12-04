package com.bank.system.services;

import com.bank.system.models.*;

import java.util.List;
import java.util.stream.Collectors;

public class StatementGenerator {
    private AccountManager accountManager;
    private TransactionManager transactionManager;
    
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
        
        StringBuilder statement = new StringBuilder();
        statement.append("GENERATE ACCOUNT STATEMENT\n\n");
        statement.append("Account: ").append(account.getCustomer().getName()).append(" (");
        statement.append(account.getClass().getSimpleName()).append(")\n");
        statement.append("Current Balance: $").append(String.format("%.2f", account.getBalance())).append("\n\n");
        
        if (transactions.isEmpty()) {
            statement.append("No transactions found for this account.\n");
        } else {
            statement.append("Transactions:\n\n");
            
            // Sort transactions by timestamp (newest first)
            List<Transaction> sortedTransactions = transactions.stream()
                    .sorted((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp()))
                    .collect(Collectors.toList());
            
            for (Transaction transaction : sortedTransactions) {
                String sign = transaction.getType().equals("DEPOSIT") || transaction.getType().equals("TRANSFER_IN") ? "+" : "-";
                statement.append(String.format("%s | %s | %s$%.2f | $%.2f\n",
                        transaction.getTransactionId(),
                        transaction.getType(),
                        sign,
                        transaction.getAmount(),
                        transaction.getBalanceAfter()));
            }
            
            // Calculate summary
            double totalDeposits = transactions.stream()
                    .filter(t -> t.getType().equals("DEPOSIT") || t.getType().equals("TRANSFER_IN"))
                    .mapToDouble(Transaction::getAmount)
                    .sum();
            
            double totalWithdrawals = transactions.stream()
                    .filter(t -> t.getType().equals("WITHDRAWAL") || t.getType().equals("TRANSFER_OUT"))
                    .mapToDouble(Transaction::getAmount)
                    .sum();
            
            double netChange = totalDeposits - totalWithdrawals;
            
            statement.append("\nSummary:\n");
            statement.append("Total Deposits: $").append(String.format("%.2f", totalDeposits)).append("\n");
            statement.append("Total Withdrawals: $").append(String.format("%.2f", totalWithdrawals)).append("\n");
            statement.append("Net Change: $").append(String.format("%.2f", netChange)).append("\n");
        }
        
        statement.append("\n✓ Statement generated successfully.");
        return statement.toString();
    }
}