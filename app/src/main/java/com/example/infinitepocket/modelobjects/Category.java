package com.example.infinitepocket.modelobjects;

import android.graphics.drawable.Drawable;

import com.example.infinitepocket.R;

import java.lang.reflect.Field;

public final class Category{

    public static final int FOOD = 0;
    public static final int SHOPPING = 1;
    public static final int TRANSPORTATION = 2;
    public static final int RENTALS = 3;
    public static final int WATER_BILL = 4;
    public static final int ELECTRICITY_BILL = 5;
    public static final int GAS_BILL = 6;
    public static final int TELEVISION_BILL = 7;
    public static final int INTERNET_BILL = 8;
    public static final int PHONE_BILL = 9;
    public static final int OTHER_UTILITY_BILLS = 10;
    public static final int HOME_MAINTENANCE = 11;
    public static final int VEHICLE_MAINTENANCE = 12;
    public static final int MEDICAL_CHECKUP = 13;
    public static final int INSURANCES = 14;
    public static final int EDUCATION = 15;
    public static final int HOUSEWARE = 16;
    public static final int PERSONAL_ITEMS = 17;
    public static final int PETS = 18;
    public static final int HOME_SERVICES = 19;
    public static final int FITNESS = 20;
    public static final int MAKEUP = 21;
    public static final int GIFTS_AND_DONATIONS = 22;
    public static final int STREAMING_SERVICES = 23;
    public static final int FUN_MONEY = 24;
    public static final int INVESTMENT = 25;
    public static final int DEBT_COLLECTION = 26;
    public static final int DEBT = 27;
    public static final int LOAN = 28;
    public static final int REPAYMENT = 29;
    public static final int PAY_INTEREST = 30;
    public static final int COLLECT_INTEREST = 31;
    public static final int SALARY = 32;
    public static final int OTHER_INCOME = 33;
    public static final int OTHER_EXPENSE = 34;
    public static final int MAX_ID = 34;

    private Category() {

    }

    public static Integer getIconId(int category_id) {
        switch (category_id) {
            default:
                return R.drawable.aus_dollar;
        }
    }

    public static String getName(int _category_id) {
        if (!isIdValid(_category_id))
            throw new IllegalArgumentException("Invalid ID");

        String name = new Category().getClass().getFields()[_category_id].getName();
        return name;
    }

    public static String getFormattedName(int _category_id) {
        String name = getName(_category_id);
        String[] words = name.split("_");
        StringBuilder stringBuilder = new StringBuilder("");
        int prev_index = 0;
        for (String word : words) {
            stringBuilder.append(word.toLowerCase() + " ");
            stringBuilder.replace(prev_index, prev_index + 1, (word.charAt(0) + "").toUpperCase());
            prev_index += word.length() + 1;
        }
        return stringBuilder.toString();
    }

    public static int getIdByName(String name) {
        Field[] fields = new Category().getClass().getFields();
        for (int i = 0; i <= Category.MAX_ID; i++) {
            if (fields[i].getName().equalsIgnoreCase(name))
                return i;
        }
        return -1;
    }

    public static boolean isIdValid(int _categoryId) {
        if (_categoryId < 0 || _categoryId > MAX_ID)
            return false;
        return true;
    }

}
