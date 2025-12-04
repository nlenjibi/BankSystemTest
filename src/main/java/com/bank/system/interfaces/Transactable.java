package com.bank.system.interfaces;

public interface Transactable {
    // type: "DEPOSIT" or "WITHDRAWAL"
    boolean  processTransaction(double amount, String type);

}
