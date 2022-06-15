package com.example.infinitepocket.modelobjects;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.infinitepocket.Communicator;
import com.example.infinitepocket.interfaces.EditableBase;
import com.example.infinitepocket.interfaces.Observable;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class Transaction extends EditableBase<Transaction> implements Cloneable{
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

    private static final String SET_AMOUNT = "set-amount";
    private static final String SET_CATEGORY = "set-category";
    private static final String SET_NOTE = "set-note";
    private static final String SET_DATE = "set-date";

    private static Observable<Transaction> observable;

    static {
        observable = Communicator.getInstance()::getNotifiedFromTransactionChange;
    }

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

    public Transaction(int id, double amount, Category category, String note, Date date, Wallet wallet/*, Event event*/) {
        this.id = id;
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

    public Transaction setId(int id) {
        this.id = id;
        return this;
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

    public Transaction setAmount(double amount) {
        addUnsavedChanges(SET_AMOUNT, amount);
        return this;
    }

    @NonNull
    public Category getCategory() {
        return category;
    }

    public Transaction setCategory(@NonNull Category category) {
        addUnsavedChanges(SET_CATEGORY, category);
        return this;
    }

    @NonNull
    public String getNote() {
        return note;
    }

    public Transaction setNote(@NonNull String note) {
        addUnsavedChanges(SET_NOTE, note);
        return this;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    public Transaction setDate(@NonNull Date date) {
        addUnsavedChanges(SET_DATE, date);
        return this;
    }

    @NonNull
    public Wallet getWallet() {
        return wallet;
    }

    @Override
    protected Transaction getInstance() {
        return this;
    }

    @Override
    public void commitEdit() {
        super.commitEdit();
        boolean changeDetected = false;
        Transaction clonedTrans = this.clone();

        Map.Entry eAmount = getUnsavedEntryFromKey(SET_AMOUNT);
        if (eAmount != null) {
            amount = Double.parseDouble(eAmount.getValue().toString());
            changeDetected = true;
        }

        Map.Entry eCategory = getUnsavedEntryFromKey(SET_CATEGORY);
        if (eCategory != null) {
            category = (Category) eCategory.getValue();
            changeDetected = true;
        }

        Map.Entry eNote = getUnsavedEntryFromKey(SET_NOTE);
        if (eNote != null) {
            note = (String) eNote.getValue();
            changeDetected = true;
        }

        Map.Entry eDate = getUnsavedEntryFromKey(SET_DATE);
        if (eDate != null) {
            date = (Date) eDate.getValue();
            changeDetected = true;
        }

        if (changeDetected) {
            observable.fire(clonedTrans);
        }
    }

    @Override
    protected Transaction clone() {
        return new Transaction(id, new Category(category.getId()), note, date, wallet);
    }
}
