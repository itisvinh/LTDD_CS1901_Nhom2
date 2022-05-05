package com.example.infinitepocket.viewmodels;

public class UpdateQuery {
    public UpdateQuery(String walletName, int day, int month, int year) {
        this.walletName = walletName;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public final String walletName;
    public final int day;
    public final int month;
    public final int year;
}
