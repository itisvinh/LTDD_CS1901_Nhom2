package com.example.infinitepocket.database;

import java.util.Date;

public final class TransactionRetrieveMode {
    public static final int MODE_ALL = 0;
    public static final int MODE_WITH_DATE = 1;

    private Date date;
    private int currentMode;

    public TransactionRetrieveMode() {
        currentMode = MODE_ALL;
    }

    public TransactionRetrieveMode(Date date) {
        currentMode = MODE_WITH_DATE;
        this.date = date;
    }

    public int getMode() {
        return currentMode;
    }

    public Date getDate() {
        if (currentMode == MODE_ALL)
            return null;
        return date;
    }
}
