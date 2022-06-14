package com.example.infinitepocket.modelobjects;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.infinitepocket.Communicator;
import com.example.infinitepocket.interfaces.EditableBase;
import com.example.infinitepocket.interfaces.Observable;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Wallet extends EditableBase<Wallet> {
    // luu csdl
    @Nullable
    private int id;
    @NonNull
    private String name;
    @NonNull
    private Currency currency;
    private double balance = 1d;
    // newly added
    private double used;
    private double available;

    // khong can luu csdl
    private static Observable<Wallet> observable;
    private boolean balanceAddedOrSet;
    private static final String SET_BALANCE = "set-balance";
    private static final String ADD_TO_BALANCE = "add-to-balance";
    private static final String SET_NAME = "set-name";
    private static final String SET_USED = "set-used";
    private static final String ADD_TO_USED = "add-to-used";
    private static final String SET_AVAILABLE = "set-available";


    static {
        if (observable == null)
            observable = Communicator.getInstance()::getNotifiedFromWalletChange;
    }
    {
        available = balance - used;
        balanceAddedOrSet = false;
    }
    public Wallet(String _name, Currency _currency, double _balance) {
        name = _name;
        currency = _currency;
        balance = _balance;
    }

    public Wallet addToBalance(double amount) {
        if (balanceAddedOrSet)
            throw new IllegalStateException("setBalance() and addToBalance() cannot be used together during the same transaction" +
                    "\nand each method is allowed to call only 1 time each transaction");
        if (amount != 0) {
            addUnsavedChanges(ADD_TO_BALANCE, amount);
            balanceAddedOrSet = true;
        }
        return this;
    }

    public Wallet addToUsed(double amount) {
        if (amount != 0)
            addUnsavedChanges(ADD_TO_USED, amount);
        return this;
    }

    public Wallet setBalance(double amount) {
        if (balanceAddedOrSet)
            throw new IllegalStateException("setBalance() and addToBalance() cannot be used together during the same transaction");
        if (balance != amount) {
            addUnsavedChanges(SET_BALANCE, amount);
            balanceAddedOrSet = true;
        }
        return this;
    }

    public double getUsed() {
        return used;
    }

    public double getAvailable() {
        return available;
    }

    @Override
    protected Wallet getInstance() {
        return this;
    }

    @Override
    public void commitEdit() {
        super.commitEdit();

        boolean changeDetected = false;
        Map.Entry eName = getUnsavedEntryFromKey(SET_NAME);
        if (eName != null) {
            name = (String) eName.getValue();
            changeDetected = true;
        }

        Map.Entry eBalance = getUnsavedEntryFromKey(SET_BALANCE);
        if (eBalance != null) {
            balance = (double) eBalance.getValue();
            available = balance - used;
            changeDetected = true;
        }

        Map.Entry eAddBalance = getUnsavedEntryFromKey(ADD_TO_BALANCE);
        if (eAddBalance != null) {
            balance += (double) eAddBalance.getValue();
            available = balance - used;
            changeDetected = true;
        }

        Map.Entry eAddUsed = getUnsavedEntryFromKey(ADD_TO_USED);
        if (eAddUsed != null) {
            used += (double) eAddUsed.getValue();
            available = balance - used;
            changeDetected = true;
        }

        if (changeDetected) {
            flushUnsavedChanged();
            balanceAddedOrSet = false;
            observable.fire(this);
        }
    }

    public Wallet setName(@NonNull String name) {
        if (!this.name.equals(name))
            addUnsavedChanges(SET_NAME, name);
        return this;
    }

    public int getId() {
        return id;
    }

    public Wallet setId(int id) {
        this.id = id;
        return this;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public Currency getCurrency() {
        return currency;
    }

    public double getBalance() {
        return balance;
    }

    public void addTransaction(Transaction transaction) {
        if (balanceAddedOrSet)
            throw new IllegalStateException("setBalance() or addToBalance() might be used prior");

        if (transaction.getWallet() != this)
            throw new IllegalArgumentException("This transaction belongs to other wallet, the operation is aborted");

        beginEdit();
        if (transaction.getCategory().getId() != Category.INCOME)
            addToUsed(transaction.getAmount());
        else
            addToBalance(transaction.getAmount());

        commitEdit();
    }

}
