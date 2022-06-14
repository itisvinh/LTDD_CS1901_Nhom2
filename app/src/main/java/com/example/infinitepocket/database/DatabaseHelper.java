package com.example.infinitepocket.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.infinitepocket.modelobjects.Currency;
import com.example.infinitepocket.modelobjects.Wallet;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, DbContract.DATABASE_NAME, null, DbContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbContract.WalletTable.CREATE_TABLE);
        db.execSQL(DbContract.TransactionTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DbContract.WalletTable.DELETE_TABLE);
        db.execSQL(DbContract.TransactionTable.DELETE_TABLE);
        onCreate(db);
    }

    public boolean addNewWallet(Wallet wallet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.WalletTable.COLUMN_NAME_NAME, wallet.getName());
        contentValues.put(DbContract.WalletTable.COLUMN_NAME_CURRENCY_ID, wallet.getCurrency().getId());
        contentValues.put(DbContract.WalletTable.COLUMN_NAME_BALANCE, wallet.getBalance());
        contentValues.put(DbContract.WalletTable.COLUMN_NAME_USED, wallet.getUsed());
        contentValues.put(DbContract.WalletTable.COLUMN_NAME_AVAILABLE, wallet.getAvailable());

        long res = db.insert(DbContract.WalletTable.TABLE_NAME, null, contentValues);

        if (res == -1)
            return false;
        else {
            wallet.setId(getLastInsertedRowId());
            return true;
        }
    }

    public boolean updateWallet(Wallet wallet) {
        if (wallet.getId() < 0)
            return false;

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DbContract.WalletTable.COLUMN_NAME_NAME, wallet.getName());
        cv.put(DbContract.WalletTable.COLUMN_NAME_BALANCE, wallet.getBalance());
        cv.put(DbContract.WalletTable.COLUMN_NAME_USED, wallet.getUsed());
        cv.put(DbContract.WalletTable.COLUMN_NAME_AVAILABLE, wallet.getAvailable());

        int res = db.update(DbContract.WalletTable.TABLE_NAME, cv,
                    DbContract.WalletTable.COLUMN_NAME_ID + " = ? ",
                    new String[]{String.valueOf(wallet.getId())});

        return res == -1 ? false : true;
    }

    public int getLastInsertedRowId() {
        SQLiteDatabase db =getReadableDatabase();
        Cursor cursor = db.rawQuery("select last_insert_rowid()",null);

        int id = -1;

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            id = cursor.getInt(0);
            cursor.close();
        }
        return id;
    }

    public List<Wallet> getAllWallets() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + DbContract.WalletTable.TABLE_NAME, null);

        if (cursor.getCount() > 0) {
            List<Wallet> wallets = new ArrayList<>(cursor.getCount());

            Wallet wallet;
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    int currencyId = cursor.getInt(2);
                    double balance = cursor.getDouble(3);
                    double used = cursor.getDouble(4);
                    wallet = new Wallet(id, name, new Currency(currencyId), balance, used);
                    wallets.add(wallet);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return wallets;
        }
        return null;
    }
}
