package com.example.infinitepocket.modelobjects;


import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.example.infinitepocket.R;

import java.util.HashMap;
import java.util.Map;


// defining currencies
// usage:
//      example, to get thr symbol of baht currency:
//      Currency.get(Currency.THAI_BAHT)
public final class Currency {
    public static final int MAX_ID = 9;
    public static final int EMPTY_CURRENCY = -1;
    public static final int US_DOLLAR = 0;
    public static final int CANADIAN_DOLLAR = 1;
    public static final int AUSTRALIAN_DOLLAR = 2;
    public static final int VIETNAM_DONG = 3;
    public static final int YEN = 4;
    public static final int WON = 5;
    public static final int BRAZILIAN_REAL = 6;
    public static final int THAI_BAHT = 7;
    public static final int POUND = 8;
    public static final int EURO = 9;

    private final int _currency_id;

    public Currency(int currency_id) {
        if (currency_id < -1 || currency_id > MAX_ID)
            throw new IllegalArgumentException("Invalid id");
        _currency_id = currency_id;
    }

    private static HashMap<Integer, String> map_symbols = new HashMap<Integer, String>() {{
        put(0, "$");
        put(1, "CA$");
        put(2, "A$");
        put(3, "đ");
        put(4, "¥");
        put(5, "₩");
        put(6, "R$");
        put(7, "฿");
        put(8, "£");
        put(9, "€");
    }};
    private static HashMap<Integer, String> map_names = new HashMap<Integer, String>() {{
        put(0, "Dollar");
        put(1, "Canadian Dollar");
        put(2, "Australian Dollar");
        put(3, "Vietnam Dong");
        put(4, "Yen");
        put(5, "Won");
        put(6, "Brazilian Real");
        put(7, "Thai Baht");
        put(8, "Pound");
        put(9, "Euro");
    }};

    public static String getSymbol(int currency_id) {
        if (currency_id == EMPTY_CURRENCY)
            throw new IllegalArgumentException("Cannot get symbols of an empty currency");
        else if (currency_id < -1 || currency_id > MAX_ID)
            throw new IllegalArgumentException("Invalid id");

        return map_symbols.get(currency_id);
    }

    public String getSymbol() {
        return map_symbols.get(_currency_id);
    }
    public int getId() {
        return _currency_id;
    }

    public static Integer getDrawableIconId(int currency_id) {
        if (currency_id > MAX_ID || currency_id < 0)
            return null;

        int res_id = 0;
        switch (currency_id) {
            case US_DOLLAR:
                res_id = R.drawable.dollar;
                break;
            case CANADIAN_DOLLAR:
                res_id = R.drawable.ca_dollar;
                break;
            case VIETNAM_DONG:
                res_id = R.drawable.dong;
                break;
            case POUND:
                res_id = R.drawable.pound;
                break;
            case THAI_BAHT:
                res_id = R.drawable.baht;
                break;
            case EURO:
                res_id = R.drawable.euro;
                break;
            case YEN:
                res_id = R.drawable.yen;
                break;
            case WON:
                res_id = R.drawable.won;
                break;
            case AUSTRALIAN_DOLLAR:
                res_id = R.drawable.aus_dollar;
                break;
            case BRAZILIAN_REAL:
                res_id = R.drawable.brazillian_real;
                break;
        }
        return res_id;
    }

    public Integer getDrawableIconId() {
        return getDrawableIconId(_currency_id);
    }

    public static String getNameById(int currency_id) {
        if (currency_id > MAX_ID || currency_id < 0)
            return null;

        return map_names.get(currency_id);
    }
    public String getName() {
        return getNameById(_currency_id);
    }

    public static Integer getCurrencyIdByName(String currency_name) {
        for (Map.Entry<Integer, String> entry : map_names.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(currency_name))
                return entry.getKey();
        }
        return null;
    }
}
