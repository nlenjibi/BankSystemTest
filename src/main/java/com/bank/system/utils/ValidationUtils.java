package com.bank.system.utils;

public class ValidationUtils {
    

    public static boolean isValidAmount(double amount) {
        return amount > 0;
    }
    

    public static boolean isValidAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return false;
        }
        
        // Simple validation: should start with "ACC" followed by digits
        return accountNumber.matches("^ACC\\d+$");
    }
    

    public static boolean isValidCustomerName(String name) {
        return name != null && !name.trim().isEmpty();
    }
    

    public static boolean isValidCustomerId(String customerId) {
        return customerId != null && !customerId.trim().isEmpty();
    }
    

    public static boolean isValidTransactionType(String transactionType) {
        return transactionType != null && 
               (transactionType.equals("DEPOSIT") || 
                transactionType.equals("WITHDRAWAL") || 
                transactionType.equals("TRANSFER_IN") || 
                transactionType.equals("TRANSFER_OUT"));
    }
}