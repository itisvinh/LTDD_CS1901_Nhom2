package com.example.infinitepocket.database;

import android.provider.BaseColumns;

public class DbContract {
        private DbContract() {}

        public static final  int    DATABASE_VERSION   = 1;
        public static final  String DATABASE_NAME      = " database.db ";
        private static final String TEXT_TYPE          = " TEXT ";
        private static final String INT_TYPE          = " INTEGER ";
        private static final String REAL_TYPE          = " REAL ";
        private static final String COMMA_SEP          = " , ";
        private static final String NOT_NULL          = " NOT NULL ";


        public static abstract class WalletTable implements BaseColumns {
                public static final String TABLE_NAME       = "wallets";
                public static final String COLUMN_NAME_ID = "id";
                public static final String COLUMN_NAME_NAME = "name";
                public static final String COLUMN_NAME_CURRENCY_ID = "currencyId";
                public static final String COLUMN_NAME_BALANCE = "balance";
                public static final String COLUMN_NAME_USED = "used";
                public static final String COLUMN_NAME_AVAILABLE = "available";


                public static final String CREATE_TABLE = "CREATE TABLE " +
                        TABLE_NAME + " (" +
                        COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                        COLUMN_NAME_CURRENCY_ID + INT_TYPE + NOT_NULL + COMMA_SEP +
                        COLUMN_NAME_BALANCE + REAL_TYPE + NOT_NULL + COMMA_SEP +
                        COLUMN_NAME_USED + REAL_TYPE + COMMA_SEP +
                        COLUMN_NAME_AVAILABLE + REAL_TYPE + " )";

                public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        }

        public static abstract class TransactionTable implements BaseColumns {
                public static final String TABLE_NAME       = "transactions";
                public static final String COLUMN_NAME_ID = "id";
                public static final String COLUMN_NAME_AMOUNT = "amount";
                public static final String COLUMN_NAME_CATEGORY_ID = "categoryId";
                public static final String COLUMN_NAME_NOTE = "note";
                public static final String COLUMN_NAME_DATE = "date";
                public static final String COLUMN_NAME_WALLET_ID = "walletId";

                public static final String CREATE_TABLE = "CREATE TABLE " +
                        TABLE_NAME + " (" +
                        COLUMN_NAME_ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                        COLUMN_NAME_AMOUNT + REAL_TYPE + NOT_NULL + COMMA_SEP +
                        COLUMN_NAME_CATEGORY_ID + INT_TYPE + NOT_NULL + COMMA_SEP +
                        COLUMN_NAME_NOTE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_DATE + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                        COLUMN_NAME_WALLET_ID + INT_TYPE + NOT_NULL + COMMA_SEP +
                        " FOREIGN KEY (" + COLUMN_NAME_WALLET_ID + ") REFERENCES " + WalletTable.TABLE_NAME + "(" + WalletTable.COLUMN_NAME_ID + "));";

                public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        }
}
