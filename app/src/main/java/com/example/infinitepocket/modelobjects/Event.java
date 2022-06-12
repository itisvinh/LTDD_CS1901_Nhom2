package com.example.infinitepocket.modelobjects;

import java.sql.Date;

public class Event {
    private Transaction transaction;
    private Date recurringTime;
    private Date lastAddedTime;

    public Event(Transaction transaction, Date recurringTime) {
        this.transaction = transaction;
        this.recurringTime = recurringTime;
    }

    public Date getLastAddedTime() {
        return lastAddedTime;
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
