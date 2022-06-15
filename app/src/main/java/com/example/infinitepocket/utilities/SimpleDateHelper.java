package com.example.infinitepocket.utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class SimpleDateHelper {
    private SimpleDateHelper() {}

    public static String simpleDateFormat(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public static Date dateFromSimpleFormat(String simpleDateFormat) {
        if (simpleDateFormat.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
            String[] tokens = simpleDateFormat.split("-");
            int day = Integer.parseInt(tokens[2]);
            int month = Integer.parseInt(tokens[1]);
            int year = Integer.parseInt(tokens[0]);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.YEAR, year);

            return calendar.getTime();
        } else
            return null;
    }
}
