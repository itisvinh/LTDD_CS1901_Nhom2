package com.example.infinitepocket.database;

import static com.example.infinitepocket.utilities.SimpleDateHelper.dateFromSimpleFormat;
import static com.example.infinitepocket.utilities.SimpleDateHelper.simpleDateFormat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.infinitepocket.Communicator;
import com.example.infinitepocket.modelobjects.Category;
import com.example.infinitepocket.modelobjects.Currency;
import com.example.infinitepocket.modelobjects.Transaction;
import com.example.infinitepocket.modelobjects.Wallet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    public boolean addNewTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DbContract.TransactionTable.COLUMN_NAME_AMOUNT, transaction.getAmount());
        cv.put(DbContract.TransactionTable.COLUMN_NAME_CATEGORY_ID, transaction.getCategory().getId());
        cv.put(DbContract.TransactionTable.COLUMN_NAME_NOTE, transaction.getNote());
        cv.put(DbContract.TransactionTable.COLUMN_NAME_DATE, simpleDateFormat(transaction.getDate()));
        cv.put(DbContract.TransactionTable.COLUMN_NAME_WALLET_ID, transaction.getWallet().getId());

        long res = db.insert(DbContract.TransactionTable.TABLE_NAME, null, cv);

        if (res == -1)
            return false;
        else {
            //transaction.setId(getLastInsertedRowId());
            transaction.setId((int) res);
            return true;
        }
    }

    public Transaction getTransactionById(int transactionId) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + DbContract.TransactionTable.TABLE_NAME + " where "
                + DbContract.TransactionTable.COLUMN_NAME_ID + " = ? ", new String[] { String.valueOf(transactionId) });

        if (cursor.getCount() > 0) {
            Transaction transaction;

            if (cursor.moveToFirst()) {
                do {
                    Wallet wallet = Communicator.getInstance().getCurrentWallet();
                    int trans_wallet_id = cursor.getInt(5);
                    if (wallet == null || wallet.getId() != trans_wallet_id)
                        return null;

                    int id = cursor.getInt(0);
                    double amount = cursor.getDouble(1);
                    Category category = new Category(cursor.getInt(2));
                    String note = cursor.getString(3);
                    //String test = cursor.getString(4).toString();
                    Date date = dateFromSimpleFormat(cursor.getString(4));

                    return new Transaction(id, amount, category, note, date, wallet);

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return null;
    }

    public boolean updateTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DbContract.TransactionTable.COLUMN_NAME_AMOUNT, transaction.getAmount());
        cv.put(DbContract.TransactionTable.COLUMN_NAME_CATEGORY_ID, transaction.getCategory().getId());
        cv.put(DbContract.TransactionTable.COLUMN_NAME_NOTE, transaction.getNote());
        cv.put(DbContract.TransactionTable.COLUMN_NAME_DATE, simpleDateFormat(transaction.getDate()));

        String id = String.valueOf(transaction.getId());
        long res = db.update(DbContract.TransactionTable.TABLE_NAME, cv,
                DbContract.TransactionTable.COLUMN_NAME_ID + "=" + id, null);

        return res <= 0 ? false : true;
    }

    public List<Transaction> getAllTransactions() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + DbContract.TransactionTable.TABLE_NAME, null);

        if (cursor.getCount() > 0) {
            List<Transaction> transactions = new ArrayList<>(cursor.getCount());

            Transaction transaction;
            if (cursor.moveToFirst()) {
                do {
                    Wallet wallet = Communicator.getInstance().getCurrentWallet();
                    int trans_wallet_id = cursor.getInt(5);
                    if (wallet == null || wallet.getId() != trans_wallet_id)
                        return null;

                    int id = cursor.getInt(0);
                    double amount = cursor.getDouble(1);
                    Category category = new Category(cursor.getInt(2));
                    String note = cursor.getString(3);
                    Date date = dateFromSimpleFormat(cursor.getString(4));

                    transaction = new Transaction(id, amount, category, note, date, wallet);
                    transactions.add(transaction);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return transactions;
        }
        return null;
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
            //wallet.setId(getLastInsertedRowId());
            wallet.setId((int) res);
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

        return res <= 0 ? false : true;
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
