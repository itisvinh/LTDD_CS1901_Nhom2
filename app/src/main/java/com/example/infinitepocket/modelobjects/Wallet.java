package com.example.infinitepocket.modelobjects;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.infinitepocket.interfaces.EditableBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wallet extends EditableBase<Wallet> {
    @Nullable
    private int id;
    @NonNull
    private String name;
    @NonNull
    private Currency currency;
    private double balance = 1d;



    public Wallet(String _name, Currency _currency, double _balance) {
        name = _name;
        currency = _currency;
        balance = _balance;
    }


    @Override
    protected Wallet getInstance() {
        return this;
    }

    @Override
    public void commitEdit() {

    }

    public void setName(@NonNull String name) {
        addUnsavedChanges(this.name, name);
    }

    public void setBalance(double balance) {
        addUnsavedChanges(this.balance, balance);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


}
