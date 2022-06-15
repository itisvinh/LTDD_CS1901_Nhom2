package com.example.infinitepocket;

import android.graphics.Color;

import com.example.infinitepocket.interfaces.Notifiable;
import com.example.infinitepocket.interfaces.Observable;
import com.example.infinitepocket.modelobjects.Category;
import com.example.infinitepocket.modelobjects.Transaction;
import com.example.infinitepocket.modelobjects.Wallet;
import com.example.infinitepocket.utilities.CustomizedToast;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.PropertyReference0Impl;


// Mediator class
// this class handles all the communication between every other classes
public final class Communicator{
    private static Communicator instance;
    private Communicator() {}

    public static Communicator getInstance() {
        if (instance == null)
            instance = new Communicator();
        return instance;
    }

    private Wallet currentWallet = null;
    private Transaction lastTransaction = null;
    private final List<Observable<Wallet>> wallet_creation_observables = new ArrayList<>();
    private final List<Observable<Wallet>> wallet_change_observables = new ArrayList<>();
    private final  List<Observable<Transaction>> transaction_creation_observables = new ArrayList<>();
    private final  List<Observable<Transaction>> transaction_change_observables = new ArrayList<>();
    private CreateWalletMode createWalletMode = CreateWalletMode.MODE_CREATE;
    private TransactionAddedRole transactionAddedRole = TransactionAddedRole.ROLE_CREATE;

    public TransactionAddedRole getTransactionAddedRole() {
        return transactionAddedRole;
    }

    public void setTransactionAddedRole(TransactionAddedRole transactionAddedRole) {
        if (transactionAddedRole == TransactionAddedRole.ROLE_EDIT && lastTransaction == null)
            throw new IllegalArgumentException("Communicator.lastTransaction is not editable: null?");
        this.transactionAddedRole = transactionAddedRole;
    }

    public CreateWalletMode getCreateWalletMode() {
        return createWalletMode;
    }

    public void setCreateWalletMode(CreateWalletMode createWalletMode) {
        if (createWalletMode == CreateWalletMode.MODE_EDIT && currentWallet == null)
            throw new IllegalArgumentException("Communicator.currentWallet is not editable: null?");
        this.createWalletMode = createWalletMode;
    }

    public Transaction getLastTransaction() {
        return lastTransaction;
    }

    public void setLastTransaction(Transaction lastTransaction) {
        if (this.lastTransaction != lastTransaction) {
            this.lastTransaction = lastTransaction;
            currentWallet.addTransaction(lastTransaction);
            fireAllTransactionCreationObservables();
        }
    }

    public void setLastTransactionNoFire(Transaction transaction) {
        this.lastTransaction = transaction;
    }

    public void setCurrentWallet(Wallet wallet) {
        if (currentWallet != wallet) {
            currentWallet = wallet;
            fireAllWalletCreationObservables();
        }
    }

    public Wallet getCurrentWallet() {
        return currentWallet;
    }

    private void fireAllWalletCreationObservables() {
        for (Observable<Wallet> observable : wallet_creation_observables)
            observable.fire(currentWallet);
    }

    private void fireAllTransactionCreationObservables() {
        for (Observable<Transaction> observable : transaction_creation_observables)
            observable.fire(lastTransaction);
    }

    public void addOnCreatedTransactionObservables(Observable<Transaction> observable) {
        transaction_creation_observables.add(observable);
    }

    public void addOnSetCurrentWalletObserver(Observable<Wallet> observable) {
        wallet_creation_observables.add(observable);
    }

    public void addOnChangedCurrentWalletObserver(Observable<Wallet> observable) {
        wallet_change_observables.add(observable);
    }

    public void addOnChangedCurrentTransactionObserver(Observable<Transaction> observable) {
        transaction_change_observables.add(observable);
    }

    public void getNotifiedFromWalletChange(Wallet wallet) {
        for (Observable<Wallet> observables : wallet_change_observables)
            observables.fire(wallet);
    }

    private void handChangesFromTransactionToWallet(Transaction unchangedTransaction) {
        if (getTransactionAddedRole() != TransactionAddedRole.ROLE_EDIT)
            throw new IllegalStateException("Transaction is uneditable");

        // remove prev transaction
        if (unchangedTransaction.getCategory().getId() == Category.INCOME)
            // affect to wallet balance
            getCurrentWallet().beginEdit().addToBalance(-unchangedTransaction.getAmount()).commitEdit();
        else
            getCurrentWallet()
                    .beginEdit()
                    .addToUsed(unchangedTransaction.getAmount())
                    .commitEdit();

        // add new transaction
        Transaction lastTrans = getLastTransaction();
        if (lastTrans.getCategory().getId() == Category.INCOME)
            // affect to wallet balance
            getCurrentWallet().beginEdit().addToBalance(lastTrans.getAmount()).commitEdit();
        else
            getCurrentWallet().beginEdit().addToUsed(lastTrans.getAmount()).commitEdit();

    }

    public void getNotifiedFromTransactionChange(Transaction unchangedTransaction) {
        handChangesFromTransactionToWallet(unchangedTransaction);
        for (Observable<Transaction> observable : transaction_change_observables)
            observable.fire(unchangedTransaction);
    }
}
