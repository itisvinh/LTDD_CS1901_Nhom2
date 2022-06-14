package com.example.infinitepocket.modelobjects;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;

public class Transaction {
    @NonNull
    private int id;
    @NonNull
    private double amount;
    @NonNull
    private Category category;
    @NonNull
    private String note;
    @NonNull
    private Date date;
    @NonNull
    private Wallet wallet;
//    @Nullable
//    private Event event;
    public Transaction(double amount, Category category, String note, Date date, Wallet wallet/*, Event event*/) {
        this.amount = amount;
        this.category = category;
        this.note = note;
        this.date = date;
        this.wallet = wallet;
        //this.event = event;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    @Nullable
//    public Event getEvent() {
//        return event;
//    }
//
//    public void setEvent(@Nullable Event event) {
//        this.event = event;
//    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @NonNull
    public Category getCategory() {
        return category;
    }

    public void setCategory(@NonNull Category category) {
        this.category = category;
    }

    @NonNull
    public String getNote() {
        return note;
    }

    public void setNote(@NonNull String note) {
        this.note = note;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    public void setDate(@NonNull Date date) {
        this.date = date;
    }

    @NonNull
    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}
