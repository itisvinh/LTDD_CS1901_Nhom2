package com.example.infinitepocket.viewmodels;

public class UpdateQuery {
    public static final int WITH_DATE_WALLET = 0;
    public static final int WITH_WALLET_ONLY= 1;

    private int queryType = -1;
    public final String walletName;
    public final int day;
    public final int month;
    public final int year;


    public UpdateQuery(String walletName, int day, int month, int year) {
        this.walletName = walletName;
        this.day = day;
        this.month = month;
        this.year = year;
        queryType = WITH_DATE_WALLET;
      
    }

    public UpdateQuery(String walletName) {
        this.walletName = walletName;
        this.day = -1;
        this.month = -1;
        this.year = -1;
        queryType = WITH_WALLET_ONLY;
    }

    public int getQueryType() {
        return queryType;
    }
}
