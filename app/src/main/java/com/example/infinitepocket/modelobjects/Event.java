package com.example.infinitepocket.modelobjects;

import java.security.PublicKey;
import java.sql.Date;

public class Event {
    // primary key
    private String aliasName;
    private Transaction transaction;
    private Date recurringTime;
    private Date lastAddedTime;

    public Event(String aliasName, Transaction transaction, Date recurringTime) {
        this.aliasName = aliasName;
        this.transaction = transaction;
        this.recurringTime = recurringTime;
    }

    public Date getLastAddedTime() {
        return lastAddedTime;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setLastAddedTime(Date _lastAddedTime) {
        if (lastAddedTime == null || _lastAddedTime.after(lastAddedTime)) {
            this.lastAddedTime = _lastAddedTime;
            return;
        }
        throw new IllegalArgumentException("The latest added time has to be after the previous one");
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Date getRecurringTime() {
        return recurringTime;
    }
}
