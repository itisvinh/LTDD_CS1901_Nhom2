package com.example.infinitepocket.modelobjects;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wallet {
    @NonNull
    private String name;
    @NonNull
    private Currency currency;
    @NonNull
    private Map<Date, Transaction> transactions;
    private double balance = 1d;

    public Wallet(String _name, Currency _currency, double _balance) {
        transactions = new HashMap<>(10);

        name = _name;
        currency = _currency;
        balance = _balance;
    }

    public void addNewTransaction(Transaction transaction) {
        transactions.put(transaction.getCreatedDate(), transaction);
    }
    public void removeTransaction(Date createdDate) {
        transactions.remove(createdDate);
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public Currency getCurrency() {
        return currency;
    }

    @NonNull
    public Map<Date, Transaction> getTransactions() {
        return transactions;
    }

    public double getBalance() {
        return balance;
    }
}
