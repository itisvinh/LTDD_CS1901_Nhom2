package com.example.infinitepocket.modelobjects;

import android.support.annotation.NonNull;

import java.util.Date;

public final class Transaction {
    @NonNull
    private final Date createdDate;
    @NonNull
    private final double amount;
    @NonNull
    private final int categoryId;
    @NonNull
    private final String note;
    @NonNull
    private final Date date;
    @NonNull
    private final Wallet wallet;

    public Transaction(Date createdDate, double amount, int categoryId, String note, Date date, Wallet wallet) {
        this.createdDate = createdDate;
        this.amount = amount;
        this.categoryId = categoryId;
        this.note = note;
        this.date = date;
        this.wallet = wallet;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public double getAmount() {
        return amount;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getNote() {
        return note;
    }

    public Date getDate() {
        return date;
    }

    public Wallet getWallet() {
        return wallet;
    }
}
