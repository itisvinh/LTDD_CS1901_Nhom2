package com.example.infinitepocket.modelobjects;

import java.security.PublicKey;
import java.sql.Date;

@Deprecated
public class Event {
    private int id;
    private String aliasName;
    private Date recurringTime;
    private Date lastAddedTime;
    public static String NONE = "None";

    public Event(String aliasName, Date recurringTime) {
        this.aliasName = formatName(aliasName);
        this.recurringTime = recurringTime;
    }

    private String formatName(String name) {
        String[] tokens = name.trim().split("\\s+");
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < tokens.length; i++) {
            if (i != 0)
                stringBuilder.append(" ");
            stringBuilder.append(String.valueOf(tokens[i].charAt(0)).toUpperCase()
                    + tokens[i].toLowerCase().substring(1));
        }
        return stringBuilder.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getRecurringTime() {
        return recurringTime;
    }
}
